package main_console;

public class ServerRunner {


	
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow(5, 3, 50);
		@SuppressWarnings("unused") //For now, will change later as we will need a pointer back to the listener
		SafeParser parser = new SafeParser(new ServerActionHandler(window));
		SocketListener socketListener = new SocketListener(48182, parser);
	}
	


}
