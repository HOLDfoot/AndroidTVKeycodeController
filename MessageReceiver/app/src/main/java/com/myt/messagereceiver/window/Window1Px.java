package com.myt.messagereceiver.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.myt.messagereceiver.R;

/**
 * 一个像素大小的view
 */
public class Window1Px extends LinearLayout {

    @SuppressLint("WrongConstant")
    public Window1Px(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.window_1px, this);
    }

}
