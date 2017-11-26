import java.util.ArrayList;

public class Circle extends PhysicalObject {

	private double radius;
	
	public Circle(Vector2 pos, Vector2 vel, double invMass, double angVel, double radius,SurfaceType surface, String image)
	{
		super(pos, vel, invMass, radius*2, radius*2, angVel, surface, image);
		initialise();
	}
	
	public Circle(Vector2 pos, double radius, SurfaceType surface, String image)
	{
		super(pos, new Vector2(0,0), 0, radius*2, radius*2, 0, surface, image);
		initialise();
	}	
	
	private void initialise()
	{
		this.radius = width/2;
		if(invMass > 0)
		{
			double mass = 1/invMass;	
			double I = mass * radius * radius / 4;
			invI = 1 / I;			
		}
		else
		{
			invI = 0;
		}		
	
	}
	
	public double radius()
	{
		return radius;
	}
		
	public ArrayList<Contact> getClosestPoints(PhysicalObject po)
	{
		ArrayList<Contact> contacts = new ArrayList<Contact>(); 
		if (po instanceof Circle)
		{
			Circle a = this;
			Circle b = (Circle)po;

			Vector2 delta = Vector2.subtract(b.pos,a.pos);

			Vector2 n;

			if (delta.m_LenSqr() > 0)
			{
				// get normal
				n = delta.Unit();
			}
			else
			{
				// default
				n = new Vector2(1,0);
			}

			// generate closest points
			Vector2 pa = Vector2.add(a.pos, Vector2.multiply(n,a.radius()));
			Vector2 pb = Vector2.subtract(b.pos, Vector2.multiply(n,b.radius()));

			// get distance
			double dist = Vector2.subtract(b.pos,a.pos).m_Len() - (a.radius() + b.radius());

			// add contact
			contacts.add( new Contact(a, b, pa, pb, n, dist) );
		}
		else if (po instanceof RectangleObject)
		{
			RectangleObject rectB = (RectangleObject)po;

			contacts = rectB.getClosestPoints(this);
			Contact.FlipContacts(contacts);			
		}
		else if (po instanceof Plane)
		{

		  Plane planeB = (Plane)po;
		  contacts = planeB.getClosestPoints(this);
		}

		
		return contacts;
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
