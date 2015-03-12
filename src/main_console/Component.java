package main_console;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Base component type for an actor-style message-passing
 * programming model.
 */
public abstract class Component
{
  /** 
   * Sink for log messages.
   */
  protected Component logger;
  private ConcurrentLinkedQueue<IMessage> inboundQueue;
  
  public Component(){
	  inboundQueue = new ConcurrentLinkedQueue<IMessage>();
  }
  
  protected void setLogger(Component logger){
	  this.logger = logger;
  }
  
  protected void log(String logString)
  {
    logString = this.getClass() + ": " + logString;
    //TODO
  }
  
  /**
   * Adds a message to the current queue, does nothing with notify/wait
   * Thread-safe
   * @param msg
   */
  protected void pushMessage(IMessage msg){
	  inboundQueue.add(msg);
  }
  
  /**
   * Pops the first message off the queue.  Does nothing with notify/wait
   * Thread-safe.
   * @return
   */
  protected IMessage popMessage(){
	  return inboundQueue.poll();
  }
  
  /**
   * Returns if the queue is empty at this time.  Does nothing with notify/wait
   * Thread-Safe
   * @return
   */
  protected boolean queueIsEmpty(){
	  return inboundQueue.isEmpty();
  }
  
  /**
   * Sends a message to this component. The implementation
   * of this method could vary between components but in all
   * case it should accept messages without blocking.
   * @param message
   *   the message to send
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
   * @param msg
   */
  public void handle(IMessage msg)
  {
    log("Unhandled message: " + msg.toString());
  }

  // Overload the handle() method for each concrete message type to
  // call the default handle() implementation.  These methods
  // are overridden by components that expect to handle particular
  // message types.
  
  public void handle(JSONMessage msg)
  {
    handle((IMessage) msg);
  }

  public void handle(UIMessage msg)
  {
    handle((IMessage) msg);
  }
  

}
