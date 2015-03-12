package main_console;

public class ServerRunner {


	
	
	public static void main(String[] args) {		
		Component console = new ConsoleOutputComponent();
		Component logger = new LoggingComponent();
		Component uiComponent = new UIComponent(logger, console);
		Component parserComponent = new ParserComponent(logger, console, uiComponent);
		Component serverListener = new SocketListenerComponent(logger, console, parserComponent, 48182);
		
		console.start();
		logger.start();
		uiComponent.start();
		parserComponent.start();
		serverListener.start();
	}
	


}
