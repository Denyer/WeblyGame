import java.util.ArrayList;

public class Geometry {

	public enum eFeaturePair
	{
		Undefined,
		VertexAFaceB,
		VertexBFaceA
	}	
	
	public static class FeaturePair
	{


		public FeaturePair(double dist, int vertex, int face, eFeaturePair fp, double centreDist)
		{
			m_dist = dist;
			m_vertex = vertex;
			m_face = face;
			m_fp = fp;
			m_centreDist = centreDist;
		}
		double m_dist;
		int m_vertex;
		int m_face;
		eFeaturePair m_fp;
		double m_centreDist;
	}

	
	static public Vector2 ProjectPointOntoEdge(Vector2 p, Vector2 e0, Vector2 e1)
	{
		Vector2 v = Vector2.subtract(p,e0);
		Vector2 e = Vector2.subtract(e1,e0);

		// time along edge
		double t = e.Dot(v) / e.m_LenSqr();
		
		// clamp to edge bounds
		t = Scalar.Clamp(t, 0, 1);

		// form point
		return Vector2.add(e0, Vector2.multiply(e,t));
	}


	static FeaturePair[] FeaturePairJudgement(	double dist, double centreDist, int edge, int supportV, eFeaturePair fpc, FeaturePair mostSeparated, FeaturePair mostPenetrating, Vector2 e0, Vector2 e1)
	{

		if (dist > 0)
		{
			// separating axis, but we want to track closest points anyway
			
			// recompute distance to clamped edge
			Vector2 p = ProjectPointOntoEdge(new Vector2(), e0, e1);

			// recompute distance
			dist = p.m_Len();
			
			if (dist < mostSeparated.m_dist)
			{
				mostSeparated = new FeaturePair(dist, supportV, edge, fpc, centreDist);
			}
			else if (dist == mostSeparated.m_dist && fpc == mostSeparated.m_fp)
			{
				// got to pick the right one - pick one closest to centre of A
				if (centreDist < mostSeparated.m_centreDist)
				{
					mostSeparated = new FeaturePair(dist, supportV, edge, fpc, centreDist);
				}
			}
		}
		else 
		{
			// penetration
			if (dist > mostPenetrating.m_dist)
			{
				mostPenetrating = new FeaturePair(dist, supportV, edge, fpc, centreDist);
			}
			else if (dist == mostPenetrating.m_dist && fpc == mostPenetrating.m_fp)
			{
				// got to pick the right one - pick one closest to centre of A
				if (centreDist < mostPenetrating.m_centreDist)
				{
					mostPenetrating = new FeaturePair(dist, supportV, edge, fpc, centreDist);
				}
			}
		}

		FeaturePair[] values = 
		{
			mostSeparated,
			mostPenetrating
		};
		return values; 
	}


	static public ArrayList<Contact> RectRectClosestPoints(RectangleObject A, RectangleObject B)
	{
		ArrayList<Contact> contacts = new ArrayList<Contact>();
        FeaturePair[] values = new FeaturePair[2];
		FeaturePair mostSeparated = new FeaturePair(Double.MAX_VALUE, -1, -1, eFeaturePair.Undefined, Double.MAX_VALUE);
		FeaturePair mostPenetrating = new FeaturePair(-Double.MAX_VALUE, -1, -1, eFeaturePair.Undefined, Double.MAX_VALUE);

		// face of A, vertices of B
		for (int i=0; i<A.localSpaceNormals.length; i++)
		{
			// get world space normal
			Vector2 wsN = A.GetWorldSpaceNormal(i);

			Vector2 wsV0 = A.GetWorldSpacePoint(i);
			Vector2 wsV1 = A.GetWorldSpacePoint( (i+1)%A.LocalSpaceNormals().length );

			// get supporting vertices of B, most opposite face normal
			RectangleObject.SupportVertex[] s = B.GetSupportVertices(Vector2.subtract(wsN));
			

			for (int j=0; j<s.length; j++)
			{

				// form point on plane of minkowski face
				Vector2 mfp0 = Vector2.subtract(s[j].v, wsV0);
				Vector2 mfp1 = Vector2.subtract(s[j].v, wsV1);

				// distance from origin to face
				double dist = mfp0.Dot(wsN);
				
				// distance to centre of A
				double centreDist = (Vector2.subtract(s[j].v, A.pos)).m_LenSqr();

				// pick correct feature pair

				values = FeaturePairJudgement(dist, centreDist, i, s[j].i, eFeaturePair.VertexBFaceA, mostSeparated, mostPenetrating, mfp0, mfp1);
				mostSeparated = values[0];
				mostPenetrating = values[1];
			}
		}

		// faces of B, vertices of A
		for (int i=0; i<B.LocalSpaceNormals().length; i++)
		{
			// get world space normal
			Vector2 wsN = B.GetWorldSpaceNormal(i);
			Vector2 wsV0 = B.GetWorldSpacePoint(i);
			Vector2 wsV1 = B.GetWorldSpacePoint( (i+1)%B.LocalSpaceNormals().length );


			// get supporting vertices of A, most opposite face normal
			RectangleObject.SupportVertex[] s = A.GetSupportVertices(Vector2.subtract(wsN));

			for (int j=0; j<s.length; j++)
			{
				// form point on plane of minkowski face
				Vector2 mfp0 = Vector2.subtract(s[j].v, wsV0);
				Vector2 mfp1 = Vector2.subtract(s[j].v, wsV1);

				// distance from origin to face
				double dist = mfp0.Dot(wsN);

				// distance to centre of B
				double centreDist = (Vector2.subtract(s[j].v,B.pos)).m_LenSqr();

				// pick correct feature pair
				values = FeaturePairJudgement(dist, centreDist, i, s[j].i, eFeaturePair.VertexAFaceB, mostSeparated, mostPenetrating, mfp0, mfp1);
				mostSeparated = values[0];
				mostPenetrating = values[1];
			}
		}

		FeaturePair featureToUse = null;
		RectangleObject vertexRect = null;
		RectangleObject faceRect = null;
		if (mostSeparated.m_dist > 0 && mostSeparated.m_fp != eFeaturePair.Undefined)
		{
			// objects are separated
			featureToUse = mostSeparated;
		}
		else if (mostPenetrating.m_dist <= 0)
		{
			// objects are penetrating
			//Debug.Assert(mostPenetrating.m_fp != eFeaturePair.Undefined);
			featureToUse = mostPenetrating; 
		}
		else
		{
			//throw new Exception("RectRectClosestPoints(): Impossible condition!");

		}

		if (featureToUse.m_fp == eFeaturePair.VertexAFaceB)
		{
			vertexRect = A;
			faceRect = B;
		}
		else
		{
			vertexRect = B;
			faceRect = A;
		}

		// world space vertex
		Vector2 worldN = faceRect.GetWorldSpaceNormal( featureToUse.m_face );
		
		// other vertex adjacent which makes most parallel normal with the collision normal
		Vector2[] worldV = vertexRect.GetSecondSupport( featureToUse.m_vertex, worldN );
		
		// world space edge
		Vector2 worldEdge0 = faceRect.GetWorldSpacePoint( featureToUse.m_face );
		Vector2 worldEdge1 = faceRect.GetWorldSpacePoint( (featureToUse.m_face+1)%faceRect.LocalSpacePoints().length );

		// form contact
		Vector2[] pointsOnA = new Vector2[2];
		Vector2[] pointsOnB = new Vector2[2];
		
		if (featureToUse.m_fp == eFeaturePair.VertexAFaceB)
		{
			// project vertex onto edge
			pointsOnA[0] = ProjectPointOntoEdge(worldEdge0, worldV[0], worldV[1]);
			pointsOnA[1] = ProjectPointOntoEdge(worldEdge1, worldV[0], worldV[1]);

			pointsOnB[0] = ProjectPointOntoEdge(worldV[1], worldEdge0, worldEdge1);
			pointsOnB[1] = ProjectPointOntoEdge(worldV[0], worldEdge0, worldEdge1);

			worldN = Vector2.subtract(worldN);
		}
		else
		{
			pointsOnA[0] = ProjectPointOntoEdge(worldV[1], worldEdge0, worldEdge1);
			pointsOnA[1] = ProjectPointOntoEdge(worldV[0], worldEdge0, worldEdge1);

			pointsOnB[0] = ProjectPointOntoEdge(worldEdge0, worldV[0], worldV[1]);
			pointsOnB[1] = ProjectPointOntoEdge(worldEdge1, worldV[0], worldV[1]);
		}

		double d0 = (Vector2.subtract(pointsOnB[0], pointsOnA[0])).Dot(worldN);
		double d1 = (Vector2.subtract(pointsOnB[1], pointsOnA[1])).Dot(worldN);

		contacts.add( new Contact(A, B, pointsOnA[0], pointsOnB[0], worldN, d0 ));
		contacts.add( new Contact(A, B, pointsOnA[1], pointsOnB[1], worldN, d1 ));

		return contacts;
	}
}
