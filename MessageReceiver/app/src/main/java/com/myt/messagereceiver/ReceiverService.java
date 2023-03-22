package com.myt.messagereceiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.myt.messagereceiver.message.MessageController;
import com.myt.messagereceiver.window.WindowController;

public class ReceiverService extends Service {

    private static final String TAG = ReceiverService.class.getSimpleName();

    private WindowController windowController = new WindowController();
    private MessageController messageController = new MessageController();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        windowController.onDestroy();
        messageController.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        windowController.onCreate(this);
        messageController.onCreate(this);
        messageController.setWindowController(windowController);
    }
}
