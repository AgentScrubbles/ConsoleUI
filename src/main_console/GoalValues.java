package main_console;

/**
 * Implemented as a immutable class
 * @author Robert
 *
 */
public class GoalValues implements IValues {

	private final String _name;
	private final String _current;
	private final String _month;
	private final String _goal;
	private final boolean _goalMet;
	
	public GoalValues(String name, String current, String month, String goal, boolean goalMet){
		_name = name;
		_current = current;
		_month = month;
		_goal = goal;
		_goalMet = goalMet;
	}
	
	@Override
	public String name() {
		return _name;
	}

	@Override
	public String firstItem() {
		return _current;
	}

	@Override
	public String secondItem() {
		return _month;
	}

	@Override
	public String thirdItem() {
		return _goal;
	}

	@Override
	public boolean boolVal() {
		return _goalMet;
	}

}
