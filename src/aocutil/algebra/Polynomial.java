package aocutil.algebra;

public class Polynomial implements AlgebraicFunction {
	/** The coefficients */
	private final double a, b, c;
	
	/**
	 * Creates a new Polynomial y = ax^2 + bx + c
	 * 
	 * @param a The second degree coefficient
	 * @param b The first degree coefficient
	 * @param c The y cut off
	 */
	public Polynomial( final double a, final double b, final double c ) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	/**
	 * Get y value for the given x
	 * 
	 * @param x The x value
	 * @return The y value
	 */
	@Override
	public double get( final double x ) {
		return a * x * x + b * x + c;
	}
	
	/**
	 * Scales the coefficients of the polynomial by the specified factor
	 * 
	 * @param factor The factor to apply to the coefficients
	 * @return The new polynomial y = f*ax^2 + f*bx + f*c
	 */
	@Override
	public Polynomial scale( final double factor ) {
		return new Polynomial( a * factor, b * factor, c * factor );
	}
	
	/**
	 * Combines this polynomial with another one
	 * 
	 * @param p2 The other polynomial
	 * @param factor The factor to apply to the coefficients
	 * @return The combined polynomial y = (a1+a2)x2 + (b1+b2)x + (c1+c2) 
	 */
	public Polynomial combine( final Polynomial p2 ) {
		return new Polynomial( a + p2.a, b + p2.b, c + p2.c );
	}
	
	/**
	 * Computes points at which y = 0
	 * 
	 * @return The one (in case a = 0) or two x values that result in y = 0
	 */
	public double[] getXIntercepts( ) {
		if( a == 0 && b == 0 ) return new double[] { Double.POSITIVE_INFINITY };		
		if( a == 0 ) return new double[] { c / b };
		
		return new double[] { 
				(-b + Math.sqrt( b * b - 4 * a * c ))/(2 * a), 
				(-b - Math.sqrt( b * b - 4 * a * c ))/(2 * a) 
		};
	}

	/** @return The function as a string */
	@Override
	public String toString( ) {
		return "y = " + a + "x^2 + " + b + "x + " + c;
	}
}
