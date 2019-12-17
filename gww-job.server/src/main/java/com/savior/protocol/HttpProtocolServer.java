package com.savior.protocol;

import com.savior.protocol.childhandler.HttpFileServerHander;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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
    // 服务启动
    private ServerBootstrap serverBootstrap;

    public HttpProtocolServer(int port) {
        this.port = port;
        bossEventLoopGroup = new NioEventLoopGroup();
        workerEventLoopGroup = new NioEventLoopGroup();
    }

    /**
     * 创建服务
     */
    public void createServer(final String url) throws InterruptedException {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(this.bossEventLoopGroup, this.workerEventLoopGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.handler(new LoggingHandler());
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                // 将多个消息转换为单一的FullHttpRequest和FullHttpResponse
                channel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
                channel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                // 执行异步发送大的码流不占用过多内存
                channel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                channel.pipeline().addLast("fileServerHandler", new HttpFileServerHander(url));
            }
        });
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(this.port)).sync();
        System.out.println("系统启动");
        future.channel().closeFuture().sync();
    }


    /**
     * 关闭服务
     */
    public void closeServer() {
        this.workerEventLoopGroup.shutdownGracefully();
        this.bossEventLoopGroup.shutdownGracefully();
    }

}
