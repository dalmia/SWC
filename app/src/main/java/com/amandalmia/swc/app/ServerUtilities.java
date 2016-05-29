package com.amandalmia.swc.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.amandalmia.swc.R;
import com.amandalmia.swc.storage.SQLiteHandler;
import com.amandalmia.swc.storage.SessionManager;
import com.amandalmia.swc.MainActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.amandalmia.swc.app.APIParameters.ERROR;
import static com.amandalmia.swc.app.APIParameters.ERROR_FALSE;
import static com.amandalmia.swc.app.APIParameters.ERROR_MESSAGE;


/**
 * Handling GCM registrations on our server for
 * the Student Side. When a new registration Id
 * is assigned to a student, register function
 * is called to register the user's GCM Registration
 * Id on our server.
 */
public final class ServerUtilities {
    public static final String TAG = ServerUtilities.class.getSimpleName();
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    public static JSONObject jsonToSend;
    public static SessionManager sessionManager;
    public static SQLiteHandler db;
    public static Context contextReal;
    public static Intent notificationIntent;

    /**
     * Register this account/device pair within the server.
     */
    public static void register(final Context context, String username, final String regId) {
        contextReal = context;
        Log.d("Here", "too");

        sessionManager = new SessionManager(context);
        db = new SQLiteHandler(context.getApplicationContext());


        Log.i("TAG", "registering device (regId = " + regId + ")");
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("username", username);

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG, "Attempt #" + i + " to register");
            try {
                //displayMessage(context, context.getString(R.string.server_registering, i, MAX_ATTEMPTS));
                jsonToSend = new JSONObject(params);
                register_post();
                GCMRegistrar.setRegisteredOnServer(context, true);
                ///CommonUtilities.displayMessage(context, message);
                return;
            } catch (Exception e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = "Server register Error";
        //CommonUtilities.displayMessage(context, message);
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
        contextReal = context;

        Log.i(TAG, "unRegistering device (regId = " + regId + ")");
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            jsonToSend = new JSONObject(params);
            // unregister_post(regId);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = "Unregistered";
            //CommonUtilities.displayMessage(context, message);
        } catch (Exception e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a description to the device, it will get
            // a "NotRegistered" error description and should unregister the device.
            String message = "Server Unregister Error";
            //CommonUtilities.displayMessage(context, message);
        }
    }

    public static void register_post() {
        String tag_string_req = "req_register";
        Log.d("Sent", jsonToSend.toString());
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                "http://amandalmia18.16mb.com/swc/StudentGCMRegister.php", jsonToSend, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response Server", response.toString());

                try {
                    String error = response.getString(ERROR);
                    if (error.equals(ERROR_FALSE)) {

                        notificationIntent = new Intent(contextReal, MainActivity.class);
                        generateNotification(contextReal, contextReal.getString(R.string.app_name), contextReal.getString(R.string.welcome_message));

                    } else {
                        Toast.makeText(contextReal, response.getString(ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error submit review", "Login Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public ServerUtilities() {
        sessionManager = new SessionManager(contextReal);
    }

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

}
