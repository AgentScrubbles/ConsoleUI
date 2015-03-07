package main_console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SocketListener implements Runnable {

	private int port;
	private final SafeParser listener;

	public SocketListener(int port, SafeParser callback) {
		this.port = port;
		this.listener = callback;
		new Thread(this).start();
	}

	@Override
	public void run() {
		startConnection();
	}

	/**
	 * Helper method for handling a client connection. Closes the socket (and
	 * therefore the associated streams) when the method returns.
	 * 
	 * @param s
	 *            Socket representing the client connection
	 * @throws IOException
	 */
	private void handleConnection(Socket s) throws IOException {
		try {
			// We expect line-oriented text input, so wrap the input stream
			// in a Scanner
			
			//while (scanner.hasNextLine()) {
				
				InputStream stream = s.getInputStream();
				byte[] b = new byte[stream.available()];
				stream.read(b);
				String file = new String(b);
				
				
				System.out.println("Received from client:");
				System.out.println(file);
				listener.setString(file);
				new Thread(listener).start();

			
			
		} finally {
			// close the connection in a finally block
			s.close();
			
		}
	}

	private void startConnection() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			while (true) {
				System.out.println("Server listening on " + port);

				// blocks here until a client attempts to connect
				final Socket s = ss.accept();

				// create a task for handling the connection, and
				// pass to the executor
				Runnable task = new Runnable() {
					public void run() {
						try {
							handleConnection(s);
						} catch (IOException e) {
							System.out.println(e);
						}
					}
				};
				new Thread(task).start();

			}
		} catch (IOException e) {
			System.out.println("I/O error: " + e);
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					// error trying to close the socket, not much we can do
					System.out.println("Error closing socket " + e);
				}
			}
		}
	}


}
