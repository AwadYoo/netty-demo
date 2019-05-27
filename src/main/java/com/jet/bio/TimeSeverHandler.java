package com.jet.bio;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author awad_yoo
 * @create 2019-05-27 10:21
 */
@Data
@Log4j2
public class TimeSeverHandler implements Runnable {

    private Socket socket;

    public TimeSeverHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);

            String currentTime = null;
            String body = null;

            while (true) {

                body = in.readLine();

                if (body == null) {
                    break;
                }
                currentTime = LocalDateTime.now().toString();
                log.info("TimeSever receive order:{}", body);
                currentTime = Objects.equals("time", body) ? currentTime : "bad request";
                //out.println(currentTime);
                out.println(currentTime);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
                out = null;
            }

            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socket = null;
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(Objects.equals("2", "2") ? "相同" : "不相同");
    }
}
