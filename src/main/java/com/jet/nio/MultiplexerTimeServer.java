package com.jet.nio;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * @author awad_yoo
 * @create 2019-05-27 14:12
 */
@Data
@Log4j2
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel svrChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            svrChannel = ServerSocketChannel.open();
            svrChannel.configureBlocking(false);
            svrChannel.socket().bind(new InetSocketAddress(port), 1024);
            //将ServerSocketChannel 注册到 Selector ,监听 SelectionKey.OP_ACCEPT 操作位
            svrChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.info("MultiplexerTimeServer is start in port : {}", port);
        } catch (IOException e) {
            e.printStackTrace();
            //异常 则系统退出
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }


    @Override
    public void run() {

        while (!stop) {
            try {
                //休眠时间 设置为1s
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;

                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handlerInput(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //多路复用器 Selector 关闭后,所有的注册在上面的Channel和Pipe等资源都会被自动注册并且关闭，无需手动释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handlerInput(SelectionKey key) throws IOException {

        if (key.isValid()) {
            //处理接入的请求信息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()) {
                //读取数据
                SocketChannel sc = (SocketChannel) key.channel();
                //开辟一个1M的缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                String currentTime = LocalDateTime.now().toString();
                //读到字节，进行编解码
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);

                    String body = new String(bytes, "UTF-8");

                    log.info("MultiplexerTimeServer receive order : {}", body);

                    //currentTime = Objects.equals("time", body) ? currentTime : "bad request";
                    doWriter(sc, currentTime);
                    //链路已经关闭，需要手动释放资源
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                    //未读到字节，属于正常，忽略
                } else {
                    //忽略
                    ;
                }


            }


        }

    }

    private void doWriter(SocketChannel channel, String resp) throws IOException {
        if (StringUtils.isEmpty(resp)) {
            return;
        }
        byte[] bytes = resp.getBytes();
        ByteBuffer writerBuffer = ByteBuffer.allocate(bytes.length);
        writerBuffer.put(bytes);
        writerBuffer.flip();
        channel.write(writerBuffer);

    }
}
