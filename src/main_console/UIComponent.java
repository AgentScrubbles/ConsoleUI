package main_console;

public class UIComponent extends Component{

	public UIComponent(Component logger, Component console) {
		super(logger, console);
		// TODO Auto-generated constructor stub
	}

	MainWindow window = new MainWindow(5, 3, 50);
	
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

}
