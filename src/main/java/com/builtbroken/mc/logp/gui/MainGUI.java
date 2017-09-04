package com.builtbroken.mc.logp.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Primary handler for the GUI
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2017.
 */
public class MainGUI extends Application
{
    public Controller controller;
    public Stage stage;

    public PieChart pieChart;
    public ListView chartList;
    public TextArea chartDataTextArea;

    public static void main(String... args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        this.stage = stage;

        //Load file
        URL guiFile = getClass().getClassLoader().getResource("fx/main.fxml");
        FXMLLoader loader = new FXMLLoader(guiFile);
        Parent root = loader.load();

        //Create scene
        Scene scene = new Scene(root, 1000, 800);

        //Load controller
        controller = loader.getController();
        controller.mainGUI = this;

        //Find pie chart
        pieChart = (PieChart) scene.lookup("#pieChartOne"); //TODO move to FXML inject
        chartDataTextArea = (TextArea) scene.lookup("#chartDataTestArea");//TODO move to FXML inject

        //Get data list
        chartList = (ListView) scene.lookup("#entryList");//TODO move to FXML inject

        //Set title
        stage.setTitle("FML Load Time Visualizer by DarkGuardsman");

        //Load scene into stage and show
        stage.setScene(scene);
        stage.show();
    }
}
