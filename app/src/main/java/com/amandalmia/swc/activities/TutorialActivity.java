package com.amandalmia.swc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.amandalmia.swc.R;
import com.amandalmia.swc.app.CommonUtilities;
import com.amandalmia.swc.storage.SessionManager;
import com.amandalmia.swc.TutorialAdapter;
import com.viewpagerindicator.CirclePageIndicator;


/**
 * @brief Shows the tutorial related to ScholarMate usage.
 */
public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {

    private TutorialAdapter mAdapter;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    SessionManager sessionManager;
    Button done, skip;
    ImageView next;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        CommonUtilities.applyFont(this, findViewById(R.id.tutorial_screens));
        done = (Button) findViewById(R.id.tutorial_done);
        skip = (Button) findViewById(R.id.tutorial_skip);
        next = (ImageView) findViewById(R.id.next);
        sessionManager = new SessionManager(this);
        if (sessionManager.getKeyIsFirstRun()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mAdapter = new TutorialAdapter(this);
        mPager = (ViewPager) findViewById(R.id.tutorial_viewpager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.tutorial_indicator);
        mIndicator.setViewPager(mPager);
        Thread timer = new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(200);
                        if (mPager.getCurrentItem() == 2) {
                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("flag", "1");
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);
                        } else {
                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("flag", "2");
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();//preserve the description
                        return;
                    }

                }


            }

            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {

                    String aResponse = msg.getData().getString("flag");

                    if ((null != aResponse)) {
                        if (aResponse.equals("1")) {
                            done.setVisibility(View.VISIBLE);
                            next.setVisibility(View.GONE);
                        } else {
                            done.setVisibility(View.GONE);
                            next.setVisibility(View.VISIBLE);
                        }
                    } else {

                    }

                }
            };

        });
        timer.start();
        next.setOnClickListener(this);
        done.setOnClickListener(this);
        skip.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                int position = mPager.getCurrentItem();
                mPager.setCurrentItem(position + 1);
                break;

            case R.id.tutorial_done:
                sessionManager.setKeyIsFirstRun(true);
                startActivity(new Intent(TutorialActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.tutorial_skip:
                sessionManager.setKeyIsFirstRun(true);
                startActivity(new Intent(TutorialActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}
