package main_console;

public class ServerRunner {


	
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		@SuppressWarnings("unused") //For now, will change later as we will need a pointer back to the listener
		SocketListener socketListener = new SocketListener(48182, new ServerActionHandler(window));
	}
	


}
