package com.savior.channelhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @Auther: weiwe
 * @Date: 2020/1/14 15:39
 * @Description:
 */
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpRequest> {

    public HttpXmlRequestDecoder(Class aClass) {
        super(aClass);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpRequest msg, List out) throws Exception {
        if (!msg.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        HttpXmlRequest httpXmlRequest = new HttpXmlRequest(msg, decode0(ctx, msg.content()));
        out.add(httpXmlRequest);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }
}
