import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;


public class Level2 extends Level{

	public Level2() {

		setTitle("Level 2");
		setWidth(800);
		setHeight(800);  	

		Vector2[] linePoints = 
			{
				new Vector2(300,430),
				new Vector2(380,440)
				
			};
		addCollidableObject(new Line(linePoints));
		Vector2[] line2Points = 
			{
				new Vector2(300,400),
				new Vector2(380,200)
			};
	}
			
	public void addFlies(int[] position, String type)
	{
		
	}
	
	public void setStartPosition()
	{
		
	}
	
	public void setEndPosition()
	{
		
	}
}
