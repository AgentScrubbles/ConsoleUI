package messages;

import components.Component;
import components.IntGenerator;

public class LogMessage implements IMessage {

	private final Component _sender;
	private final int _correlationID;
	private final int _id;
	private final String _logString;
	
	public LogMessage(Component sender, int correlationID, String logString){
		_sender = sender;
		_correlationID = correlationID;
		_logString = logString;
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
	
	public String getLogString(){
		return _logString;
	}

}
