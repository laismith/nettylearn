package com.zjut.SimpleRPC.core;

import io.netty.channel.Channel;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ryan on 2016/11/25.
 */
public class RpcClientProxyByCglib implements MethodInterceptor ,RpcProxy{
    public static final Logger logger = Logger.getLogger(RpcClientProxyByCglib.class);
    private RpcClient rpcClient;

    private Channel channel;

    private Map<String,RpcResponse> rpcResponses;

    public RpcClientProxyByCglib(RpcClient rpcClient, Map<String,RpcResponse> rpcResponses) {
        this.rpcClient = rpcClient;
        this.channel = rpcClient.getChannel();
        this.rpcResponses= rpcResponses;
    }

    public RpcClientProxyByCglib(Channel channel) {
        this.channel = channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setRpcClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    private Enhancer enhancer = new Enhancer();

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> clazz) {
        //设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //通过字节码技术动态创建子类实例
        return (T)enhancer.create();
    }

    //实现MethodInterceptor接口方法
    @Override
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        //封装rpcRequest
        RpcRequest rpcRequest = new RpcRequest();
        String s = UUID.randomUUID().toString();
        rpcRequest.setRequestId(s);
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        //发送rpcRequest
        channel.writeAndFlush(rpcRequest);

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(s);
        rpcResponses.put(s,rpcResponse);

        synchronized (rpcResponse){
            rpcResponse.wait();
        }


        return rpcResponse.getResult();
    }
}