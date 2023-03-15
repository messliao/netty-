package com.phei.netty.protocol.netty.server;

import com.phei.netty.protocol.netty.MessageType;
import com.phei.netty.protocol.netty.struct.Header;
import com.phei.netty.protocol.netty.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAppender;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelHandlerAppender {

    private final static Log LOG= LogFactory.getLog(LoginAuthRespHandler.class);

    private Map<String,Boolean> nodeCheck=new ConcurrentHashMap<>();
    private String[] whiteList={"127.0.0.1","192.168.1.104"};


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message=(NettyMessage)msg;

        //如果是握手请求消息，处理，其它消息透传
        if(message.getHeader()!=null && message.getHeader().getType()== MessageType.LOGIN_REQ.value()){
            String nodeIndex=ctx.channel().remoteAddress().toString();
            NettyMessage loginResp=null;
            //重复登录，拒绝
            if(nodeCheck.containsKey(nodeIndex)){
                loginResp=buildResponse((byte)-1);
            }else{
                InetSocketAddress address=(InetSocketAddress)ctx.channel().remoteAddress();
                String ip=address.getAddress().getHostAddress();
                boolean isOK=false;
                for(String WIP: whiteList){
                    if(WIP.equals(ip)){
                        isOK=true;
                        break;
                    }
                }
                loginResp=isOK?buildResponse((byte)0):buildResponse((byte)-1);
                if(isOK)
                    nodeCheck.put(nodeIndex,true);
            }
            LOG.info("The login response is : "+loginResp+"body["+loginResp.getBody()+"]");
            ctx.writeAndFlush(loginResp);
        }else{
            ctx.writeAndFlush(msg);
        }
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message=new NettyMessage();
        Header header=new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
