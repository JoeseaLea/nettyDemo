package com.joesea.netty.server.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * <p>@author : Joesea Lea</p>
 * <p>@date : 2020/2/21</p>
 * <p>@description : </p>
 */
public class NettyHttpServer {

    private static final int PORT = 8001;

    private static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2;
    private static final int BIZTHREADSIZE = 100;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

    public static void service() throws Exception {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel> () {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

//                        ChannelPipeline pipeline = socketChannel.pipeline();
//                        pipeline.addLast(new HttpServerCodec());// http 编解码
//                        pipeline.addLast("httpAggregator",new HttpObjectAggregator(512*1024)); // http 消息聚合器                                                                     512*1024为接收的最大contentlength
//                        pipeline.addLast(new NettyHttpServerHandler());// 请求处理器
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpServerCodec());// http 编解码
//                        pipeline.addLast("encoder",new HttpResponseEncoder());
//                        pipeline.addLast("decoder",new HttpRequestDecoder());
                        pipeline.addLast(new HttpObjectAggregator(10*1024*1024));//把单个http请求转为FullHttpReuest或FullHttpResponse
                        pipeline.addLast(new HttpServerExpectContinueHandler());
                        pipeline.addLast(new NettyHttpServerHandler());// 服务端业务逻辑

                    }
                });

        ChannelFuture f = bootstrap.bind(new InetSocketAddress(PORT)).sync();
        System.out.println("Http服务器启动完成");
        f.channel().closeFuture().sync();

    }

    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("开始启动Http服务器...");
        NettyHttpServer.service();
    }
}
