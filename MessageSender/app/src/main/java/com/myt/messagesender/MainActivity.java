package com.myt.messagesender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "zyl";
    private static boolean isFirst = true;
    public static String targetIpAddress = "192.168.3.181";
    public static UdpSender udpSender;

    private GridView gridViewNumber;
    private GridView gridViewCenter;
    private GridView gridViewTop;
    private ImageButton ivPower;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridViewNumber = findViewById(R.id.grid_number);
        gridViewCenter = findViewById(R.id.grid_center);
        gridViewTop = findViewById(R.id.grid_top);
        ivPower = findViewById(R.id.iv_close_power);
        initNumberGrid();
        initNumberCenter();
        initNumberTop();
        EditText editText = findViewById(R.id.ip_input);
        String ip = CommonUtil.getStringByKey(MainActivity.this, "targetIpAddress");
        if (!TextUtils.isEmpty(ip)) {
            targetIpAddress = ip;
        }
        editText.setText(targetIpAddress);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                targetIpAddress = s.toString();
                CommonUtil.saveStringByKey(MainActivity.this, "targetIpAddress", targetIpAddress);
            }
        });
        ivPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPowerEvent(true);
            }
        });
        ivPower.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sendPowerEvent(false);
                return false;
            }
        });
        udpSender = new UdpSender();
    }

    public void sendPowerEvent(boolean reStart) {
        String keyCommand = "reboot -p";
        if (reStart) {
            keyCommand = "reboot";
        }
        GridAdapter.sendShellCommand(keyCommand);
    }

/*    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            moveTaskToBack(true);
            isFirst = false;
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(1, 1000);
            startActivity(new Intent(this, MainActivity2.class));
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(1, 1000);
            setKeyPress(KeyEvent.KEYCODE_DPAD_CENTER);
        }
    };

    public void setKeyPress(int keycode) {
        try {
            String keyCommand = "input keyevent " + keycode;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
            Log.e(TAG, "setKeyPress keycode: " + keycode);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "setKeyPress keycode: " + keycode + " e: " + e.getLocalizedMessage());
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        udpSender.destroy();
    }

    private void initNumberTop() {
        List<TextKeyCodeModel> keyCodeModelList = new ArrayList<>();
        keyCodeModelList.add(new TextKeyCodeModel(true, "音量+", KeyEvent.KEYCODE_VOLUME_UP));
        keyCodeModelList.add(new TextKeyCodeModel(true, "TV", KeyEvent.KEYCODE_F1));
        keyCodeModelList.add(new TextKeyCodeModel(true, "TV1", KeyEvent.KEYCODE_UNKNOWN));
        keyCodeModelList.add(new TextKeyCodeModel(true, "音量-", KeyEvent.KEYCODE_VOLUME_DOWN));
        keyCodeModelList.add(new TextKeyCodeModel(true, "Master", KeyEvent.KEYCODE_F2));
        keyCodeModelList.add(new TextKeyCodeModel(true, "TV2", KeyEvent.KEYCODE_VOLUME_MUTE));

        gridViewTop.setAdapter(new GridAdapter(this, keyCodeModelList));
    }

    private void initNumberCenter() {
        List<TextKeyCodeModel> keyCodeModelList = new ArrayList<>();
        keyCodeModelList.add(new TextKeyCodeModel(true, "主页", KeyEvent.KEYCODE_HOME));
        keyCodeModelList.add(new TextKeyCodeModel(true, "上", KeyEvent.KEYCODE_DPAD_UP));
        keyCodeModelList.add(new TextKeyCodeModel(true, "返回", KeyEvent.KEYCODE_BACK));
        keyCodeModelList.add(new TextKeyCodeModel(true, "左", KeyEvent.KEYCODE_DPAD_LEFT));
        keyCodeModelList.add(new TextKeyCodeModel(true, "OK", KeyEvent.KEYCODE_DPAD_CENTER));
        keyCodeModelList.add(new TextKeyCodeModel(true, "右", KeyEvent.KEYCODE_DPAD_RIGHT));
        keyCodeModelList.add(new TextKeyCodeModel(true, "鼠标", KeyEvent.KEYCODE_CALCULATOR));
        keyCodeModelList.add(new TextKeyCodeModel(true, "下", KeyEvent.KEYCODE_DPAD_DOWN));
        keyCodeModelList.add(new TextKeyCodeModel(true, "菜单", KeyEvent.KEYCODE_MENU));

        gridViewCenter.setAdapter(new GridAdapter(this, keyCodeModelList));
    }

    private void initNumberGrid() {
        List<TextKeyCodeModel> keyCodeModelList = new ArrayList<>();
        keyCodeModelList.add(new TextKeyCodeModel(true, "1", KeyEvent.KEYCODE_1));
        keyCodeModelList.add(new TextKeyCodeModel(true, "2", KeyEvent.KEYCODE_2));
        keyCodeModelList.add(new TextKeyCodeModel(true, "3", KeyEvent.KEYCODE_3));
        keyCodeModelList.add(new TextKeyCodeModel(true, "4", KeyEvent.KEYCODE_4));
        keyCodeModelList.add(new TextKeyCodeModel(true, "5", KeyEvent.KEYCODE_5));
        keyCodeModelList.add(new TextKeyCodeModel(true, "6", KeyEvent.KEYCODE_6));
        keyCodeModelList.add(new TextKeyCodeModel(true, "7", KeyEvent.KEYCODE_7));
        keyCodeModelList.add(new TextKeyCodeModel(true, "8", KeyEvent.KEYCODE_8));
        keyCodeModelList.add(new TextKeyCodeModel(true, "9", KeyEvent.KEYCODE_9));
        keyCodeModelList.add(new TextKeyCodeModel(true, ".", KeyEvent.KEYCODE_PERIOD));
        keyCodeModelList.add(new TextKeyCodeModel(true, "0", KeyEvent.KEYCODE_0));
        keyCodeModelList.add(new TextKeyCodeModel(true, "x", KeyEvent.KEYCODE_DEL));

        gridViewNumber.setAdapter(new GridAdapter(this, keyCodeModelList));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown keyCode: " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}