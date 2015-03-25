package components;

import java.util.Date;

import messages.AlertMessage;
import messages.ErrorMessage;
import messages.IMessage;
import messages.JSONMessage;
import messages.LoadMessage;
import messages.TextMessage;
import messages.UIMessage;

/**
 * Base component type for an actor-style message-passing programming model.
 */
public abstract class Component {
	/**
	 * Sink for log messages.
	 */
	protected Component logger;
	protected Component console;

	public Component(Component logger, Component console) {
		this.logger = logger;
		this.console = console;
	}


	protected void log(String logString) {
		logString = generateClassString() + logString;
		logger.send(new TextMessage(this, IntGenerator.generateCorrelation(),
				logString + "\n"));
	}

	protected void print(String printString) {
		printString = generateClassString() + printString;
		console.send(new TextMessage(this, IntGenerator.generateCorrelation(),
				printString));
	}

	protected String generateClassString(){
		return "[ " + new Date().toString() + " ] [ " + Thread.currentThread().getId() + " ]  [ " + getFriendlyName() + " ] ";
	}
	
	protected String getFriendlyName(){
		return this.getClass().getSimpleName();
	}
	

	/**
	 * Sends a message to this component. The implementation of this method
	 * could vary between components but in all case it should accept messages
	 * without blocking.
	 * 
	 * @param message
	 *            the message to send
	 */
	public abstract void send(IMessage message);

	/**
	 * Signals to this component that it may begin processing messages.
	 */
	public abstract void start();

	/**
	 * Signals to this component that it should stop.
	 */
	public abstract void stop();

	/**
	 * Default message handling method.
	 * 
	 * @param msg
	 */
	public void handle(IMessage msg) {
		log("Unhandled message: " + msg.toString());
	}

	// Overload the handle() method for each concrete message type to
	// call the default handle() implementation. These methods
	// are overridden by components that expect to handle particular
	// message types.

	public void handle(JSONMessage msg) {
		handle((IMessage) msg);
	}

	public void handle(UIMessage msg) {
		handle((IMessage) msg);
	}
	
	public void handle(TextMessage msg){
		handle((IMessage) msg);
	}
	
	public void handle(LoadMessage msg){
		handle((IMessage) msg);
	}


	public void handle(ErrorMessage msg) {
		handle((IMessage) msg);
	}
	
	public void handle(AlertMessage msg){
		handle((IMessage) msg);
	}

}
