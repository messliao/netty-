package com.phei.netty.protocol.netty.client;

import com.phei.netty.protocol.netty.MessageType;
import com.phei.netty.protocol.netty.struct.Header;
import com.phei.netty.protocol.netty.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoginAuthReqHandler extends ChannelHandlerAppender{
    private static final Log LOG= LogFactory.getLog(LoginAuthReqHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage)msg;
        //如果是握手应答消息，需要判断是否让认证成功
        if(message.getHeader()!=null && message.getHeader().getType()==MessageType.LOGIN_RESP.value()){
            byte loginResult=(byte)message.getBody();
            if(loginResult!=(byte)0){
                //握手失败，关闭连接
                ctx.close();
            }else{
                System.out.println("Login is Ok : "+message);
                ctx.fireChannelRead(msg);
            }
        }else{
            //不是握手消息，直接传给后面的handler进行处理
            ctx.fireChannelRead(msg);
        }
        super.channelRead(ctx, msg);
    }

    /**
     * 构造握手请求消息发送给服务端，采用ip认证机制，不需要携带消息体，消息类型3
     * @return
     */
    private NettyMessage buildLoginReq() {
        NettyMessage message=new NettyMessage();
        Header header=new Header();
        header.setType(MessageType.LOGIN_REQ.value());//握手请求消息
        message.setHeader(header);
        return message;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

