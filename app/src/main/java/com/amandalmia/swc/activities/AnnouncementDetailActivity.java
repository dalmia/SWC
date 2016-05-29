package com.amandalmia.swc.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amandalmia.swc.app.APIParameters;
import com.amandalmia.swc.R;

public class AnnouncementDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Announcement");
        Bundle values=  getIntent().getExtras();
        if(values!=null){
            ((TextView)findViewById(R.id.notice_title)).setText(values.getString(APIParameters.TITLE));
            ((TextView)findViewById(R.id.notice_description)).setText(values.getString(APIParameters.DESCRIPTION));
        }
    }

}
