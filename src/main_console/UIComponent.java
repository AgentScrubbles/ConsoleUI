package main_console;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class UIComponent extends Component{

	private ConcurrentLinkedQueue<UIMessage> inboundMessages;
	private AtomicBoolean _stop;
	private MainWindow window;
	
	public UIComponent(Component logger, Component console) {
		super(logger, console);
		inboundMessages = new ConcurrentLinkedQueue<UIMessage>();
		_stop = new AtomicBoolean(false);
		window = new MainWindow(5, 3, 50);
	}

	
	@Override
	public void send(IMessage message) {
		message.dispatch(this);
	}

	@Override
	public void start() {
		new Thread(new Runnable(){

			@Override
			public void run() {
				window.show();
				processMessages();
			}
			
		}).start();
	}

	@Override
	public void stop() {
		_stop.set(true);
	}
	
	private synchronized void processMessages(){
		while(!_stop.get()){
			while(inboundMessages.isEmpty()){
				try {
					wait();
				} catch (InterruptedException e) {
					log(e.getMessage());
					print(e.getMessage());
				}
			}
			UIMessage uiMsg = inboundMessages.poll();
			window.UpdateValues(uiMsg.getValues());
		}
	}
	
	@Override
	public synchronized void handle(UIMessage msg){
		inboundMessages.add(msg);
		notifyAll();
	}

}
