package main_console;

public class ServerRunner {

	private static final String SAVE_FILE_PATH = "savedinfo.txt";
	private static final int LISTEN_PORT = 48182;
	
	
	public static void main(String[] args) {		
		Component console = new ConsoleOutputComponent();
		Component logger = new LoggingComponent();
		UIComponent uiComponent = new UIComponent(logger, console);
		Component parserComponent = new ParserComponent(logger, console, uiComponent);
		Component persistantComponent = new PersistantComponent(logger, console, parserComponent, SAVE_FILE_PATH);
		Component serverListener = new SocketListenerComponent(logger, console, parserComponent, persistantComponent, LISTEN_PORT);
		uiComponent.setLoader(persistantComponent);
		
		console.start();
		logger.start();
		uiComponent.start();
		parserComponent.start();
		serverListener.start();
		persistantComponent.start();
	}
	


}
