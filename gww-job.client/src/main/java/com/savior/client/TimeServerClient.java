package com.savior.client;

import com.savior.channelhandler.TimeChannelHandler;
import com.savior.marshalling.MarshallingCodeFactory;
import com.savior.proto.ClssInfoProtoBuf;
import com.savior.vo.ClassInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * @Auther: weiwe
 * @Date: 2019/11/28 14:32
 * @Description:
 */
public class TimeServerClient {

    public void connected(int port) {
        // 创建Nio线程组
        EventLoopGroup workGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
//                ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                // 增加处理半包器
//                ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
//                // 增加解码器(并且设置解码目标)
//                ch.pipeline().addLast(new ProtobufDecoder(ClssInfoProtoBuf.Response.getDefaultInstance()));
//                // 为消息增加32位的整形字段，用于标识消息长度
//                ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
//                // 设置编码器
//                ch.pipeline().addLast(new ProtobufEncoder());
                //
                ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder());
                ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncode());
                ch.pipeline()
                        /*.addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                        .addLast(new ObjectEncoder()) // 编码器*/
//                        .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
//                        .addLast(new FixedLengthFrameDecoder(20))
//                        .addLast(new StringDecoder())
                        .addLast(new TimeChannelHandler());
            }
        });
        try {
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(port)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
}
