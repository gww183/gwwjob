package com.savior.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

/**
 * @Auther: weiwe
 * @Date: 2020/1/14 17:21
 * @Description:
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse httpXmlResponse, List<Object> out) throws Exception {
        ByteBuf byteBuf = encode0(httpXmlResponse.getBody());
        FullHttpResponse httpResponse = httpXmlResponse.getResponse();
        if(httpResponse == null) {
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        } else {
            httpResponse = new DefaultFullHttpResponse(httpXmlResponse.getResponse().protocolVersion(),
                    httpXmlResponse.getResponse().status(), byteBuf);
        }
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/xml");
        HttpHeaderUtil.setContentLength(httpResponse, byteBuf.readableBytes());
        out.add(httpResponse);
    }
}
