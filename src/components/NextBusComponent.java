package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import web_connections.HttpWebService;
import main_console.BusValues;
import main_console.IValues;
import messages.IMessage;
import messages.ValueMessage;

public class NextBusComponent extends Component {

	// Hard coded for now, config later.

	final String _url;
	final String _agencyTag;
	final String _stopID;
	final int _refreshTime;
	final AtomicBoolean _stop;
	final HttpWebService ws;
	final Component _receiver;

	public NextBusComponent(Component logger, Component console, Component receiver, String url,
			String agencyTag, String stopID, int refreshTime) {
		super(logger, console);

		_url = url;
		_agencyTag = agencyTag;
		_stopID = stopID;
		_refreshTime = refreshTime;
		_stop = new AtomicBoolean(false);
		ws = new HttpWebService(url);
		_receiver = receiver;
	}

	@Override
	public void send(IMessage message) {
		message.dispatch(this);
	}

	@Override
	public void start() {
		print("Started");
		Runnable r = new Runnable(){

			@Override
			public void run() {
				listen();
			}
			
		};
		new Thread(r).start();
		
		
	}
	
	private void listen(){
		while(!_stop.get()){
			List<Integer> predictions = getPrediction();
			IValues values = new BusValues(_agencyTag, _stopID, predictions.get(0), predictions.get(1));
			List<IValues> list = new ArrayList<IValues>();
			list.add(values);
			ValueMessage ui = new ValueMessage(this, IntGenerator.generateCorrelation(), list, true);
			_receiver.send(ui);
			try {
				Thread.sleep(_refreshTime);
			} catch (InterruptedException e) {
				print("" + e);
				log("" + e);
			}
			print("Sent new prediction to screen");
		}
	}

	@Override
	public void stop() {
		_stop.set(true);
	}

	public List<Integer> getPrediction() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("command", "predictions");
		params.put("a", _agencyTag);
		params.put("stopId", _stopID);
		List<Integer> predictions = new ArrayList<Integer>();
		Document doc;
		try {
			doc = ws.callComplete(params);
			NodeList outerList = doc.getElementsByTagName("prediction");
			for (int j = 0; j < outerList.getLength(); j++) {
				Node outer = outerList.item(j);
				if (outer.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) outer;
					predictions.add(Integer.parseInt(elem
							.getAttribute("minutes")));
				}
			}
		} catch (Exception e) {
			log("" + e);
			print("" + e);
		}
		return predictions;
	}

}
