package com.zjut.SimpleRPC.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import javax.annotation.Resource;

/**
 * Created by Ryan on 2016/11/24.
 */
public class Decoder extends LengthFieldBasedFrameDecoder {

    @Resource
    private NettyCodecAdapter nettyCodecAdapter;


    private Class<?> clazz;

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Decoder(Class<?> clazz,NettyCodecAdapter nettyCodecAdapter) {
        super(1048576, 0, 4, 0, 4);
        this.clazz = clazz;
        this.nettyCodecAdapter = nettyCodecAdapter;
    }

//    public Decoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
//        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
//    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        int dataLength = frame.readableBytes();
        byte[] data = new byte[dataLength];
        frame.readBytes(data);
        return nettyCodecAdapter.decode(data, clazz);
    }

}
