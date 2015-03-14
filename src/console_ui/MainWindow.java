package console_ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import main_console.IValues;

public class MainWindow extends JPanel {

	private static final int FONT_SIZE = 20;
	
	private Font labelFont;
	private JLabel firstLabel;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private ArrayList<Box> boxes;
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
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initComponents();
			}
		});

	}

	private void initComponents() {

		//setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		firstLabel = new JLabel();
		// firstLabel.setFont(labelFont);
		firstLabel.setText("Data for " + dateFormat.format(new Date()));
		MainWindow.this.add(firstLabel, BorderLayout.PAGE_START);
		this.setVisible(true);
	}

	private void initializeBoxes() {

		// Center the word in the window, if possible; otherwise keep
		// a minimum left margin of one letter width
		int totalWidth = (boxes.size() - 1) * padding + boxWidth();
		int windowWidth = Math.max(getWidth(), totalWidth + 2 * boxWidth());
		int leftMargin = (windowWidth - totalWidth) / 2;
		leftMargin = Math.max(leftMargin, boxWidth());

		
	}
	
	  @Override
	  public void paintComponent(Graphics g)
	  {
	    
	    Graphics2D g2 = (Graphics2D) g;
	    Color savedColor = g2.getColor();
	    g2.setBackground(Color.LIGHT_GRAY);
	    g2.clearRect(0, 0, getWidth(), getHeight());

	    // paint the rectangles...
	    for (int i = 0; i < boxes.size(); ++i)
	    {
	      Box r = boxes.get(i);
	      g2.setColor(Color.WHITE);

	      g2.fillRect(r.x + 1, r.y + 1, r.width() - 2, r.height() - 2);
	      g2.setColor(Color.BLACK);
	      g2.draw(r);
	      
	      Font f = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
	      g2.setFont(f);
	      FontMetrics fm = g2.getFontMetrics(f);
	      String text = "" + r.values().name();
	      int h = fm.getHeight();
	      int w = fm.stringWidth(text);
	      int x = r.x + r.width() / 2 - (w / 2);
	      int y = r.y + r.height() / 2 + (h / 2) - 2;
	      g2.drawString(text, x, y);

	      g2.setColor(savedColor);
	    }
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
				//mainPanel = new JPanel();
				//GridLayout mainPanelLayout = new GridLayout(numBoxesWidth,
				//		numBoxesHeight);
				//mainPanelLayout.setHgap(padding);
				//mainPanelLayout.setVgap(padding);
				//mainPanel.setLayout(mainPanelLayout);
				for (IValues vals : values) {
					Box box = new Box(labelFont, labelFont, vals);
					box.paintComponent();
					
					// set character and x, y position for each rectangle
					
						int x = padding + boxes.size() * padding;
						box.setLocation(x, y);
						boxes.add(box);
						//mainPanel.add(box);

				}
				//mainPanel.repaint();
				//mainPanel.setVisible(true);
				//MainWindow.this.add(mainPanel, BorderLayout.CENTER);
				MainWindow.this.repaint();
			}
		});
	}

	
	

}
