import java.util.ArrayList;

public abstract class PhysicalObject {
	
	protected Vector2 vel;
	protected Vector2 pos;
	protected double angVel;
	protected double invMass;
	protected double invI;
	protected double angle = 0;
	protected double width;
	protected double height;
	protected SurfaceType surface;
	protected String image;
    protected double friction;
	protected Matrix23 matrix = new Matrix23(0, new Vector2(0,0)); 
	
	public PhysicalObject(Vector2 pos, Vector2 vel, double invMass, double width, double height, double angVel, SurfaceType surface, String image)
	{
		this.width = width;
		this.height = height;
		this.pos = pos;
		this.vel = vel;
		this.angVel = angVel;
		this.invMass = invMass;
		this.surface = surface;
		this.image = image;
		friction = surface.getFriction();
		matrix = new Matrix23(Math.toRadians(angle), pos);
	}
	
	
	public abstract ArrayList<Contact> getClosestPoints(PhysicalObject po);
	
	public void integrate(double dt)
	{
		pos = Vector2.add(pos, Vector2.multiply(vel,dt));
		angle += angVel*dt;
		matrix = new Matrix23(Math.toRadians(angle), pos);
	}
	
	public void changeVelocity(Vector2 newVelocity)
	{
		vel = newVelocity;
	}
	
	
}
