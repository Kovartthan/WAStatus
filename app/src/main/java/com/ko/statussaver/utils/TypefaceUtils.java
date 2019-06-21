package com.ko.statussaver.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class TypefaceUtils {
    public static final int FABRICA = 0;
    public static final int ROBOTO_BOLD = 1;
    public static final int ROBOTO_BOLD_ITALIC = 2;
    public static final int ROBOTO_ITALIC = 3;
    public static final int ROBOTO_LIGHT = 4;
    public static final int ROBOTO_LIGHT_ITALIC = 5;
    public static final int ROBOTO_MEDIUM = 6;
    public static final int ROBOTO_MEDIUM_ITALIC = 7;
    public static final int ROBOTO_REGULAR = 8;
    public static final int ROBOTO_THIN = 9;
    public static final int ROBOTO_THIN_ITALIC = 10;

    public static final String S_FABRICA = "Fabrica.otf";
    public static final String S_ROBOTO_BOLD = "roboto_bold.ttf";
    public static final String S_ROBOTO_BOLD_ITALIC = "roboto_bold_italic.ttf";
    public static final String S_ROBOTO_ITALIC = "roboto_italic.ttf";
    public static final String S_ROBOTO_LIGHT = "roboto_light.ttf";
    public static final String S_ROBOTO_LIGHT_ITALIC = "roboto_light_italic.ttf";
    public static final String S_ROBOTO_MEDIUM = "roboto_medium.ttf";
    public static final String S_ROBOTO_MEDIUM_ITALIC = "roboto_medium_italic.ttf";
    public static final String S_ROBOTO_REGULAR = "roboto_regular.ttf";
    public static final String S_ROBOTO_THIN = "roboto_thin.ttf";
    public static final String S_ROBOTO_THIN_ITALIC = "roboto_thin_italic.ttf";

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, int tf) {
        synchronized (cache) {
            String assetPath = null;
            switch (tf) {
                case FABRICA:
                    assetPath = S_FABRICA;
                    break;
                case ROBOTO_BOLD:
                    assetPath = S_ROBOTO_BOLD;
                    break;

                case ROBOTO_BOLD_ITALIC:
                    assetPath = S_ROBOTO_BOLD_ITALIC;
                    break;

                case ROBOTO_ITALIC:
                    assetPath = S_ROBOTO_ITALIC;
                    break;

                case ROBOTO_LIGHT:
                    assetPath = S_ROBOTO_LIGHT;
                    break;

                case ROBOTO_LIGHT_ITALIC:
                    assetPath = S_ROBOTO_LIGHT_ITALIC;
                    break;

                case ROBOTO_MEDIUM:
                    assetPath = S_ROBOTO_MEDIUM;
                    break;

                case ROBOTO_MEDIUM_ITALIC:
                    assetPath = S_ROBOTO_MEDIUM_ITALIC;
                    break;

                case ROBOTO_REGULAR:
                    assetPath = S_ROBOTO_REGULAR;
                    break;

                case ROBOTO_THIN:
                    assetPath = S_ROBOTO_THIN;
                    break;

                case ROBOTO_THIN_ITALIC:
                    assetPath = S_ROBOTO_THIN_ITALIC;
                    break;

                default:
                    //TODO assign the default font here
                    assetPath = S_ROBOTO_REGULAR;
                    break;
            }
            if (!cache.containsKey(assetPath)) {
                try {
//                    Log.e("Typeface", "create from asset typeface -- assetPath --> " + assetPath);
                    Typeface t = Typeface.createFromAsset(c.getAssets(), "fonts/" + assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
//                    Log.e("Typeface", "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}