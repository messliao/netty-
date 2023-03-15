package com.phei.netty.timeServer.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther zrs
 * @data  2020/10/14
 * @version 1.0
 */
public class TimeServer {
    public static AtomicInteger count = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        int port=8080;
        if(args!=null && args.length>0){
            try{
                port=Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                //采用默认值
            }
        }
        ServerSocket server=null;
        try{
            server=new ServerSocket(port);
            System.out.println("The time server is start in port : "+port);
            Socket socket=null;
            while(true){
                socket=server.accept();
                new Thread(new TimeServerHandler(socket), "业务线程" + count.getAndAdd(1)).start();
            }
        } finally {
            if(server!=null){
                System.out.println("The time server close");
                server.close();
                server=null;
            }
        }
    }
}
