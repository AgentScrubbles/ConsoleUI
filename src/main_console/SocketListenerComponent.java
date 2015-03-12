package main_console;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketListenerComponent extends Component {

	private int _port;
	Component _sendComponent;
	private AtomicBoolean _stop;

	public SocketListenerComponent(Component logger, Component console, Component jsonComponent, int port) {
		super(logger, console);
		_sendComponent = jsonComponent;
		_stop = new AtomicBoolean(false);
		_port = port;
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

			// while (scanner.hasNextLine()) {

			InputStream stream = s.getInputStream();
			byte[] b = new byte[stream.available()];
			stream.read(b);
			String file = new String(b);

			print("Received from client:\n" + file);
			IMessage jsonMessage = new JSONMessage(this,
					CorrelationGenerator.generate(), file);
			_sendComponent.send(jsonMessage);

		} finally {
			// close the connection in a finally block
			s.close();

		}
	}

	private void startConnection() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(_port);
			while (!_stop.get()) {
				print("Server listening on " + _port);

				// blocks here until a client attempts to connect
				final Socket s = ss.accept();

				// create a task for handling the connection, and
				// pass to the executor
				Runnable task = new Runnable() {
					public void run() {
						try {
							handleConnection(s);
						} catch (IOException e) {
							SocketListenerComponent.this.print(e.getMessage());
							SocketListenerComponent.this.log(e.getMessage());
						}
					}
				};
				new Thread(task).start();

			}
		} catch (IOException e) {
			print(e.getMessage());
			log(e.getMessage());
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					print(e.getMessage());
					log(e.getMessage());
				}
			}
		}
		print("Server stopped.");
	}

	@Override
	public void send(IMessage message) {
		message.dispatch(this);
	}

	@Override
	public void start() {
		print("Server started.");
		new Thread(new Runnable() {

			@Override
			public void run() {
				startConnection();
			}

		});
	}

	@Override
	public void stop() {
		_stop.set(true);
	}

}
