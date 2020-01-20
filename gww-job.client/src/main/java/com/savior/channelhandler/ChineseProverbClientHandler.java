package com.savior.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;


/**
 * @Auther: weiwe
 * @Date: 2020/1/20 16:02
 * @Description:
 */
public class ChineseProverbClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String response = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
