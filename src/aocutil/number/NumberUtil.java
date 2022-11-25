package aocutil.number;

/**
 * Generic utility functions for number manipulation
 * 
 * @author Joris
 */
public class NumberUtil {
	
	/**
	 * Converts a long value into an array of its digits
	 * 
	 * @param value The value to convert
	 * @return Array of digits 
	 */
	public static int[] toDigits( final long value ) {
		return toDigits( "" + value );
	}
	
	/**
	 * Converts a string of digits into an array of ints
	 * 
	 * @param value The string value
	 * @return The array of digits
	 */
	public static int[] toDigits( final String value ) {
		final int[] result = new int[ value.length( ) ];
		for( int i = 0; i < value.length( ); i++ ) result[i] = (int)(value.charAt( i ) - '0');
		return result;
	}
	
	/**
	 * Returns the value that is described by the digits of the array
	 * 
	 * @param array The array of digits
	 * @return The long value represented by the array
	 */
	public static long fromDigits( final int[] array ) {
		return fromDigits( array, 0, array.length );
	}
	
	/**
	 * Returns the value that is described by the first n digits of the array
	 * 
	 * @param array The array of digits
	 * @param length The number of digits to consume from the array
	 * @return The long value represented by the first n digits in the array, or
	 *   the remainder of the array if there are not sufficient digits available
	 */
	public static long fromDigits( final int[] array, final int length ) {
		return fromDigits( array, 0, length );
	}

	/**
	 * Returns the value that is described by the n digits of the array, starting
	 * from the index specified by offset. Note: this function does not check for
	 * overflow.
	 * 
	 * @param array The array of digits
	 * @param offset The start offset
	 * @param length The number of digits to consume from the array
	 * @return The long value represented by the n digits from the given offset,
	 *   or the remainder of the array if there are not sufficient digits
	 *   available
	 */
	public static long fromDigits( final int[] array, final int offset, final int length ) {
		long val = 0;
		for( int i = offset; i < offset + length && i < array.length - 1; i++ ) {
			val *= 10;
			val += array[i];
		}
		return val;
	}
	
	
	/**
	 * Finds the lowest common multiplier of the specified integers
	 * 
	 * @param a
	 * @param b
	 * @return The lowest common multiplier
	 */
	public static long lowestCommonMultiplier( final long a, final long b ) {
		// special case
		if( a == 0 && b == 0 ) return 0;
		
		return Math.abs( a * b ) / greatestCommonDivisor( a, b );
	}
	
	/**
	 * Finds the greatest common integer divisor of the specified integers
	 * 
	 * @param a
	 * @param b
	 * @return The greatest common integer divisor
	 */
	public static long greatestCommonDivisor( final long a, final long b ) {
		return gcd( Math.min( a, b ), Math.max( a, b ) );
	}
	
	/**
	 * Recursive function to determine greatest common divisor. This is an
	 * implementation of the Euclidean algorithm
	 * 
	 * @param a The largest of the two integers
	 * @param b The smallest of the two integer
	 * @return The greatest common divisor
	 */
	private static long gcd( final long a, final long b ) {
		if( b == 0 ) return a;
		return gcd( b, a % b );
	}
}
