package ui.results;

import com.sun.org.apache.regexp.internal.RE;
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

    public ResultsView() {
        results = new Batch()
            .putInteger("1", 2)
            .putInteger("2", 5)
            .putInteger("3", 3);
    }

    public ResultsView(Batch results) {
        this.results = results;
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

        for(int x = 1; true; x++) {
            String key = ""+x;
            if(results.containsKey(key)) {
                Number num = (Number) results.get(key);
                ser.getData().add(new XYChart.Data<String, Number>(key, num));
            } else {
                break;
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
