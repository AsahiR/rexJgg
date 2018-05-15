package application;

import ga.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.paint.*;
import javafx.scene.shape.Sphere;
import javafx.scene.layout.*;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;

import javafx.animation.Animation.Status;



import java.io.IOException;

import javafx.fxml.Initializable;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlValue;

import com.sun.glass.ui.Size;

import javafx.scene.layout.*;

import javafx.stage.FileChooser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.Node;

import java.text.DecimalFormat;



public class PopulationViewerController {

	/**
	 * selection1
	 */
	@FXML
	TextField fStartTextField;
	@FXML
	TextField fEndTextField;
	@FXML
	TextField fByTextField;
	
	/**
	 * chart and sliders
	 */
	@FXML
	AnchorPane fDisplayAnchorPane;
    @FXML
     NumberAxis fXAxis = new NumberAxis();
    @FXML
     NumberAxis fYAxis = new NumberAxis();
    @FXML
     ScatterChart<Number,Number> fChart;
    @FXML
     Slider fXSlider;
    @FXML
     Slider fYSlider;
    
    /**
     * Bottom buttons
     */

    @FXML
     Button fSaveButton;
    @FXML
     Button fStopButton;
    @FXML
     Button fRestartButton;
	@FXML
	Button fSubmitButton;

	/**
	 * selection3
	 */

    @FXML
     ComboBox fFunctionComboBox;
    @FXML
     ComboBox fMethodComboBox;
    
    /**
     * Display
     */
    @FXML
    Label fGenLabel;
    @FXML
    Label fBestEvlLabel;
    
    /**
     * selection2
     */
    @FXML
    TextField fDimensionTextField;
    @FXML
    TextField fParentsTextField;
    @FXML
    TextField fKidsTextField;
    @FXML
    TextField fPopulationTextField;
    @FXML
    TextField fMinTextField;
    @FXML
    TextField fMaxTextField;

    /**
     * Animation
     */
	Timeline fTimeline;
	
	/**
	 * collection readfrom fSourceName. Populations.Parents.Kids.
	 */
    private TPopulation[] fPopulations;

    /**
     * name of file for displaying in chart.
     */
    private String fSourceName = "SourceName";
    
    /**
     * current generation.
     */
    private int fGeneration;

    /**
     *  use for fGeneration calculation
     */
	private int fStartGeneration;
	private int fEndGeneration;
	private int fByGeneration;
	private double fBestEvl = Double.NaN;

     
    /**
     * Parse TextFileds in selection container
     * and make fSourceName
     * @return fSourceName
     */
     String makeSourceName() {
    	 int dimension = parseTextFiled(2, fDimensionTextField);
    	 int parentsSize = parseTextFiled(dimension+1, fParentsTextField);
    	 int kidsSize = parseTextFiled(dimension * 2, fKidsTextField);
    	 int populationSize = parseTextFiled(dimension * 20 ,fPopulationTextField);
    	 double minElement = parseTextFiled(-5, fMinTextField);
    	 double maxElement = parseTextFiled(5, fMaxTextField);
    	 String functionName = (String)fFunctionComboBox.getValue();
    	 String methodName = (String)fMethodComboBox.getValue();
    	 fSourceName = "log/"+methodName+"_"+functionName+"_"+minElement+"to"+maxElement
    			 +"_"+"p"+parentsSize+"k"+kidsSize+"_Trl0_Evl";
    	 return fSourceName;
     }

    /**
     * Clicked submit button, parse TextFiles and start animation.
     */
	@FXML
	public void submitButtonClickedHandler() {
		// cannot receive event
		fStartGeneration = parseTextFiled(290, fStartTextField);
		fEndGeneration = parseTextFiled(800000, fEndTextField);
		fByGeneration = parseTextFiled(10, fByTextField);
		fSourceName = makeSourceName();
		fGeneration = fStartGeneration;
		startAnimation();
	}
	
	/**
	 * Stop Animation and save
	 */
	public void saveButtonClickedHandler() {
		stopButtonClickedHandler();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as");
		String[] tmp = fSourceName.split("/");
		// get baseName
		String destName = tmp[ Math.max(0, tmp.length - 1) ]+fGeneration+".png";
		fileChooser.setInitialFileName(destName);
		File file = fileChooser.showSaveDialog(null);
		/**
		 * Canseled on save dialog, do nothing
		 */
		if(file == null) {
			return;
		}
		WritableImage image = new WritableImage(600,600);
		WritableImage renderImage = fChart.snapshot(null, image);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(renderImage,null),
					"png", file);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Pause animation
	 */
	@FXML
	public void stopButtonClickedHandler() {
		if(fTimeline != null) {
            fTimeline.pause();
		}
	}
	
	/**
	 * Restart animation.
	 */
	@FXML
	public void restartButtonClickedHandler() {
		if (fTimeline != null &&
				fTimeline.getStatus() == Status.PAUSED) {
			fTimeline.play();
		}
	}
	
	/**
	 *  When not animation set, start animation.
	 */
	void startAnimation() {
		if(fTimeline != null) {
			return;
		}
		final int waitTime = 1000;
		fTimeline = new Timeline(new KeyFrame(Duration.millis(waitTime),
				new EventHandler<ActionEvent>() {
                  @Override
                  public void handle(ActionEvent event) {
                    if(fGeneration > fEndGeneration ||
                    	fGeneration < fStartGeneration) {
                    	fGeneration = fStartGeneration;
                    }
                    readData(fGeneration);
                	DecimalFormat df = new DecimalFormat("0.######E0#####");
                	setLabel(fBestEvlLabel, "Best: "+df.format(fBestEvl));
                	setLabel(fGenLabel, "G: "+fGeneration);
                    fGeneration +=  fByGeneration;
                  }
                }
		));
		fTimeline.setCycleCount(Timeline.INDEFINITE);
		fTimeline.play();
	}


	/**
	 *  Parse TextFiled.
	 * @param defaultValue Return defaultValue ,when parse failed.
	 * @param input Parsed TextFiled
	 * @return parsed integer or defaultValue
	 */
	 int parseTextFiled(int defaultValue, TextField input) {
		String str = input.getText();
		int result = defaultValue;
		if(str !=  null) {
			try {
				int temp = Integer.parseInt(str);
				result = temp;
			} catch (NumberFormatException e) {
			}
		}
		return result;
	}
	 
	 void setSlider(Slider slider, NumberAxis axis) {
		 slider.valueProperty().addListener(new ChangeListener<Number>() {
			 @Override
			 public void changed(ObservableValue<? extends Number> observable,
					 Number oldValue, Number newValue) {
				 sliderHandler(slider, axis);
			 }
		 });
	 }
	
	 /**
	  *  Called by Application object.
	  *  Set controller uncontrolled by fxml file.
	  */
	 void init(){
		 sliderHandler(fXSlider, fXAxis);
		 sliderHandler(fYSlider, fYAxis);
		 setSlider(fXSlider, fXAxis);
		 setSlider(fYSlider, fYAxis);
         fChart = new ScatterChart<Number,Number>(fXAxis,fYAxis);
         fDisplayAnchorPane.getChildren().add(fChart);
         fDisplayAnchorPane.setTopAnchor((Node)fChart, 0.0);
         fDisplayAnchorPane.setLeftAnchor((Node)fChart, 0.0);
         fDisplayAnchorPane.setRightAnchor((Node)fChart, 0.0);
         fDisplayAnchorPane.setBottomAnchor((Node)fChart, 0.0);
         setComboBox();

	 }
	 
	 /**
	  * Display generation and population data on chart
	  * @param generation
	  */
	  void readData(int generation){
		 fBestEvl = Double.NaN;
         try(BufferedReader br = new BufferedReader(new FileReader(fSourceName+generation+".txt"))){
             fPopulations = readFrom(br);
             fChart.getData().clear();
             setData(fGeneration, 0, "Population");
             setData(fGeneration, 1, "Parents");
             setData(fGeneration, 2, "Kids");
         } catch(IOException e) {
        	 /** 
        	  * File None,do nothing.
        	  */
         }
	 }
	  
		/**
		 * @param br
		 * @return TPopulation[] index 0: fPopulation, index 1: fParents, index 2: fKids
		 * @throws IOException
		 */
		public static TPopulation[] readFrom(BufferedReader br) throws IOException{
			int fieldNum = Integer.parseInt(br.readLine());
			TPopulation[] result = new TPopulation[fieldNum];
			for(int i = 0; i < fieldNum ; i++) {
				TPopulation tmp = new TPopulation();
	            tmp.readFrom(br);
	            result[i] = tmp;
			}
			return result;
		}

	 /**
	  *  Call this from readData()
	  * @param generation
	  * @param index population or parents or kids from 0 to 2
	  * @param populationName legendName
	  */
	  void setData(int generation, int index, String populationName){
        XYChart.Series series = new XYChart.Series();
        series.setName(populationName);
        
        TPopulation population = fPopulations[index];
        /**
         * sort for setting Tooltip
         */
        population.sortByEvaluationValue();

      	String format = "rank: %4d\n"+"eval: %f\n"+"(x,y): (%2.6f, %2.6f)\n";
        for(int i = 0; i < population.getPopulationSize(); i++){
        	TIndividual individual = population.getIndividual(i);
        	/**
        	 * invalid population
        	 */
        	if(Double.isNaN(individual.getEvaluationValue())) {
        		break;
        	}
            TVector vector = individual.getVector();
            double x = vector.getElement(0);
            double y = vector.getElement(1);
            Data<Number, Number> point = new XYChart.Data<Number, Number>(x, y);

            series.getData().add(point);
            point = (Data<Number, Number>)series.getData().get(i);
            /**
             * This line not effective for Node == null at this time
             */
            Tooltip.install(point.getNode(), 
            		new Tooltip(String.format(format,
            				i, individual.getEvaluationValue(), x,y)));

        }
        fChart.getData().add(series);
        /**
         * set Tooltip only if Population
         */
        if(index ==  0) {
        	fBestEvl = fPopulations[index].getIndividual(0).getEvaluationValue();
            setTooltip(index);
        }
	 }
	  
	  Label setLabel(Label label, String text){
		  label.setText(text);
		  return label;
	  }
	  
	  /**
	   * @param index Set tooltip on data of fPopulatins[index]
	   */
	  void setTooltip(int index) {
        	  ObservableList<Data<Number,Number>> dataList = fChart.getData().get(index).getData();
        	  int size = dataList.size();
        	  for(int i = 0; i < size; i++) {
              	String format = "rank: %4d\n"+"eval: %f\n"+"(x,y): (%2.6f, %2.6f)\n";
            	TIndividual individual = fPopulations[index].getIndividual(i);
                TVector vector = individual.getVector();
                double x = vector.getElement(0);
                double y = vector.getElement(1);

        		Data<Number,Number> data = dataList.get(i);
                Tooltip.install(data.getNode(), 
                		new Tooltip(String.format(format,
                				i, individual.getEvaluationValue(), data.getXValue(),data.getYValue())));
        	  }
	  }


	  /**
	   * Set axis from slider. Weight is pow(base, slider's value)
	   * @param slider
	   * @param axis
	   */
	  public void sliderHandler(Slider slider, NumberAxis axis) {
		  double base = 2;
		  double index = slider.getValue();
		  double weight = Math.pow(base,index);
		  setAxis(axis, weight, -5, 5, 1);
	  }
	  
	  /**
	   * Set axis from min to max by unit
	   * @param axis
	   * @param weight
	   * @param min
	   * @param max
	   * @param unit tick unit
	   */
	  public void setAxis(NumberAxis axis,
			  double weight, double min, double max, double unit) {
		  axis.setAutoRanging(false);
		  axis.setUpperBound(max * weight);
		  axis.setLowerBound(min * weight);
		  axis.setTickUnit(unit * weight);
	  }
	  
	  /**
	   * Set comboBoxes for function(eg.KTablet) and method(eg.JggRex)
	   */
	  void setComboBox() {
		  fFunctionComboBox.setItems(FXCollections.observableArrayList(EnumFunction.KTablet.toString(),
				  EnumFunction.Sphere.toString()));
		  fFunctionComboBox.setValue(EnumFunction.KTablet.toString());
		  fMethodComboBox.setItems(FXCollections.observableArrayList("RexJgg","ArexJgg","UndxMgg"));
		  fMethodComboBox.setValue("RexJgg");
	  }
	  
}