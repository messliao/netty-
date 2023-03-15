package com.phei.netty.protocol.netty.struct;

import java.util.HashMap;
import java.util.Map;

public class Header {

    //netty私有协议的版本号
    private int crcCode=0xabef0101;
    //消息长度
    private int length;
    //回话ID
    private long sessionID;
    //消息类型
    private byte type;
    //消息优先级
    private byte priority;
    //用于拓展消息头
    private Map<String,Object> attachment=new HashMap<String, Object>();

    /**
     * @return the crcCode
     */
    public final int getCrcCode(){
        return crcCode;
    }

    public final Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Header[" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                ']';
    }
}

