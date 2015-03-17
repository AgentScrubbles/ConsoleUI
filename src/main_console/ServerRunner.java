package main_console;

import java.util.Scanner;

import components.Component;
import components.ConsoleOutputComponent;
import components.LoggingComponent;
import components.NextBusComponent;
import components.ParserComponent;
import components.PersistantComponent;
import components.SocketListenerComponent;
import components.StagingComponent;
import components.UIComponent;

public class ServerRunner {

	private static final String SAVE_FILE_PATH = "savedinfo.txt";
	private static final String LOG_FILE_PATH = "logfile.txt";
	private static final String NEXTBUS_URL = "http://webservices.nextbus.com/service/publicXMLFeed";
	private static final String NEXTBUS_AGENCY = "cyride";
	private static final String NEXTBUS_STOPID = "1045";
	private static final int NEXTBUS_REFRESHTIME = 5000;
	private static final int LISTEN_PORT = 48182;
	
	
	public static void main(String[] args) throws InterruptedException {
		int numBoxesAcross = 0;
		int numBoxesDown = 0;
		
		if(args.length > 0){
			try{
				numBoxesAcross = Integer.parseInt(args[0]);
				numBoxesDown = Integer.parseInt(args[1]);
			} catch (Exception ex){
				System.out.println("Please run as:");
				System.out.println("\t$ ServerRunner numBoxesAcross numBoxesDown");
			}
		} else {
			numBoxesAcross = 4;
			numBoxesDown = 3;
		}
		
		Component console = new ConsoleOutputComponent();
		Component logger = new LoggingComponent(LOG_FILE_PATH);
		UIComponent uiComponent = new UIComponent(logger, console, numBoxesAcross, numBoxesDown);
		Component stagingComponent = new StagingComponent(logger, console, uiComponent);
		Component busComponent = new NextBusComponent(logger, console, stagingComponent, NEXTBUS_URL, NEXTBUS_AGENCY, NEXTBUS_STOPID, NEXTBUS_REFRESHTIME);
		Component parserComponent = new ParserComponent(logger, console, stagingComponent);
		Component persistantComponent = new PersistantComponent(logger, console, parserComponent, SAVE_FILE_PATH);
		Component serverListener = new SocketListenerComponent(logger, console, parserComponent, persistantComponent, LISTEN_PORT);
		uiComponent.setLoader(persistantComponent);
		
		console.start();
		logger.start();
		uiComponent.start();
		parserComponent.start();
		serverListener.start();
		persistantComponent.start();
		stagingComponent.start();
		busComponent.start();
		
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
		System.out.println("Complete.");
	}
	


}
