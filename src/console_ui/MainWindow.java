package console_ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main_console.IValues;

public class MainWindow extends JPanel {

	/**
	 * Font to use for primary label
	 */
	private Font labelFont;
	
	/**
	 * Font to use for secondary labels
	 */
	private Font secondaryFont;
	
	/**
	 * DateTime at top left
	 */
	private JLabel firstLabel;
	
	/**
	 * Format for dates
	 */
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/**
	 * List of boxes, must only be created once as references are passed
	 */
	private ArrayList<Box> boxes;
	
	/**
	 * Screen size to play with
	 */
	private Dimension screen;
	
	/**
	 * Amount of boxes that can be placed in x-coordinate
	 */
	private int numBoxesWidth;
	
	/**
	 * Amount of boxes that can be placed in y-coordinate
	 */
	private int numBoxesHeight;
	
	/**
	 * Total amount of boxes on the screen (x * y)
	 */
	private int maxBoxes;
	
	/**
	 * Pixel padding between boxes
	 */
	private int padding;
	
	/**
	 * Amount of padding in the y direction before the main label on each box
	 */
	private int heightPadding;
	
	/**
	 * Current good spot to place a box
	 */
	private Point currentLocation;
	
	/**
	 * Color to paint a 'goal met' box
	 */
	private Color goodColor;
	
	/**
	 * Color to paint a 'not goal met' box
	 */
	private Color badColor;
	
	/**
	 * Background color of this JPanel
	 */
	private Color backgroundColor;
	
	/**
	 * Reset currentLocation to default for new boxes, or keep where it is
	 */
	private AtomicBoolean clearOrMove;
	
	/**
	 * Animator for all objects
	 */
	private Animator animator;

	/**
	 * 
	 */
	
	private ArrayList<Point> boxLocations;
	
	private static final long serialVersionUID = -3880026026104218593L;

	public MainWindow(int numberOfBoxesAcross, int numberOfBoxesUpDown,
			int padding, int heightPadding) {

		labelFont = new Font("FUTURA", Font.PLAIN, 22);
		secondaryFont = new Font("FUTURA", Font.PLAIN, 12);
		boxes = new ArrayList<Box>();
		screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.heightPadding = heightPadding;
		this.numBoxesWidth = numberOfBoxesAcross;
		this.numBoxesHeight = numberOfBoxesUpDown;
		this.maxBoxes = numberOfBoxesAcross * numberOfBoxesUpDown;
		this.padding = padding;
		this.currentLocation = new Point(padding, padding);
		this.goodColor = new Color(127, 224, 143);
		this.badColor = new Color(253, 100, 100);
		//this.backgroundColor = new Color(244, 123, 41);
		this.backgroundColor = Color.white;
		this.clearOrMove = new AtomicBoolean(true); // Clear the screen
		this.animator = new Animator(this, boxes, screen, maxBoxes, clearOrMove);
		this.boxLocations = new ArrayList<Point>();

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
		setDataLabel();
		MainWindow.this.add(firstLabel, BorderLayout.PAGE_START);
		this.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		setDataLabel();
		synchronized (boxes) {
			Graphics2D g2 = (Graphics2D) g;
			Color savedColor = g2.getColor();

			g2.setBackground(this.backgroundColor);
			g2.clearRect(0, 0, getWidth(), getHeight());
			if (clearOrMove.get()) {
				resetLocation();
			}
			// paint the rectangles...
			for (int i = 0; i < boxes.size(); ++i) {
				Box r = boxes.get(i);
				g2.setColor(r.backgroundColor());

				if (clearOrMove.get()) {
					// Get the next open point
					Point p = generateLocation();
					r.x = p.x;
					r.y = p.y;
				} else {
					// r.x and r.y are already set
				}

				// Fill Rectangle
				g2.fillRect(r.x, r.y, r.width(), r.height());
				g2.setColor(Color.BLACK);
				g2.draw(r);

				// Set text
				// Font f = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
				Font f = r.getPrimaryFont();
				g2.setFont(f);
				FontMetrics fm = g2.getFontMetrics(f);
				String text = "" + r.values().name();
				Point textLoc = generateTextLocation(fm, text, r,
						-this.heightPadding);
				g2.drawString(text, textLoc.x, textLoc.y);

				Font s = r.getSecondaryFont();
				g2.setFont(s);
				fm = g2.getFontMetrics(s);
				text = "" + r.values().firstItem();
				Point secLoc = generateBelowTextLocation(fm, text, r, textLoc);
				g2.drawString(text, secLoc.x, secLoc.y);

				text = "" + r.values().secondItem();
				Point thrLoc = generateBelowTextLocation(fm, text, r, secLoc);
				g2.drawString(text, thrLoc.x, thrLoc.y);

				text = "" + r.values().thirdItem();
				Point fthLoc = generateBelowTextLocation(fm, text, r, thrLoc);
				g2.drawString(text, fthLoc.x, fthLoc.y);

				text = "(" + r.x + ", " + r.y + ")";
				Point sthLoc = generateBelowTextLocation(fm, text, r, fthLoc);
				g2.drawString(text, sthLoc.x, sthLoc.y);

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

		if (boxes.size() > 0) { // Move current items off screen
			//animator.scrollBoxesLeft(); // this wait.

			try {
				Thread.sleep(100);
			} catch (InterruptedException ignore) {
			}

			synchronized (animator.animatingLock()) {
				while (animator.animatingLock().get()) {
					try {
						animator.animatingLock().wait();
					} catch (InterruptedException ignore) {
					}
				}
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				synchronized (boxes) {
					clearOrMove.set(false);
					boxes.clear();
					for (IValues vals : values) {
						Box box = new Box(boxWidth(), boxHeight(), labelFont,
								secondaryFont, vals, goodColor, badColor);
						boxes.add(box);
					}
					if (boxes.size() > maxBoxes) {
						animator.startAutoScroll();
					}
				}
				MainWindow.this.repaint();

			}
		});
	}

	private void setDataLabel() {
		firstLabel.setText("Data for " + dateFormat.format(new Date()));
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

	private void resetLocation() {
		synchronized (currentLocation) {
			currentLocation.x = padding;
			currentLocation.y = padding;
		}
	}
	
	private void generateAllLocations(){
		for(int i = 0; i < maxBoxes; i++){
			boxLocations.add(i, generateLocation());
		}
		currentLocation.x = padding;
		currentLocation.y = padding;
	}

	private Point generateLocation() {
		synchronized (currentLocation) {
			Point p = new Point(currentLocation.x, currentLocation.y);
			// Get ready for the next one
			currentLocation.x += (boxWidth() + padding);
			if (currentLocation.x > (screen.getWidth() - padding - boxWidth())) {
				// Create new row
				currentLocation.x = padding;
				currentLocation.y += (boxHeight() + padding);
			}
			return p;
		}
	}

	private Point generateTextLocation(FontMetrics fm, String text, Box r,
			int heightPad) {
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
