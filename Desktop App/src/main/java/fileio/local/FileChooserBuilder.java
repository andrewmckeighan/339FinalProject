package fileio.local;

import data.Batch;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

/**
 * FileChooserBuilder.java is a class that is a facade for the FileChooser class in javafx.
 *
 * This class was designed to allow a user to easily create and modify this class to be any file chooser for any implementation.
 * It also narrows down exactly what our project needs by only having methods that are necessary. Other methods are hidden.
 *
 * Also, this class restricts the opening and saving of files to only be serialized Batch objects
 */
public class FileChooserBuilder {

    private FileChooser fc;

    public FileChooserBuilder() {
        fc = new FileChooser();
        title("File Chooser");
        initialDirectory(new File(System.getProperty("user.home")));
        initialFileName("Untitled");
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Project File", "*.qab"));
    }

    public Batch openAFile(Stage context) {
        File saveLocation = fc.showOpenDialog(context);

        if(saveLocation == null)
            return null;

        Batch project = null;

        try {
            FileInputStream fileIn = new FileInputStream(saveLocation);
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

    /**
     * Saves projectInfo to the saveLocation. This class will throw a
     * @param saveData A batch file that will be saved to the file. This object is exactly what is loaded in the load() method
     * @param context The stage of the main UI thread to show the dialogue
     * @return true if the file was saved, or false if the file wasn't saved
     */
    public boolean saveAFile(Batch saveData, Stage context) {
        File saveLocation = fc.showSaveDialog(context);

        if(saveLocation == null)
            return false;

        try {
            FileOutputStream fileOut = new FileOutputStream(saveLocation);
            ObjectOutputStream object = new ObjectOutputStream(fileOut);
            object.writeObject(saveData);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public FileChooserBuilder title(String title) {
        fc.setTitle(title);
        return this;
    }

    public FileChooserBuilder initialDirectory(File initDir) {
        fc.setInitialDirectory(initDir);
        return this;
    }

    @Deprecated
    public FileChooserBuilder extension(FileChooser.ExtensionFilter... filters) {
        fc.getExtensionFilters().addAll(filters);
        return this;
    }

    public FileChooserBuilder initialFileName(String name) {
        fc.setInitialFileName(name);
        return this;
    }
}
