package components;

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

import messages.ErrorMessage;
import messages.IMessage;
import messages.LoadMessage;
import messages.UIMessage;
import console_ui.MainWindow;

public class UIComponent extends Component {

	private ConcurrentLinkedQueue<UIMessage> inboundMessages;
	private Component _loadComponent;
	private AtomicBoolean _stop;
	private MainWindow window;
	private JFrame frame;

	public UIComponent(Component logger, Component console) {
		super(logger, console);
		inboundMessages = new ConcurrentLinkedQueue<UIMessage>();
		_stop = new AtomicBoolean(false);
		bigBang();
	}

	public void setLoader(Component c) {
		_loadComponent = c;
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
				processMessages();
			}

		}).start();
	}

	public void bigBang() {
		// user interface (this is how God controls the universe)
		window = new MainWindow(6, 4, 10, 15);
		frame = new JFrame();
		frame.getContentPane().add(window);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

		fullScreen(frame, true);

		// make sure it closes when you click the close button on the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		

	}

	/**
	 * Requests a message from the loadComponent to load the latest message.
	 * Thread-safe Must have LoadComponent set
	 */
	public void loadSavedValues() {
		IMessage msg = new LoadMessage(this, IntGenerator.generateCorrelation());
		if (_loadComponent != null) {
			_loadComponent.send(msg);
		}
	}

	@Override
	public synchronized void stop() {
		_stop.set(true);
		notifyAll();
	}

	private synchronized void processMessages() {
		print("Started");
		loadSavedValues();
		while (!_stop.get()) {
			while (inboundMessages.isEmpty()) {
				try {
					wait();
					if (_stop.get()) {
						return;
					}
				} catch (InterruptedException e) {
					log(e.getMessage());
					print(e.getMessage());
				}
			}
			UIMessage uiMsg = inboundMessages.poll();
			window.UpdateValues(uiMsg.getValues());
			print("Values updated");
		}
		print("Stopped");
	}

	@Override
	public synchronized void handle(UIMessage msg) {
		inboundMessages.add(msg);
		notifyAll();
	}

	@Override
	public synchronized void handle(ErrorMessage msg) {
		print(msg.getError());
	}
	
	/**
	 * @param frame
	 * @param doPack
	 * @return device.isFullScreenSupported
	 */
	private static boolean fullScreen(final JFrame frame, boolean doPack) {

	    GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
	    boolean result = device.isFullScreenSupported();

	    if (result) {
	        frame.setUndecorated(true);
	        frame.setResizable(true);

	        frame.addFocusListener(new FocusListener() {

	            @Override
	            public void focusGained(FocusEvent arg0) {
	                frame.setAlwaysOnTop(true);
	            }

	            @Override
	            public void focusLost(FocusEvent arg0) {
	                frame.setAlwaysOnTop(false);
	            }
	        });

	        if (doPack)
	            frame.pack();

	        device.setFullScreenWindow(frame);
	    }
	    else {
	        frame.setPreferredSize(frame.getGraphicsConfiguration().getBounds().getSize());

	        if (doPack)
	            frame.pack();

	        frame.setResizable(true);

	        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	        boolean successful = frame.getExtendedState() == Frame.MAXIMIZED_BOTH;

	        frame.setVisible(true);

	        if (!successful)
	            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	    }
	    return result;
	}

}
