import java.util.ArrayList;


public class RectangleObject extends PhysicalObject {
	
	Vector2 halfExtents;
	Vector2 [] localSpacePoints;
	Vector2 [] localSpaceNormals; 

	public static class SupportVertex {
		public Vector2 v;
		public int i;
			
		public SupportVertex(int i, Vector2 v)
		{
			this.v = v;
			this.i = i;
		}

	}
	
	public RectangleObject(Vector2 pos, Vector2 vel, double invMass, double width, double height, double angVel, SurfaceType surface, String image)
	{
		super(pos, vel, invMass, width, height, angVel, surface, image);
		initialise();
	}

	public RectangleObject(Vector2 pos, double width, double height, SurfaceType surface, String image)
	{
		super(pos, new Vector2(0,0), 0, width, height, 0, surface, image);
		initialise();
	}	
	
	private void initialise()
	{
		halfExtents = new Vector2(this.width / 2, this.height / 2);	
		// form local space points
		localSpacePoints = new Vector2[]
		{
			new Vector2(halfExtents.x, -halfExtents.y),
			new Vector2(-halfExtents.x, -halfExtents.y),
			new Vector2(-halfExtents.x, halfExtents.y),
			new Vector2(halfExtents.x, halfExtents.y)
		};

		// and local space normals
		localSpaceNormals = new Vector2[localSpacePoints.length];
		for (int i=0; i<localSpacePoints.length; i++)
		{
			localSpaceNormals[i] = (Vector2.subtract(localSpacePoints[(i+1)%localSpacePoints.length],localSpacePoints[i])).Unit().Perp();
		}
		
		
		if (invMass > 0)
		{
			double mass = 1 / invMass;
			
			double I = mass * (Math.pow(width,2) + Math.pow(height,2)) / 12.0;
			invI = 1 / I;
		}
		else
		{
			invI = 0;
		}	
	}
	
	public ArrayList<Contact> getClosestPoints(PhysicalObject po)
	{
		ArrayList<Contact> contacts = new ArrayList<Contact>(); 
		if (po instanceof Circle)
		{
			RectangleObject ra = this;
			Circle rb = (Circle)po;

			// clamp point to exterior of rectangle
			Vector2 delta = Vector2.subtract(rb.pos,ra.pos);

            Matrix transformMatrix = Matrix.TransformMatrix(angle);

            Vector2 rdelta = delta.RotateIntoSpaceOf(transformMatrix);

			Vector2 dClamped = rdelta.Clamp(Vector2.subtract(halfExtents), halfExtents);
			Vector2 clampedP = Vector2.add(ra.pos,dClamped.RotateBy(transformMatrix));

			// vector from clamped point to circle
			Vector2 d = Vector2.subtract(rb.pos,clampedP);
			Vector2 n = d.Unit();

			// form closest points
			Vector2 pa = clampedP;
			Vector2 pb = Vector2.subtract(rb.pos, Vector2.multiply(n,rb.radius()));

			// return distance
			double dist = d.m_Len() - rb.radius();

			contacts.add( new Contact(ra, rb, pa, pb, n, dist) );
		}
		else if (po instanceof Plane)
		{
			Plane rb = (Plane)po;

			Vector2[] worldP = new Vector2[localSpacePoints.length];
			double[] worldD = new double[localSpacePoints.length];
			
			int i = 0;
			
			for(Vector2 v : localSpacePoints)
			{
				// world space rect point
				worldP[i] = matrix.TransformBy(v);

				// distance to plane
				worldD[i] = rb.DistanceToPoint(worldP[i]);
				i++;
			}

			int closest = -1;
			int secondClosest = -1;
			double closestD = Double.MAX_VALUE;
			double secondClosestD = Double.MAX_VALUE;
			for (i = 0; i < localSpacePoints.length; i++)
			{
				if (worldD[i] < closestD)
				{
					closestD = worldD[i];
					closest = i;
				}
			}

			for (i = 0; i < localSpacePoints.length; i++)
			{
				if (i!=closest && worldD[i] < secondClosestD)
				{
					secondClosestD = worldD[i];
					secondClosest = i;
				}
			}

			// normal points from a->b
			Contact ca = new Contact(this, rb, worldP[closest], rb.ProjectPointOntoPlane(worldP[closest]),
												rb.normal, worldD[closest]);

			Contact cb = new Contact(this, rb, worldP[secondClosest], rb.ProjectPointOntoPlane(worldP[secondClosest]),
												rb.normal, worldD[secondClosest]);
			contacts.add( ca );
			contacts.add( cb );
		}
		else if (po instanceof Line)
		{
			Line line = (Line)po;

			Vector2[] worldP = new Vector2[localSpacePoints.length];
			double[] worldD = new double[localSpacePoints.length];
			
			int i = 0;
			for(Vector2 v : localSpacePoints)
			{
				// world space rect point
				worldP[i] = matrix.TransformBy(v);
				
				// distance to line
				worldD[i] = line.DistanceToPoint(worldP[i],pos);
				
				i++;
			}

			int closest = -1;
			int secondClosest = -1;
			double closestD = Double.MAX_VALUE;
			double secondClosestD = Double.MAX_VALUE;
			for (i = 0; i < localSpacePoints.length; i++)
			{
				if (worldD[i] < closestD)
				{
					closestD = worldD[i];
					closest = i;
				}
			}
			for (i = 0; i < localSpacePoints.length; i++)
			{
				if (i!=closest && worldD[i] < secondClosestD)
				{
					secondClosestD = worldD[i];
					secondClosest = i;
				}
			}

            Contact ca;
            Contact cb;
            Vector2 pa;
            Vector2 pb;

			if(line.isPointOnLine(line.ProjectPointOntoLine(worldP[closest],pos)))
			{
				ca = new Contact(this, line, worldP[closest], line.ProjectPointOntoLine(worldP[closest],pos),
					line.normal(pos), closestD);
				contacts.add( ca );
			}
			else
			{
				pa = line.start();
				if(Vector2.subtract(worldP[closest],line.start()).Mag() > Vector2.subtract(worldP[closest],line.end()).Mag())
				{
					pa = line.end();
				} 
				pb = Geometry.ProjectPointOntoEdge(pa, worldP[closest], worldP[secondClosest]);
				double d1 = line.DistanceToPoint(pb,pos);
                if(line.isPointOnLine(line.ProjectPointOntoLine(pb,pos)))
                {
				  ca = new Contact(this, line, pa, pb, line.normal(pos), d1);
				  contacts.add( ca );
                }
				
			}

			if(line.isPointOnLine(line.ProjectPointOntoLine(worldP[secondClosest],pos)))
			{
				cb = new Contact(this, line, worldP[secondClosest], line.ProjectPointOntoLine(worldP[secondClosest],pos),
					line.normal(pos), secondClosestD);
				contacts.add( cb );
			}
			else
			{
				pa = line.start();
				if(Vector2.subtract(worldP[secondClosest],line.start()).Mag() > Vector2.subtract(worldP[secondClosest],line.end()).Mag())
				{
					pa = line.end();
				}
                pb = Geometry.ProjectPointOntoEdge(pa, worldP[closest], worldP[secondClosest]);
                double d2 = line.DistanceToPoint(pb,pos);
                if(line.isPointOnLine(line.ProjectPointOntoLine(pb,pos)))
                {
                	cb = new Contact(this, line, pa, pb, line.normal(pos), d2);
                	contacts.add( cb );
                }
			}
			
		}
		else if (po instanceof RectangleObject)
		{
			
			return Geometry.RectRectClosestPoints(this, (RectangleObject)po);
		}
		
		
		return contacts;
	}

	
	public SupportVertex[] GetSupportVertices(Vector2 direction)
	{
		// rotate into rectangle space
		Vector2 v = matrix.RotateIntoSpaceOf(direction);

		// get axis bits
		int closestI = -1;
		int secondClosestI = -1;
		double closestD = -Double.MAX_VALUE;
		double secondClosestD = -Double.MAX_VALUE;

		// first support
		for (int i = 0; i < localSpacePoints.length; i++)
		{
			double d = v.Dot(localSpacePoints[i]);

			if (d > closestD)
			{
				closestD = d;
				closestI = i;
			}
		}

		// second support
		int num=1;
		for (int i = 0; i < localSpacePoints.length; i++)
		{
			double d = v.Dot(localSpacePoints[i]);

			if (i!=closestI && d == closestD)
			{
				secondClosestD = d;
				secondClosestI = i;
				num++;
				break;
			}
		}

		// closest vertices
		SupportVertex[] spa = new SupportVertex[num];
		spa[0] = new SupportVertex(closestI, matrix.TransformBy( localSpacePoints[closestI] ));
		if (num > 1)
		{
			spa[1] = new SupportVertex(secondClosestI, matrix.TransformBy( localSpacePoints[secondClosestI]));
		}

		return spa;
	}	
	
	public Vector2[] GetSecondSupport(int v, Vector2 n)
	{
		Vector2 va = GetWorldSpacePoint( (v-1 + localSpacePoints.length)%localSpacePoints.length );
		Vector2 vb = GetWorldSpacePoint( v );
		Vector2 vc = GetWorldSpacePoint( (v+1)%localSpacePoints.length );

		Vector2 na = (Vector2.subtract(vb, va)).Perp().Unit();
		Vector2 nc = (Vector2.subtract(vc, vb)).Perp().Unit();

		Vector2[] support = new Vector2[2];

		if (na.Dot(n) < nc.Dot(n))
		{
			support[0] = va;
			support[1] = vb;
		}
		else
		{
			support[0] = vb;
			support[1] = vc;
		}

		return support;
	}	
	
	public Vector2[] LocalSpaceNormals()
	{
		return localSpaceNormals;
	}

	public Vector2[] LocalSpacePoints()
	{
		return localSpacePoints;
	}

	public Vector2 GetWorldSpacePoint(int i)
	{
		return matrix.TransformBy( localSpacePoints[i] );
	}

	public Vector2 GetWorldSpaceNormal(int i)
	{
		return matrix.RotateBy( localSpaceNormals[i] );
	}	
}
