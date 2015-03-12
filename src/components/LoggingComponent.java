package components;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import messages.IMessage;
import messages.TextMessage;

public class LoggingComponent extends Component{

	private ConcurrentLinkedQueue<TextMessage> inboundMessages;
	private AtomicBoolean _stop;
	
	public LoggingComponent() {
		super(null, null);
		inboundMessages = new ConcurrentLinkedQueue<TextMessage>();
		_stop = new AtomicBoolean(false);
	}

	@Override
	public void send(IMessage message) {
		message.dispatch(this);
		
	}

	private synchronized void processMessages(){
		while(!_stop.get()){
			while(inboundMessages.isEmpty()){
				try {
					wait();
				} catch (InterruptedException ignore) { //Nothing we can do, we are the logger
				}
			}
			TextMessage msg = inboundMessages.poll();
			logMessage(msg.getMessage());
		}
	}
	
	private void logMessage(String msg){
		//TODO
	}
	
	@Override
	public synchronized void handle(TextMessage msg){
		inboundMessages.add(msg);
		notifyAll();
	}
	
	@Override
	public synchronized void handle(IMessage msg){
		inboundMessages.add(new TextMessage(this, CorrelationGenerator.generate(), "Unhandled Message."));
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
	public void stop() {
		_stop.set(true);
	}
	
}
