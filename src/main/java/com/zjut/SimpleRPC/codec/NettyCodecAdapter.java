package com.zjut.SimpleRPC.codec;


import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import com.zjut.SimpleRPC.codec.serialize.JsonSerializer;
import com.zjut.SimpleRPC.common.SerializationUtil;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by Ryan on 2016/11/24.
 */
public class NettyCodecAdapter implements Codec {

    private NettyCodecAdapter(){}

    private static JsonSerializer jsonSerializer = new JsonSerializer();

    public byte[] encode(Object obj) throws IOException {
//        return jsonSerializer.serialize(obj);
        return SerializationUtil.serialize(obj);
    }

    public <T> T decode(byte[] bytes, Class<T> clazz) throws IOException {
//        return jsonSerializer.deserialize(bytes, clazz);
        return SerializationUtil.deserialize(bytes,clazz);
    }

    public static NettyCodecAdapter getInstance(){
        return Instance.getInstance();
    }

    static class Instance{
        private static NettyCodecAdapter nettyCodecAdapter= new NettyCodecAdapter();
        public static NettyCodecAdapter getInstance(){
            return nettyCodecAdapter;
        }
    }
}
