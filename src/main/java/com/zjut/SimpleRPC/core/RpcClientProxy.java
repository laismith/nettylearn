package com.zjut.SimpleRPC.core;

import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by Ryan on 2016/11/25.
 */
public class RpcClientProxy implements RpcProxy{

    public static final Logger logger = Logger.getLogger(RpcClientProxy.class);

    private RpcClient rpcClient;

    private Channel channel;

    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setRpcClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        channel.writeAndFlush(request);
                        logger.info(request);

                        //TODO:得到返回值

//                        RpcClient client = new RpcClient(host, port); // 初始化 RPC 客户端
//                        RpcResponse response = client.send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应

//                        if (response.isError()) {
//                            throw response.getError();
//                        } else {
//                            return response.getResult();
//                        }
                        String s = "";
                        for (Object arg : args) {
                            s += arg.toString();
                        }
                        return "JDKProxy:" + method.getName() + " " + s;
                    }
                }
        );
    }
}
