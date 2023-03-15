package com.phei.netty.protocol.netty.struct;

public class NettyMessage {
    //消息头
    private Header header;
    //消息体
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage[" +
                "header=" + header +
                ", object=" + body +
                ']';
    }
}
