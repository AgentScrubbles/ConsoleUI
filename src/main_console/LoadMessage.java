package main_console;

public class LoadMessage implements IMessage {

	private Component _sender;
	private int _correlationID;
	
	public LoadMessage(Component sender, int id){
		_sender = sender;
		_correlationID = id;
	}
	
	@Override
	public Component getSender() {
		return _sender;
	}

	@Override
	public int getId() {
		return _correlationID;
	}

	@Override
	public int getCorrelationId() {
		return _correlationID;
	}

	@Override
	public void dispatch(Component receiver) {
		receiver.handle(this);
	}

}
