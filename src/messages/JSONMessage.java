package messages;

import components.Component;
import components.IntGenerator;

public class JSONMessage implements IMessage {

	private final Component _sender;
	private final int _id;
	private final int _correlation;
	private final String _json;
	
	public JSONMessage(Component sender, int correlationID, String json){
		_sender = sender;
		_id = IntGenerator.generateUniqueID();
		_json = json;
		_correlation = correlationID;
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
		return _correlation;
	}

	@Override
	public void dispatch(Component receiver) {
		receiver.handle(this);
	}
	
	public String getJSON(){
		return _json;
	}

}
