package com.phei.netty.timeServer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable{
    private Selector selector;

    private ServerSocketChannel servChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     */
    public MultiplexerTimeServer(int port){
        try{
            selector=Selector.open();
            servChannel=ServerSocketChannel.open();
            servChannel.configureBlocking(false);
            servChannel.socket().bind(new InetSocketAddress(port),1024);
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : "+port);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop=stop;
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
        while(!stop){
            try{
                selector.select(1000);
                Set<SelectionKey>  selectionKeys=selector.selectedKeys();
                Iterator<SelectionKey> it=selectionKeys.iterator();
                SelectionKey key=null;
                while(it.hasNext()){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
