package com.phei.netty.timeServer.nettyNIO;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
    public void bind(int port) throws InterruptedException {
        //配置服务端线程组
        EventLoopGroup bossGroup=new NioEventLoopGroup();//线程组，包含一组NIO线程，用于网络事件的处理，Reactor线程组  服务端接收客户端的链接
        EventLoopGroup workerGroup=new NioEventLoopGroup(); //socketchannel的网络读写
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChildChannelHandler());
            //绑定端口，同步等待成功
            ChannelFuture f=b.bind(port).sync();//异步操作的通知回调

            // 阻塞 等待服务端监听端口关闭才从main退出
            f.channel().closeFuture().sync();
        }finally {
            //优雅的退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port=8080;
        if(args!=null && args.length>0){
            try{
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //default
            }
        }
        new TimeServer().bind(port);
    }
}
