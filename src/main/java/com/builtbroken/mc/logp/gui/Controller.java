package com.builtbroken.mc.logp.gui;

import com.builtbroken.mc.logp.parse.LogParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
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

    final FileChooser fileChooser = new FileChooser();
    final LogParser parser = new LogParser();


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //nothing to init
    }

    @FXML
    public void loadModData(ActionEvent actionEvent)
    {
        File file = fileChooser.showOpenDialog(mainGUI.stage);
        if (file != null)
        {
            parser.loadDataFromFile(file);
            mainGUI.chart.setData(parser.buildChartData());
        }
        else
        {
            //TODO display error?
        }
    }
}
