package com.zjut.object;

import com.google.gson.Gson;
import com.zjut.SimpleRPC.codec.Decoder;
import com.zjut.SimpleRPC.codec.Encoder;
import com.zjut.SimpleRPC.codec.NettyCodecAdapter;
import com.zjut.SimpleRPC.core.RpcClientHandler;
import com.zjut.SimpleRPC.core.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * Created by Ryan on 2016/11/22.
 */
public class ObjectClient {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.git
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            if (sslCtx != null) {
                                pipeline.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
                            }
                            //p.addLast(new LoggingHandler(LogLevel.INFO));
//                            pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//                            pipeline.addLast("decoder", new StringDecoder());
//                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast("decoder", new Decoder(RpcRequest.class,NettyCodecAdapter.getInstance()));
                            pipeline.addLast("encoder", new Encoder(NettyCodecAdapter.getInstance()));
                            pipeline.addLast("handler", new RpcClientHandler(null));
//                            pipeline.addLast("handler", new ObjectClientHandler());
                        }
                    });
            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // 控制台输入
            User user = new User("Ryan",20161123,19);
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setClassName("test111");
            Gson gson = new Gson();
            String userString = gson.toJson(rpcRequest);
            f.channel().writeAndFlush(rpcRequest);
//            f.channel().closeFuture().sync();
        } finally {
            Thread.sleep(100);
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}
