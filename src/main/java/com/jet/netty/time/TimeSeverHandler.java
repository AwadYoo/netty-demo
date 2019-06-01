package com.jet.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * @author awad_yoo
 * @create 2019-05-28 11:24
 */
@Log4j2
@Data
public class TimeSeverHandler extends ChannelInboundHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");

        log.info("TimeSeverHandler receive order : {},the counter is : {}", body, ++counter);

        String time = Objects.equals("time", body) ? LocalDateTime.now().toString() : "bad request";
        ByteBuf resp = Unpooled.copiedBuffer(time.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
