import java.awt.Point;


public class Vector2 {
	public double x, y;

	public Vector2()
	{
       x = 0;
       y = 0;
	}
	
    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

	public Vector2(Point p)
	{
		x = p.x;
		y = p.y;
	}

    public Vector2(double c)
    {
        x = c;
        y = c;
    }

    public static Vector2 FromAngle(double radians)
    {
        return new Vector2(Math.cos(radians), Math.sin(radians));
    }

	public float ToAngle()
	{
		float angle = (float) Math.atan2(y, x);

		// make the returned range 0 -> 2*PI
		if (angle < 0.0f)
		{
			angle += 2 * (float)Math.PI;
		}
		return angle;
	}
    
    public Point GetPoint()
    {
        return new Point((int) x, (int) y);
    }

    public double Mag()
    {
    	return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }
    
    public double m_LenSqr()
    {
    	return Dot(this);     
    }

    public double m_Len()
    {
    	return Math.sqrt(m_LenSqr());
    }

    public double Dot(Vector2 v)
    {
        return x * v.x + y * v.y;
    }

	public Vector2 Perp()
	{
		return new Vector2(-y, x);
	}

	public Vector2 Floor()
	{
		return new Vector2(Math.floor(x), Math.floor(y));
	}

	public Vector2 Min(Vector2 v)
	{
		return new Vector2(Math.min(x, v.x), Math.min(y, v.y));
	}

	public Vector2 Max(Vector2 v)
	{
		return new Vector2(Math.max(x, v.x), Math.max(y, v.y));
	}

	public Vector2 Clamp(Vector2 min, Vector2 max)
	{
		this.x = Max(min).x;
		this.y = Max(min).y;
		return Min(max);
	}

	public Vector2 Unit()
	{
		return divide(this, m_Len());
	}

	public Vector2 Abs()
	{
		return new Vector2(Math.abs(x), Math.abs(y));
	}

	public Vector2 Frac()
	{
		Vector2 abs = Abs();
		return subtract(abs, abs.Floor());
	}

	public double Angle()
	{
		return Math.atan2(y, x);
	}


    public static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x + b.x, a.y + b.y);
    }
    public static Vector2 subtract(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x - b.x, a.y - b.y);
    }
    public static Vector2 multiply(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x * b.x, a.y * b.y);
    }
    public static Vector2 divide(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x / b.x, a.y / b.y);
    }
    public static Vector2 multiply(Vector2 a, double fB)
    {
        return new Vector2(a.x * fB, a.y * fB);
    }
    public static Vector2 multiply(double fA, Vector2 b)
    {
        return new Vector2(b.x * fA, b.y * fA);
    }
    public static Vector2 divide(Vector2 a, double fB)
    {
        return new Vector2(a.x / fB, a.y / fB);
    }
	public static Vector2 subtract(Vector2 v)
    {
        return new Vector2(-v.x, -v.y);
    }
	public static Vector2 subtract(Vector2 a, double fb) {
		return new Vector2( a.x - fb, a.y - fb);
	}

	public String ToString()
	{
		return "x=" + x + ",y=" + y;
	}

	public Vector2 RotateIntoSpaceOf(Matrix m)
	{
		Vector2 row0 = new Vector2(m.get(1,1), m.get(1,2));
		Vector2 row1 = new Vector2(m.get(2,1), m.get(2,2));

		return new Vector2( this.Dot(row0), this.Dot(row1) );
	}

	public Vector2 RotateBy(Matrix m)
	{
		Vector2 row0 = new Vector2(m.get(1,1), m.get(1,2));
		Vector2 row1 = new Vector2(m.get(2,1), m.get(2,2));

		return add(multiply(row0,x),multiply(row1,y));
	}
	

}
