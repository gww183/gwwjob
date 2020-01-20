package com.savior.udpprotocle;

import com.savior.channelhandler.ChineseProverbServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * @Auther: weiwe
 * @Date: 2020/1/20 14:31
 * @Description:
 */
public class ChineseProverServer {

    private Integer port;

    private EventLoopGroup group;

    public ChineseProverServer(Integer port) {
        this.port = port;
        this.group = new NioEventLoopGroup();
    }

    public void createServer() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(this.group);
        bootstrap.channel(NioDatagramChannel.class);
        bootstrap.option(ChannelOption.SO_BROADCAST, true);
        bootstrap.handler(new ChineseProverbServerHandler());
        try {
            ChannelFuture future = bootstrap.bind(new InetSocketAddress(this.port)).sync();
            future.channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
