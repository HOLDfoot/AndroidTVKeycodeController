package com.myt.messagereceiver;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpSender {

    private DatagramSocket udpSocket;
    private ExecutorService mExecutorService;//线程池
    private String netIP = "255.255.255.255";//目标ip
    private int netPort = 1234;//目标端口
    private Handler uHandler = new Handler();

    UdpSender() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        try {
            udpSocket = new DatagramSocket(null);
            udpSocket.setReuseAddress(true);
            //设置本地端口
            udpSocket.bind(new InetSocketAddress(netPort));
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e("zyl", "e: " + e.getLocalizedMessage());
        }
    }

    //发送数据，可以暴露出去，在其他地方调用
    public void sendData(final String ss) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //uHandler.postDelayed(fasong,5000);定时发送
                    byte[] data = ss.getBytes();
                    DatagramPacket send = new DatagramPacket(data, data.length,new InetSocketAddress(netIP, netPort));
                    udpSocket.send(send);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void destory(){
        uHandler.removeCallbacksAndMessages(null);
        udpSocket.disconnect();
        udpSocket.close();
    }

}