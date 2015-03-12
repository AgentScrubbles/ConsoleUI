package main_console;

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
		logString = this.getClass() + ": " + logString;
		logger.send(new TextMessage(this, CorrelationGenerator.generate(),
				logString));
	}

	protected void print(String printString) {
		printString = this.getClass() + ": " + printString;
		console.send(new TextMessage(this, CorrelationGenerator.generate(),
				printString));
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

}
