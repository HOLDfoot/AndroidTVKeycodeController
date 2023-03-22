package com.myt.messagesender;

public class DigitalUtil {

    //判断字符是否是IP
    public static boolean isCorrectIp(String ipString) {
        if (ipString == null) {
            return false;
        }
        if (ipString.contains(":")) {
            String[] ipStr = ipString.split(":");
            if (ipStr.length != 2) {
                return false;
            }
            int port = Integer.parseInt(ipStr[1]);
            if (port < 1024 || port > 65536) {
                return false;
            }
            ipString = ipStr[0];
        }


        //1、判断是否是7-15位之间（0.0.0.0-255.255.255.255.255）
        if (ipString.length() < 7 || ipString.length() > 15) {
            return false;
        }
        //2、判断是否能以小数点分成四段
        String[] ipArray = ipString.split("\\.");
        if (ipArray.length != 4) {
            return false;
        }
        for (int i = 0; i < ipArray.length; i++) {
            //3、判断每段是否都是数字
            try {
                int number = Integer.parseInt(ipArray[i]);
                //4.判断每段数字是否都在0-255之间
                if (number < 0 || number > 255) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static int getPortOfIp(String ipString) {
        int port = 0;
        if (ipString != null && ipString.contains(":")) {
            String[] ipStr = ipString.split(":");
            if (ipStr.length != 2) {
                return port;
            }
            port = Integer.parseInt(ipStr[1]);
        }
        return port;
    }

    public static String getAddressOfIp(String ipString) {
        if (ipString != null && ipString.contains(":")) {
            String[] ipStr = ipString.split(":");
            return ipStr[0];
        }
        return ipString;
    }
}
