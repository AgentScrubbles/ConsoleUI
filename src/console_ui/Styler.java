package console_ui;

import java.awt.Color;
import java.awt.Font;

public class Styler {
	public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
	public static final Color BOX_COLOR = Color.LIGHT_GRAY;
	
	public static final Color FONT_GOOD_COLOR = Color.BLUE;
	public static final Color FONT_BAD_COLOR = Color.RED;
	public static final Color FONT_STANDARD_COLOR = Color.BLACK;
	
	public static final Font FONT_BOX_TITLE = new Font("FUTURA", Font.PLAIN, 22);
	public static final Font FONT_SUB_ITEM = new Font("FUTURA", Font.BOLD, 14);
	
	public static final String ALERT_IMAGE_FILE_LOC = "alert.png";
	
	public static final int ALERT_TIMEOUT_TIME = 60 * 1000 * 10; //Ten minutes;
}