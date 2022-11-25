package aocutil.algorithm;

import java.util.function.Function;

/**
 * Implementation of the binary search algorithm
 * 
 * @author Joris
 */
public class BinarySearch {

	/**
	 * Simple binary search algorithm that halves the search space in each
	 * iteration until the min and max values agree 
	 * 
	 * @param min The lower bound on the search range
	 * @param max The upper bound on the search range
	 * @param testfunc A function that returns true iff the search criteria are
	 *   met, false otherwise
	 * @return The lowest value in the search range that returns true
	 */
	public static long findFirst( long min, long max, final Function<Long, Boolean> testfunc ) {
		while( min != max ) {
			final long half = (max + min) / 2;
			final boolean result = testfunc.apply( half );
			if( result ) { max = half; } else { min = half + 1; }			
		}
		return min;
	}
}
