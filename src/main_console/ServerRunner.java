package main_console;

public class ServerRunner {


	
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow(5, 3, 50);
		SafeParser parser = new SafeParser(new ServerActionHandler(window));
		@SuppressWarnings("unused")
		SocketListener socketListener = new SocketListener(48182, parser);
	}
	


}
