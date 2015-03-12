package messages;

import components.Component;
import components.IntGenerator;

public class LoadMessage implements IMessage {

	private final Component _sender;
	private final int _correlationID;
	private final int _id;
	
	public LoadMessage(Component sender, int correlationID){
		_sender = sender;
		_correlationID = correlationID;
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

}
