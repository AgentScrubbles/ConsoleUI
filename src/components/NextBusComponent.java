package components;

import messages.IMessage;

public class NextBusComponent extends Component {

	public NextBusComponent(Component logger, Component console) {
		super(logger, console);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(IMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	class BusListener implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
