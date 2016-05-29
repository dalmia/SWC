package com.amandalmia.swc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amandalmia.swc.activities.AnnouncementDetailActivity;
import com.amandalmia.swc.app.CommonUtilities;
import com.amandalmia.swc.app.ServerUtilities;
import com.amandalmia.swc.storage.SQLiteHandler;
import com.amandalmia.swc.storage.SessionManager;
import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import static com.amandalmia.swc.app.APIParameters.DESCRIPTION;
import static com.amandalmia.swc.app.APIParameters.TITLE;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";
    public static Intent notificationIntent;
    private static SessionManager sessionManager;
    SQLiteHandler db;
    String username;

    public GCMIntentService() {
        //Required initiation with the Sender ID
        super(CommonUtilities.SENDER_ID);
    }

    /**
     * Issues a notification to inform the user that server has sent a description.
     */
    private static void generateNotification(Context context, String notificationTitle, String notificationMessage) {

        // NotificationManager to handle Notifications
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // set intent so it does not start a new activity

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context,
                (int) System.currentTimeMillis(), notificationIntent, 0);


        Notification notification = new Notification.Builder(context)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSmallIcon(R.drawable.icon_big)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);

    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        sessionManager = new SessionManager(context);
        //displayMessage(context, "Your device registered with GCM");
        sessionManager.setKeyRegId(registrationId);
        ServerUtilities.register(context,sessionManager.getKeyUsername(), registrationId);
        notificationIntent = new Intent(context, MainActivity.class);


    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");

    }


    /**
     * Method called on Receiving a new description
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        sessionManager = new SessionManager(context.getApplicationContext());
        username = sessionManager.getKeyUsername();
        db = new SQLiteHandler(context.getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            db = new SQLiteHandler(context.getApplicationContext());
            Log.i(TAG, "Received description");
            sessionManager = new SessionManager(context.getApplicationContext());
            if (intent.getExtras().getString("registered") != null) {
                Log.d("ma","mAA");
            } else if (intent.getExtras().getString("notice") != null) {
                String notice = intent.getExtras().getString("notice");
                try {
                    JSONObject json_obj = new JSONObject(notice);
                    String title = json_obj.getString(TITLE);
                    String description = json_obj.getString(DESCRIPTION);
                    db.addAnnouncement(title, description);
                    CommonUtilities.displayNoticeBoard(context, notice);
                    Bundle bundle = new Bundle();
                    bundle.putString(TITLE, title);
                    bundle.putString(DESCRIPTION, description);
                    notificationIntent = new Intent(context, AnnouncementDetailActivity.class);
                    notificationIntent.putExtras(bundle);
                    generateNotification(context, "Announcement", title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * Method called on receiving a deleted description
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        //displayMessage(context, message);
        // notifies user
        // generateNotification(context, ,content);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        //displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log description
        Log.i(TAG, "Received recoverable error: " + errorId);
        // displayMessage(context, getString(R.string.gcm_recoverable_error,errorId));
        return super.onRecoverableError(context, errorId);
    }

}
