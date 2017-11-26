import java.util.ArrayList;

public class ContactSolver {
	
	static int numSolved = 0;
	//static double kTimeStep = 1.0/30.0;
	static double kTimeStep = 1;
	static public void solve(ArrayList<Contact> contacts, int numIterations)
	{
		for (int j=0; j<numIterations; j++)
		{
			for (int i=0; i<contacts.size(); i++)
			{
				Contact con = contacts.get(i);
				Vector2 n = con.normal;
	
				// get all of relative normal velocity
	
				Vector2 dv = Vector2.subtract(con.m_VelPb(),con.m_VelPa());

				double relNv = dv.Dot(n);

				// get tangential velocity
				Vector2 tangent = n.Perp();
				double tanV = dv.Dot(tangent);
	
				double remove = relNv + (con.dist/kTimeStep);

				if (remove < 0)
				{
					// compute impulse
					double mag = remove * con.invDenom;
					Vector2 imp = Vector2.multiply(con.normal,mag);
	
					// apply impulse
					con.ApplyImpulses(imp);
					double absMag = Math.abs( mag )*con.friction;
	
					// friction
					mag = Scalar.Clamp( tanV * con.invDenomTan, -absMag, absMag );
				    imp = Vector2.multiply(tangent, mag);
	
					// apply impulse
					//con.ApplyImpulses(imp);
	
					numSolved++;
				}

			}
		}
	
		//System.out.println("solved"+numSolved);
		
	}
}
