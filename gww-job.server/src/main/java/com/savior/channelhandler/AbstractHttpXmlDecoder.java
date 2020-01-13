package com.savior.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * @Auther: weiwe
 * @Date: 2020/1/10 17:22
 * @Description:
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

    private IBindingFactory bindingFactory;

    private StringReader stringReader;

    private Class<T> tClass;

    private boolean isPrint;

    private final static String CHARSET_NAME = "UTF-8";

    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    public AbstractHttpXmlDecoder(Class<T> tClass) {
        this(tClass, false);
    }

    public AbstractHttpXmlDecoder(Class<T> tClass, boolean isPrint) {
        this.tClass = tClass;
        this.isPrint = isPrint;
    }

    protected Object decode0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws JiBXException {
        bindingFactory = BindingDirectory.getFactory(tClass);
        String content = byteBuf.toString(UTF_8);
        if (isPrint) {
            System.out.println("The body is " + content);
        }
        stringReader = new StringReader(content);
        IUnmarshallingContext uctx = bindingFactory.createUnmarshallingContext();
        Object result = uctx.unmarshalDocument(stringReader);
        stringReader.close();
        stringReader = null;
        return result;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (stringReader != null) {
            stringReader.close();
            stringReader = null;
        }
    }
}
