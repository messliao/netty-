package com.phei.netty.timeServer.io;

import com.phei.netty.timeServer.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port=8080;
        if(args!=null && args.length>0){
            try{
                port=Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //default
            }
        }
        ServerSocket server=null;
        try{
            server=new ServerSocket(port);
            System.out.println("The time server is start in port :"+port);
            TimeServerHnadlerExecutePool singleExecutor=new TimeServerHnadlerExecutePool(50,10000);//创建I/O任务线程池、
            Socket socket=null;
            while(true){
                socket=server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        }finally {
            if(server!=null){
                System.out.println("The time server close");
                server.close();
                server=null;
            }
        }
    }
}
