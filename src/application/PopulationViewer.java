package application;

import ga.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.paint.*;
import javafx.scene.layout.*;

import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import java.util.ArrayList;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import javafx.scene.Parent;


public class PopulationViewer extends Application {
	public void start(Stage stage) throws IOException{
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("try.fxml"));
        AnchorPane root = fxml.load();
        
        PopulationViewerController controller = (PopulationViewerController)
        		fxml.getController();
        controller.init();
        
        stage.setTitle("populationViewr");
        stage.setScene(new Scene(root));
        stage.show();
	}
	
	 
	 public static void main(String[] args) {
		 Application.launch(args);
	 }
}
