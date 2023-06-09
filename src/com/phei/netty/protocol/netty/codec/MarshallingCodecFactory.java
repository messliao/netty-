package com.phei.netty.protocol.netty.codec;


import org.jboss.marshalling.*;

import java.io.IOException;

public final class MarshallingCodecFactory {
    /**
     *  创建Jboss Marshaller
     * @return
     * @throws IOException
     */
    protected static Marshaller buildMarshalling() throws IOException {
        final MarshallerFactory marshallerFactory= Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
         configuration.setVersion(5);
         Marshaller marshaller=marshallerFactory.createMarshaller(configuration);
         return marshaller;
    }

    /**
     * 创建Jboss Unmarshaller
     */
    protected static Unmarshaller buildUnMarshalling() throws IOException {
        final MarshallerFactory marshallerFactory=Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration=new MarshallingConfiguration();
        configuration.setVersion(5);
        final Unmarshaller unmarshaller=marshallerFactory.createUnmarshaller(configuration);
        return  unmarshaller;
    }
}
