package com.savior.httpxml;

import com.savior.channelhandler.HttpXmlResponseDecoder;
import com.savior.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.Socket;

/**
 * @Auther: weiwe
 * @Date: 2020/1/14 18:30
 * @Description:
 */
public class HttpXmlClient {

    private EventLoopGroup eventLoopGroup;
    private int port;

    public HttpXmlClient(int port) {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.port = port;
    }

    public void connection() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(this.eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true)
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("http-decoder", new HttpResponseDecoder());
                ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                ch.pipeline().addLast("xml-decoder", new HttpXmlResponseDecoder(Order.class, true))
            }
        })
    }

}
