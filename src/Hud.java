import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Hud {

	public Hud() {
		
	}

	public BufferedImage draw(BufferedImage backBuffer)
	{
		String s = "Flies " + Game.fliesCollected + "/" + Game.totalFlies;
		char[] c_arr = s.toCharArray();
		Graphics bbg = backBuffer.getGraphics();
		bbg.drawChars(c_arr, 0, c_arr.length, 30, 20);
		return backBuffer;
	}
}
