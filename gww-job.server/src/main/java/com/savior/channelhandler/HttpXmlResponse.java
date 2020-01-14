package com.savior.channelhandler;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @Auther: weiwe
 * @Date: 2020/1/14 17:18
 * @Description:
 */
public class HttpXmlResponse {

    // 响应
    private FullHttpResponse response;

    private Object body;

    public HttpXmlResponse(FullHttpResponse response, Object body) {
        this.response = response;
        this.body = body;
    }

    public FullHttpResponse getResponse() {
        return response;
    }

    public void setResponse(FullHttpResponse response) {
        this.response = response;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
