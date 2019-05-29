package com.jet.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

/**
 * @author awad_yoo
 * @create 2019-05-28 15:01
 */
@Log4j2
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;
        log.info("第 {} 次收到客户端信息：{}", ++count, body);
        body += "$_";
        ByteBuf buf = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        log.error(cause.getMessage());
        ctx.close();
    }
}
