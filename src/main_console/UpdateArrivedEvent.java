package main_console;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	private final Collection<IValues> values;
	
	public UpdateArrivedEvent(Object arg0, int arg1, String arg2, Collection<IValues> values) {
		super(arg0, arg1, arg2);
		this.values = values;
	}
	
	public Collection<IValues> getValues(){
		return values;
	}

}
