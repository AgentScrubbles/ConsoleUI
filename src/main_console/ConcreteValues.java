package main_console;

/**
 * Implemented as a immutable class
 * @author Robert
 *
 */
public class ConcreteValues implements IValues {

	private final String _name;
	private final String _current;
	private final String _month;
	private final String _goal;
	
	public ConcreteValues(String name, String current, String month, String goal){
		_name = name;
		_current = current;
		_month = month;
		_goal = goal;
	}
	
	@Override
	public String name() {
		return _name;
	}

	@Override
	public String current() {
		return _current;
	}

	@Override
	public String month() {
		return _month;
	}

	@Override
	public String goal() {
		return _goal;
	}

	@Override
	public boolean metGoal() {
		// TODO Auto-generated method stub
		return false;
	}

}
