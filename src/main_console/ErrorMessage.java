package main_console;

public class ErrorMessage implements IMessage{

	private Component _sender;
	private int _correlationID;
	private String _errorText;
	
	public ErrorMessage(Component sender, int correlationID, String errorText){
		_sender = sender;
		_correlationID = correlationID;
		_errorText = errorText;
	}
	
	@Override
	public Component getSender() {
		return _sender;
	}

	@Override
	public int getId() {
		return _correlationID;
	}

	@Override
	public int getCorrelationId() {
		return _correlationID;
	}

	@Override
	public void dispatch(Component receiver) {
		receiver.handle(this);
	}
	
	public String getError(){
		return _errorText;
	}

}
