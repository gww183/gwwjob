package com.savior.serverhandler;

import com.savior.proto.ClssInfoProtoBuf;
import com.savior.vo.ClassInfo;
import com.savior.vo.ResponInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 11:05
 * @Description:
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] req = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(req);
        ClassInfo classInfo = (ClassInfo) msg;
        System.out.println(classInfo);
        Date date = new Date();
//        ClssInfoProtoBuf.Request request = (ClssInfoProtoBuf.Request) msg;
//        System.out.println(request.toString());
       /* String currentTime = "";
        if(body.equals("query time")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTime = simpleDateFormat.format(new Date());
        } else {
            currentTime = "illegal quer type";
        }*/
        /*ResponInfo responInfo = new ResponInfo();
        responInfo.setRespCode(200);
        responInfo.setMessage("服务器在"+date.getTime()+"时间收到 类名为" + body.getName() + "的请求");
        ctx.writeAndFlush(responInfo);*/
        count++;
        ResponInfo responInfo = new ResponInfo();
        responInfo.setMessage("成功");
        responInfo.setRespCode(200);
        ctx.writeAndFlush(responInfo);
//        ctx.writeAndFlush(createResponse(request.getClaseName(), count));
    }

    private ClssInfoProtoBuf.Response createResponse(String className, int id) {
        ClssInfoProtoBuf.Response.Builder response = ClssInfoProtoBuf.Response.newBuilder();
        response.setStatusCode(200);
        response.setMessage("成功");
        ClssInfoProtoBuf.Data.Builder dataBuild = ClssInfoProtoBuf.Data.newBuilder();
        dataBuild.setName(className);
        dataBuild.setAge(2000);
        dataBuild.setId(id);
        dataBuild.setAddress("家乡第:" + id + "号");
        response.setData(dataBuild.build());
        return response.build();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
