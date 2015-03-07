package main_console;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.event.ActionListener;
import java.lang.reflect.Type;

public class SafeParser implements Runnable{
	private String _jsonString;
	private ActionListener _callback;
	private Map<String, IValues> _values;
	
	
	public SafeParser(ActionListener callback){
		_callback = callback;
		_values = new ConcurrentHashMap<String, IValues>();
	}
	
	public synchronized void setString(String jsonString){
		_jsonString = jsonString;
	}
	
	private synchronized void parse(){
		Type type = new TypeToken<Map<String, Object>>(){}.getType();
	    Gson gson = new Gson();
	    Map<String, Map<String,String>> full = gson.fromJson(_jsonString, type);

	    for(String val : full.keySet()){
	    	IValues ivals = mapToValues(val, full.get(val));
	    	_values.put(val, ivals);
	    }
	}

	private IValues mapToValues(String name, Map<String,String> map){
		String current = map.get("current");
		String month = map.get("month");
		String goal = map.get("goal");
		return new ConcreteValues(name, current, month, goal);
	}


	@Override
	public void run() {
		parse(); //TODO
		_callback.actionPerformed(new UpdateArrivedEvent(this, 0, "COMPLETE", _values.values()));
	}
}
