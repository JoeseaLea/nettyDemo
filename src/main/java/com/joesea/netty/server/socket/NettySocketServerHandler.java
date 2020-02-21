package com.joesea.netty.server.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>@author : Joesea Lea</p>
 * <p>@date : 2019/4/22</p>
 * <p>@description : </p>
 */
public class NettySocketServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        ByteBuf byteBuf = (ByteBuf)msg;

        int msgLength = byteBuf.readInt();
        byte[] msgByte = new byte[msgLength];
        byteBuf.readBytes(msgByte);

        System.out.println(new String(msgByte));

        ctx.close();

//        // TODO Auto-generated method stub
//        System.out.println("server receive message :"+ msg);
//
//        for (int i = 0; i < 10000; i ++) {
//            msg += "0000000000";
//        }
//
//        msg += "abcdefghijklmn";
//        ctx.channel().writeAndFlush("yes server already accept your message" + msg);
//        ctx.close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channelActive>>>>>>>>");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exception is general");
    }
}
