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
