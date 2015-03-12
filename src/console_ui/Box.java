package console_ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main_console.IValues;

public class Box extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2930160163094115692L;
	private int pos_X;
	private int pos_Y;
	private int width;
	private int height;
	private IValues values; // Immutable and thread safe
	private Font bigFont;
	private Font smallFont;
	private Color backgroundColor;
	private boolean manualPositioning;

	public Box(final int loc_x, final int loc_y, int width, int height,
			Font bigFont, Font smallFont, IValues values) {
		this.pos_X = loc_x;
		this.pos_Y = loc_y;
		this.width = width;
		this.height = height;
		this.bigFont = bigFont;
		this.smallFont = smallFont;
		this.values = values;
		this.backgroundColor = Color.WHITE;
		manualPositioning = true;
	}
	
	public Box(Font bigFont, Font smallFont, IValues values){
		this.bigFont = bigFont;
		this.smallFont = smallFont;
		this.values = values;
		manualPositioning = false;
	}

	/**
	 * Paints and adds this panel to the parent
	 * 
	 * @param parentPanel
	 */
	public void paintComponent() {
		if (manualPositioning) {
			this.setPreferredSize(new Dimension(width, height));
			this.setLocation(pos_X, pos_Y);
		}
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JLabel nameLabel = new JLabel(values.name());
		nameLabel.setFont(bigFont);
		nameLabel.setVisible(true);
		JLabel currentLabel = new JLabel("Current:\n" + values.current());
		currentLabel.setFont(smallFont);
		currentLabel.setVisible(true);
		JLabel monthLabel = new JLabel("Month:\n" + values.month());
		monthLabel.setFont(smallFont);
		monthLabel.setVisible(true);
		JLabel goalLabel = new JLabel("Goal:\n" + values.goal());
		goalLabel.setFont(smallFont);
		goalLabel.setVisible(true);

		this.add(nameLabel, BorderLayout.PAGE_START);
		this.add(currentLabel, BorderLayout.LINE_START);
		this.add(monthLabel, BorderLayout.CENTER);
		this.add(goalLabel, BorderLayout.LINE_END);

		this.setBackground(backgroundColor);
		this.setVisible(true);
		this.repaint();
	}
}
