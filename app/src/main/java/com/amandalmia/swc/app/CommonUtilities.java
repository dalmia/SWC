package com.amandalmia.swc.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class CommonUtilities {
    public Context mContext;

    // Google project id
    public static final String SENDER_ID = "4754098247";

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "SchoolMate";


    public static final String DISPLAY_NOTICE_BOARD =
            "com.amandalmia.swc.DISPLAY_NOTICE_BOARD";


    public static final String EXTRA_MESSAGE = "description";

    /**
     * Notifies UI to display a description.
     * <p/>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param mContext application's mContext.
     * @param message  description to be displayed.
     */


    public static void displayNoticeBoard(Context mContext, String message) {
        Intent intent = new Intent(DISPLAY_NOTICE_BOARD);
        Log.d("Message", message);
        intent.putExtra(EXTRA_MESSAGE, message);
        mContext.sendBroadcast(intent);
    }


    public static String getTime(String sentTime) {
        String resultTime = null;
        Calendar calendar = Calendar.getInstance();
        long time = Long.parseLong(sentTime);
        long newTime = System.currentTimeMillis();
        calendar.setTimeInMillis(newTime);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(time);
        int sentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int sentMinute = calendar.get(Calendar.MINUTE);
        int sentMonth = calendar.get(Calendar.MONTH);
        int sentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (sentMonth == currentMonth) {
            if (currentDay - sentDay < 7) {
                if (currentHour - sentHour >= 0) {
                    String minute = null, hour = null;


                    hour = String.valueOf(sentHour);
                    minute = String.valueOf(sentMinute);

                    if(sentMinute<10){
                        minute = "0" + minute;
                    }
                    if(sentHour<10){
                        hour = "0" + hour;
                    }
                    resultTime = hour + ":" + minute;
                } else {
                    SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date;
                    try {
                        date = inFormat.parse(inFormat.format(calendar.getTime()));
                        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                        resultTime = outFormat.format(date).substring(0, 3);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                resultTime = sentDay + "/" + sentMonth;
            }
        } else {
            resultTime = sentDay + "/" + sentMonth;
        }


        return resultTime;
    }

    public static void applyFont(final Context context, final View root) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i));
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), "roboto_light.ttf"));
        } catch (Exception e) {
            Log.e("Font",  "Not found");
            e.printStackTrace();
        }
    }

}
  