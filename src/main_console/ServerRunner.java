package main_console;

public class ServerRunner {


	
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		SocketListener socketListener = new SocketListener(48182, new ServerActionHandler(window));
	}
	


}
