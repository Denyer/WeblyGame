import java.util.ArrayList;

public class Matrix {
	
	private ArrayList<Double> values;
	private int rows;
	private int elementsPerRow;
	
	public Matrix(ArrayList<Double> vals, int[] dimension)
	{
		values = vals;
		rows = dimension[0];
		elementsPerRow = vals.size()/rows;
	}
	
	public double get(int i, int j)
	{
		
		int index = (i-1)*elementsPerRow + j;
		return values.get(index-1);
	}
	
	public static Matrix TransformMatrix(double angle)
	{
		ArrayList<Double> points = new ArrayList<Double>();
		points.add(Math.cos(Math.toRadians(angle)));
		points.add(Math.sin(Math.toRadians(angle)));
		points.add(-Math.sin(Math.toRadians(angle)));
		points.add(Math.cos(Math.toRadians(angle)));
		int[] dimensions = {2,4};
		return new Matrix(points, dimensions);
	}
}
