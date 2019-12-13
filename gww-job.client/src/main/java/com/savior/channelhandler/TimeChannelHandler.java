package com.savior.channelhandler;

import com.savior.vo.ClassInfo;
import com.savior.vo.ResponInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 14:48
 * @Description:
 */
public class TimeChannelHandler extends ChannelHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 100; i++) {
            /*byte[] req = ("query time$_").getBytes();
            ByteBuf byteBuf = Unpooled.buffer(req.length);
            byteBuf.writeBytes(req);*/
            /*ClssInfoProtoBuf.Request.Builder request = ClssInfoProtoBuf.Request.newBuilder();
            request.setClaseName("class_" + i);
            request.setMethod("method_" + i);
            request.setField("field_" + i);
            ctx.write(request.build());*/
            ClassInfo classInfo = new ClassInfo();
            classInfo.setParam("param_" + i);
            classInfo.setMethod("method_" + i);
            classInfo.setFieldName("field_" + i);
            classInfo.setName("class_" + i);
            ctx.write(classInfo);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf req = (ByteBuf) msg;
//        byte[] bytes = new byte[req.readableBytes()];
//        req.readBytes(bytes);
//        String body = new String(bytes, "UTF-8");
        /*ClssInfoProtoBuf.Response response = (ClssInfoProtoBuf.Response) msg;
        System.out.println(response.toString());
        if(response.getStatusCode() == 200) {
            System.out.println(response.getData());
        }*/
        ResponInfo responInfo = (ResponInfo) msg;
        System.out.println(responInfo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
