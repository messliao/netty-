package com.phei.netty.protocol.netty.client;

import com.phei.netty.protocol.netty.NettyConstant;
import com.phei.netty.protocol.netty.codec.NettyMessageDecoder;
import com.phei.netty.protocol.netty.codec.NettyMessageEncoder;
import com.phei.netty.protocol.netty.server.LoginAuthRespHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private ScheduledExecutorService executor= Executors.newScheduledThreadPool(1);
    EventLoopGroup group=new NioEventLoopGroup();
    public void connect(int port,String host) throws InterruptedException {
        try{
            Bootstrap b=new Bootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));
                            ch.pipeline().addLast("MessageEncoder",new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler",new LoginAuthRespHandler());
                            ch.pipeline().addLast("HeartBeatHandler",new HeartBeatReqHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture future=b.connect(new InetSocketAddress(host,port),new InetSocketAddress(NettyConstant.LOCALIP,NettyConstant.LOCAL_PORT) ).sync();
        }finally {
            //所有资源释放完成后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        TimeUnit.SECONDS.sleep(5);
                        try{
                            connect(NettyConstant.PORT,NettyConstant.REMOTEIP);//发起重新来连接
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect(NettyConstant.PORT,NettyConstant.REMOTEIP);
    }
}
