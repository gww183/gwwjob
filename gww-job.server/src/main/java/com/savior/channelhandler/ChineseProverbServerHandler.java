package com.savior.channelhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;


/**
 * @Auther: weiwe
 * @Date: 2020/1/20 14:44
 * @Description:
 */
public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String[] DICTIONARY = {
            "只要功夫深，铁棒磨成针",
            "旧时王谢堂前燕，飞入寻常百姓家",
            "洛阳亲友如相问,一篇冰心在玉壶",
            "一寸光阴一寸金，寸金难买寸光阴",
            "老骥伏枥,志在千里, 壮士暮年，壮心不已"
    };

    private String nextQuote() {
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String req = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);
        Thread.sleep(6000);
        ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(nextQuote(), CharsetUtil.UTF_8), msg.sender()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

