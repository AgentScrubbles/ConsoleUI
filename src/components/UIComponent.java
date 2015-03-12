package components;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import messages.ErrorMessage;
import messages.IMessage;
import messages.LoadMessage;
import messages.UIMessage;
import console_ui.MainWindow;

public class UIComponent extends Component{

	private ConcurrentLinkedQueue<UIMessage> inboundMessages;
	private Component _loadComponent;
	private AtomicBoolean _stop;
	private MainWindow window;
	
	public UIComponent(Component logger, Component console) {
		super(logger, console);
		inboundMessages = new ConcurrentLinkedQueue<UIMessage>();
		_stop = new AtomicBoolean(false);
		window = new MainWindow(5, 3, 50);
	}

	public void setLoader(Component c){
		_loadComponent = c;
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

	/**
	 * Requests a message from the loadComponent to load the latest message.
	 * Thread-safe
	 * Must have LoadComponent set
	 */
	public void loadSavedValues(){
		IMessage msg = new LoadMessage(this, CorrelationGenerator.generate());
		if(_loadComponent != null){
			_loadComponent.send(msg);
		}
	}
	
	@Override
	public void stop() {
		_stop.set(true);
	}
	
	private synchronized void processMessages(){
		print("Started");
		loadSavedValues();
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
		print("Stopped");
	}
	
	@Override
	public synchronized void handle(UIMessage msg){
		inboundMessages.add(msg);
		notifyAll();
	}
	
	@Override
	public synchronized void handle(ErrorMessage msg){
		print(msg.getError());
	}

}
