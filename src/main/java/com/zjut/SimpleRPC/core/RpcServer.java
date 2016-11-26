package com.zjut.SimpleRPC.core;

import com.zjut.SimpleRPC.codec.Decoder;
import com.zjut.SimpleRPC.codec.Encoder;
import com.zjut.SimpleRPC.codec.NettyCodecAdapter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by Ryan on 2016/11/24.
 */
@Component
public class RpcServer {
    private static final Logger logger = Logger.getLogger(RpcServer.class);

    static final boolean SSL = System.getProperty("ssl") != null;

    //    @Value("#{server-rpc['host']}")
    private String host;

    //    @Value("#{server-rpc['port']}")
    private int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Exporter rpcExporter;

    public RpcServer init(Exporter rpcExporter, String host, int port) {
        this.rpcExporter = rpcExporter;
        this.host = host;
        this.port = port;
        return this;
    }
    public RpcServer init(Exporter rpcExporter) {
        this.rpcExporter = rpcExporter;
        return this;
    }

    public void start() throws Exception {
        if (StringUtils.isEmpty(host)) {
            logger.info("can not find Server host config-info ,set host to default value which is 127.0.0.1");
            this.host = "127.0.0.1";
        }

        if (port == 0) {
            logger.info("can not find Server port config-info ,set port to default value which is 8007");
            this.port = 8007;
        }
        this.bootNetty();
    }

    public void bootNetty() throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (sslCtx != null) {
                            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
                        }
                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        // 解码 和 编码
                        pipeline.addLast("decoder", new Decoder(RpcRequest.class, NettyCodecAdapter.getInstance()));
                        pipeline.addLast("encoder", new Encoder(NettyCodecAdapter.getInstance()));

                        // 自己的逻辑Handler
                        pipeline.addLast(new RpcServerHandler(rpcExporter));
                    }
                });

        // Start the server.
        ChannelFuture f = b.bind(host, port).sync();

        // Wait until the server socket is closed.
        f.channel().closeFuture().sync();
    }

    public void shutDown() {
        // Shut down all event loops to terminate all threads.
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public RpcServer export(Class<?> clazz, Object object) {
        return export(clazz, object, null);
    }

    public RpcServer export(Class<?> clazz, Object object, String version) {
        rpcExporter.export(clazz, object, version);
        return this;
    }
}
