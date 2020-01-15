package com.savior.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * @Auther: weiwe
 * @Date: 2020/1/9 16:35
 * @Description:
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {

    private IBindingFactory factory = null;

    private StringWriter writer = null;

    private final static String CHARSET_NAME = "UTF-8";

    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected ByteBuf encode0(Object body) throws Exception {
        try {
            factory = BindingDirectory.getFactory(body.getClass());
            writer = new StringWriter();
            IMarshallingContext mctx = factory.createMarshallingContext();
            mctx.setIndent(2);
            mctx.marshalDocument(body, CHARSET_NAME, null, writer);
            String xmlStr = writer.toString();
            writer.close();
            writer = null;
            return Unpooled.copiedBuffer(xmlStr, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

}
