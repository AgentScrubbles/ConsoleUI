package main_console;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;



public class MainWindow extends JFrame {

	Font labelFont;
	JLabel firstLabel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3880026026104218593L;

	public MainWindow() {

		labelFont = new Font("FUTURA", Font.PLAIN, 20);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initComponents();
			}
		});
	}

	private void initComponents() {
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		firstLabel = new JLabel();
		//firstLabel.setFont(labelFont);
		firstLabel.setText("Pending...");
		MainWindow.this.add(firstLabel);
		MainWindow.this.pack();
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
				firstLabel.setText(values.getRank());
				firstLabel.repaint();
				MainWindow.this.repaint();
			}
		});
	}

}
