package messages;

import components.Component;
import components.IntGenerator;

public class AlertMessage implements IMessage {

	private final String _alertMsg;
	private final Component _sender;
	private final int _correlationID;
	private final int _uniqueID;
	
	public AlertMessage(Component sender, int correlationID, String msg){
		_sender = sender;
		_alertMsg = msg;
		_correlationID = correlationID;
		_uniqueID = IntGenerator.generateUniqueID();
	}
	
	public String getMsg(){
		return _alertMsg;
	}
	
	@Override
	public Component getSender() {
		return _sender;
	}

	@Override
	public int getId() {
		return _uniqueID;
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
