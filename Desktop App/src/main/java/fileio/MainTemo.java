package fileio;

import data.Batch;
import data.Question;
import fileio.local.SaveFormat;

import java.io.File;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class MainTemo {
    public static void main(String[] args) {
        Batch exampleData = new Batch();

        exampleData.putString("Project Name", "Stupid Questions");
        exampleData.putQuestion("Question1", new Question().ask("What is life's goal?").withAnswers("There is none", "Go home"));

        File saveLocation = new File("exampleFile.txt");

        SaveFormat.save(exampleData, saveLocation);
        System.out.println("Saved");

        exampleData = SaveFormat.load(saveLocation);

        System.out.println(exampleData.toString());
    }
}
