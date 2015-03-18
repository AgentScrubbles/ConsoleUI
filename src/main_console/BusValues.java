package main_console;

public class BusValues implements IValues {

	final String _name;
	final String _first;
	final int _timeOne;
	final int _timeTwo;
	
	public BusValues(String agency, String route, int timeOne, int timeTwo){
		_name = agency;
		_first = route;
		_timeOne = timeOne;
		_timeTwo = timeTwo;
	}
	
	@Override
	public String name() {
		return _name;
	}

	@Override
	public String firstItem() {
		return "Stop: " + _first;
	}

	@Override
	public String secondItem() {
		return "Arriving in " + Integer.toString(_timeOne) + " minutes.";
	}

	@Override
	public String thirdItem() {
		return "Arriving in " + Integer.toString(_timeTwo) + " minutes.";
	}

	@Override
	public boolean boolVal() {
		return _timeOne > 5;
	}

}
