import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Level1 extends Level {
	
	public Level1() {
		
		setTitle("Level 1");

		Vector2[] line1Points = 
			{
				new Vector2(335,540),
				new Vector2(335,520)
				
			};
		addCollidableObject(new Line(line1Points));			
		
		Vector2[] line2Points = 
			{
				new Vector2(335,520),
				new Vector2(350,510)
				
			};
		addCollidableObject(new Line(line2Points));				
		
		Vector2[] line3Points = 
			{
				new Vector2(350,510),
				new Vector2(390,523)
				
			};
		addCollidableObject(new Line(line3Points));
		Vector2[] line4Points = 
			{
				new Vector2(390,523),
				new Vector2(455,554)
				
			};
		addCollidableObject(new Line(line4Points));		
		Vector2[] line5Points = 
			{
				new Vector2(455,554),
				new Vector2(550,545)
				
			};
		addCollidableObject(new Line(line5Points));	
		Vector2[] line6Points = 
			{
				
				new Vector2(550,545),
				new Vector2(630,520)
				
			};
		addCollidableObject(new Line(line6Points));	
		setWidth(900);
		setHeight(675);
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
