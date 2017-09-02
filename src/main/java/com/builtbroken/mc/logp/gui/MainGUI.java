package com.builtbroken.mc.logp.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
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
    public static void main(String... args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        URL guiFile = getClass().getClassLoader().getResource("fx/main.fxml");
        Parent root = FXMLLoader.load(guiFile);

        Scene scene = new Scene(root, 1000, 800);

        SplitPane splitPane = (SplitPane) scene.lookup("#splitPane");

        AnchorPane node = (AnchorPane) splitPane.getItems().get(1);

        PieChart chart = (PieChart) node.lookup("#pieChartOne");

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));

        chart.setData(pieChartData);

        stage.setTitle("Forge Load Time Display");
        stage.setScene(scene);
        stage.show();
    }
}
