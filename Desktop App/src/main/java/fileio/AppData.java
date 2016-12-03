package fileio;

import data.Batch;
import fileio.local.FileChooserBuilder;
import fileio.net.Connection;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class AppData {

    private final static AppData singleton = new AppData();

    public Connection client;
    private FileChooserBuilder files;


    private AppData() {
        /*try {
            client = new Connection(new URI("ws://localhost:80"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();*/
    }

    public static AppData send() {
        return singleton;
    }

    public void serverRequest(Batch serverData, Callback callback, int type) {

    }

    /**
     *
     *
     * @param data - The data to save. If you are loading data, this parameter can be null
     * @param callback - the callback function. Depending on the type of request will determine the type of parameters sent on callback
     * @param type - The type of local request you are making. Check Appdata.Local for different request types
     */
    public void localRequest(Batch data, Stage stage, final Callback callback, int type) {
        switch(type) {
            case Local.LOAD_FILE: {
                initFileChooser(data.getBatch(Local.DIALOGUE_DATA));

                callback.handle(type, files.openAFile(stage));
                break;
            }
            case Local.SAVE_FILE:

                initFileChooser(data.getBatch(Local.DIALOGUE_DATA));
                Batch saveData = data.getBatch(Local.SAVE_DATA);
                Batch returnData = new Batch().putBoolean(Local.SUCCESSFUL_SAVE, files.saveAFile(saveData, stage));

                callback.handle(type, returnData);
                break;
            default:
                throw new IllegalArgumentException("Local Request #"+type+" is not a recognized request.");
        }
    }

    private void initFileChooser(Batch data) {
        files = new FileChooserBuilder();

        //Get the title, if available
        {
            String title = data.getString(Local.DIALOGUE_TITLE);
            if (title != null)
                files.title(title);
        }

        //Get the initial directory, if available
        {
            Object initialDir = data.get(Local.DIALOGUE_INITIAL_DIR);
            if (initialDir instanceof File)
                files.initialDirectory((File) initialDir);
        }

        //Get a filter, if available. There can only be one filter
        {
            String filter_name = data.getString(Local.DIALOGUE_EXTENSION_FILTER_DESCRIPTION);
            Object filters = data.get(Local.DIALOGUE_EXTENSION_FILTER_TYPES);

            if (filter_name != null && filters instanceof List && instanceofStringList((List) filters)) {
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filter_name, (List<String>) filters);
                files.extension(extFilter);
            }
        }

        //Get the initial file name, if available
        {
            String fileName = data.getString(Local.DIALOGUE_INITIAL_FILE_NAME);
            if(fileName != null)
                files.initialFileName(fileName);
        }
    }

    private boolean instanceofStringList(List<?> filters) {
        for(Object o: filters) {
            if(!(o instanceof String))
                return false;
        }
        return true;
    }


    public interface Callback {
        void handle(int type, Batch response);
    }

    public class Local {
        public static final int SAVE_FILE = 1;
        public static final int LOAD_FILE = 2;
        public static final String SUCCESSFUL_SAVE = "j3UvTExT2V";
        public static final String SAVE_DATA = "Np7xqaM8Lj";
        public static final String DIALOGUE_DATA = "tHktFLN8DM";
        public static final String DIALOGUE_TITLE = "xSg6XhVO2c";
        public static final String DIALOGUE_INITIAL_DIR = "RxkvKNUqZH";
        public static final String DIALOGUE_EXTENSION_FILTER_DESCRIPTION = "UpjRzzf9rh";
        public static final String DIALOGUE_EXTENSION_FILTER_TYPES = "tkDCldIIBY";
        public static final String DIALOGUE_INITIAL_FILE_NAME = "eO0Euvum1w";
    }
}
