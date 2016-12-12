package ui.results;

import data.Batch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class ResultsView extends Application
{
    private Batch results;
    private int defaultSize;

    public ResultsView(Batch results, int defaultSize) {
        this.results = results;
        this.defaultSize = defaultSize;
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Results from your Question");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

        barChart.setTitle("Your results");
        xAxis.setLabel("Question Number");
        yAxis.setLabel("Number of responses");

        XYChart.Series<String, Number> ser = new XYChart.Series<String, Number>();
        ser.setName("Number of Responses");

        System.out.println("ResultsView.start results=" + results);
        for(int x = 1; x <= defaultSize; x++) {
            String key = ""+x;
            if(results.containsKey(key)) {
                Number num = (Number) results.get(key);
                ser.getData().add(new XYChart.Data<String, Number>(key, num));
            } else {
                ser.getData().add(new XYChart.Data<String, Number>(key, 0));
            }
        }

        barChart.getData().add(ser);
        primaryStage.setScene(new Scene(barChart, 400, 400));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
