package components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import main_console.IValues;
import messages.IMessage;
import messages.UIMessage;
import messages.ValueMessage;

public class StagingComponent extends Component {

	private final Component _receiver; // Usually the UI
	private ConcurrentLinkedQueue<ValueMessage> _pending;
	private AtomicBoolean _stop;
	private List<IValues> _currentValues;

	public StagingComponent(Component logger, Component console,
			Component receiver) {
		super(logger, console);
		_receiver = receiver;
		_pending = new ConcurrentLinkedQueue<ValueMessage>();
		_stop = new AtomicBoolean(false);
		_currentValues = new ArrayList<IValues>(); // Manual synchronization
	}

	@Override
	public void send(IMessage message) {
		message.dispatch(this);
	}

	@Override
	public void start() {
		print("Started");
		new Thread(new Runnable() {

			@Override
			public void run() {
				messageProcessor();
			}

		}).start();

	}

	private synchronized void messageProcessor() {
		while (!_stop.get()) {
			while (_pending.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					log("" + e);
					print("" + e);
				}
			}
			while (!_pending.isEmpty()) {
				ValueMessage msg = _pending.poll();
				List<IValues> newValues = msg.values();
				IMessage ui = new UIMessage(this, msg.getCorrelationId(),
						safeModify(newValues, msg.doAppend()));
				_receiver.send(ui);

			}
		}
	}

	private List<IValues> safeModify(List<IValues> newValues, boolean append) {
		synchronized (_currentValues) {
			if (!append) {
				_currentValues.clear();
			}
			_currentValues.addAll(newValues);
			List<IValues> safeList = new ArrayList<IValues>();
			for(IValues val : _currentValues){
				safeList.add(val);
			}
			return safeList;
		}
	}
	

	@Override
	public synchronized void stop() {
		_stop.set(true);
		print("Stopped");
		notifyAll();
	}

	@Override
	public synchronized void handle(ValueMessage msg) {
		_pending.add(msg);
		notifyAll();
	}

}
