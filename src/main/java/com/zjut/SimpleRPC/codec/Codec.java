package com.zjut.SimpleRPC.codec;

import java.io.IOException;

/**
 * Created by Ryan on 2016/11/24.
 */
public interface Codec {
    /**
     * 编码
     * @param obj
     * @return
     * @throws IOException
     */
    byte[] encode(Object obj) throws IOException;

    /**
     * 解码
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T decode(byte[] bytes,Class<T> clazz) throws IOException;
}
