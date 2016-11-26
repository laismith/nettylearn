package com.zjut.object;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Created by Ryan on 2016/11/22.
 */

public class ObjectServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(ObjectServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 收到消息直接打印输出
        logger.info(ctx.channel().remoteAddress() + " Say : " + msg);
        Gson gson = new Gson();
        User user = gson.fromJson(msg.toString(), User.class);
        logger.info("object:" + user.toString());
        //加\n分隔符
        ctx.write(msg+ "\n");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
