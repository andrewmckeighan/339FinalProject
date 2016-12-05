package data;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Project File:
        -Needs:
            A Batch full of Question objects
            A Batch full of different settings
                Session Key
                Which Question is being asked
                Last Known Save Location
                Last Known File Name
            A Batch full of Results
 */
public class Project {
    public static final String QUESTIONS = "QuestionsLinkedList";
    public static final String SETTINGS = "SettingsBatch";
    public class settings {
        public static final String SESSION_KEY = "SessionKeyString";
        public static final String CURRENT_QUESTION = "CurrentQuestionQuestion";
        public static final String LAST_KNOWN_SAVE_LOC = "LastKnownSaveLocFile";
        public static final String LAST_KNOWN_FILE_NAME = "LastKnownFileNameString";
    }
    public static final String RESULTS = "ResultsLinkedList";

    private Batch proj;
    public Project() {
        LinkedList<Question> questions = new LinkedList<Question>();

        //TODO: Fill out the type of results
        LinkedList<String> results = new LinkedList<String>();

        proj = new Batch().put(QUESTIONS, questions)
            .putBatch(SETTINGS, new Batch()
                    .putString(settings.SESSION_KEY, null)
                    .putQuestion(settings.CURRENT_QUESTION, null)
                    .putFile(settings.LAST_KNOWN_SAVE_LOC, null)
                    .putString(settings.LAST_KNOWN_FILE_NAME, null))
            .put(RESULTS, results);
    }

    public Project(Batch startingStructure) {
        if(!verifyProjectStructure(startingStructure))
            throw new IllegalArgumentException("Batch is not of project structure.");

        proj = startingStructure;
    }

    public static boolean verifyProjectStructure(Batch project) {
        { // Check to see if there is a Questions field
            if(!project.containsKey(QUESTIONS)) {
                return false;

            } else {
                if(!(project.get(QUESTIONS) instanceof LinkedList))
                    return false;
                else {
                    if(!listOnlyContains((LinkedList)project.get(QUESTIONS), Question.class))
                        return false;

                }
            }
        }

        { // Check to see if this project contains basic settings
            if (!project.containsKey(SETTINGS)) {
                return false;
            } else {
                Field[] settingsFields = Project.settings.class.getFields();
                Batch settings = project.getBatch(SETTINGS);

                //Loop through all fields
                for (Field f : settingsFields) {
                    Class<?> type = f.getType();
                    if (type == String.class) {
                        try {
                            String fieldValue = (String) f.get(null);
                            if (!settings.containsKey(fieldValue))
                                return false;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            }
        }

        { // Check to see if there is a Results field
            if(!project.containsKey(RESULTS)) {
                return false;

            } else {
                if(!(project.get(RESULTS) instanceof LinkedList))
                    return false;
                else {
                    if(!listOnlyContains((LinkedList)project.get(RESULTS), String.class))
                        return false;
                }
            }
        }


        return true;
    }

    private static boolean listOnlyContains(List<?> filters, Class<?> c) {
        for(Object o: filters) {
            if(!(c.isInstance(o)))
                return false;
        }
        return true;
    }

    public LinkedList<Question> questions()  {
        return (LinkedList<Question>) proj.get(QUESTIONS);
    }

    public Batch settings() {
        return proj.getBatch(SETTINGS);
    }

    public LinkedList<String> results() {
        return (LinkedList<String>) proj.get(RESULTS);
    }

    public Batch toBatch()
    {
        return proj;
    }

    @Override
    public String toString() {
        return "Project: " + proj.toString();
    }
}
