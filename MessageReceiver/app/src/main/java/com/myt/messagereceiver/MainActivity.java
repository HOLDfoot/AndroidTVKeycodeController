package com.myt.messagereceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.myt.messagereceiver.tool.AddressUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    
    private static TextView tvMessage;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMessage = findViewById(R.id.tvMessage);
        String ipAddress = AddressUtils.getLocalHost();
        if (TextUtils.isEmpty(ipAddress)) {
            //Toast.makeText(this, "没有连接网络, 需要连接网络", Toast.LENGTH_LONG).show();
            //finish(); todo
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown keyCode: " + keyCode);
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvMessage = null;
        stopService(new Intent(this, ReceiverService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvMessage.setText("在手机端输入IP地址: " + AddressUtils.getLocalHost());
        if (isFirst) {
            startService(new Intent(this, ReceiverService.class));
            Toast.makeText(this, tvMessage.getText().toString(), Toast.LENGTH_LONG).show();
            moveTaskToBack(true);
            isFirst = false;
        }
    }

    public static void updateMessage(final String msg) {
        if (tvMessage != null) {
            tvMessage.post(new Runnable() {
                @Override
                public void run() {
                    tvMessage.append("\n" + msg);
                }
            });
            Log.i("zyl", "updateMessage msg: " + msg);
        } else {
            Log.e("zyl", "updateMessage msg: " + msg);
        }
    }
}
