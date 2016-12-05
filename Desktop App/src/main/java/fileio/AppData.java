package fileio;

import data.Batch;
import fileio.local.FileChooserBuilder;
import fileio.net.SocketConnection;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Squiggs on 11/28/2016.
 */
public class AppData {

    private final static AppData singleton = new AppData();

    private FileChooserBuilder files;
    private SocketConnection server;
    private String serverKey = null;

    private AppData() {

    }

    public static AppData send() {
        return singleton;
    }

    /**
     * This method deals with a server request.
     *
     * Given a batch and a type, this method will send a server request for a specific purpose. Check ServerRequest for details.
     *
     * @param serverData - The data to send. This changes with each different type of request. Check AppData.ServerRequest for  different Batch formats
     * @param type - The type of server request. Check AppData.ServerRequest for the different request types and what the batch object requires.
     * @return true if the command was successfully executed, false otherwise
     * @throws IllegalStateException if a request is sent and you are not connected to the server
     */
    public boolean serverRequest(Batch serverData, int type) throws IllegalStateException{
        switch(type) {
            case Server.Request.SESSION_KEY:
                if(server.isConnected()) {
                    server.emit(SocketConnection.REQUEST_SESSION_KEY, null);
                    return true;
                } else {
                    throw new IllegalStateException("You have not connected. Please create a connect first.");
                }
                //break;
            case Server.Request.ASK_QUESTION:
                if(server.isConnected()) {
                    //TODO: Verify Serverdata only has one connection object

                    server.emit(SocketConnection.ASK_A_QUESTION, serverData);
                    return true;
                } else {
                    throw new IllegalStateException("You have not connected. Please create a connect first.");
                }
                //break;
            case Server.Request.END_QUESTION:
                if(server.isConnected()) {
                    server.emit(SocketConnection.RESOLVE_A_QUESTION, null);
                    return true;
                }
                else {
                    throw new IllegalStateException("You have not connected. Please create a connect first.");
                }
                //break;
            case Server.Request.CONNECT:
                try {
                    //TODO allow custom connections
                    server = new SocketConnection();
                    this.subscribeToServerRersponse(Server.Response.RECEIVE_SESSION_KEY, new Server.Response.Listener() {
                        public void call(Batch object) {
                            serverKey = object.getString(Server.Response.Data.SESSION_KEY);
                        }
                    });
                    server.connect();
                    return true;
                } catch(URISyntaxException e) {
                    return false;
                }
                //break;
            case Server.Request.DISCONNECT:
                server.disconnect();
                return true;
                //break;
        }
        return false;
    }
    public void subscribeToServerRersponse(String serverEventName, Server.Response.Listener listener) {
        server.on(serverEventName, listener);
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
            File initialDir = data.getFile(Local.DIALOGUE_INITIAL_DIR);
            if (initialDir != null)
                files.initialDirectory(initialDir);
        }

        //Get the initial file name, if available
        {
            String fileName = data.getString(Local.DIALOGUE_INITIAL_FILE_NAME);
            if(fileName != null)
                files.initialFileName(fileName);
        }
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

    public static final class Server {
        /**
         * This nested class deals with all server components that have to do with a Server Request
         */
        public static class Request {
            /**
             * Requests a SESSION_KEY from the server.
             * The Batch object is not taken into account when sending this request. It's contents can be anything (including null)
             * You can register a ServerResponse.Listener for the event RECEIVE_SESSION_KEY to get the session key you asked for
             */
            public static final int SESSION_KEY = 1;

            /**
             * Asks a question to the server
             * The Batch object must contain a Question object with the key ServerRequest.QUESTION. This question is what will be sent to the server.
             * The Batch object must also contain a String with the key ServerRequest.KEY. This is the SESSION_KEY that the server returned with the .
             * You can register a ServerResponse.Listener for the event RECEIVE_SENT_QA_CONFIRMATION to determine if the question was asked.
             */
            public static final int ASK_QUESTION = 2;

            /**
             * Tells the server to end the current asking question
             * The Batch object must contain a String with the key ServerRequest.KEY. This key is what determines.
             * You can register a ServerResponse.Listener for the event RECEIVE_RESULTS to get the results from the question you just ended
             */
            public static final int END_QUESTION = 3;

            //TODO finish documentation
            /**
             * Tells the server to disconnect from the server, thus nullifying your session key.
             */
            public static final int DISCONNECT = 4;
            public static final int CONNECT = 5;

            public static final String QUESTION = "5weDOw6jkP";
            public static final String KEY = "EcIc5CrEDz";
        }

        /**
         * This nested class deals with the responses you can get from a server request
         * This class is juts a mirror of SocketConnection's constants. They are put here so other files don't have to import SocketConnections
         */
        public static class Response {
            public interface Listener extends SocketConnection.Listener{}

            public static final String RECEIVE_SESSION_KEY = SocketConnection.GET_SESSION_KEY;
            public static final String RECEIVE_SENT_QA_CONFIRMATION = SocketConnection.GET_ASK_CONFIRMATION;
            public static final String RECEIVE_RESULTS = SocketConnection.GET_RESULTS;

            /**
             * This nested class deals with the values in a received Batch file from a response.
             */
            public static class Data {
                public static final String SESSION_KEY = SocketConnection.SESSION_KEY;
            }
        }


    }






}
