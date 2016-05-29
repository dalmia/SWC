package com.amandalmia.swc;

import android.support.annotation.DrawableRes;

/**
 * @brief Enum used as model for holding tutorial data.
 *
 */
public enum Tutorial {
//    START(R.drawable.start_tutorial_screen),
    FIRST(R.drawable.first_tutorial_screen, "View your lecture and lab schedule"),
    SECOND(R.drawable.second_tutorial_screen,"Receive instant notification for any announcement"),
    THIRD(R.drawable.third_tutorial_screen, "View each branch's details");


    private
    @DrawableRes
    int image;

    private String information;

    Tutorial(int image, String information) {
        this.image = image;
        this.information = information;
    }

    public int getImage() {
        return image;
    }
    public String getInformation() {
        return information;
    }


}
