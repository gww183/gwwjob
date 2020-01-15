package com.savior.channelhandler;

import com.savior.jibx.pojo.Address;
import com.savior.jibx.pojo.Customer;
import com.savior.jibx.pojo.Order;
import com.savior.jibx.pojo.Shipping;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;

/**
 * @Auther: weiwe
 * @Date: 2020/1/15 09:49
 * @Description:
 */
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpXmlRequest request = new HttpXmlRequest(null, createOrder(11000));
        ctx.writeAndFlush(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
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

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("the client receive response of http header is :" + msg.getResponse().headers().names());
        System.out.println("the client receive response of http body is : " + msg.getBody());
    }
}
