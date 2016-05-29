package com.amandalmia.swc.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amandalmia.swc.R;

import static com.amandalmia.swc.app.APIParameters.DESCRIPTION;
import static com.amandalmia.swc.app.APIParameters.TITLE;

public class BranchDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About Branches");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle values = getIntent().getExtras();
        if (values != null) {

            ((TextView) (findViewById(R.id.title))).setText(values.getString(TITLE));
            ((TextView) (findViewById(R.id.description))).setText(values.getString(DESCRIPTION));
        }
    }

}
