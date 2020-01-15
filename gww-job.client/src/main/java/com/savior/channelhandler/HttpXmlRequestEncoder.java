package com.savior.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Auther: weiwe
 * @Date: 2020/1/9 16:33
 * @Description:
 */
public class HttpXmlRequestEncoder extends AbstractHttpXmlEncoder<HttpXmlRequest> {


    @Override
    public void encode(ChannelHandlerContext ctx, HttpXmlRequest msg, List out) throws Exception {
        ByteBuf body = encode0(msg.getBody());
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", body);
            HttpHeaders headers = request.headers();
            String hostName = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostName();
            headers.set(HttpHeaderNames.HOST, "127.0.0.1");
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            headers.set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP + "," + HttpHeaderValues.DEFLATE);
            headers.set(HttpHeaderNames.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            headers.set(HttpHeaderNames.ACCEPT_LANGUAGE, "zh");
            headers.set(HttpHeaderNames.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }
        HttpHeaderUtil.setContentLength(request, body.readableBytes());
        out.add(request);
    }
}
