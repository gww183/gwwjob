package com.savior.marshalling;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * @Auther: weiwe
 * @Date: 2019/12/12 10:35
 * @Description: marshalling decode/encode factory
 */
public class MarshallingCodeFactory {

    /**
     * 构建解码器
     *
     * @return
     */
    public static MarshallingDecoder buildMarshallingDecoder() {
        MarshallingDecoder decoder = null;
        try {
            final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
            final MarshallingConfiguration configuration = new MarshallingConfiguration();
            configuration.setVersion(5);
            UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
            decoder = new MarshallingDecoder(provider, 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decoder;
    }

    /***
     * 创建编码器
     * 
     * @return
     */
    public static MarshallingEncoder buildMarshallingEncode() {
        MarshallingEncoder marshallingEncoder = null;
        try {
            final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
            final MarshallingConfiguration configuration = new MarshallingConfiguration();
            configuration.setVersion(5);
            MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
            marshallingEncoder = new MarshallingEncoder(provider);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return marshallingEncoder;
    }


}
