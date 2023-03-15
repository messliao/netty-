package com.phei.netty.protocol.netty.server;

import com.phei.netty.protocol.netty.MessageType;
import com.phei.netty.protocol.netty.struct.Header;
import com.phei.netty.protocol.netty.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatRespHandler extends ChannelHandlerAppender {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage)msg;
        //返回心跳应答消息
        if(message.getHeader()!=null && message.getHeader().getType()== MessageType.HEARTBEAT_REQ.value()){
            System.out.println("Receive client heart beat message : ----> "+message);
            NettyMessage heartBeat=buildHeatBeat();
            System.out.println("Send heart beat response message yo client ： ---->"+heartBeat);
            ctx.writeAndFlush(heartBeat);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeatBeat() {
        NettyMessage message=new NettyMessage();
        Header header=new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return  message;
    }
}
