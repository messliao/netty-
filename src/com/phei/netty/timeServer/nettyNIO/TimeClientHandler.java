package com.phei.netty.timeServer.nettyNIO;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

public class TimeClientHandler  extends ChannelHandlerAppender {
    private static final Logger logger=Logger.getLogger(TimeClientHandler.class.getName());

    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req="QUERY TIME ORDER".getBytes();
        firstMessage= Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    //当客户端和服务端TCP链路建立成功后， -- 发送查询时间的指令给服务端，
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage); //将请求消息发送给服务端
    }

    //服务端返回应答消息后 调用 读消息并打印
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req,"UTF-8");
        System.out.println("Now is ； "+body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //释放资源
        logger.warning("Unexcepted exception from downstream : "+cause.getMessage());
        ctx.close();
    }
}
