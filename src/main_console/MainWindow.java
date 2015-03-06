package main_console;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3880026026104218593L;

	public MainWindow() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initComponents();
			}
		});
	}

	private void initComponents() {
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}
	
	/**
	 * If IValues is implemented as thread safe (immutable is best), then 
	 * this will work correctly.  If it is not, then this will display whatever
	 * IValues return values are
	 * @param values
	 */
	public void UpdateValues(final IValues values){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				//Update the UI here with values.
				//TODO
			}
		});
	}

}
