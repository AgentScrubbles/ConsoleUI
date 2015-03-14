package console_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JLabel;
import main_console.IValues;

public class Box extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2930160163094115692L;
	private int width;
	private int height;
	private IValues values; // Immutable and thread safe
	private Font bigFont;
	private Font smallFont;
	private Color backgroundColor;

	public Box(int width, int height, Font bigFont, Font smallFont,
			IValues values) {
		
		super(0, 0, width, height);
		this.width = width;
		this.height = height;
		this.bigFont = bigFont;
		this.smallFont = smallFont;
		this.values = values;
		this.backgroundColor = Color.WHITE;
	}

	public Box(Font bigFont, Font smallFont, IValues values) {
		this.bigFont = bigFont;
		this.smallFont = smallFont;
		this.values = values;
	}
	
	public int width(){
		return width;
	}
	
	public int height(){
		return height;
	}
	
	public IValues values(){
		return values;
	}

	/**
	 * Paints and adds this panel to the parent
	 * 
	 * @param parentPanel
	 */
	public void paintComponent() {

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

	}
}
