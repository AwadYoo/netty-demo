package com.jet.dig4;

import com.jet.util.FileUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.concurrent.TimeUnit;


/**
 * @author awad_yoo
 * @create 2019-05-29 13:47
 */
public class MainService {
    private static Logger logger = LogManager.getLogger(MainService.class);

    private void serviceInitial(int port) {
        try {
            ConfigurationSource source = new ConfigurationSource(FileUtil.getResourcesFileInputStream("log4j2.xml"));
            Configurator.initialize(null, source);
            checkRunning();
            run(port);
            logger.info("dig4服务端初始化完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(int port) {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IdleStateHandler(3, 1, 5, TimeUnit.SECONDS));
                    ch.pipeline().addLast("handler", new DigChannelHandler());
                }
            });
            b.option(ChannelOption.SO_BACKLOG, 128);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            //b.handler(new LoggingHandler(LogLevel.INFO));
            ChannelFuture future = b.bind(port).sync();

            logger.info("Netty服务已启动！正在监听地址：{} ", future.channel().localAddress());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动Netty服务出现异常，原因位：{}", e.getMessage());
        } finally {
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }

    protected void checkRunning() {
        try {
            // 报告内存使用情况
            Runtime r = Runtime.getRuntime();
            long freeMem = r.freeMemory();
            long maxMem = r.maxMemory();
            long totalMem = r.totalMemory();
            logger.info("totalMem: {},  maxMem: {},  freeMem: {}", totalMem, maxMem, freeMem);
        } catch (Exception e) {
            logger.error("server.checkRunning()发生异常，原因为：{}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 12345;
        new MainService().serviceInitial(port);
    }

}
