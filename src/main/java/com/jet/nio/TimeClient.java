package com.jet.nio;

/**
 * @author awad_yoo
 * @create 2019-05-28 09:35
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8089;
        String ip = "127.0.0.1";
        if (args != null && args.length > 0) {

            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new Thread(new TimeClientHandle(ip, port)).start();
    }
}
