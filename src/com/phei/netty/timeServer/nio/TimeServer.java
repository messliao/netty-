package com.phei.netty.timeServer.nio;

public class TimeServer {
    public static  void main(String[] args) {
        int port=8080;
        if(args!=null && args.length>0){
            try{
                port=Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                //default
            }
        }
        MultiplexerTimeServer timeServer=new MultiplexerTimeServer(port);
        new Thread((Runnable) timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
