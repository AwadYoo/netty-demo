package com.jet.dig4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author awad_yoo
 * @create 2019-06-01 16:01
 */
public class clientHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LogManager.getLogger(clientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] bytes = {
                0x4E, 0x01, 0x12, 0x34, 0x56, 0x78, 0x01, 0x01, (byte) 0xFF, 0x55, 0x55, 0x22, 0x22, 0x11, 0x11, 0x11, 0x11, 0x12, 0x00, (byte) 0xA8
        };
        ByteBuf buf = Unpooled.copiedBuffer(bytes);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        log.info(new String(req, "utf-8"));
    }
}
