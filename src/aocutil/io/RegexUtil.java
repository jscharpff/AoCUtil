package aocutil.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	/**
	 * Convenient function to perform a simple regex and extract a single group
	 * from the string
	 * 
	 * @param pattern The regex pattern to match
	 * @param input The input string to extract the group from
	 * @return The part of the String that was matched
	 * @throws IllegalArgumentException if the string was not matched or has
	 *   multiple match groups
	 */
	public static String extract( final String pattern, final String input ) {
		final Matcher m = Pattern.compile( pattern ).matcher( input );
		if( !m.find( ) ) throw new IllegalArgumentException( "Pattern failed to match the input string: " + input );
		if( m.groupCount( ) != 1 ) throw new IllegalArgumentException( "Invalid number of groups in matched input: " + m.groupCount( ) );
		
		return m.group( 1 );
	}
	
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
