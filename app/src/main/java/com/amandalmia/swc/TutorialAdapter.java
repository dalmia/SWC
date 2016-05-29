package com.amandalmia.swc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class TutorialAdapter extends PagerAdapter {

    private Activity mActivity;
    private Tutorial[] mTutorials;
    private ListColor[] colors = ListColor.values();

    public TutorialAdapter(Activity activity) {
        mActivity = activity;
        mTutorials = Tutorial.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return mTutorials.length;
    }


    public Tutorial getItem(int position) {
        return mTutorials[position];
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Tutorial tutorial = getItem(position);

        View convertView = inflater.inflate(R.layout.tutorial_layout, null);
        ImageView deviceImage = (ImageView) convertView
                .findViewById(R.id.device_image);
        TextView deviceText = (TextView) convertView
                .findViewById(R.id.device_text);

        int color = colors[position % colors.length].getColor();
        deviceImage.setImageResource(tutorial.getImage());
        deviceText.setText(tutorial.getInformation());




        container.addView(convertView, 0);

        return convertView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ViewGroup) object);
    }

    private enum ListColor {
        BLUE("#2196F3"),
        RED("#FF5252"),
        YELLOW("#FFEB3B");

        private
        @ColorRes
        int color;

        ListColor(String colorCode) {
            this.color = Color.parseColor(colorCode);
        }

        public int getColor() {
            return color;
        }
    }

}
