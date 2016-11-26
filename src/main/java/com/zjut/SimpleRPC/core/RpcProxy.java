package com.zjut.SimpleRPC.core;

/**
 * Created by Ryan on 2016/11/26.
 */
public interface RpcProxy {
    <T> T create(Class<?> interfaceClass);
}
