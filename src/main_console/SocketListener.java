package main_console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class SocketListener implements Runnable{

	private int port;
	private String location;
	private Socket socket;
	private ActionListener listener;
	
	public SocketListener(int port, String location, ActionListener actOn){
		this.port = port;
		this.location = location;
		this.socket = new Socket();
	}

	@Override
	public void run() {
		while(true){
			final String result = get(); //Blocking
			new Thread(new Runnable(){

				@Override
				public void run() {
					IValues receivedValues = new ValuesReceived(result);
					ActionEvent event = new UpdateArrivedEvent(this, java.awt.event.ActionEvent.ACTION_PERFORMED, "COMPLETE", receivedValues);
					listener.actionPerformed(event);
				}
				
			}).start(); //Let the server get back to what it does best.  Serving.
			
		}
	}
	
	private String get(){
		//TODO
		return null;
	}
	
	/**
	 * May be implemented as a parsing object later for JSON
	 * @author Robert
	 *
	 */
	class ValuesReceived implements IValues{

		private String result;
		public ValuesReceived(String result){
			this.result = result;
		}
		@Override
		public String getRank() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
