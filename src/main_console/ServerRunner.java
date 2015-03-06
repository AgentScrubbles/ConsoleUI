package main_console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerRunner {

	public static void main(String[] args) {
		MainWindow window = new MainWindow();
	}
	
	/**
	 * Serves as the connection from the backend to the frontend
	 * @author Robert
	 *
	 */
	class ServerActionHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			UpdateArrivedEvent event = (UpdateArrivedEvent) e;
			IValues values = event.getValues();
		}
		
	}

}
