package messages;

import java.util.List;

import main_console.IValues;
import components.Component;
import components.IntGenerator;

public class ValueMessage implements IMessage{

	final Component _sender;
	final int _correlationID;
	final List<IValues> _values;
	final boolean _append;
	final int _uniqueID;
	
	public ValueMessage(Component sender, int correlationID, List<IValues> values, boolean append){
		_sender = sender;
		_correlationID = correlationID;
		_values = values;
		_append = append;
		_uniqueID = IntGenerator.generateUniqueID();
		
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
	
	public List<IValues> values(){
		return _values;
	}
	
	public boolean doAppend(){
		return _append;
	}

}
