package com.savior.udp;

import com.savior.channelhandler.ChineseProverbClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @Auther: weiwe
 * @Date: 2020/1/20 15:16
 * @Description:
 */
public class UDPProtocol {

    public void run(int port) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioDatagramChannel.class);
        bootstrap.option(ChannelOption.SO_BROADCAST, true);
        bootstrap.handler(new ChineseProverbClientHandler());
        Channel channel = null;
        try {
            ByteBuf byteBuf = Unpooled.copiedBuffer("测试中国古诗", CharsetUtil.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(byteBuf, new InetSocketAddress("255.255.255.255", port));
            channel = bootstrap.bind(0).sync().channel();
            channel.writeAndFlush(datagramPacket).sync();
            if (!channel.closeFuture().await(5000)) {
                System.out.println("查询超时");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
