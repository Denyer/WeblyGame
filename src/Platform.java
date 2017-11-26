import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Platform {
	
	private final double height;
	private final double width;
	private final SurfaceType surface;
	private Vector2 pos;
	private ArrayList<PhysicalObject> components;
	private BufferedImage image;

	public BufferedImage draw(BufferedImage backBuffer)
	{
		Vector2 drawOrig = drawOrig();
		Graphics bbg = backBuffer.getGraphics();
		bbg.drawImage(image, (int) drawOrig.x, (int) drawOrig.y, (int) (drawOrig.x+width), (int) (drawOrig.y+height) , 0, 315, (int) width, (int) height+315, null);
		return backBuffer;
	}
	
	public static class Builder
	{
		// required parameters
		private final Vector2 pos;
		private final double height;
		private final double width;
		private ArrayList<PhysicalObject> components = new ArrayList<PhysicalObject>();
		
		// optional parameters
		private SurfaceType surface;
		
		public Builder(Vector2 pos, double width, double height)
		{
			this.pos    = pos;
			this.width  = width;
			this.height = height;
		}
		
		public Builder surface(SurfaceType surface)
		{
			this.surface = surface;
			return this;
		}
		
		
		public Platform build()
		{
			//populate components
			//need one rectangle with two circles, one either side
			double rectWidth = width - height;
			double rectHeight = height;
			double ballWidth = height;
			Vector2 rectPos = pos;
			Vector2 ball1Pos = new Vector2(pos.x-rectWidth/2,pos.y);
			Vector2 ball2Pos = new Vector2(pos.x+rectWidth/2,pos.y);
			//Vector2 pos, Vector2 vel, double invMass, double angVel, double radius, String image
			components.add(new Circle(ball1Pos, ballWidth/2, surface, ""));
			components.add(new RectangleObject(rectPos, rectWidth, rectHeight, surface, ""));
			components.add(new Circle(ball2Pos, ballWidth/2, surface, ""));
			return new Platform(this);
		}
	}
	
	private Platform(Builder builder)
	{
		pos        = builder.pos;
		width      = builder.width;
		height     = builder.height;
		surface    = builder.surface;
		components = builder.components;
		try
        {
			image =  ImageIO.read(new File("resources/world_1_platform_objects.png"));
		}
		catch (IOException ex) 
		{
            // suppress error
        }		
	}	
	
	public ArrayList<PhysicalObject> components()
	{
		return components;
	}
	
	public Vector2 drawOrig()
	{
		return Vector2.subtract(pos,new Vector2(width/2,height/2));
	}
}
