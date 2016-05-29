package com.amandalmia.swc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.amandalmia.swc.MainActivity;
import com.amandalmia.swc.app.APIParameters;
import com.amandalmia.swc.app.AppController;
import com.amandalmia.swc.app.CommonUtilities;
import com.amandalmia.swc.R;
import com.amandalmia.swc.storage.SQLiteHandler;
import com.amandalmia.swc.storage.SessionManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.amandalmia.swc.app.APIParameters.ERROR;
import static com.amandalmia.swc.app.APIParameters.ERROR_MESSAGE;
import static com.amandalmia.swc.app.APIParameters.PASSWORD;
import static com.amandalmia.swc.app.APIParameters.USERNAME;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    String url, usernameValue, passwordValue;
    AutoCompleteTextView username;
    EditText password;
    ProgressBar progressBar;
    ImageView toggle;
    SessionManager sessionManager;
    SQLiteHandler db;
    RelativeLayout layout, loginLayout, overlay;
    int passwordVisibility = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiate();
        ArrayList<String> usernames = new ArrayList();
        String[] user1 = sessionManager.getUsername().split(",");
        if (user1 != null) {
            for (int q = 0; q < user1.length; q++) {
                usernames.add(user1[q]);
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, usernames);
        username.setAdapter(adapter);
        username.setThreshold(2);

        CommonUtilities.applyFont(this, findViewById(R.id.login_screen));
        Intent intent = null;
        password.setTransformationMethod(new PasswordTransformationMethod());
        if (sessionManager.isLoggedIn()) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
        if (password.length() > 0) {
            toggle.setVisibility(View.VISIBLE);
        }
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    toggle.setVisibility(View.VISIBLE);
                    password.setSelection(password.length());
                } else {
                    toggle.setVisibility(View.GONE);
                    password.setSelection(password.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void initiate() {
        toggle = (ImageView) findViewById(R.id.show_hide_password);
        username = (AutoCompleteTextView) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        layout = (RelativeLayout) findViewById(R.id.login_screen);
        loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
        overlay = (RelativeLayout) findViewById(R.id.overlay);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        sessionManager = new SessionManager(this);
        db = new SQLiteHandler(getApplicationContext());
    }

    public void login(View v) {
        usernameValue = username.getText().toString().trim();
        passwordValue = password.getText().toString().trim();
        if (!usernameValue.isEmpty() && !passwordValue.isEmpty()) {
            loginUserTask();

        } else {
            Snackbar.make(v, getString(R.string.login_detail_not_given), Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void loginUserTask() {
        progressBar.setVisibility(View.VISIBLE);
        username.setEnabled(false);
        password.setEnabled(false);
        overlay.setVisibility(View.VISIBLE);
        loginLayout.setClickable(false);


        String tag_string_req = "tag_login";
        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put(USERNAME, usernameValue);
        loginParams.put(PASSWORD, passwordValue);
        JSONObject credentialsJSON = new JSONObject(loginParams);
        Log.d(TAG, credentialsJSON.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                "http://amandalmia18.16mb.com/swc/student_login.php",
                credentialsJSON,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d(TAG, response.toString());
                            String error = response.getString(ERROR);
                            if (error.equals(APIParameters.ERROR_FALSE)) {
                                JSONObject user = new JSONObject(response.getString("user"));
                                sessionManager.setKeyUsername(usernameValue);
                                sessionManager.setUsernames(usernameValue);
                                JSONArray schedule = new JSONArray(user.getString("schedule"));
                                JSONArray announcements = new JSONArray(user.getString("announcements"));
                                for(int i = 0; i<schedule.length();i++){
                                    JSONObject daySchedule = schedule.getJSONObject(i);
                                    db.addSchedule(daySchedule.getString("day"),
                                            daySchedule.getString("time"),
                                            daySchedule.getString("venue"),
                                            daySchedule.getString("subject"));
                                }
                                for(int i = 0; i<announcements.length();i++){
                                    JSONObject announcement = announcements.getJSONObject(i);
                                    db.addAnnouncement(announcement.getString("title"),
                                            announcement.getString("description"));
                                }

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                sessionManager.setLogin(true);
                                finish();
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                overlay.setVisibility(View.GONE);
                                loginLayout.setClickable(true);
                                username.setEnabled(true);
                                password.setEnabled(true);
                                Snackbar.make(layout, response.getString(ERROR_MESSAGE), Snackbar.LENGTH_SHORT).show();

                            }
                        }  catch (Exception e) {
                            db.deleteUsers();
                            username.setEnabled(true);
                            password.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            overlay.setVisibility(View.GONE);
                            loginLayout.setClickable(true);
                            Snackbar.make(layout, getString(R.string.error_logging_in), Snackbar.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.GONE);
                loginLayout.setClickable(true);
                username.setEnabled(true);
                password.setEnabled(true);
                Snackbar.make(layout, getString(R.string.error_logging_in), Snackbar.LENGTH_SHORT).show();


            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }


    public void toggleVisibility(View view) {

        if (passwordVisibility == 0) {

            toggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide_password));
            password.setTransformationMethod(null);
            passwordVisibility = 1;

        } else {
            toggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_show_password));
            password.setTransformationMethod(new PasswordTransformationMethod());
            passwordVisibility = 0;
        }

    }

}

