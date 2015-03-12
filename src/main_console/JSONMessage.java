package main_console;

public class JSONMessage implements IMessage {

	private Component _sender;
	private int _id;
	private String _json;
	
	public JSONMessage(Component sender, int id, String json){
		_sender = sender;
		_id = id;
		_json = json;
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
	
	public String getJSON(){
		return _json;
	}

}
