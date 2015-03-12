package main_console;

import java.util.List;

public class UIMessage implements IMessage {

	private Component _sender;
	private int _id;
	private List<IValues> _values;
	
	public UIMessage(Component sender, int id, List<IValues> values){
		_sender = sender;
		_id = id;
		_values = values;
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
	
	public List<IValues> getValues(){
		return _values;
	}

}
