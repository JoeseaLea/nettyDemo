package com.joesea.netty.server.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * <p>@author : Joesea Lea</p>
 * <p>@date : 2019/4/22</p>
 * <p>@description : </p>
 */
public class NettySocketServer {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 5656;
    private static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2;

    private static final int BIZTHREADSIZE = 100;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);
    public static void service() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                // TODO Auto-generated method stub
                ChannelPipeline pipeline = ch.pipeline();
                LengthFieldBasedFrameDecoder h;
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 0));
//                pipeline.addLast(new LengthFieldPrepender(4));
//                pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
//                pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new NettySocketServerHandler());
            }

        });
        ChannelFuture f = bootstrap.bind(IP, PORT).sync();
        f.channel().closeFuture().sync();
        System.out.println("TCP服务器已启动");
    }

    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("开始启动TCP服务器...");
        NettySocketServer.service();
    }
}
