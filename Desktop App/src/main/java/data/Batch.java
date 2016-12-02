package data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Squiggs on 12/1/2016.
 */
public class Batch extends HashMap<String, Serializable>{

    public void putString(String key, String str){
        put(key, str);
    }

    public String getString(String key){
        Object o = get(key);
        if(o!=null && o instanceof String)
            return (String) o;
        return null;
    }

    public void putInteger(String key, int anInt){
        put(key, new Integer(anInt));
    }

    public Integer getInteger(String key){
        Object o = get(key);
        if(o!=null && o instanceof Integer)
            return (Integer) o;
        return null;
    }

    public void putDouble(String key, float aDouble){
        put(key, new Float(aDouble));
    }

    public Double getDouble(String key){
        Object o = get(key);
        if(o!=null && o instanceof Double)
            return (Double) o;
        return null;
    }

    public void putBoolean(String key, boolean aBoolean){
        put(key, new Boolean(aBoolean));
    }

    public Boolean getBoolean(String key){
        Object o = get(key);
        if(o!=null && o instanceof Boolean)
            return (Boolean) o;
        return null;
    }

    public void putQuestion(String key, Question aQuestion){
        put(key, aQuestion);
    }

    public Question getQuestion(String key){
        Object o = get(key);
        if(o!=null && o instanceof Question)
            return (Question) o;
        return null;
    }


}
