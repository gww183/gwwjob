package com.savior.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * @Auther: weiwe
 * @Date: 2020/1/16 16:07
 * @Description:
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {


    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) { // 正常请求
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) { // webSocket请求
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
    }

    /**
     * 处理webSocket请求
     *
     * @param ctx
     * @param msg
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) throws UnsupportedOperationException {
        if (msg instanceof CloseWebSocketFrame) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            webSocketServerHandshaker.close(ctx.channel(), ((CloseWebSocketFrame) msg).retain());
            return;
        }
        if (!(msg instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", msg.getClass().getName()));
        }

        String request = ((TextWebSocketFrame) msg).text();
        System.out.println(String.format("the server recive msg %s", request));
        int i = 10;
        while(i > 0) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request + ", 发送：" + i));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i --;
        }
    }

    /**
     * 处理正常请求
     *
     * @param ctx
     * @param msg
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        if (!msg.decoderResult().isSuccess() || (!"websocket".equals(msg.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, msg, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8899/websocket", null, false);
        webSocketServerHandshaker = wsFactory.newHandshaker(msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            webSocketServerHandshaker.handshake(ctx.channel(), msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest msg, FullHttpResponse response) {
        if (response.status() != HttpResponseStatus.OK) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
            HttpHeaderUtil.setContentLength(response, response.content().readableBytes());
        }

        ChannelFuture future = ctx.channel().writeAndFlush(response);
        if (!HttpHeaderUtil.isKeepAlive(msg) || response.status() != HttpResponseStatus.OK) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
