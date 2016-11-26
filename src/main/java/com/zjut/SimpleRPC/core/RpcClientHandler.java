package com.zjut.SimpleRPC.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by Ryan on 2016/11/25.
 */

public class RpcClientHandler extends  SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger logger = Logger.getLogger(RpcClientHandler.class);

    private Map<String,RpcResponse> rpcResponses;

    public RpcClientHandler(Map<String, RpcResponse> rpcResponses) {
        this.rpcResponses = rpcResponses;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelActive");
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("Unexpected exception from downstream.", cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        //TODO
        logger.debug(response.toString());
        RpcResponse rpcResponse= rpcResponses.get(response.getRequestId());
        synchronized (rpcResponse){
            rpcResponse.setResult(response.getResult());
            rpcResponse.notifyAll();
        }
    }
}
