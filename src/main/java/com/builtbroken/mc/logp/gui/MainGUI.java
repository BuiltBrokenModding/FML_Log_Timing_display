package com.builtbroken.mc.logp.gui;

import com.builtbroken.mc.logp.data.ModData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;

/**
 * Primary handler for the GUI
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/2/2017.
 */
public class MainGUI extends Application
{
    public HashMap<String, ModData> modLoadData = new HashMap();

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
        SplitPane splitPane = (SplitPane) scene.lookup("#splitPane");
        AnchorPane node = (AnchorPane) splitPane.getItems().get(1);
        pieChart = (PieChart) node.lookup("#pieChartOne");
        chartDataTextArea = (TextArea) node.lookup("#chartDataTestArea");

        //Get data list
        node = (AnchorPane) splitPane.getItems().get(0);
        chartList = (ListView) node.lookup("#entryList");

        //Set title
        stage.setTitle("Forge Load Time Display");

        //Load scene into stage and show
        stage.setScene(scene);
        stage.show();
    }
}
