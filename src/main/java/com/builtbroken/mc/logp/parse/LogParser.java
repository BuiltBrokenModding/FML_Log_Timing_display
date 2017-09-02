package com.builtbroken.mc.logp.parse;

import com.builtbroken.mc.logp.data.ModData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2017.
 */
public class LogParser
{
    Pattern modIDPattern = Pattern.compile("^.*(?=(took))");
    Pattern loadTimePattern = Pattern.compile("(?<=took).*");

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    HashMap<String, ModData> data = new HashMap();


    public ObservableList<PieChart.Data> buildChartData()
    {
        pieChartData.clear();

        pieChartData.add(new PieChart.Data("Grapefruit", 13));
        pieChartData.add(new PieChart.Data("Oranges", 25));
        pieChartData.add(new PieChart.Data("Plums", 10));
        pieChartData.add(new PieChart.Data("Pears", 22));
        pieChartData.add(new PieChart.Data("Apples", 30));

        return pieChartData;
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

    protected void readLine(String inputLine)
    {
        //TODO do with regex
        // input : [03:57:14] [main/DEBUG] [FML/]: Bar Step: ModIdMapping - Actually Additions took 0.000s

        if (inputLine.contains("Bar Step"))
        {
            //Remove: [03:57:14] [main/DEBUG] [FML/]:
            String line = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

            //Get bar action
            String action = line.substring(0, line.indexOf("-")).trim();
            String remains = line.substring(line.indexOf("-") + 1, line.length()).trim();
            //Get mod/package running

            Matcher m = modIDPattern.matcher(remains);
            if(m.find())
            {
                String mod = m.group(0);
                m = loadTimePattern.matcher(remains);
                if(m.find())
                {
                    String load = m.group(0);

                    ModData modData;

                    if(!data.containsKey(mod))
                    {
                        modData = new ModData(mod);
                        data.put(mod, modData);
                    }
                    else
                    {
                        modData = data.get(mod);
                    }

                    modData.addEntry(action, load);
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
