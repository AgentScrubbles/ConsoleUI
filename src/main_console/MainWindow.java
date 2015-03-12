package main_console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainWindow extends JFrame {

	private Font labelFont;
	private JLabel firstLabel;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private List<Box> boxes;
	private Dimension screen;
	private JPanel mainPanel;
	private int numBoxesWidth;
	private int numBoxesHeight;
	private int padding;
	private int x;
	private int y;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3880026026104218593L;

	public MainWindow(int numberOfBoxesAcross, int numberOfBoxesUpDown,
			int padding) {

		labelFont = new Font("FUTURA", Font.PLAIN, 20);
		boxes = new ArrayList<Box>();
		screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.numBoxesWidth = numberOfBoxesAcross;
		this.numBoxesHeight = numberOfBoxesUpDown;
		this.padding = padding;
		this.x = padding;
		this.y = padding;
		
	}
	
	public void show(){
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
		this.setLayout(new BorderLayout());
		firstLabel = new JLabel();
		// firstLabel.setFont(labelFont);
		firstLabel.setText("Data for " + dateFormat.format(new Date()));
		MainWindow.this.add(firstLabel, BorderLayout.PAGE_START);
		MainWindow.this.pack();
		this.setVisible(true);
	}

	/**
	 * If IValues is implemented as thread safe (immutable is best), then this
	 * will work correctly. If it is not, then this will display whatever
	 * IValues return values are
	 * 
	 * @param values
	 */
	public void UpdateValues(final Collection<IValues> values) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				boxes.clear();
				mainPanel = new JPanel();
				GridLayout mainPanelLayout = new GridLayout(numBoxesWidth, numBoxesHeight);
				mainPanelLayout.setHgap(padding);
				mainPanelLayout.setVgap(padding);
				mainPanel.setLayout(mainPanelLayout);
				for (IValues vals : values) {
					Box box = new Box(labelFont, labelFont, vals);
					box.paintComponent();
					boxes.add(box);
					mainPanel.add(box);
					
				}
				mainPanel.repaint();
				mainPanel.setVisible(true);
				MainWindow.this.add(mainPanel, BorderLayout.CENTER);
				MainWindow.this.repaint();
			}
		});
	}

	@SuppressWarnings("unused")
	private Point generateNextLocation() {
		Point acceptable = new Point(x, y);
		x += boxWidth() + padding;
		if (x >= screen.width) {
			x = padding;
			y = y + padding + boxHeight();
		}
		return acceptable;
	}

	private int boxWidth() {
		return (screen.width - (numBoxesWidth * padding)) / numBoxesWidth;
	}

	private int boxHeight() {
		return (screen.height - (numBoxesHeight * padding)) / numBoxesHeight;
	}

}
