package com.amandalmia.swc;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.amandalmia.swc.activities.LoginActivity;
import com.amandalmia.swc.fragments.AboutBranchesFragment;
import com.amandalmia.swc.fragments.AnnouncementFragment;
import com.amandalmia.swc.app.CommonUtilities;
import com.amandalmia.swc.fragments.DocumentRequiredFragment;
import com.amandalmia.swc.fragments.HomeFragment;
import com.amandalmia.swc.app.ServerUtilities;
import com.amandalmia.swc.storage.SQLiteHandler;
import com.amandalmia.swc.storage.SessionManager;
import com.google.android.gcm.GCMRegistrar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivity extends AppCompatActivity {

    public static TabLayout mTabLayout;
    public static ViewPager mViewPager;
    public static RelativeLayout tutorial;
    public static String[] subjects = new String[]{"Home", "Announcements", "Document Required", "About the Branches"};
    AsyncTask<Void, Void, Void> mRegisterTask;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupGCM();
        sessionManager  = new SessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        mViewPager = (ViewPager) findViewById(R.id.homework_viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mTabLayout = (TabLayout) findViewById(R.id.homework_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();

                case 1:
                    return new AnnouncementFragment();

                case 2:
                    return new DocumentRequiredFragment();

                case 3:
                    return new AboutBranchesFragment();
            }

            return null;

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return subjects[0];
                case 1:
                    return subjects[1];
                case 2:
                    return subjects[2];
                case 3:
                    return subjects[3];

            }
            return null;
        }
    }

    public void setupGCM() {

        GCMRegistrar.checkDevice(getApplicationContext());

        // Make sure the manifest was properly set - comment out getApplicationContext() line
        // while developing the app, then uncomment it when it's ready.
        //GCMRegistrar.checkManifest(getApplicationContext());

        //registerReceiver(mHandleMessageReceiver, new IntentFilter(
        //     DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
        // Check if regId already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(getApplicationContext(), CommonUtilities.SENDER_ID);

        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
                // Skips registration.
                // Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = getApplicationContext();
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server updates the student regId
                        Log.d("Here", "Here");
                         ServerUtilities.register(context, sessionManager.getKeyUsername(), regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
            GCMRegistrar.onDestroy(getApplicationContext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                sessionManager.clearAll();
                sessionManager.setKeyIsFirstRun(true);
                new SQLiteHandler(getApplicationContext()).deleteUsers();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
