import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GraphicsRenderer {
	
    private BufferedImage spriteSheet;
	private BufferedImage backBuffer;
	private BufferedImage bgImage;
	private Game game;
	private int world;
	private int level;
	private double[] windowPosition = {0,0};
    private int windowWidth; 
    private int windowHeight;
    private final int frameHeight = 768; 
    private final int frameWidth = 1024; 
    private Insets insets;
    
    public GraphicsRenderer(Game game)
    {
    	this.game = game;
    	windowWidth = frameWidth;
    	windowHeight = frameHeight;
	    backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB); 
    }
    
    private void initialise()
    {
    	String worldDir = "resources/World-" + world + "/";
    	String backGround = worldDir + "level-" + level + ".png";
    	try
    	{
    		//this.spriteSheet = ImageIO.read(new File(spriteSheet));
    	}
    	catch(Exception e)
    	{
    		//unable to load images
    	}  
	 	try
	    {
	 		bgImage = ImageIO.read(new File(backGround));
	    }
		catch (IOException ex) 
		{
	           // suppress error
	    }	
    }
    
    public GraphicsRenderer setWorld(int world)
    {
    	this.world = world;
    	initialise();
    	return this;
    }
  
    public GraphicsRenderer setLevel(int level)
    {
    	this.level = level;
    	initialise();
    	return this;
    }   
    
    public double[] getWindowPosition()
    {
    	return windowPosition;
    }
    
    public int getWindowWidth()
    {
    	return windowWidth;
    }
    
    public int getWindowHeight()
    {
    	return windowHeight;
    }
    
    public void draw(ArrayList<Contact> contacts)
    {
    	Graphics bbg = backBuffer.getGraphics();
    	insets = game.getInsets();
		bbg.drawImage(bgImage, 0, 0, frameWidth, frameHeight , (int) windowPosition[0] , (int) windowPosition[1] , (int) (windowPosition[0]+frameWidth), (int) (windowPosition[1]+frameHeight), null);
    	drawObjects(game.currentLevel.getCollidableObjects());
	   	for(Contact c : contacts)
	   	{
	   		drawObject(new Circle(c.pointA,10,SurfaceType.GRASS,""));
	   		drawObject(new Circle(c.pointB,10,SurfaceType.GRASS,""));
	  	}
    	game.getGraphics().drawImage(backBuffer, insets.left, insets.top, game); 
    }
    
    private void drawObjects(ArrayList<PhysicalObject> objs)
    {
    	for(PhysicalObject obj : objs)
    	{
    		drawObject(obj);
    	}
    }
    
	public void drawObject(PhysicalObject obj)
	{
		Graphics bbg = backBuffer.getGraphics();
		if(obj instanceof RectangleObject)
		{
			RectangleObject o = (RectangleObject)obj;
			Vector2 drawOrig = Vector2.subtract(o.pos,o.halfExtents);
            Graphics2D bbg2d = (Graphics2D)bbg;
			bbg2d.setColor(Color.BLACK);
			Rectangle rect =  new Rectangle((int) drawOrig.x, (int) drawOrig.y, (int) o.width, (int) o.height);
			bbg2d.rotate(Math.toRadians(o.angle), (o.pos.x), (o.pos.y));;
			bbg2d.fill(rect);
		}
		else if(obj instanceof Circle)
		{
			Circle o = (Circle)obj;
			Vector2 drawOrig = Vector2.subtract(o.pos,o.radius());
			Graphics2D bbg2d = (Graphics2D)bbg;
			bbg2d.setColor(Color.GREEN);
			bbg2d.fillOval((int) drawOrig.x, (int) drawOrig.y, (int) o.width, (int) o.width);
		}
		else if(obj instanceof Line)
		{
			//do not render lines
			
			Line o = (Line)obj;
            Graphics2D bbg2d = (Graphics2D)bbg;
			bbg2d.setColor(Color.BLACK);
			Rectangle rect =  new Rectangle(
					(int) o.start().x,
					(int) o.start().y,
                    (int) (Vector2.subtract(o.start(),o.end())).Mag(),
                    (100)
            );
			bbg2d.rotate(Math.toRadians(o.angle()), (o.start().x), (o.start().y));
			bbg2d.fill(rect);
			
			
		}

	}
	
}
