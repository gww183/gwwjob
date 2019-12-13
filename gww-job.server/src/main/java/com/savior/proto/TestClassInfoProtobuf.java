package com.savior.proto;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @Auther: weiwe
 * @Date: 2019/12/6 16:03
 * @Description:
 */
public class TestClassInfoProtobuf {

    private static byte[] encode(ClssInfoProtoBuf.Request request) {
        return request.toByteArray();
    }

    private static ClssInfoProtoBuf.Request decode(byte[] body) {
        try {
            return ClssInfoProtoBuf.Request.parseFrom(body);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ClssInfoProtoBuf.Request createRequest() {
        ClssInfoProtoBuf.Request.Builder builder = ClssInfoProtoBuf.Request.newBuilder();
        builder.setClaseName("测试class");
        builder.setField("name");
        builder.setMethod("invoke");
        return builder.build();
    }

    public static void main(String[] arg) {
        ClssInfoProtoBuf.Request request = createRequest();
        System.out.println("Before encode:" + request.toString());
        ClssInfoProtoBuf.Request request1 = decode(encode(request));
        System.out.println("After decode :" + encode(request).length);
    }

}
