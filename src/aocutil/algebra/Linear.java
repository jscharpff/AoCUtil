package aocutil.algebra;

/**
 * Linear function
 * 
 * @author Joris
 */
public class Linear implements AlgebraicFunction {
	/** The slope coefficient */
	private final double a;
	
	/** The y cutoff */
	private final double b;
	
	/**
	 * Creates a new linear function
	 * 
	 * @param a The slope
	 * @param b The y cutoff
	 */
	public Linear( final double a, final double b ) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Computes the value of the function at the given x
	 * 
	 * @param x The x value to compute the function value for
	 * @return The y value
	 */
	@Override
	public double get( double x ) {
		return a * x + b;
	}
	
	/**
	 * Scales the coefficients of the function by the factor
	 * 
	 * @param factor The scale factor
	 * @return A new linear function (a * factor)x + b * factor  
	 */
	@Override
	public Linear scale( double factor ) {
		return new Linear( a * factor, b * factor );
	}
	
	/**
	 * Determines the function that corresponds to the equation this = lf
	 * 
	 * @param lf A linear function to solve for
	 * @return A new function that corresponds to this = lf
	 */
	public Linear equalsTo( final Linear lf ) {
		// for y1 = a1 * x + b1 and y2 = a2 * x + b1 solve y1 = y2 ->
		// a1 * x1 + b1 = a2 * x2 + b2 -> x1 = a2/a1 * x2 + (b2 - b1)/a1
		return new Linear( lf.a / a, (lf.b - b) / a );
	}
	
	/**
	 * Solves the equality this = lf, assuming they are functions of the same
	 * parameter x
	 * 
	 * @param lf The other linear function to equal to
	 * @return The value of x for which this = lf
	 */
	public double solveForX( final Linear lf ) {
		// solve y1 = y2 where both are functions of the same variable x
		// That is: y1 = y2 -> a1 * x + b1 = a2 * x + b2 -> x = (b2 - b1) / (a1 - a2)
		return (lf.b - b) / (a - lf.a);
	}
	
	
	/**
	 * Computes intersection point of this line and another
	 * 
	 * @param lf The other linear function
	 * @return A 2D array that holds the coordinate at which the two functions
	 *   intersect, null if they do not intersect 
	 */
	public double[] intersects( final Linear lf ) {
		final double x = (lf.b - b) / (a - lf.a);
		final double y = get( x );
		
		return new double[] { x, y };
	}
	
	/** @return The string describing the function */
	@Override
	public String toString() {
		return "y=" + (a != 0 ? a + "x + " : "") + b;
	};
}
