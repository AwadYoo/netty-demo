package com.jet.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.log4j.Log4j2;

/**
 * @author awad_yoo
 * @create 2019-05-29 10:11
 */
@Log4j2
public class WebSocketSever {

    public void run(int port) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    //HttpServerCodec 将请求和应答消息编码或解码为HTTP消息
                    pipeline.addLast("http-codec", new HttpServerCodec());
                    //HttpObjectAggregator 将多个http消息的多个部分组成一条完整的http消息
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65535));
                    //ChunkedWriteHandler 向客户端发送HTML5文件，主要用于支持浏览器和服务端进行socket通讯
                    socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    pipeline.addLast("handler", new WebSocketServerHandler());
                }
            });

            Channel channel = b.bind(port).sync().channel();
            log.info("web socket sever started at port: {}, please visit {}", port, "http://127.0.0.1:8089");
            channel.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new WebSocketSever().run(8080);
    }

}
