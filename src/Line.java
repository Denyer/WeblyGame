import java.util.ArrayList;

public class Line extends PhysicalObject {

	private Vector2[] points;
	private double angle;
	private Vector2 normal;
	private Vector2 invNormal;

	public Line(Vector2[] points)
	{
		super(points[0],new Vector2(0,0), 0, 0, 0, 0, SurfaceType.GRASS, "");
		this.points = points;
		angle = Math.toDegrees(Vector2.subtract(points[1],points[0]).Angle());
		Matrix transformMatrix = Matrix.TransformMatrix(angle);
		normal = new Vector2(transformMatrix.get(2,1), transformMatrix.get(2,2));
		matrix = new Matrix23(Math.toRadians(angle), pos);
		//System.out.println(normal.x);
		//System.out.println(normal.y);
		invNormal = Vector2.subtract(normal);
		//System.out.println(invNormal.x);
		//System.out.println(invNormal.y);
	}
	
	public boolean isPointOnLine(Vector2 p)
	{
	    if(p.x >= Math.min(points[1].x, points[0].x) && p.x <= Math.max(points[1].x, points[0].x) && p.y >= Math.min(points[0].y,points[1].y) && p.y <= Math.max(points[1].y,points[0].y))
	    {
		  return true;
	    }
	    return false;
	}
	
	public Vector2 normal(Vector2 p)
	{
		Vector2 d = Vector2.subtract(points[0],p);
		if(d.Dot(normal) < 0)
		{
			return invNormal;
		}
		return normal;
	}
	
	public Vector2 invNormal()
	{
		return invNormal;
	}
	
	public Vector2 start()
	{
		return points[0];
	}

	public Vector2 end()
	{
		return points[1];
	}
	
	public double angle()
	{
		return angle;
	}
	
	public double DistanceToPoint(Vector2 p, Vector2 pos)
	{
		Vector2 d = Vector2.subtract(points[0],p);
		double  dist = d.Dot(normal(pos));
		return dist;
	}

	public Vector2 ProjectPointOntoLine(Vector2 p, Vector2 pos)
	{
		Vector2 d = Vector2.subtract(p,points[0]);
		double dist = d.Dot(normal(pos));
		return Vector2.subtract(p, Vector2.multiply(normal(pos), dist));
	}	
	
	public ArrayList<Contact> getClosestPoints(PhysicalObject po)
	{
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		if (po instanceof Circle)
		{
			Circle rb = (Circle)po;
			double dist = DistanceToPoint(rb.pos,rb.pos) - rb.radius();

			Vector2 pointOnLine = ProjectPointOntoLine(rb.pos,rb.pos);
			Vector2 pointOnBall = Vector2.add(rb.pos, Vector2.multiply(normal, dist));

			contacts.add( new Contact(this, rb, pointOnLine, pointOnBall, Vector2.subtract(normal), dist) );
		}
		else if (po instanceof RectangleObject)
		{
			RectangleObject rb = (RectangleObject)po;

			contacts = rb.getClosestPoints(this);
			Contact.FlipContacts(contacts);
		}
		return contacts;
	}
	
	public RectangleObject.SupportVertex[] GetSupportVertices(Vector2 direction)
	{
		// closest vertices
		RectangleObject.SupportVertex[] spa = new RectangleObject.SupportVertex[2];
		spa[0] = new RectangleObject.SupportVertex(0, matrix.TransformBy( start() ));
		spa[1] = new RectangleObject.SupportVertex(1, matrix.TransformBy( end()));

		return spa;
	}		
}
