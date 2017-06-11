package com.yl.yuanlu.whatsnext.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by LUYUAN on 6/5/2017.
 */

public class ModelUtils {

    private static Gson gson = new Gson();
    private static SharedPreferences sp;

    public static void save_to_sp(String pref_name, Context context, String jsonString, String key) {
        sp = context.getSharedPreferences(pref_name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, jsonString);
        editor.apply();
    }

    public static String load_from_sp(String pref_name, Context context, String key) {
        sp = context.getSharedPreferences(pref_name, context.MODE_PRIVATE);
        String jsonString = sp.getString(key, null);  //null is default value, will return null if no key present
        return jsonString;
    }

    public static void delete_from_sp(String pref_name, Context context, String key) {
        sp = context.getSharedPreferences(pref_name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public static <T> T toObject(String jsonString, TypeToken<T> typeToken) {
        return gson.fromJson(jsonString, typeToken.getType());
    }

    public static <T> String toString(T object, TypeToken<T> typeToken) {
        return gson.toJson(object, typeToken.getType());
    }

}
