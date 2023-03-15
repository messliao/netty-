package com.phei.netty.timeServer.nettyNIO;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeClient {
    public void connect(int port,String host) throws InterruptedException {
        //配置客户端nio线程组
        EventLoopGroup group=new NioEventLoopGroup();//创建客户端处理I/O读写的NioEventLoop线程组
        try{
            Bootstrap b=new Bootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            //发起异步连接操作
            ChannelFuture f=b.connect(host,port).sync();//发起异步连接，调用同步方法等待连接成功

            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally{
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
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
        new TimeClient().connect(port,"127.0.0.1");
    }
}
