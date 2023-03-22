package com.myt.messagereceiver.message;

import android.os.Handler;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpReceiver {
    
    private static final String TAG = UdpReceiver.class.getSimpleName();

    private DatagramSocket udpSocket;
    private ExecutorService mExecutorService;//线程池
    private String netIP = "255.255.255.255";//目标ip
    private int netPort = 1234;//目标端口
    private int heartTime = 5000;//间隔时间，接收和发送数据
    private Handler uHandler = new Handler();
    private int linkCount = 0;//连接次数

    private UdpMessageListener udpMessageListener;

    public UdpReceiver() {
        mExecutorService = Executors.newCachedThreadPool();
        mExecutorService.execute(initSocket);
    }

    public void startUdpListener(UdpMessageListener messageListener) {
        this.udpMessageListener = messageListener;
    }

    private Runnable initSocket = new Runnable() {
        @Override
        public void run() {
            try {
                //这里必须要设置null，不然下面设置本地端口会报异常
                udpSocket = new DatagramSocket(null);
                udpSocket.setReuseAddress(true);
                //设置本地端口
                udpSocket.bind(new InetSocketAddress(netPort));
                linkCount = 1;
                uHandler.post(jieShou);
            }catch (Exception e){
                e.printStackTrace();
                linkCount++;
                Log.e(TAG, "连接失败，进行第" + linkCount + "次重连");
                uHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mExecutorService.execute(initSocket);
                    }
                }, heartTime);
            }

        }
    };

    private Runnable jieShou = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "执行接收");
            receiveData();
        }
    };

    private void receiveData() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //uHandler.postDelayed(jieshou,5000);
                    byte[] result = new byte[1024];
                    while (true) {
                        DatagramPacket reveive = new DatagramPacket(result, result.length, new InetSocketAddress(netIP, netPort));
                        udpSocket.receive(reveive);
                        byte[] data = reveive.getData();
                        String ss = new String(data,0,reveive.getLength());
                        Log.e(TAG, ss);
                        if (udpMessageListener != null) {
                            udpMessageListener.onNewMessage(ss);
                        }
                    }
                } catch (Exception e) {
                    Log.i(TAG,"接收异常=="+e);
                }
            }
        });
    }

    public void onDestroy(){
        uHandler.removeCallbacksAndMessages(null);
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                udpSocket.disconnect();
                udpSocket.close();
            }
        });
        udpMessageListener = null;
    }

}