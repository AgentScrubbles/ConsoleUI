package main_console;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ServerActionHandler implements ActionListener{

	MainWindow _window;
	
	public ServerActionHandler(MainWindow window){
		_window = window;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		UpdateArrivedEvent event = (UpdateArrivedEvent) e;
		Collection<IValues> values = event.getValues();
		_window.UpdateValues(values);
	}
	
}
