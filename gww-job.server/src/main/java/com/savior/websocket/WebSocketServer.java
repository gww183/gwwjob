package com.savior.websocket;

import com.savior.channelhandler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * @Auther: weiwe
 * @Date: 2020/1/16 15:27
 * @Description:
 */
public class WebSocketServer {

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    private int port;

    public WebSocketServer(int port) {
        this.bossGroup = new NioEventLoopGroup();
        this.workGroup = new NioEventLoopGroup();
        this.port = port;
    }

    public void connection() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("http-codec", new HttpServerCodec());
                ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                ch.pipeline().addLast("handler", new WebSocketServerHandler());
            }
        });

        try {
            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(this.port)).sync();
            System.out.println("websocket server is open");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.workGroup.shutdownGracefully();
            this.bossGroup.shutdownGracefully();
        }
    }

}
