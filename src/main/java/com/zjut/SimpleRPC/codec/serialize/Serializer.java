package com.zjut.SimpleRPC.codec.serialize;

import java.io.IOException;

/**
 * Created by Ryan on 2016/11/24.
 */
public interface Serializer {

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;

}
