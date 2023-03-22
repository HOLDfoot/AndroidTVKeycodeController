package com.myt.messagesender;

import android.os.Handler;
import android.text.TextUtils;
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
    private int netPort = 1234;//目标端口, 1234和1239都被占用

    public UdpSender() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        try {
            udpSocket = new DatagramSocket(null);
            udpSocket.setReuseAddress(true);
            //设置本地端口
            udpSocket.bind(new InetSocketAddress(netPort));
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e("UdpSender", "UdpSender e: " + e.getLocalizedMessage());
        }
    }

    //发送数据，可以暴露出去，在其他地方调用
    public void sendData(final String ss, String targetIP) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] data = ss.getBytes();
                    DatagramPacket send = new DatagramPacket(data, data.length, new InetSocketAddress(targetIP, netPort));
                    udpSocket.send(send);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("UdpSender", "sendData e: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void destroy() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                udpSocket.disconnect();
                udpSocket.close();
            }
        });
    }

}