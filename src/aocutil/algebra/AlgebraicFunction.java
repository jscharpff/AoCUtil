package aocutil.algebra;

/**
 * Abstract function for all mathematical functions
 * 
 * @author Joris
 */
public interface AlgebraicFunction {
	/**
	 * Get y value for the given x
	 * 
	 * @param x The x value
	 * @return The y value
	 */
	public double get( final double x );
	
	/**
	 * Scales the coefficients of the function by the specified factor
	 * 
	 * @param factor The factor to apply to the function's coefficient
	 * @return The scaed function
	 */
	public AlgebraicFunction scale( final double factor );


	/** @return The function as a string */
	public String toString( );
	
}
