package com.savior.proto;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @Auther: weiwe
 * @Date: 2019/12/6 16:03
 * @Description:
 */
public class TestClassInfoProtobuf {

    private static byte[] encode(ClssInfoProtoBuf.Response request) {
        return request.toByteArray();
    }

    private static ClssInfoProtoBuf.Response decode(byte[] body) {
        try {
            return ClssInfoProtoBuf.Response.parseFrom(body);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ClssInfoProtoBuf.Response createRequest() {
        ClssInfoProtoBuf.Response.Builder builder = ClssInfoProtoBuf.Response.newBuilder();
        builder.setStatusCode(100);
        builder.setMessage("成功");
        ClssInfoProtoBuf.Data.Builder data = ClssInfoProtoBuf.Data.newBuilder();
        data.setId(1111);
        data.setAge(20);
        builder.setData(data.build());
        return builder.build();
    }

    public static void main(String[] arg) {
        ClssInfoProtoBuf.Response request = createRequest();
        System.out.println("Before encode:" + request.toString());
        ClssInfoProtoBuf.Response request1 = decode(encode(request));
        System.out.println("After decode :" + encode(request).length);
    }

}
