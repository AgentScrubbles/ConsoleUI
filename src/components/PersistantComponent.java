package components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import messages.ErrorMessage;
import messages.IMessage;
import messages.JSONMessage;
import messages.LoadMessage;

public class PersistantComponent extends Component {

	private String _filepath;
	private AtomicBoolean _stop;
	private ConcurrentLinkedQueue<JSONMessage> inboundJSONMessages;
	private ConcurrentLinkedQueue<LoadMessage> inboundLoadMessages;
	private Component _sendLoadedMessageComponent;

	public PersistantComponent(Component logger, Component console,
			Component sendLoadedMessageComponent, String filepath) {
		super(logger, console);
		_filepath = filepath;
		_stop = new AtomicBoolean(false);
		inboundJSONMessages = new ConcurrentLinkedQueue<JSONMessage>();
		inboundLoadMessages = new ConcurrentLinkedQueue<LoadMessage>();
		_sendLoadedMessageComponent = sendLoadedMessageComponent;
	}

	@Override
	public void send(IMessage message) {
		message.dispatch(this);
	}

	@Override
	public void start() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				print("Started");
				processMessages();
			}

		}).start();
	}

	private synchronized void processMessages() {
		while (!_stop.get()) {
			while (inboundJSONMessages.isEmpty()
					&& inboundLoadMessages.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					print(e.getMessage() + "\n" + e.getStackTrace());
					log(e.getMessage());
				}
			}
			// Check JSONMessages first, they are higher priority
			if (!inboundJSONMessages.isEmpty()) {
				saveLastJSON();
			}
			if (!inboundLoadMessages.isEmpty()) {
				LoadMessage msg = inboundLoadMessages.poll();
				String loaded = load();
				if (loaded == null) {
					IMessage retMessage = new ErrorMessage(this, msg.getCorrelationId(),
							"Error loading messages");
					msg.getSender().send(retMessage);
				} else {
					IMessage retMessage = new JSONMessage(this, msg.getCorrelationId(),
							loaded);
					_sendLoadedMessageComponent.send(retMessage);
				}
			}
		}
		print("Stopped");
	}

	/**
	 * Saves the last JSON message, discards any before the last one
	 */
	private void saveLastJSON() {
		while (inboundJSONMessages.size() > 1) {
			inboundJSONMessages.poll();
		}
		JSONMessage mostRecent = inboundJSONMessages.poll();
		save(mostRecent.getJSON());
	}

	private void save(String json) {

		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(_filepath));
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			print("IOException: " + e.getStackTrace());
			log("IOException: " + e.getStackTrace());
		}
	}

	private String load() {
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(_filepath));
			String complete = "";
			String line = reader.readLine();
			while (line != null) {
				complete += line;
				line = reader.readLine();
			}
			reader.close();
			return complete;

		} catch (FileNotFoundException e) {
			print("FileNotFound: " + e.getStackTrace());
			log("FileNotFound: " + e.getStackTrace());
		} catch (IOException e) {
			print("IOException: " + e.getStackTrace());
			log("IOException: " + e.getStackTrace());
		}
		return null;
	}

	@Override
	public void stop() {
		_stop.set(true);
	}

	@Override
	public synchronized void handle(JSONMessage msg) {
		inboundJSONMessages.add(msg);
		notifyAll();
	}

	@Override
	public synchronized void handle(LoadMessage msg) {
		inboundLoadMessages.add(msg);
		notifyAll();
	}

}
