package com.savior.channelhandler;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @Auther: weiwe
 * @Date: 2020/1/10 15:08
 * @Description:
 */
public class HttpXmlRequest {

    private FullHttpRequest request;

    private Object body;

    public HttpXmlRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
