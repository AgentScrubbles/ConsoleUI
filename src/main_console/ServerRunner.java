package main_console;

import java.util.Scanner;

import components.Component;
import components.ConsoleOutputComponent;
import components.LoggingComponent;
import components.ParserComponent;
import components.PersistantComponent;
import components.SocketListenerComponent;
import components.UIComponent;

public class ServerRunner {

	private static final String SAVE_FILE_PATH = "savedinfo.txt";
	private static final int LISTEN_PORT = 48182;
	
	
	public static void main(String[] args) throws InterruptedException {		
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
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter 'q' to quit.");
		while(keyboard.next().charAt(0) != 'q'){
			System.out.println(ServerRunner.class + "Unknown command");
		}
		keyboard.close();
		
		uiComponent.stop();
		parserComponent.stop();
		serverListener.stop();
		persistantComponent.stop();
		
		Thread.sleep(3000);

		console.stop();
		logger.stop();
	}
	


}
