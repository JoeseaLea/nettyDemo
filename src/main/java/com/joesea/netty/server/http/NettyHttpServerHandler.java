package com.joesea.netty.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>@author : Joesea Lea</p>
 * <p>@date : 2020/2/21</p>
 * <p>@description : </p>
 */
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {
//    private ServerBean serverBean;
//    private ConcurrentHashMap<String, MapData> channelMap;
    private HttpRequest request = null;
    private int contentLen = 0;
    private ByteBuf msgBuf = null;
    private String uuid = null;
//    public ChannelBuffer bufferOK = null;
//    public ChannelBuffer bufferError = null;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String result="";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
        }
//        //请求头处理
//        if (msg instanceof HttpRequest) {
//            request = (HttpRequest) msg;
//            //获取content内容的长度
//            String str = request.headers().get("Content-Length");
//            if (str != null) {
//                contentLen = Integer.parseInt(request.headers().get("Content-Length"));
//                if(msgBuf == null){
//                    msgBuf = Unpooled.buffer(contentLen);
//                }
//            }
//        }
//        //请求内容获取
//        if (msg instanceof HttpContent) {
//            HttpContent content = (HttpContent) msg;
//            ByteBuf buf = content.content();
////            msgBuf.writeBytes(buf);
////            buf.release();
//            System.out.println(buf.toString(CharsetUtil.UTF_8));
//        }

        if(!(msg instanceof FullHttpRequest)){
            result="未知请求!";
//            send(ctx,result,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        try{
            String path=httpRequest.uri();          //获取路径
            String body = getBody(httpRequest);     //获取参数
            HttpMethod method=httpRequest.method();//获取请求方法
            //如果不是这个路径，就直接返回错误
//            if(!"/test".equalsIgnoreCase(path)){
//                result="非法请求!";
//                send(ctx,result,HttpResponseStatus.BAD_REQUEST);
//                return;
//            }
            System.out.println("接收到:"+method+" 请求");
            //如果是GET请求
            if(HttpMethod.GET.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="GET请求";
//                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
            //如果是POST请求
            if(HttpMethod.POST.equals(method)){
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder("/?" + body);
                Map<String, List<String>> params = queryStringDecoder.parameters();

                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="POST请求";
//                send(ctx,result,HttpResponseStatus.OK);
                return;
            }

            //如果是PUT请求
            if(HttpMethod.PUT.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="PUT请求";
//                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
            //如果是DELETE请求
            if(HttpMethod.DELETE.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="DELETE请求";
//                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
            //如果是DELETE请求
            if(HttpMethod.OPTIONS.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="OPTIONS请求";
//                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
        }catch(Exception e){
            System.out.println("处理请求失败!");
            e.printStackTrace();
        }finally{
            //释放请求
            httpRequest.release();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        result = "hello world，你好，世界";
        send(ctx,result,HttpResponseStatus.OK);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 发送的返回值
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
     */
    private void send(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {

        byte[] msgByte = context.getBytes();

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(msgByte));


        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        //返回值是json

       
       //允许跨域访问
       response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
       response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS,"Origin, X-Requested-With, Content-Type, Accept");
       response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS,"GET, POST, PUT,DELETE");
       
       response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
//       if (HttpHeaders.isKeepAlive(request)) {
//           response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.Values.KEEP_ALIVE);
//       }
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderNames.KEEP_ALIVE);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            System.out.println("-----end-----");
        }
//        ctx.flush();

//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
//        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 获取body参数
     * @param request
     * @return
     */
    private String getBody(FullHttpRequest request){
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }


}
