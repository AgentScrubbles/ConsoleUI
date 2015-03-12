package main_console;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParserComponent extends Component {
	private Component _sendComponent;
	private AtomicBoolean _stop;
	private ConcurrentLinkedQueue<JSONMessage> _inboundQueue;

	public ParserComponent(Component logger, Component console, Component callback) {
		super(logger, console);
		_sendComponent = callback;
		_stop = new AtomicBoolean(false);
		_inboundQueue = new ConcurrentLinkedQueue<JSONMessage>();
	}

	/**
	 * Parses the json string to a list of IValues
	 * 
	 * @param jsonString
	 *            String consistent of JSON, refer to specs for how to create
	 * @return List of IValue components
	 */
	private List<IValues> parse(String jsonString) {
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		Map<String, Map<String, String>> full = gson.fromJson(jsonString, type);
		List<IValues> list = new ArrayList<IValues>();
		for (String val : full.keySet()) {
			IValues ivals = mapToValues(val, full.get(val));
			list.add(ivals);
		}
		return list;
	}

	private IValues mapToValues(String name, Map<String, String> map) {
		String current = map.get("current");
		String month = map.get("month");
		String goal = map.get("goal");
		return new ConcreteValues(name, current, month, goal);
	}

	@Override
	public void send(IMessage message) {
		message.dispatch(this);
	}

	@Override
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				runMessageProcesser();
			}
		}).start();
	}

	@Override
	public synchronized void handle(JSONMessage msg) {
		_inboundQueue.add(msg);
		notifyAll();
	}

	/**
	 * Runs a continuous loop, evaluating incoming messages and sending new
	 * ones.
	 */
	private synchronized void runMessageProcesser() {
		while (!_stop.get()) {
			while (_inboundQueue.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException ignore) {
				}
			}
			try {
				JSONMessage jsonMsg = _inboundQueue.poll();
				List<IValues> completed = parse(jsonMsg.getJSON());
				IMessage uiMsg = new UIMessage(this,
						jsonMsg.getCorrelationId(), completed);
				_sendComponent.send(uiMsg);
			} catch (Exception ex) {
				print(ex.getMessage());
				log(ex.getMessage());
			}
		}
		log("Stopped.");
		print("Stopped.");
	}

	@Override
	public void stop() {
		_stop.set(true);
	}

}