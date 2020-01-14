package com.savior.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * @Auther: weiwe
 * @Date: 2020/1/14 17:34
 * @Description:
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {

    public HttpXmlResponseDecoder(Class<?>  tClass) {
        this(tClass, true);
    }

    public HttpXmlResponseDecoder(Class<?> tClass, boolean isPrint) {
        super(tClass, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
        HttpXmlResponse response = new HttpXmlResponse(msg, decode0(ctx, msg.content()));
        out.add(response);
    }
}
