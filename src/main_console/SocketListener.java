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
	private ActionListener listener;

	public SocketListener(int port, ActionListener actOn) {
		this.port = port;
		this.listener = actOn;
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
			Scanner scanner = new Scanner(s.getInputStream());
			
			//while (scanner.hasNextLine()) {
				
				InputStream stream = s.getInputStream();
				byte[] b = new byte[stream.available()];
				stream.read(b);
				String file = new String(b);
				
				final String line = file;
				
				System.out.println("Received from client:");
				System.out.println(line);
				new Thread(new Runnable() {

					@Override
					public void run() {
						IValues receivedValues = new ValuesReceived(line);
						ActionEvent event = new UpdateArrivedEvent(this,
								java.awt.event.ActionEvent.ACTION_PERFORMED,
								"COMPLETE", receivedValues);
						listener.actionPerformed(event);
					}

				}).start();

			
			
			scanner.close();

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

	/**
	 * May be implemented as a parsing object later for JSON
	 * 
	 * @author Robert
	 *
	 */
	class ValuesReceived implements IValues {

		private String result;

		public ValuesReceived(String result) {
			this.result = result;
		}

		@Override
		public String getRank() {
			return result;
		}

	}
}
