package main_console;

public class TextMessage implements IMessage {

	private String _message;
	private Component _sender;
	private int _id;
	
	public TextMessage(Component sender, int id, String msg){
		_message = msg;
		_id = id;
		_sender = sender;
	}
	
	@Override
	public Component getSender() {
		return _sender;
	}

	@Override
	public int getId() {
		return _id;
	}

	@Override
	public int getCorrelationId() {
		return _id;
	}

	@Override
	public void dispatch(Component receiver) {
		receiver.handle(this);
	}
	
	public String getMessage(){
		return _message;
	}

}
