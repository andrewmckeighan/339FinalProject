package fileio.local;

import javafx.stage.*;

import java.io.File;

/**
 * Created by Squiggs on 12/1/2016.
 */
public class Explorer {

    private FileChooser fc;

    public Explorer() {
        this("Open File");
    }

    public Explorer(String title) {
        this(title, new File(System.getProperty("user.home")));
    }

    public Explorer(String title, File initialDir) {
        this(title, initialDir, null);
    }

    public Explorer(String title, File initialDir, FileChooser.ExtensionFilter... extentions) {
        fc = new FileChooser();
        fc.setTitle(title);
        fc.setInitialDirectory(initialDir);
        fc.getExtensionFilters().addAll(extentions);
    }

    public File open(Stage context) {
        return fc.showOpenDialog(context);
    }

    public void close() {

    }
}
