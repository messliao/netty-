package com.phei.netty.timeServer.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable {
    private Socket socket;
    public TimeServerHandler(Socket socket){
        this.socket=socket;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */

    @Override
    public void run() {
        BufferedReader in=null;
        PrintWriter out=null;
        try{
            Thread.sleep(20000);
            in=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out=new PrintWriter(this.socket.getOutputStream(),true);
            String cuurrentTime=null;
            String body=null;
            while(true){
                body=in.readLine();
                if(body==null){
                    break;
                }
                System.out.println("The time server receive order : "+body);
                cuurrentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(
                        System.currentTimeMillis()).toString():"BAD ORDER";
                out.println(cuurrentTime);

            }
        } catch (IOException e) {
            if(in!=null){
                try{
                    in.close();
                }catch(IOException le){
                    le.printStackTrace();
                }
            }
            if(out!=null){
                out.close();
                out=null;
            }
            if(this.socket!=null){
                try{
                    this.socket.close();
                }catch(IOException el){
                    el.printStackTrace();
                }
                this.socket=null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
