package data;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Squiggs on 12/1/2016.
 */
public class Batch extends HashMap<String, Serializable> implements Serializable{

    public static final long serialVersionUID = 4637745574l;

    public Batch put(String key, Serializable s) {
        super.put(key, s);
        return this;
    }

    public Batch putString(String key, String str){
        put(key, str);
        return this;
    }

    public String getString(String key){
        Object o = get(key);
        if(o!=null && o instanceof String)
            return (String) o;
        return null;
    }

    public Batch putInteger(String key, int anInt){
        put(key, anInt);
        return this;
    }

    public Integer getInteger(String key){
        Object o = get(key);
        if(o!=null && o instanceof Integer)
            return (Integer) o;
        return null;
    }

    public Batch putDouble(String key, double aDouble){
        put(key, aDouble);
        return this;
    }

    public Double getDouble(String key){
        Object o = get(key);
        if(o!=null && o instanceof Double)
            return (Double) o;
        return null;
    }

    public Batch putBoolean(String key, boolean aBoolean){
        put(key, aBoolean);
        return this;
    }

    public Boolean getBoolean(String key){
        Object o = get(key);
        if(o!=null && o instanceof Boolean)
            return (Boolean) o;
        return null;
    }

    public Batch putQuestion(String key, Question aQuestion){
        put(key, aQuestion);
        return this;
    }

    public Question getQuestion(String key){
        Object o = get(key);
        if(o!=null && o instanceof Question)
            return (Question) o;
        return null;
    }

    public Batch putBatch(String key, Batch aBatch) {
        put(key, aBatch);
        return this;
    }

    public Batch getBatch(String key) {
        Serializable o = get(key);
        if(o != null && o instanceof Batch)
            return (Batch) o;
        return null;
    }

    public Batch putFile(String key, File aFile) {
        put(key, aFile);
        return this;
    }

    public File getFile(String key) {
        Serializable o = get(key);
        if(o != null && o instanceof File)
            return (File) o;
        return null;
    }
}
