package messages;

import components.Component;
import components.IntGenerator;

public class TextMessage implements IMessage {

	private final String _message;
	private final Component _sender;
	private final int _id;
	private final int _correlationID;
	
	public TextMessage(Component sender, int correlationID, String msg){
		_message = msg;
		_correlationID = correlationID;
		_sender = sender;
		_id = IntGenerator.generateUniqueID();
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
		return _correlationID;
	}

	@Override
	public void dispatch(Component receiver) {
		receiver.handle(this);
	}
	
	public String getMessage(){
		return _message;
	}

}
