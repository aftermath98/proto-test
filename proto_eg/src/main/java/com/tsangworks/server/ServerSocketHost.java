package com.tsangworks.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class ServerSocketHost
    extends AbstractServerHost
{
    private static final Logger logger = LogManager.getLogger(ServerSocketHost.class);

    protected PrintWriter out = null;
    protected InputStream inputStream = null;

    public static void main(String[] args)
    {
        ServerSocketHost serverSocketHost = new ServerSocketHost();
        serverSocketHost.runHost();
    }

    @Override
    protected void runHost()
    {
        final int port = 2000;
        final int timeout = 60000;
        final String RESPONSE = "EFX EURDEALAV00001666100000000002                                                   ";

        final char[] messageHeaderBuffer = new char[23];

        AtomicInteger numProcessed = new AtomicInteger();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeout);
            Socket socket = serverSocket.accept();

            socket.setTcpNoDelay(true);
            socket.setSoTimeout(timeout);
            socket.setKeepAlive(true);

            out = new PrintWriter(socket.getOutputStream(), true);
            inputStream = socket.getInputStream();
            while (runningService) {
                try {
                    String header = readReuseCharBufferHeader (messageHeaderBuffer);
                    if (messageHeader.getRecordSize() != 0) {
                        String body = readBody();

                        out.write(RESPONSE);
                        out.flush();

                        messageHeader.setRecordSize(0);
                        numProcessed.getAndIncrement();

                    }
                }
                catch (Exception e) {
                    logger.error(e);
                }
            }

        }
        catch (IOException e)
        {
            logger.error(e);
        }
        finally
        {

        }

    }

    private String readBody()
    {
        startBody = System.nanoTime();
        int bytesRead = 0;

        char[] messageBodyBuffer = new char[messageHeader.getRecordSize()];
        try {
            bytesRead = readFromInputStream(messageBodyBuffer);
            messageBodyString = String.valueOf(messageBodyBuffer);
        }
        catch (SocketTimeoutException e) {
            logger.error(e);
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        endBody = System.nanoTime();
        return messageBodyString;
    }

    protected String readReuseCharBufferHeader(char[] messageHeaderBuffer) {
        startHeader = System.nanoTime();
        int bytesRead = 0;
        try {
            bytesRead = readFromInputStream (messageHeaderBuffer);
            if (bytesRead > 0) {
                messageHeaderString = String.valueOf(messageHeaderBuffer);
                messageHeader.init(messageHeaderString);
            }
        }
        catch (SocketTimeoutException e) {
            logger.error(e);
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        endHeader = System.nanoTime();
        return messageHeaderString;
    }

    protected int readFromInputStream(char[] buffer)
        throws IOException {
        int c;
        for (int i = 0; i < buffer.length; i++) {
            c = inputStream.read();
            if (c == -1) {
                return i;
            }
            else {
                buffer[i] = (char) c;
            }
        }
        return buffer.length;
    }
}
