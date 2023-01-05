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
	 * Finds the positive modulus of integers a and b
	 * 
	 * @param a
	 * @param b
	 * @return The remainder of dividing a by b such that 0 <= a < b
	 */
	public static long mod( final long a, final long b ) {
		return ((a % b) + b) % b;
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
		
		return Long.divideUnsigned( a * b, greatestCommonDivisor( a, b ) );
	}
	
	/**
	 * Finds the greatest common integer divisor of the specified integers
	 * 
	 * @param a
	 * @param b
	 * @return The greatest common integer divisor
	 */
	public static long greatestCommonDivisor( final long a, final long b ) {
		
		return extendedEuclidean( Long.compareUnsigned( a, b ) < 0 ? a : b, Long.compareUnsigned( a, b ) < 0 ? b : a )[0];
	}
	
	/**
	 * Extended version of the Euclidean algorithm that finds the greatest common
	 * divisor of numbers a and b, while also returning the Bézout coefficients
	 * 
	 * @see https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
	 * 
	 * @param a
	 * @param b
	 * @return An array of longs [gcd(a,b), s, t] such that gcd contains the
	 *   greatest common divisor of a and b, while s and t are the Bézout
	 *   coefficients such that a*s + b*t = 1 
	 */
	public static long[] extendedEuclidean( final long a, final long b ) {
		long old_r = a; long r = b;
		long old_s = 1; long s = 0;
		
		while( r != 0 ) {
			final long q = Long.divideUnsigned( old_r, r );
			final long temp_r = r;
			r = old_r - q * r;
			old_r = temp_r;
			final long temp_s = s;
			s = old_s - q * s;
			old_s = temp_s;
		}
		
		final long t = b == 0 ? 0 : Long.divideUnsigned( old_r - old_s * a, b );
		return new long[] { old_r, old_s, t };
	}
}
