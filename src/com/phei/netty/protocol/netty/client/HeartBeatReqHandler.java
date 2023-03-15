package com.phei.netty.protocol.netty.client;

import com.phei.netty.protocol.netty.MessageType;
import com.phei.netty.protocol.netty.struct.Header;
import com.phei.netty.protocol.netty.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler extends ChannelHandlerAppender {

    private static final Log LOG= LogFactory.getLog(HeartBeatReqHandler.class);

    private volatile ScheduledFuture<?> heartBeat;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage) msg;
        //握手成功，主动发送心跳消息
        if(message.getHeader().getType()== MessageType.LOGIN_RESP.value()){
            //启动无限循环定时器用于定期发送信条消息
            heartBeat=ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HearBeatTask(ctx),0,5000, TimeUnit.MILLISECONDS);
        }else if(message.getHeader()!=null && message.getHeader().getType()== MessageType.HEARTBEAT_RESP.value()){
            //用于接收服务端发送的心跳应答消息
            System.out.println("Client receive server heart message : ------->"+message);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private class HearBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HearBeatTask(final ChannelHandlerContext ctx){
            this.ctx=ctx;
        }

        @Override
        public void run(){
            NettyMessage heartBeat=buildHeartBeat();
            System.out.println("Client send heart beat message to server : --->"+heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat() {
            NettyMessage nettyMessage=new NettyMessage();
            Header header=new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            return nettyMessage;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       if(heartBeat!=null){
           heartBeat.cancel(true);
           heartBeat=null;
       }
       ctx.fireExceptionCaught(cause);
    }
}
