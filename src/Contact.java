import java.util.ArrayList;

/*
 * contact class, contains all info necessary to solve contact between two objects
 */
public class Contact {

	PhysicalObject objA;  
	PhysicalObject objB;		
	Vector2 pointA;			//closest point on a
	Vector2 pointB;			//closest point on b
	Vector2 radiusA;			//radius of a
	Vector2 radiusB;			//radius of b
	Vector2 normal;		//collision normal
	double impulseN;		//impulse along normal
	double impulseT;		//impulse along tangent for friction
	double dist;			//distance between points
	double invDenom;		//denominator for impulse calculation
	double invDenomTan;	//denominator for tangent impulse calculation	
	double friction;		//friction of the collision
	

	public Contact(PhysicalObject A, PhysicalObject B, Vector2 pa, Vector2 pb, Vector2 n, double dist)
	{
		objA = A;   				
		objB = B;   				
		pointA = pa; 				
		pointB = pb; 				
		normal = n;
		this.dist = dist;
		impulseN = 0;

		friction = objA.friction * objB.friction;
		
		
		// calculate radius arms
		radiusA = Vector2.subtract(pointA, A.pos).Perp();
		radiusB = Vector2.subtract(pointB,B.pos).Perp();

		//compute denominator in impulse equation
		//using Runge-Kutta method
		//compute all constants to pass to solver
		//equation is of form:
		//imp =  -(1-e)xrelVelocity . normal/(1/ma + 1/mb + (rap x n)^2/ Ia + (rbp x n)^2/Ib)
		//calculate denominator
		double a = A.invMass;
		double b = B.invMass;
		double ran = radiusA.Dot(n);
		double rbn = radiusB.Dot(n);
		double c = Math.pow(ran,2) * A.invI;
		double d =  Math.pow(rbn,2) * B.invI;

		invDenom = 1 / (a + b + c + d);

		Vector2 t = n.Perp();
		ran = radiusA.Dot(t);
		rbn = radiusB.Dot(t);
		c = ran * ran * A.invI;
		d = rbn * rbn * B.invI;

		invDenomTan = 1 / (a + b + c + d);
	}



	/// Contact point velocity
	public Vector2 m_VelPa()
	{
		//return webly.game.Vector2.add(m_a.vel,webly.game.Vector2.multiply(m_ra, m_a.angVel));
		return objA.vel;
	}


	/// Contact point velocity
	public Vector2 m_VelPb()
	{
		//return webly.game.Vector2.add(m_b.vel,webly.game.Vector2.multiply(m_rb, m_b.angVel));
		return objB.vel;
	}


	public void ApplyImpulses(Vector2 imp)
	{
		// linear
		objA.vel = Vector2.add(objA.vel, Vector2.multiply(imp,objA.invMass));
		objB.vel = Vector2.subtract(objB.vel, Vector2.multiply(imp,objB.invMass));
        
		//System.out.println(imp.x);
		//System.out.println(m_a.angVel);
		//System.out.println(m_b.angVel);
		
		// angular
		//System.out.println(imp.x);

		objA.angVel += imp.Dot(radiusA) * objA.invI;
		objB.angVel -= imp.Dot(radiusB) * objB.invI;
		//System.out.println(m_b.angle);
	}



	static public void FlipContacts(ArrayList<Contact> contacts)
	{
		for (int i = 0; i < contacts.size(); i++)
		{
			PhysicalObject tempRb = contacts.get(i).objA;
			Vector2 tempV = contacts.get(i).pointA;

			contacts.get(i).objA = contacts.get(i).objB;
			contacts.get(i).objB = tempRb;
			contacts.get(i).pointA = contacts.get(i).pointB;
			contacts.get(i).pointB = tempV;

			tempV = contacts.get(i).radiusA;
			contacts.get(i).radiusA = contacts.get(i).radiusB;
			contacts.get(i).radiusB = tempV;

			contacts.get(i).normal = Vector2.subtract(contacts.get(i).normal);
		}
	}
}
