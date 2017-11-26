public class Scalar {
	
	public static double Clamp(double a, double min, double max)
	{
		return Math.min( Math.max(a, min), max );
	}

	public static double AngleFromVector(Vector2 v)
	{
		return Math.atan2(v.y, v.x);
	}
}
