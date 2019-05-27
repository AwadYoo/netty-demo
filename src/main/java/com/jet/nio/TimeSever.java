package com.jet.nio;

import java.net.ServerSocket;

/**
 * @author awad_yoo
 * @create 2019-05-27 14:09
 */
public class TimeSever {

    public static void main(String[] args) {
        int port = 8089;
        if (args != null && args.length > 0) {

            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();

    }
}
