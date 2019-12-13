package com.savior.channelhandler;

import com.savior.marshalling.MarshallingCodeFactory;
import com.savior.proto.ClssInfoProtoBuf;
import com.savior.serverhandler.TimeServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.marshalling.MarshallerFactory;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 10:46
 * @Description:
 */
public class TimeChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
//        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
//        ch.pipeline().addLast(new ProtobufDecoder(ClssInfoProtoBuf.Request.getDefaultInstance()));
//        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
//        ch.pipeline().addLast(new ProtobufEncoder());
        ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
        ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncode());
        ch.pipeline()
                /*.addLast(new ObjectDecoder(1024*1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                .addLast(new ObjectEncoder())*/
//                .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
//                .addLast(new FixedLengthFrameDecoder(20))
//                .addLast(new StringDecoder())
                .addLast(new TimeServerHandler());
    }
}
