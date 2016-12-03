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
     * Make a local request to load data from the machine this program is running on. The only types of localRequests there are is
     * AppData.Local.LOAD_FILE and AppData.Local.SAVE_FILE.
     *
     * @param data - The data to save. If you are loading data, this parameter can be null
     * @param stage - The UI stage that this app will interrupt.
     * @param callback - the callback function. Depending on the type of request will determine the type of parameters sent on callback
     * @param type - The type of local request you are making.
     */
    public void localRequest(Batch data, Stage stage, final Callback callback, int type) {
        switch(type) {
            case Local.LOAD_FILE: {
                Batch fileChooserInfo = data.getBatch(Local.DIALOGUE_DATA);

                if(fileChooserInfo == null)
                    initFileChooser(data);
                else
                    initFileChooser(fileChooserInfo);

                callback.handle(type, files.openAFile(stage));
                break;
            }
            case Local.SAVE_FILE:
                Batch fileChooserInfo = data.getBatch(Local.DIALOGUE_DATA);

                if(fileChooserInfo == null)
                    initFileChooser(data);
                else
                    initFileChooser(fileChooserInfo);

                Batch saveData = data.getBatch(Local.SAVE_DATA);

                if(saveData == null)
                    throw new IllegalArgumentException("saveData not found. Make sure the Batch object you're passing contains a batch object with the key AppData.Local.SAVE_DATA.");

                Batch returnData = new Batch().putBoolean(Local.SUCCESSFUL_SAVE, files.saveAFile(saveData, stage));

                callback.handle(type, returnData);
                break;
            default:
                throw new IllegalArgumentException("Local Request #"+type+" is not a recognized request.");
        }
    }

    private void initFileChooser(Batch data) {
        files = new FileChooserBuilder();

        if(data == null)
            return;

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

        /**
         * Key for a boolean that will tell you whether a successful save happened or not.
         * Gives true when a save is successful, false if it was unsuccessful (this includes canceling the save operation)
         * This key will be returned when you make a localRequest() with type SAVE_FILE.
         */
        public static final String SUCCESSFUL_SAVE = "j3UvTExT2V";

        /**
         * Key for a Batch object that tells the localRequest() method what is the data to send.
         * When trying to save data, save all the data with this key. The data will be extracted and saved from there.
         * This key is never returned to the user.
         * This key is mandatory when you pass the key SAVE_FILE.
         */
        public static final String SAVE_DATA = "Np7xqaM8Lj";

        /**
         * All the Dialogue constants here are for setting the settings for the dialogue.
         * None of these are mandatory.
         * DIALOGUE_DATA is a key for a Batch object that contains the other DIALOGUE constants.
         * This key is not mandatory for sending Dialogue settings as data, but it is helpful so you can create one batch file for all saving and loading.
         */
        public static final String DIALOGUE_DATA = "tHktFLN8DM";

        /**
         * The title of the Dialogue that will open.
         * This is not mandatory, but if not sent, a default title will be used.
         */
        public static final String DIALOGUE_TITLE = "xSg6XhVO2c";

        /**
         * The initial directory that the dialogue will open to.
         * This is not mandatory, but if not, a default value will be used.
         */
        public static final String DIALOGUE_INITIAL_DIR = "RxkvKNUqZH";

        /**
         * The initial file name that will be shown to the user once the window opens.
         * This is not mandatory, but if not, a default value will be used.
         */
        public static final String DIALOGUE_INITIAL_FILE_NAME = "eO0Euvum1w";
    }
}
