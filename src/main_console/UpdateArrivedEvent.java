package main_console;

/**
 * Custom ArrivedEvent that will store a pointer to the IValues that are received by the
 * socket listener
 * @author Robert
 *
 */
public class UpdateArrivedEvent extends java.awt.event.ActionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -419058479375682804L;
	private final IValues values;
	
	public UpdateArrivedEvent(Object arg0, int arg1, String arg2, IValues values) {
		super(arg0, arg1, arg2);
		this.values = values;
	}
	
	public IValues getValues(){
		return values;
	}

}
