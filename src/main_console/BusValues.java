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
		return _first;
	}

	@Override
	public String secondItem() {
		return Integer.toString(_timeOne);
	}

	@Override
	public String thirdItem() {
		return Integer.toString(_timeTwo);
	}

	@Override
	public boolean boolVal() {
		return _timeOne > 5;
	}

}
