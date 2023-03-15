package com.phei.netty.timeServer.nettyNIO;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class TimeServerHandler  extends ChannelHandlerAppender {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;
        byte[] req=new byte[buf.readableBytes()];//readableBytes() 获取缓冲区可读字节的个数
        buf.readBytes(req);//将缓冲区的字节数组复制到新建的byte数组
        String body=new String(req,"UTF-8");//从字节数组中获得请求消息
        System.out.println("The time server receive order: "+body);
        String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        ByteBuf resp= Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);//异步消息给客户端 把待发送的消息放到缓冲数组中
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();//将消息发送队列中（缓冲区 缓冲数组）的消息写到SocketChannel中发送给对方
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
