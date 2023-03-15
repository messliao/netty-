package com.phei.netty.protocol.netty.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * {@link ByteOutput} implementation which writes the data to a {@link ByteBuf}
 *
 *
 */
public class ChannelBufferByteOutput implements ByteOutput{

    private final ByteBuf buffer;

    /**
     * Create a new instance which use the given {@link ByteBuf}
     */
    public ChannelBufferByteOutput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    /**
     * Writes to the output stream the eight low-order bits of the argument {@code b}. The 24 high-order bits of
     * {@code b} are ignored.
     *
     * @param b the byte to write
     * @throws IOException if an error occurs
     */
    @Override
    public void write(int b) throws IOException {
        buffer.writeByte(b);
    }

    /**
     * Write all the bytes from the given array to the stream.
     *
     * @param b the byte array
     * @throws IOException if an error occurs
     */
    @Override
    public void write(byte[] bytes) throws IOException {
        buffer.writeBytes(bytes);
    }

    /**
     * Write some of the bytes from the given array to the stream.
     *
     * @param bytes   the byte array
     * @param off the index to start writing from
     * @param len the number of bytes to write
     * @throws IOException if an error occurs
     */
    @Override
    public void write(byte[] bytes, int off, int len) throws IOException {
        buffer.writeBytes(bytes,off,len);
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
         //do nothing
    }

    /**
     * Flushes this stream by writing any buffered output to the underlying
     * stream.
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void flush() throws IOException {
        //do nothing
    }
}
