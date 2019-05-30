package com.jet.dig4;

import com.jet.util.ByteTools;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

/**
 * @author awad_yoo
 * @create 2019-05-29 14:15
 */
public class DigChannelHandler extends ChannelHandlerAdapter {

    private static Logger log = LogManager.getLogger(DigChannelHandler.class);

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel in = ctx.channel();
        log.info("客户端：{} 断开连接!", in.remoteAddress());
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        buf.release();
        log.info("{}收到的字节为：{}", ctx.channel().remoteAddress(), ByteTools.bytesToHex(bytes));
        byte[] bytes1 = LocalDateTime.now().toString().getBytes();
        ByteBuf buffer = Unpooled.copiedBuffer(bytes1);
        ctx.channel().writeAndFlush(buffer);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接成功，地址是：{}", ctx.channel().remoteAddress());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Unexpected exception from downstream : {}", cause.getMessage());
        ctx.close();// 出现异常时关闭channel
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    public static void main(String[] args) {
        String bytes = ByteTools.bytesToHex(new byte[]{01, 12, 32, 12});
        System.out.println(bytes);
    }
}
