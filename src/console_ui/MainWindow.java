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
	private Font secondaryFont;
	private JLabel firstLabel;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private ArrayList<Box> boxes;
	private Dimension screen;
	private int numBoxesWidth;
	private int numBoxesHeight;
	private int padding;
	private int heightPadding;
	private Point currentLocation;
	private Color goodColor;
	private Color badColor;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3880026026104218593L;

	public MainWindow(int numberOfBoxesAcross, int numberOfBoxesUpDown,
			int padding, int heightPadding) {

		labelFont = new Font("FUTURA", Font.PLAIN, 20);
		secondaryFont = new Font("FUTURA", Font.PLAIN, 12);
		boxes = new ArrayList<Box>();
		screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.heightPadding = heightPadding;
		this.numBoxesWidth = numberOfBoxesAcross;
		this.numBoxesHeight = numberOfBoxesUpDown;
		this.padding = padding;
		this.currentLocation = new Point(padding, padding);
		this.goodColor = Color.getHSBColor(129, 43, 87);
		this.badColor = Color.getHSBColor(0, 60, 99);
				
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initComponents();
			}
		});

	}

	private void initComponents() {

		// setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		firstLabel = new JLabel();
		// firstLabel.setFont(labelFont);
		firstLabel.setText("Data for " + dateFormat.format(new Date()));
		MainWindow.this.add(firstLabel, BorderLayout.PAGE_START);
		this.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		synchronized (boxes) {
			Graphics2D g2 = (Graphics2D) g;
			Color savedColor = g2.getColor();
			g2.setBackground(Color.LIGHT_GRAY);
			g2.clearRect(0, 0, getWidth(), getHeight());

			// paint the rectangles...
			for (int i = 0; i < boxes.size(); ++i) {
				Box r = boxes.get(i);
				g2.setColor(r.backgroundColor());

				// Get the next open point
				Point p = generateLocation();
				r.x = p.x;
				r.y = p.y;

				// Fill Rectangle
				g2.fillRect(r.x + padding, r.y + padding, r.width(), r.height());
				g2.setColor(Color.BLACK);
				g2.draw(r);

				// Set text
				// Font f = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
				Font f = r.getPrimaryFont();
				g2.setFont(f);
				FontMetrics fm = g2.getFontMetrics(f);
				String text = "" + r.values().name();
				Point textLoc = generateTextLocation(fm, text, r, -this.heightPadding);
				g2.drawString(text, textLoc.x, textLoc.y);

				Font s = r.getSecondaryFont();
				g2.setFont(s);
				fm = g2.getFontMetrics(s);
				text = "Current: " + r.values().current();
				Point secLoc = generateBelowTextLocation(fm, text, r, textLoc);
				g2.drawString(text, secLoc.x, secLoc.y);
				
				text = "Month: " + r.values().month();
				Point thrLoc = generateBelowTextLocation(fm, text, r, secLoc);
				g2.drawString(text, thrLoc.x, thrLoc.y);
				
				text = "Goal: " + r.values().goal();
				Point fthLoc = generateBelowTextLocation(fm, text, r, thrLoc);
				g2.drawString(text, fthLoc.x, fthLoc.y);

				g2.setColor(savedColor);
			}
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
				synchronized (boxes) {
					boxes.clear();
					for (IValues vals : values) {
						Box box = new Box(boxWidth(), boxHeight(), labelFont,
								secondaryFont, vals, goodColor, badColor);
						box.paintComponent();
						boxes.add(box);
					}
				}
				MainWindow.this.repaint();
			}
		});
	}

	private int boxWidth() {
		double boxWithPaddingWidth = (screen.getWidth() / numBoxesWidth)
				- padding;
		return (int) boxWithPaddingWidth - padding;
	}

	private int boxHeight() {
		double boxWithPaddingHeight = (screen.getHeight() / numBoxesHeight)
				- padding;
		return (int) boxWithPaddingHeight - padding;
	}

	private Point generateLocation() {
		synchronized (currentLocation) {
			currentLocation.x += (boxWidth() + padding);
			if (currentLocation.x > (screen.getWidth() - padding)) {
				// Create new row
				currentLocation.x = padding;
				currentLocation.y += (boxHeight() + padding);
			}
			return (Point) currentLocation.clone();
		}
	}

	private Point generateTextLocation(FontMetrics fm, String text, Box r, int heightPad) {
		int h = fm.getHeight() + heightPad;
		int w = fm.stringWidth(text);
		int x = r.x + r.width() / 2 - (w / 2);
		int y = r.y + r.height() / 2 + (h / 2) - 2;
		return new Point(x, y);
	}

	private Point generateBelowTextLocation(FontMetrics fm, String text, Box r,
			Point startPoint) {
		int h = fm.getHeight();
		// int w = fm.stringWidth(text);
		int x = startPoint.x;
		int y = startPoint.y + h + 3;
		return new Point(x, y);
	}

}
