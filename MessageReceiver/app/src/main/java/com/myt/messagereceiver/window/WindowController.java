package com.myt.messagereceiver.window;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

public class WindowController {

    private Window1Px window1Px;
    private WindowManager mWindowManager;
    private Context mContext;
    private Point point = new Point();
    private boolean isShowMouse = false;

    public void onCreate(Service context) {
        mContext = context;
        showWindow1Px();
    }

    public void moveTo(int dx, int dy) {
        if (!isShowMouse()) {
            Log.i("zyl", "moveTo isShowMouse false");
            return;
        }
        point.offset(dx, dy);
        Log.i("zyl", "moveTo dx: " + dx + " dy: " + dy + " point: " + point);
        final WindowManager.LayoutParams Params = (WindowManager.LayoutParams) window1Px.getLayoutParams();
        Params.x = point.x;
        Params.y = point.y;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mWindowManager.updateViewLayout(window1Px, Params);
            }
        });
    }

    public void onDestroy() {
        if (window1Px != null && window1Px.isAttachedToWindow()) {
            mWindowManager.removeView(window1Px);
        }
    }

    public Context getContext() {
        return mContext;
    }

    public Point getPoint() {
        return point;
    }

    private void showWindow1Px() {
        //创建MyWindow的实例
        window1Px = new Window1Px(getContext());

        //窗口管理者
        mWindowManager = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
        //窗口布局参数
        WindowManager.LayoutParams Params = new WindowManager.LayoutParams();
        //布局坐标,以屏幕左上角为(0,0)
        Params.x = point.x;
        Params.y = point.y;
        Params.format = PixelFormat.TRANSLUCENT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            Params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //布局flags
        Params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
        Params.flags = Params.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        Params.flags = Params.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
        Params.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;

        //布局的gravity
        Params.gravity = Gravity.LEFT | Gravity.TOP;

        //布局的宽和高
        int windowSize = 1;
        if (isShowMouse) {
            windowSize = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        Params.width = windowSize;
        Params.height = windowSize;

        if (!window1Px.isAttachedToWindow()) {
            mWindowManager.addView(window1Px, Params);
        }
    }

    public boolean isShowMouse() {
        return isShowMouse;
    }

    public void showMouse() {
        final WindowManager.LayoutParams Params = (WindowManager.LayoutParams) window1Px.getLayoutParams();
        Params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        Params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mWindowManager.updateViewLayout(window1Px, Params);
            }
        });
        isShowMouse = true;
    }

    public void dismissMouse() {
        final WindowManager.LayoutParams Params = (WindowManager.LayoutParams) window1Px.getLayoutParams();
        Params.width = 1;
        Params.height = 1;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mWindowManager.updateViewLayout(window1Px, Params);
            }
        });
        isShowMouse = false;
    }
}
