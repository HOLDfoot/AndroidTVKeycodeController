package com.myt.messagereceiver.message;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import com.myt.messagereceiver.event.KeyCode;
import com.myt.messagereceiver.window.WindowController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MessageController {

    private static final String TAG = MessageController.class.getSimpleName();

    private Context mContext;
    private UdpReceiver udpReceiver;
    private WindowController windowController;

    public void onCreate(Service context) {
        mContext = context;
        Log.e(TAG, "onCreate");
        udpReceiver = new UdpReceiver();
        udpReceiver.startUdpListener(udpMessageListener);
    }

    public void setWindowController(WindowController windowController) {
        this.windowController = windowController;
    }

    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        udpReceiver.onDestroy();
    }

    public Context getContext() {
        return mContext;
    }

    private UdpMessageListener udpMessageListener = new UdpMessageListener() {
        @Override
        public void onNewMessage(String message) {
            Log.e(TAG, "onNewMessage message: " + message);
            if (message == null) {
                return;
            }
            if (message.equals(MessageType.shutdown)) {
                shutdown();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    String msgType = jsonObject.getString("msgType");
                    String command = null;
                    String msgBody = jsonObject.getString("msgBody");
                    if ("shellCommand".equals(msgType)) {
                        command = msgBody;
                        execCommand(command);
                    } else if ("keyCommand".equals(msgType)) {
                        // 根据keyCode判断是执行input keyevent还是控制window的显示,隐藏,移动,点击input tap
                        int keyCode = Integer.parseInt(msgBody.substring(15)); // input keyevent 210
                        if (keyCode == KeyCode.keycode_mouse_show) {
                            windowController.showMouse();
                        } else if (keyCode == KeyCode.keycode_mouse_dismiss) {
                            windowController.dismissMouse();
                        } else if (keyCode == KeyCode.keycode_mouse_ok) {
                            if (windowController.isShowMouse()) {
                                int xDistance = (int) (50 * 0.37); // 图片中食指距离左侧37%的位置
                                int yDistance = 5;
                                command = "input tap " + (windowController.getPoint().x + xDistance) + " " + (windowController.getPoint().y - yDistance);
                            }
                        } else if (!resolveMouseMove(keyCode)) {
                            command = msgBody;
                        }
                        if (command != null) {
                            execCommand(command);
                        }
                        Log.e(TAG, "onNewMessage keyCode: " + keyCode + " keyCommand: " + command);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onNewMessage message can't be resolved");
            }
        }
    };

    public boolean resolveMouseMove(int input) {
        boolean canResolve = true;
        switch (input) {
            case KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT:
                windowController.moveTo(+10, 0);
                break;
            case KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT:
                windowController.moveTo(-10, 0);
                break;
            case KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP:
                windowController.moveTo(0, -10);
                break;
            case KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN:
                windowController.moveTo(0, +10);
                break;
            default:
                canResolve = false;
        }
        return canResolve;
    }

    private void execCommand(String keyCommand) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
            Log.e(TAG, "execCommand keyCommand: " + keyCommand);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "execCommand e: " + e.getLocalizedMessage());
        }
    }

    private void shutdown() {
        try {
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand("reboot -p ", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
