package com.builtbroken.mc.logp.parse;

import com.builtbroken.mc.logp.data.ModData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2017.
 */
public class LogParser
{
    public static final Pattern modIDPattern = Pattern.compile("^.*(?=(took))");
    public static final Pattern loadTimePattern = Pattern.compile("(?<=took).*");

    public final ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    public final HashMap<String, ModData> data = new HashMap();

    public final List<String> exclude = new ArrayList();

    public LogParser()
    {
        //List of entry names to exclude so to focus on mod load time
        exclude.add("Minecraft Coder Pack");
        exclude.add("Loading Item Renderer");
        exclude.add("Loading Texture Map");
        exclude.add("Minecraft");
        exclude.add("Loading Model Manager");
        exclude.add("Reloading listeners");
        exclude.add("Loading Entity Renderer");
        exclude.add("Default");
        exclude.add("Forge Mod Loader");
        exclude.add("GL Setup");
        exclude.add("Minecraft Forge");
    }


    /**
     * Called to convert the loaded data into a pie chart data array
     *
     * @return
     */
    public ObservableList<PieChart.Data> buildPieChartData()
    {
        pieChartData.clear();

        for (ModData data : this.data.values())
        {
            if (!excludeFromDisplay(data.modName))
            {
                System.out.println("Mod: " + data.modName + "  time " + data.getTime());
                for (Map.Entry<String, Long> entry : data.loadTimes.entrySet())
                {
                    System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
                }
                pieChartData.add(new PieChart.Data(data.modName, data.getTime()));
            }
        }

        return pieChartData;
    }

    public boolean excludeFromDisplay(String modName)
    {
        for (String name : exclude)
        {
            if (modName.equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public void loadDataFromFile(File file)
    {
        //TODO create progress bar pop up window (lines / max lines)
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line = br.readLine();

            while (line != null)
            {
                readLine(line);
                line = br.readLine();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Called to read/parse a single line from a log
     *
     * @param inputLine
     */
    protected void readLine(String inputLine)
    {
        // input : [03:57:14] [main/DEBUG] [FML/]: Bar Step: ModIdMapping - Actually Additions took 0.000s

        //We only care about lines containing bar step
        if (inputLine.contains("Bar Step"))
        {
            //Remove: [03:57:14] [main/DEBUG] [FML/]:
            String line = inputLine.substring(inputLine.indexOf("Bar Step:") + 9, inputLine.length()); //TODO do with regex

            //Get bar action
            String action = line.substring(0, line.indexOf("-")).trim(); //TODO do with regex
            String remains = line.substring(line.indexOf("-") + 1, line.length()).trim(); //TODO do with regex
            //Get mod/package running

            //Match mod name
            Matcher m = modIDPattern.matcher(remains);
            if (m.find())
            {
                String modName = m.group(0).trim();

                //Remove resource name prefix
                if (modName.contains("FMLFileResourcePack"))
                {
                    modName = modName.replace("FMLFileResourcePack:", "");
                }

                //Match time
                m = loadTimePattern.matcher(remains);
                if (m.find())
                {
                    String loadTimeString = m.group(0).replace("s", "").trim();


                    //Get or build data
                    ModData modData;
                    if (!data.containsKey(modName))
                    {
                        modData = new ModData(modName);
                        data.put(modName, modData);
                    }
                    else
                    {
                        modData = data.get(modName);
                    }

                    //Add time entry to data
                    modData.addEntry(action, loadTimeString);
                }
            }
        }
    }

    public static void main(String... args)
    {
        LogParser parser = new LogParser();
        parser.readLine("[03:57:14] [main/DEBUG] [FML/]: Bar Step: ModIdMapping - Actually Additions took 0.000s");
    }
}
