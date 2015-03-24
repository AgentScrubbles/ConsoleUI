package console_ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;

public class ImageBox {
	
	private Image img;
	
	public ImageBox(String imageLocation){
		img = Toolkit.getDefaultToolkit().getImage(imageLocation);
	}
	
	public void drawImage(Graphics2D g, JComponent canvas){
	    g.drawImage(img, 10, 10, canvas);
	    Dimension size = canvas.getSize();
	    g.drawImage(img, 0, 0, size.width, size.height, 0, 0, size.width, size.height, canvas);
	    g.finalize();
	}
}
