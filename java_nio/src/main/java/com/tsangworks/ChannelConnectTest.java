package com.tsangworks;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class ChannelConnectTest
{
    static byte[] outputByteArray = new byte[1024];
    static byte[] inputByteArray = new byte[1024];

    public static void main(String[] args)
        throws Exception
    {

        int port = 1234;	// default

        String message = "hard to believe";

        ByteBuffer outputBuffer = ByteBuffer.wrap (outputByteArray);
        ByteBuffer inputBuffer = ByteBuffer.wrap (inputByteArray);

        String host = "localhost";

        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(inetSocketAddress);

        while (!socketChannel.finishConnect()) {
            System.out.println("doing something useless");
        }


        outputBuffer.put(message.getBytes());

        outputBuffer.rewind();

        while(outputBuffer.hasRemaining()) {
            socketChannel.write(outputBuffer);
        }


        socketChannel.read(inputBuffer);

        inputBuffer.flip();
        StringBuffer stringBuffer = new StringBuffer();
        while(inputBuffer.hasRemaining()) {
            stringBuffer.append((char) inputBuffer.get());
        }

        System.out.println("inputBuffer: " + stringBuffer.toString());
        socketChannel.close();

    }
}
