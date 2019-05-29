package com.jet.netty.echo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.log4j.Log4j2;

/**
 * @author awad_yoo
 * @create 2019-05-28 15:28
 */
@Log4j2
public class EchoClientHandler extends ChannelHandlerAdapter {

    private int counter;

    private static String ECHO_REQ = "HI,yhy! WELCOME TO NETTY.$_";

    public EchoClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("这是第 {} 次从服务端收到消息：{}", ++counter, msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        log.error(cause.getMessage());
        ctx.close();

    }
}
