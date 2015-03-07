package main_console;

import java.awt.event.ActionListener;

public class MessageHandler implements Runnable {
	private SafeParser _parser;
	private IValues _returnValues;
	private ActionListener _returnHandler;
	
	public MessageHandler(ActionListener returnHandler){
		_returnHandler = returnHandler;
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
