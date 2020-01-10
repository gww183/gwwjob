package com.savior.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @Auther: weiwe
 * @Date: 2019/12/18 10:04
 * @Description:
 */
public class HttpXmlProtocolServer {

    private int port;

    private EventLoopGroup bossEventGroup;

    private EventLoopGroup workerEventGroup;

    public HttpXmlProtocolServer(int port) {
        this.port = port;
        this.bossEventGroup = new NioEventLoopGroup();
        this.workerEventGroup = new NioEventLoopGroup();
    }

    public void createServer() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventGroup, workerEventGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.handler(new LoggingHandler());
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {

            }
        });
        try {
            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(this.port)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭服务
     */
    public void closeServer() {
        this.workerEventGroup.shutdownGracefully();
        this.bossEventGroup.shutdownGracefully();
    }

}
