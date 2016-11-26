package com.zjut.SimpleRPC.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by Ryan on 2016/11/24.
 */
public class Encoder extends MessageToByteEncoder<Serializable> {

    private NettyCodecAdapter nettyCodecAdapter;

    public Encoder(NettyCodecAdapter nettyCodecAdapter) {
        this.nettyCodecAdapter = nettyCodecAdapter;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        byte[] bytes = nettyCodecAdapter.encode(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
