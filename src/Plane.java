import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Plane extends PhysicalObject{
	
	Vector2 point;
	Vector2 normal;
    Matrix transformMatrix;

	public Plane(Vector2 point1, double angle)
	{
		super(point1,new Vector2(0,0), 0, 0, 0, 0, SurfaceType.GRASS, "");
		this.angle = angle;
		// calc inertia tensor
		invI = 0;

		// point on plane
		point = pos;

		transformMatrix = Matrix.TransformMatrix(angle);

		normal = new Vector2(transformMatrix.get(2,1),transformMatrix.get(2,2));
	}

	public Vector2 Normal()
	{
		return normal;
	}

	public double DistanceToPoint(Vector2 p)
	{
		Vector2 d = Vector2.subtract(point,p);
		double dist = d.Dot(normal);
		return dist;
	}

	public Vector2 ProjectPointOntoPlane(Vector2 p)
	{
		Vector2 d = Vector2.subtract(p,point);
		double dist = d.Dot(normal);
		return Vector2.subtract(p, Vector2.multiply(normal, dist));
	}

	public ArrayList<Contact> getClosestPoints(PhysicalObject po)
	{
		ArrayList<Contact> contacts = new ArrayList<Contact>();

		if (po instanceof Circle)
		{
			Circle rb = (Circle)po;
			double dist = DistanceToPoint(rb.pos) - rb.radius();

			Vector2 pointOnPlane = ProjectPointOntoPlane(rb.pos);
			Vector2 pointOnBall = Vector2.add(rb.pos, Vector2.multiply(normal, dist));

			contacts.add( new Contact(this, rb, pointOnPlane, pointOnBall, Vector2.subtract(normal), dist) );
		}
		else if (po instanceof RectangleObject)
		{
			RectangleObject rb = (RectangleObject)po;

			contacts = rb.getClosestPoints(this);
			Contact.FlipContacts(contacts);
		}


		return contacts;
	}
	
	public Vector2 drawOrig()
	{
		return pos;
	}
	
	public BufferedImage draw(BufferedImage backBuffer)
	{
		return backBuffer;
	}
}
