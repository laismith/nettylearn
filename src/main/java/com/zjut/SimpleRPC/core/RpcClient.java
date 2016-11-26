package com.zjut.SimpleRPC.core;

import com.zjut.SimpleRPC.codec.Decoder;
import com.zjut.SimpleRPC.codec.Encoder;
import com.zjut.SimpleRPC.codec.NettyCodecAdapter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ryan on 2016/11/24.
 */
public class RpcClient {
    private static final Logger logger = Logger.getLogger(RpcClient.class);

    @Value("#{clientRpcProperty['host']}")

    private String host;

    @Value("#{clientRpcProperty['port']}")
    private int port;
    private EventLoopGroup group;

    public Channel getChannel() {
        return channel;
    }

    private Channel channel;

    public Map<String, RpcResponse> getRpcResponses() {
        return rpcResponses;
    }

    public void setRpcResponses(Map<String, RpcResponse> rpcResponses) {
        this.rpcResponses = rpcResponses;
    }

    private Map<String,RpcResponse> rpcResponses = new ConcurrentHashMap<>();

    public RpcClient() {}

    public void start() throws Exception {
        if (StringUtils.isEmpty(host)) {
            logger.info("can not find client host config-info ,set host to default value which is 127.0.0.1");
            this.host = "127.0.0.1";
        }

        if (port == 0) {
            logger.info("can not find client port config-info ,set port to default value which is 8007");
            this.port = 8007;
        }
        this.bootNetty();
    }


    public void bootNetty() throws Exception {

        group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        // 解码 和 编码
                        pipeline.addLast("decoder", new Decoder(RpcResponse.class,NettyCodecAdapter.getInstance()));
                        pipeline.addLast("encoder", new Encoder(NettyCodecAdapter.getInstance()));
                        pipeline.addLast("handler", new RpcClientHandler(rpcResponses));
                    }
                });
        // Start the client.
        ChannelFuture f = b.connect(host, port).sync();
        channel = f.channel();
//        f.channel().closeFuture().sync();
    }

    public void shutdown() {
        this.group.shutdownGracefully();
    }

}
