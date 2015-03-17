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

	private Font labelFont;
	private Font secondaryFont;
	private JLabel firstLabel;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private ArrayList<Box> boxes;
	private Dimension screen;
	private int numBoxesWidth;
	private int numBoxesHeight;
	private int maxBoxes;
	private int padding;
	private int heightPadding;
	private Point currentLocation;
	private Color goodColor;
	private Color badColor;
	private Color backgroundColor;
	private boolean clearOrMove;
	private AtomicBoolean animating;

	/**
	 * 
	 */
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
		this.backgroundColor = new Color(244, 123, 41);
		this.clearOrMove = true; // Clear the screen
		animating = new AtomicBoolean(false);

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
			if (clearOrMove) {
				resetLocation();
			}
			// paint the rectangles...
			for (int i = 0; i < boxes.size(); ++i) {
				Box r = boxes.get(i);
				g2.setColor(r.backgroundColor());

				if (clearOrMove) {
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
				text = "Current: " + r.values().current();
				Point secLoc = generateBelowTextLocation(fm, text, r, textLoc);
				g2.drawString(text, secLoc.x, secLoc.y);

				text = "Month: " + r.values().month();
				Point thrLoc = generateBelowTextLocation(fm, text, r, secLoc);
				g2.drawString(text, thrLoc.x, thrLoc.y);

				text = "Goal: " + r.values().goal();
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
			scrollBoxesLeft();									// this wait.

			try {
				Thread.sleep(100);
			} catch (InterruptedException ignore) {
			}

			synchronized (animating) {
				while (animating.get()) {
					try {
						animating.wait();
					} catch (InterruptedException ignore) {
					}
				}
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				synchronized (boxes) {
					clearOrMove = true;
					boxes.clear();
					for (IValues vals : values) {
						Box box = new Box(boxWidth(), boxHeight(), labelFont,
								secondaryFont, vals, goodColor, badColor);
						box.paintComponent();
						boxes.add(box);
					}
					if(boxes.size() > maxBoxes){
						startAutoScroll();
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

	private void scrollBoxesLeft() {
		new Thread(new Animator(10, -10, 0)).start();
	}

	private void scrollBoxesDown(){
		new Thread(new Animator(10, 0, -10)).start();
	}
	
	private void scrollBoxesUp(){
		new Thread(new Animator(10, 0, 10)).start();
	}
	
	private void startAutoScroll(){
		Runnable scroller = new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} //Let everything in the UI catch up before we start scrolling
				while(boxes.size() > maxBoxes){
					//Scroll down
					
					try {
						scrollBoxesUp();
						Thread.sleep(5000); //Sleep for 5 seconds, then scroll back up
						if(boxes.size() < maxBoxes || animating.get()){
							return; //Modified while changed
						}
						scrollBoxesDown();
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //Repeat until boxes has updated.
				}
			}
		};
		new Thread(scroller).start();
	}
	
	class Animator implements Runnable {
		// private Random rand = new Random(100);
		private final int sleepTime;
		private final int xDirChange;
		private final int yDirChange;

		public Animator(int sleepTime, int xDirChange, int yDirChange) {
			this.sleepTime = sleepTime;
			this.xDirChange = xDirChange;
			this.yDirChange = yDirChange;
		}

		public void moveBoxes() {
			synchronized (animating) {
				animating.set(true);
			}
			clearOrMove = false;
			int amnt = 10;
			for (int i = 0; i < screen.getWidth(); i += amnt) {
				for (Box b : boxes) {
					b.changeCoordinates(xDirChange, yDirChange);
				}
				MainWindow.this.repaint();

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ingore) {
				}
			}
			// Absolutely terrible, but just in case, make this wait for
			// everything to finish on the event thread
			/**
			 * try { Thread.sleep(sleepTime * boxes.size()); } catch
			 * (InterruptedException e) { block e.printStackTrace(); }
			 **/

			synchronized (animating) {
				clearOrMove = true; // Ready to fill again
				animating.set(false);
				animating.notifyAll();
			}
		}

		@Override
		public void run() {
			moveBoxes();
		}

	}

}
