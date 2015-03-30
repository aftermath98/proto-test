
package com.ronsoft.books.nio.channels;

import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;

/**
 * Test non-blocking accept() using ServerSocketChannel.
 * Create and bind a ServerSocketChannel, then place the
 * channel in non-blocking mode.  Loop infinitely, sleeping
 * two seconds between checks for incoming connections.
 * Rather than sleeping, the thread could be doing something
 * useful.  When a connection comes in, send a greeting down
 * the channel then close it.
 * Start this program, then "telnet localhost 1234" to connect
 * to it.
 *
 * Created April 2002
 * @author Ron Hitchens (ron@ronsoft.com)
 * @version $Id: ChannelAccept.java,v 1.1 2002/04/28 01:47:58 ron Exp $
 */
public class ChannelAccept
{
	public static final String GREETING = "Hello I must be going.\r\n";
	static byte[] inputByteArray = new byte[1024];

	public static void main (String [] argv)
		throws Exception
	{
		int port = 1234;	// default

		if (argv.length > 0) {
			port = Integer.parseInt (argv [0]);
		}

		ByteBuffer outputByteBuffer = ByteBuffer.wrap (GREETING.getBytes());
		ByteBuffer inputByteBuffer = ByteBuffer.wrap (inputByteArray);
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		serverSocketChannel.socket().bind (new InetSocketAddress (port));
		serverSocketChannel.configureBlocking(false);

		while (true) {
			System.out.println("Waiting for connections");

			SocketChannel socketChannel = serverSocketChannel.accept();

			if (socketChannel == null) {
				// no connections, snooze a while
				Thread.sleep (2000);
			} else {
				System.out.println("Incoming connection from: "
										   + socketChannel.socket().getRemoteSocketAddress());

				socketChannel.read(inputByteBuffer);
				inputByteBuffer.flip();
				StringBuffer stringBuffer = new StringBuffer();
				while(inputByteBuffer.hasRemaining()) {
					stringBuffer.append((char) inputByteBuffer.get());
				}
				System.out.println("server: " + stringBuffer.toString());

				outputByteBuffer.rewind();
				socketChannel.write(outputByteBuffer);
				socketChannel.close();
			}
		}
	}
}
