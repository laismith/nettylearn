package com.zjut.SimpleRPC.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

/**
 * Created by Ryan on 2016/11/25.
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOG = Logger.getLogger(RpcServerHandler.class);

    @Resource
    private Exporter rpcExporter;

    public RpcServerHandler(Exporter rpcExporter) {
        this.rpcExporter = rpcExporter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        LOG.debug(request.toString());
        //TODO:服务器反射，加返回response
        //ExportedServiceBean容器内拿注册的服务
        Object serviceBean = rpcExporter.getExportedServiceBean(request.getClassName(), null);
        Class<?> serviceClass = serviceBean.getClass();
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(request.getMethodName(), request.getParameterTypes());
        Object o = serviceFastMethod.invoke(serviceBean, request.getParameters());
        //封装响应报文
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        response.setResult(o);
        //响应数据传回去
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info("channelActive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error("server caught exception", cause);
        ctx.close();
    }
}
