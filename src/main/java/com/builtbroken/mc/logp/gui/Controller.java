package com.builtbroken.mc.logp.gui;

import com.builtbroken.mc.logp.data.ModData;
import com.builtbroken.mc.logp.data.ModDataTimeSorter;
import com.builtbroken.mc.logp.parse.LogParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Handles interaction with the GUI
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2017.
 */
public class Controller implements Initializable
{
    public MainGUI mainGUI;

    public final FileChooser fileChooser = new FileChooser();
    public final LogParser parser = new LogParser();

    DecimalFormat percentFormat = new DecimalFormat("##.##%");

    @FXML
    public ComboBox chartTypeComboBox;

    @FXML
    public CheckBox excludeForgeCheckBox;

    @FXML
    public CheckBox compressModsCheckBox;

    @FXML
    public Button reloadChartButton;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Limit file types
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files", "*.txt", "*.log");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    @FXML
    public void loadModData(ActionEvent actionEvent)
    {
        //Reset component states
        reloadChartButton.setDisable(true);

        File file = fileChooser.showOpenDialog(mainGUI.stage);
        if (file != null)
        {
            parser.loadDataFromFile(file);
            loadPieChartDisplay();
            loadModDataList();
            createTextOutput();
            reloadChartButton.setDisable(false);
        }
        else
        {
            //TODO display error?
        }
    }

    @FXML
    public void reloadChartDisplay(ActionEvent actionEvent)
    {
        loadPieChartDisplay();
    }

    protected void loadPieChartDisplay()
    {
        mainGUI.pieChart.setData(parser.buildPieChartData(excludeForgeCheckBox.isSelected(), compressModsCheckBox.isSelected()));
    }

    protected void loadModDataList()
    {
        mainGUI.chartList.getItems().clear();

        for (PieChart.Data data : mainGUI.pieChart.getData())
        {
            mainGUI.chartList.getItems().add(data.getName()); //TODO create custom factory to display with color
        }
    }

    protected void createTextOutput()
    {
        StringBuilder chartTextData = new StringBuilder();
        chartTextData.append("Load data information\n");
        chartTextData.append("--------------------------------------\n");
        chartTextData.append("Entries: " + parser.data.values().size() + "\n");

        long actualLoadTime = 0;
        long excludedLoadIime = 0;
        long totalTime = 0;

        List<ModData> actualData = new ArrayList();
        List<ModData> excludedData = new ArrayList();
        for (ModData data : parser.data.values())
        {
            if (!parser.excludeFromDisplay(data.modName))
            {
                actualData.add(data);
                actualLoadTime += data.getTime();
            }
            else
            {
                excludedData.add(data);
                excludedLoadIime += data.getTime();
            }
        }

        totalTime = actualLoadTime + excludedLoadIime;

        //Sort values
        Collections.sort(actualData, new ModDataTimeSorter());
        Collections.sort(excludedData, new ModDataTimeSorter());

        //Output totals for time
        chartTextData.append("Total Time: " + totalTime + "ms \n");
        chartTextData.append("Excluded Time: " + excludedLoadIime + "ms \n");
        chartTextData.append("Actual Time: " + actualLoadTime + "ms \n");

        //Output excluded entry data
        chartTextData.append("--------------------------------------\n");
        chartTextData.append("Excluded: " + excludedData.size() + "\n");
        for (ModData data : excludedData)
        {
            float percentExcluded = (float) data.getTime() / (float) excludedLoadIime;
            float percentTotal = (float) data.getTime() / (float) totalTime;
            chartTextData.append(data.modName + ": " + formatTime(data.getTime()) + "ms \n");
            chartTextData.append("\t Time percent: " + formatPercent(percentExcluded) + " \n");
            chartTextData.append("\t Total time percent: " + formatPercent(percentTotal) + " \n");
        }

        //Output actual entry data
        chartTextData.append("--------------------------------------\n");
        chartTextData.append("Actual: " + excludedData.size() + "\n");
        for (ModData data : actualData)
        {
            float percentExcluded = (float) data.getTime() / (float) actualLoadTime;
            float percentTotal = (float) data.getTime() / (float) totalTime;
            chartTextData.append(data.modName + ": " + formatTime(data.getTime()) + "ms \n");
            chartTextData.append("\t Time percent: " + formatPercent(percentExcluded) + " \n");
            chartTextData.append("\t Total time percent: " + formatPercent(percentTotal) + " \n");
        }

        //Build and add to display
        mainGUI.chartDataTextArea.setText(chartTextData.toString());
    }

    protected String formatTime(long time)
    {
        return "" + time; //TODO format
    }

    protected String formatPercent(float time)
    {
        if (time <= 0.001)
        {
            return "less than a percent";
        }
        return percentFormat.format(time);
    }
}
