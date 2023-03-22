package com.myt.messagesender;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private static final String TAG = "zyl";
    public List<TextKeyCodeModel> keyCodeModelList;
    public Context context;
    public static boolean isCheckMouse = false;

    public GridAdapter(Context context, List<TextKeyCodeModel> keyCodeModelList) {
        this.context = context;
        this.keyCodeModelList = keyCodeModelList;
    }

    @Override
    public int getCount() {
        return keyCodeModelList.size();
    }

    @Override
    public TextKeyCodeModel getItem(int position) {
        return keyCodeModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void updateMouseButton(View v, boolean isClick) {
        Button textView = (Button) v;
        if (TextUtils.equals("鼠标", textView.getText().toString())) {
            if (isClick) {
                isCheckMouse = !isCheckMouse;
            }
            if (isCheckMouse) {
                textView.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                textView.setTextColor(context.getResources().getColor(android.R.color.white));
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
        Button button = view.findViewById(R.id.btn_text);
        button.setText(getItem(position).text);
        button.setEnabled(getItem(position).enable);
        updateMouseButton(button, false);
        if (getItem(position).enable) {
            int finalPosition = position;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMouseButton(v, true);
                    sendKeyEvent(getItem(finalPosition).keyCode);
                }
            });
        }
        return view;
    }

    private static Handler handler = new android.os.Handler();

    public void sendKeyEvent(int keycode) {
        if (!DigitalUtil.isCorrectIp(MainActivity.targetIpAddress)) {
            Toast.makeText(context, "请检查ip", Toast.LENGTH_SHORT).show();
            return;
        }
        keycode = translateMouseKeycode(keycode);
        Log.v(TAG, "sendKeyEvent keycode: " + keycode + " isCheckMouse: " + isCheckMouse);

        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER && isCheckMouse) {
            //String keyCommand = "input tap 960 540";
            String keyCommand = "input keyevent " + KeyCode.keycode_mouse_ok;
            sendKeyCommand(keyCommand);
/*            List<Integer> keyEventList = new ArrayList<>();
            keyEventList.add(KeyEvent.ACTION_DOWN);
            keyEventList.add(KeyEvent.ACTION_UP);
            for (int keyEvent : keyEventList) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendKeyCode(keyEvent);
                    }
                }, 100);
            }*/
        } else {
            sendKeyCode(keycode);
        }
    }

    private void sendKeyCode(int keyCode) {
        String keyCommand = "input keyevent " + keyCode;
        sendKeyCommand(keyCommand);
    }

    public static void sendKeyCommand(String keyCommand) {
        sendCommand(keyCommand, "keyCommand");
    }

    public static void sendShellCommand(String shellCommand) {
        sendCommand(shellCommand, "shellCommand");
    }

    public static void sendCommand(String command, String msgType) {
        Log.e(TAG, "sendCommand command: " + command + " isCheckMouse: " + isCheckMouse);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msgType", msgType);
            jsonObject.put("msgBody", command);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String ip = MainActivity.targetIpAddress;
        MainActivity.udpSender.sendData(jsonObject.toString(), ip);
    }

    public int translateMouseKeycode(int input) {
        int output = input;
        if (input == KeyEvent.KEYCODE_CALCULATOR) { // 鼠标按钮对应的按键
            if (isCheckMouse) {
                output = KeyCode.keycode_mouse_show;
            } else {
                output = KeyCode.keycode_mouse_dismiss;
            }
            return output;
        }
        if (isCheckMouse) {
            switch (input) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    output = KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    output = KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    output = KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    output = KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN;
                    break;
            }
        }
        return output;
    }


    /**
     * Key code constant: Consumed by the system for navigation up
     */
    public static final int KEYCODE_SYSTEM_NAVIGATION_UP = 280;
    /**
     * Key code constant: Consumed by the system for navigation down
     */
    public static final int KEYCODE_SYSTEM_NAVIGATION_DOWN = 281;
    /**
     * Key code constant: Consumed by the system for navigation left
     */
    public static final int KEYCODE_SYSTEM_NAVIGATION_LEFT = 282;
    /**
     * Key code constant: Consumed by the system for navigation right
     */
    public static final int KEYCODE_SYSTEM_NAVIGATION_RIGHT = 283;
}
