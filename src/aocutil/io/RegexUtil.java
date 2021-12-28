package aocutil.io;

import java.util.regex.Matcher;

public class RegexUtil {
	/**
	 * Uses the matcher to find and return an array of integers for every capture
	 * group in the match
	 * 
	 * @param matcher The matcher that contains the results
	 * @return An array of ints for every capture group in the string
	 * @throws RuntimeException if the pattern was not matched
	 */
	public static int[] readInts( final Matcher matcher ) {
		final int[] indexes = new int[ matcher.groupCount( ) ];
		for( int i = 0; i < indexes.length; i++ ) indexes[i] = i + 1;
		return readInts( matcher, indexes );
	}
	
	/**
	 * Uses the matcher to find and return an array of integers for every capture
	 * group in the match. Returns only groups at the specified indexes
	 * 
	 * @param matcher The matcher that contains the results
	 * @param groupIndexes The indexes of the groups to return, in user specified
	 *   order. Note that group numbers start at 1 as is typical for the matcher
	 * @return An array of ints for every capture group in the string
	 * @throws RuntimeException if the pattern was not matched
	 */
	public static int[] readInts( final Matcher matcher, final int... groupIndexes ) {		
		// build result array
		final int[] result = new int[ groupIndexes.length ];
		for( int i = 0; i < groupIndexes.length; i++  ) result[i] = Integer.parseInt( matcher.group( groupIndexes[ i ] ) );
		return result;
	}
}
