package util;

import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Roman on 13.12.2015.
 */
public class Util {

    /* extract JSON value as float with default -1 if key not found */
    public static float getJSONfloat(String key, JSONObject jsonObject) {
        Object obj = jsonObject.get(key);
        if(obj == null)
            return -1;

        if(obj instanceof Long) {
            return ((Long)obj).floatValue();
        } else if(obj instanceof Double) {
            return ((Double)obj).floatValue();
        } else if(obj instanceof String) {
            return Float.parseFloat((String)obj);
        } else {
            throw new RuntimeException("can not convert " + obj.getClass().getName() + " to float.");
        }
    }

    /* extract JSON value as float; return defaultValue if key not found */
    public static int getJSONint(String key, JSONObject jsonObject, int defaultValue) {
        Object obj = jsonObject.get(key);
        if(obj == null)
            return defaultValue;

        if(obj instanceof Long) {
            return ((Long)obj).intValue();
        } else if(obj instanceof Double) {
            return ((Double)obj).intValue();
        } else if(obj instanceof String) {
            return Integer.parseInt((String)obj);
        } else {
            throw new RuntimeException("can not convert " + obj.getClass().getName() + " to int.");
        }
    }

    public static Icon getIcon(String filename) {
        try {
            return new ImageIcon(ImageIO.read( ClassLoader.getSystemResource( "icons/" + filename + ".png" )));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(11);
        }
        return null;
    }
}
