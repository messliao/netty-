package com.phei.netty.timeServer.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {
    static int port=8080;
    public static void main(String[] args) throws InterruptedException {
        int i =0;

        while (true) {
            System.out.println("oo :   " + i);
            new SendThread("请求线程" + i++).start();

            if(i > 100) {
                break;
            }
        }

        Thread.sleep(1000000);
    }

    static class SendThread  extends Thread {
        SendThread(String thrreadName) {
          super.setName(thrreadName);
        }

        public void run() {
            Socket socket=null;
            BufferedReader in=null;
            PrintWriter out=null;
            try{
                socket=new Socket("127.0.0.1",port);
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println("QUERY TIME ORDER");
                System.out.println("Send order 2 server succeed.");
                String resp=in.readLine();
                System.out.println("Now is : "+resp);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(out!=null){
                    out.close();
                    out=null;
                }

                if(in!=null){
                    try{
                        in.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    in=null;
                }

                if(socket!=null){
                    try{
                        socket.close();
                    }catch(IOException E){
                        E.printStackTrace();
                    }
                    socket=null;
                }
            }
        }
    }
}
