package com.savior.protocol;

import com.savior.protocol.childhandler.HttpChildHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @Auther: weiwe
 * @Date: 2019/12/13 18:07
 * @Description:
 */
public class HttpProtocolServer {

    // 处理连接的线程
    private EventLoopGroup bossEventLoopGroup;
    // 处理读写的线程
    private EventLoopGroup workerEventLoopGroup;
    // 端口号
    private int port;

    public HttpProtocolServer(int port) {
        this.port = port;
        bossEventLoopGroup = new NioEventLoopGroup();
        workerEventLoopGroup = new NioEventLoopGroup();
    }

    /**
     * 创建服务
     */
    public void createServer() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(this.bossEventLoopGroup, this.workerEventLoopGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.handler(new LoggingHandler());
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,  true);
        serverBootstrap.childHandler(new HttpChildHandler());
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(this.port)).sync();
    }

    /**
     * 关闭服务
     */
    public void closeServer() {
        this.workerEventLoopGroup.shutdownGracefully();
        this.bossEventLoopGroup.shutdownGracefully();
    }

}
