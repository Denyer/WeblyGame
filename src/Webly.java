public class Webly extends RectangleObject {

	public Webly(Vector2 pos, Vector2 vel, double invMass, double width, double height, double angVel, String image)
	{
		super(pos, vel, invMass, width, height,angVel, SurfaceType.GRASS, image);
	}
	
	public void move(boolean left)
	{
		Vector2 moveVelocity;
		if(left)
		{
			moveVelocity = Vector2.add(Vector2.multiply(Vector2.FromAngle(Math.toRadians(angle)), new Vector2(-3,0)), new Vector2(0, vel.y));
			changeVelocity(moveVelocity);	
		}
		else
		{
			moveVelocity = Vector2.add(Vector2.multiply(Vector2.FromAngle(Math.toRadians(angle)), new Vector2(3,0)), new Vector2(0, vel.y));
			changeVelocity(moveVelocity);	
		}
	}	
	
}
