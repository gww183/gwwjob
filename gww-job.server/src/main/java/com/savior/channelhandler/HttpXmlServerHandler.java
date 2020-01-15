package com.savior.channelhandler;

import com.savior.channelhandler.HttpXmlRequest;
import com.savior.channelhandler.HttpXmlResponse;
import com.savior.jibx.pojo.Address;
import com.savior.jibx.pojo.Customer;
import com.savior.jibx.pojo.Order;
import com.savior.jibx.pojo.Shipping;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Arrays;

/**
 * @Auther: weiwe
 * @Date: 2020/1/15 10:48
 * @Description:
 */
public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {

    @Override
    protected void messageReceived(final ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
        HttpRequest httpRequest = msg.getRequest();
        Order order = (Order) msg.getBody();
        System.out.println("Http server receive request:" + order.getCustomer().getLastName());

        Order order1 = createOrder(456);
        ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, order1));
        if (!HttpHeaderUtil.isKeepAlive(httpRequest)) {
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    ctx.close();
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param ctx
     * @param
     */
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("失败:" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * @param
     * @return
     */
    private Order createOrder(int orderNumber) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setShipping(Shipping.PRIORITY_MALL);
        Customer customer = new Customer();
        customer.setCustomerNumber(1112222);
        customer.setFistName("张");
        customer.setLastName("三");
        customer.setMiddleNames(Arrays.asList("王", "刘"));
        order.setCustomer(customer);
        order.setTotal(12.4f);
        Address address = new Address();
        address.setCity("北京");
        address.setCountry("中国");
        address.setPostCode("010");
        address.setState("中国");
        address.setStreet1("长安街");
        address.setStreet2("万宝路");
        order.setBillTo(address);
        return order;
    }

}
