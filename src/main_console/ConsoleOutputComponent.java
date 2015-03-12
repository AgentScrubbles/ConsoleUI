package main_console;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsoleOutputComponent extends Component{

	private ConcurrentLinkedQueue<TextMessage> inboundMessages;
	private AtomicBoolean _stop;
	
	public ConsoleOutputComponent() {
		super(null, null);
		_stop = new AtomicBoolean(false);
		inboundMessages = new ConcurrentLinkedQueue<TextMessage>();
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
				} catch (InterruptedException ignore) { //Nothing we can do, we are in the console.
				}
			}
			TextMessage msg = inboundMessages.poll();
			System.out.println(msg.getMessage());
		}
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
	
	@Override
	public synchronized void handle(IMessage msg){
		inboundMessages.add(new TextMessage(this, CorrelationGenerator.generate(), "Unhandled message."));
		notifyAll();
	}
	
	@Override
	public synchronized void handle(TextMessage msg){
		inboundMessages.add(msg);
		notifyAll();
	}

}
