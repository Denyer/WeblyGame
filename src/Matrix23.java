public class Matrix23 {
	private Vector2 m_row0;
	private Vector2 m_row1;
	private Vector2 m_pos;

	public Matrix23()
	{
		m_row0 = new Vector2(1,0);
		m_row1 = new Vector2(0,1);
		m_pos = new Vector2();
	}

	public Matrix23(double angle, Vector2 pos)
	{
		m_row0 = Vector2.FromAngle(angle);
		m_row1 = m_row0.Perp();
		m_pos = pos;
	}

	public Vector2 m_Row0()
	
	
	{
		return m_row0;
	}

	public Vector2 m_Row1()
	{
		return m_row1;
	}

	public Vector2 m_Pos()
	{
		return m_pos;
	}

	public Vector2 RotateIntoSpaceOf(Vector2 v)
	{
		return new Vector2( v.Dot(m_row0), v.Dot(m_row1) );
	}


	public Vector2 RotateBy(Vector2 v)
	{
		return Vector2.add(Vector2.multiply(v.x,m_row0), Vector2.multiply(v.y,m_row1));
	}

	public Vector2 TransformBy(Vector2 v)
	{
		return Vector2.add(RotateBy(v),m_pos);
	}
}
