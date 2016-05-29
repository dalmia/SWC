package com.amandalmia.swc.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


/**
 * Shared Preferences Class used for storing all session management values
 * like keeping the user logged in, school Name, etc.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "SchoolMate";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_SAVED_USERNAME = "savedUsername";

    private static final String KEY_NAV_DRAWER_SELECTED_ITEM = "selectedItem";
    private static final String KEY_IS_FIRST_RUN_DONE = "isFirstRun";
    private static final String KEY_IS_CHOICE_MADE = "isChoiceMade";
    private static final String KEY_CHOICE = "choice";
    private static final String KEY_NETWORK_INFO = "network_info";
    private static final String KEY_SCHOOL_NAME = "schoolName";
    private static final String KEY_REG_ID = "regId";
    private static final String KEY_SCROLL_POSTION = "scrollPosition";
    private static final String KEY_CHOICE_SELECT = "choiceSelect";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_HOMEWORK = "homework";
    private static final String KEY_DASHBOARD = "dashboard";
    private static final String KEY_NOTICE = "notice";
    private static final String KEY_TEACHER_NOTE= "teacherNote";
    private static final String KEY_ATTENDANCE = "attendance";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_BROADCAST = "broadcast";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);


        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setKeyUsername(String username) {

        editor.putString(KEY_USERNAME, username);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setChoiceSeen(boolean flag) {

        editor.putBoolean(KEY_CHOICE_SELECT, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setUsernames(String username) {
        int flag = 0;
        String oldUsername = getUsername();
        String[] usernames = oldUsername.split(",");
        for(int i = 0; i < usernames.length; i++){
            if(usernames[i].equals(username)){
                flag = 1;
                break;
            }
        }

        if(flag==0) {
            if (!oldUsername.equals("")) {
                editor.putString(KEY_SAVED_USERNAME, oldUsername + "," + username);
            } else {
                editor.putString(KEY_SAVED_USERNAME, username);
            }
            editor.commit();

            Log.d(TAG, "User login session modified!");
        }
    }

    public void setBroadcastSeen(boolean flag) {

        editor.putBoolean(KEY_BROADCAST, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }


    public void setLoginSeen(boolean flag) {

        editor.putBoolean(KEY_LOGIN, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setKeyDashboard(boolean flag) {

        editor.putBoolean(KEY_DASHBOARD, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setKeyHomework(boolean flag) {

        editor.putBoolean(KEY_HOMEWORK, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setKeyNotice(boolean flag) {

        editor.putBoolean(KEY_NOTICE, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setKeyAttendance(boolean flag) {

        editor.putBoolean(KEY_ATTENDANCE, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setTeacherNote(boolean flag) {

        editor.putBoolean(KEY_TEACHER_NOTE, flag);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }



    public void setKeyRegId(String regId) {

        editor.putString(KEY_REG_ID, regId);


        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setScrollPosition(String position) {

        editor.putString(KEY_SCROLL_POSTION, position);


        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setSelectedId(String id) {

        editor.putString(KEY_NAV_DRAWER_SELECTED_ITEM, id);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }


    public void setKeyIsFirstRun(boolean isFirstRun) {

        editor.putBoolean(KEY_IS_FIRST_RUN_DONE, isFirstRun);


        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setSchool(String school) {

        editor.putString(KEY_SCHOOL_NAME, school);


        editor.commit();

        Log.d(TAG, "User login session modified!");
    }


    public void setChoice(boolean isChoiceMade, String choice) {

        editor.putBoolean(KEY_IS_CHOICE_MADE, isChoiceMade);
        editor.putString(KEY_CHOICE, choice);

        editor.commit();


    }


    public void setNetState(boolean netState) {

        editor.putBoolean(KEY_NETWORK_INFO, netState);


        editor.commit();


    }


    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isChoiceSeen() {
        return pref.getBoolean(KEY_CHOICE_SELECT, false);
    }

    public boolean isLoginSeen() {
        return pref.getBoolean(KEY_LOGIN, false);
    }

    public boolean isDashboardSeen() {
        return pref.getBoolean(KEY_DASHBOARD, false);
    }

    public boolean isHomeworkSeen() {
        return pref.getBoolean(KEY_HOMEWORK, false);
    }

    public boolean isNoticeSeen() {
        return pref.getBoolean(KEY_NOTICE, false);
    }

    public boolean isBroadcastSeen() {
        return pref.getBoolean(KEY_BROADCAST, false);
    }

    public boolean isAttendanceSeen() {
        return pref.getBoolean(KEY_ATTENDANCE, false);
    }

    public boolean isTeacherNoteSeen() {
        return pref.getBoolean(KEY_TEACHER_NOTE, false);
    }

    public boolean isChoiceMade() {
        return pref.getBoolean(KEY_IS_CHOICE_MADE, false);
    }

    public String choice() {
        return pref.getString(KEY_CHOICE, "");
    }

    public boolean getKeyIsFirstRun() {
        return pref.getBoolean(KEY_IS_FIRST_RUN_DONE, false);
    }

    public String getSchoolName() {
        return pref.getString(KEY_SCHOOL_NAME, null);
    }


    public String getSelectedId() {
        return pref.getString(KEY_NAV_DRAWER_SELECTED_ITEM, "");
    }

    public String getKeyRegId() {
        return pref.getString(KEY_REG_ID, null);
    }

    public String getKeyUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getscrollPostion() {
        return pref.getString(KEY_SCROLL_POSTION, String.valueOf(0));
    }


    public String getUsername() {
        return pref.getString(KEY_SAVED_USERNAME, "");
    }

    public void clear_scrollPosition() {
        editor.remove(KEY_SCROLL_POSTION);
        editor.commit();
    }


    public void clearAll() {
        editor.clear();
        editor.commit();
    }

}
