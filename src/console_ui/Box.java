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
	private IValues values; // Immutable and thread safes

	public Box(int width, int height,
			IValues values) {
		
		super(0, 0, width, height);
		this.width = width;
		this.height = height;
		this.values = values;
	}

	
	public synchronized void changeCoordinates(int x, int y){
		this.x += x;
		this.y += y;
	}
	
	public synchronized int width(){
		return width;
	}
	
	public synchronized int height(){
		return height;
	}
	
	public synchronized IValues values(){
		return values;
	}
	
	public boolean goodOrBad(){
		return values.metGoal();
	}

}
