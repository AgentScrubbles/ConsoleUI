package components;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import messages.IMessage;
import messages.TextMessage;

public class LoggingComponent extends Component{

	private ConcurrentLinkedQueue<TextMessage> inboundMessages;
	private AtomicBoolean _stop;
	private final String _filepath;
	
	public LoggingComponent(String filepath) {
		super(null, null);
		_filepath = filepath;
		inboundMessages = new ConcurrentLinkedQueue<TextMessage>();
		_stop = new AtomicBoolean(false);
	}

	@Override
	public void send(IMessage message) {
		message.dispatch(this);
		
	}

	private synchronized void processMessages(){
		logMessage(generateClassString() + "Started");
		while(!_stop.get()){
			while(inboundMessages.isEmpty()){
				try {
					wait();
					if(_stop.get()){
						logMessage(generateClassString() + "Stopped");
						logMessage(lineBreak());
						return;
					}
				} catch (InterruptedException ignore) { //Nothing we can do, we are the logger
				}
			}
			TextMessage msg = inboundMessages.poll();
			logMessage(msg.getMessage());
		}
	}
	
	private void logMessage(String msg){
		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter(_filepath));
			writer.append(msg + "\n");
			writer.close();
		} catch (IOException ingnore) {
		}
	}
	
	private String lineBreak(){
		String line = "";
		for(int i = 0; i < 20; i++){
			line += "-";
		}
		return line;
	}
	
	@Override
	public synchronized void handle(TextMessage msg){
		inboundMessages.add(msg);
		notifyAll();
	}
	
	@Override
	public synchronized void handle(IMessage msg){
		inboundMessages.add(new TextMessage(this, IntGenerator.generateCorrelation(), "Unhandled Message."));
		notifyAll();
	}
	
	@Override
	public void start() {
		new Thread(new Runnable(){

			@Override
			public void run() {
				processMessages();
			}
			
		}).start();
	}

	@Override
	public synchronized void stop() {
		_stop.set(true);
		notifyAll();
	}
	
}
