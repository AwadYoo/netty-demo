package com.jet.nio;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author awad_yoo
 * @create 2019-05-28 09:36
 */
@Log4j2
@Data
public class TimeClientHandle implements Runnable {

    private int port;
    private String ip;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandle(String ip, int port) {
        this.ip = StringUtils.isEmpty(ip) ? "127.0.0.1" : ip;
        this.port = StringUtils.isEmpty(port) ? 8089 : port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            log.info("TimeClientHandle is start in port : {}", port);
        } catch (IOException e) {
            e.printStackTrace();
            //异常 则系统退出
            System.exit(1);
        }
    }

    @Override
    public void run() {

        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

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
                    } catch (Exception e) {
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
                System.exit(1);
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
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWriter(sc);
                } else {
                    System.exit(1);
                }

                if (key.isReadable()) {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(readBuffer);
                    //读到字节，进行编解码
                    if (readBytes > 0) {
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);

                        String body = new String(bytes, "UTF-8");

                        log.info("MultiplexerTimeServer receive order : {}", body);

                        this.stop = true;
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

    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(ip, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWriter(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWriter(SocketChannel socketChannel) throws IOException {
        byte[] req = "time".getBytes();
        ByteBuffer writerBuffer = ByteBuffer.allocate(req.length);
        writerBuffer.put(req);
        writerBuffer.flip();
        socketChannel.write(writerBuffer);
        if (!writerBuffer.hasRemaining()) {
            log.info("Send order time to server succeed.");
        }

    }
}
