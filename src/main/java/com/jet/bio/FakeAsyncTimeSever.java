package com.jet.bio;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 伪异步I/O客户端
 *
 * @author awad_yoo
 * @create 2019-05-27 10:58
 */
@Log4j2
public class FakeAsyncTimeSever {

    public static void main(String[] args) {

        int port = 8089;
        if (args != null && args.length > 0) {

            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            log.info("TimeServer is start in port:{}", port);
            Socket socket = null;
            TimeSeverHandlerExecutorPool singeExecutor = new TimeSeverHandlerExecutorPool(50, 1000);
            while (true) {
                socket = server.accept();
                singeExecutor.execute(new TimeSeverHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                log.info("TimeServer close");
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }
        }

    }
}
