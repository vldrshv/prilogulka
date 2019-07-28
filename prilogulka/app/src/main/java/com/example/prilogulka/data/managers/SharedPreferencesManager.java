package com.example.prilogulka.data.managers;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesManager {

    private final String SHARED_PREFERENCES_NAME = "userInfo";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        initUserInfoStorer(context);
    }

    /**
     *
     * @param keyInSharedPreferences - get value, that has this key
     * @return string value, connected to the input key
     */
    public String getStringFromSharedPreferences(String keyInSharedPreferences) {
        return sharedPreferences.getString(keyInSharedPreferences, "");
    }

    /**
     *
     * @param key - unique key for the value will make new note. The existing key will update the
     *            existing value.
     * @param stringToPut - the value connected to the key to be put in file
     */
    private void putInSharedPreferences(String key, String stringToPut) {
        editor.putString(key, stringToPut);
        editor.commit();
    }

    /**
     *
     * @param context - in which context you call this method. NonNull value. Should be defined
     *                necessarily.
     */
    private void initUserInfoStorer(@NonNull Context context){
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
        if (getActiveUser().equals(""))
            setFirstEnter(true);
    }

    public String getPreferencesName() {
        return SHARED_PREFERENCES_NAME;
    }

    /**
     *
     * @param email - email of active user
     */
    public void setActiveUser(String email){
        putInSharedPreferences("active_user", email);
    }

    /**
     *
     * @return email of active user
     */
    public String getActiveUser(){
        return getStringFromSharedPreferences("active_user");
    }

    /**
     *  @param coefficient - coefficient, o which WP multiplies if the video fully watched
     */
    public void setWPCoefficient(float coefficient) {
        putInSharedPreferences("coefficient", coefficient+"");
    }

    /**
     * @return value of WP coefficient
     */
    public float getWPCoefficient(){
        return Float.parseFloat(getStringFromSharedPreferences("coefficient"));
    }

    /**
     * @param wasSent - true if user has answered additional questionnaire
     */
    public void setQuestionnaire(boolean wasSent) {
        putInSharedPreferences("questionnaire", wasSent + "");
    }

    public boolean getQuestionnaire() {
        return Boolean.parseBoolean(getStringFromSharedPreferences("questionnaire"));
    }

    /**
     * @param firstEnter - true if user entered the app for the first time
     */
    public void setFirstEnter(boolean firstEnter) {
        putInSharedPreferences("firstEnter", firstEnter + "");
    }

    public boolean isFirstEnter() {
        return Boolean.parseBoolean(getStringFromSharedPreferences("firstEnter"));
    }
}
