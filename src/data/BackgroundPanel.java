package data;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;;

public class BackgroundPanel extends JPanel {
	Image img;
	public BackgroundPanel(String str)
    {
        // Loads the background image and stores in img object.
        img = Toolkit.getDefaultToolkit().createImage(str);
    }
	public void paintComponent(Graphics g) {
		   super.paintComponent(g);
		   g.drawImage(img, 0, 0, null);
	}
}
