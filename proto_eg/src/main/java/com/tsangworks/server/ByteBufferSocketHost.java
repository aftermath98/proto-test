package com.tsangworks.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class ByteBufferSocketHost
    extends AbstractServerHost
{

    private static final Logger logger = LogManager.getLogger(ByteBufferSocketHost.class);

    @Override
    protected void runHost()
    {

        final int port = 2000;
        final int timeout = 60000;
        final String RESPONSE = "EFX EURDEALAV00001666100000000002                                                   ";

        final byte[] inputByteArray = new byte[1024];

        final byte[] outputByteArray = new byte[2048];

        AtomicInteger numProcessed = new AtomicInteger();

        ByteBuffer inputByteBuffer = ByteBuffer.wrap(inputByteArray);
        ByteBuffer outputByteBuffer = ByteBuffer.wrap(outputByteArray);

        final boolean blocking = true;

        ServerSocketChannel serverSocketChannel = null;

        try{
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(blocking);

            while (runningService) {

                SocketChannel socketChannel = serverSocketChannel.accept();
                int bytesRead = socketChannel.read(inputByteBuffer);
                while (bytesRead != -1) {
                    startHeader = System.nanoTime();

                    StringBuilder stringBuilder = new StringBuilder();
                    inputByteBuffer.flip(); // make buffer ready for read
                    int bytesProcessed = 0;
                    while (inputByteBuffer.hasRemaining()) {
                        stringBuilder.append((char) inputByteBuffer.get()); // read 1 bytes at a time
                        bytesProcessed++;
                        if (bytesProcessed == 23) {
                            messageHeaderString = stringBuilder.toString();
                            messageHeader.init(messageHeaderString);
                            endHeader = System.nanoTime();
                            startBody = System.nanoTime();
                        }
                    }
                    endBody = System.nanoTime();
                    messageBodyString = stringBuilder.substring(23);

                    inputByteBuffer.clear();

                    outputByteBuffer.clear();

                    outputByteBuffer.put(RESPONSE.getBytes());

                    outputByteBuffer.flip();

                    while(outputByteBuffer.hasRemaining()) {
                        socketChannel.write(outputByteBuffer);
                    }

                    numProcessed.getAndIncrement();

                    bytesRead = socketChannel.read(inputByteBuffer);

                }

                startHeader = 0;
                endHeader = 0;
                startBody = 0;
                endBody = 0;
            }
        }
        catch (IOException e)
        {
            logger.error(e);
        }

    }
}
