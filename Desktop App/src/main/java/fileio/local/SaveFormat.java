package fileio.local;

import data.Batch;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Squiggs on 12/1/2016.
 */
public class SaveFormat {
    public static void save(Batch projectInfo, File saveLocation) {
        try {
            FileOutputStream fileOut = new FileOutputStream(saveLocation);
            ObjectOutputStream object = new ObjectOutputStream(fileOut);
            object.writeObject(projectInfo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Batch load(File projectLocation) {
        if(projectLocation == null)
            throw new IllegalArgumentException("projectLocation is null");

        Batch project = null;

        try {
           FileInputStream fileIn = new FileInputStream(projectLocation);
           ObjectInputStream object = new ObjectInputStream(fileIn);

           project = (Batch) object.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return project;
    }
}
