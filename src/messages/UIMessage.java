package messages;

import java.util.List;

import components.Component;
import components.IntGenerator;
import main_console.IValues;

public class UIMessage implements IMessage {

	private final Component _sender;
	private final int _id;
	private final int _correlationID;
	private final List<IValues> _values;
	
	public UIMessage(Component sender, int correlationID, List<IValues> values){
		_sender = sender;
		_correlationID = correlationID;
		_values = values;
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
	
	public List<IValues> getValues(){
		return _values;
	}

}
