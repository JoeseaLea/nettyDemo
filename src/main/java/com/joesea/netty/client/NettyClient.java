package com.joesea.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * <p>@author : Joesea Lea</p>
 * <p>@date : 2019/4/22</p>
 * <p>@description : </p>
 */
public class NettyClient {

    private void connect() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 0));
//                    pipeline.addLast(new LengthFieldPrepender(4));
//                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
//                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

                    pipeline.addLast(new NettyClientHandler());
                }
            });
            for (int i = 0; i < 10; i++) {
                ChannelFuture f = b.connect("127.0.0.1", 5656).sync();

                byte[] msg = "12345678abc123".getBytes();
                ByteBuf byteBuf = Unpooled.buffer();
                byteBuf.writeInt(msg.length);
                byteBuf.writeBytes(msg);

//                System.out.println(byteBuf.readInt());
                System.out.println(byteBuf);

                f.channel().writeAndFlush(byteBuf);
                f.channel().closeFuture().sync();
            }


        } catch (Exception e) {

        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect();
    }
}
