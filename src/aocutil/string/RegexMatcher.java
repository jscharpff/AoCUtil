package aocutil.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher {
	/** The pattern we are matching on */
	private final Pattern pattern;
	
	/** The current matcher that is being used */
	private Matcher matcher;
	
	/** The input string we are working on */
	private String matchstring;
	
	/**
	 * Creates a new Regex matcher for the given pattern
	 * 
	 * @param pattern The pattern to regex on 
	 */
	public RegexMatcher( final String pattern ) {
		this.pattern = Pattern.compile( pattern );
	}
	
	/**
	 * Creates a new Regex matcher for the given pattern, also immediately
	 * performs matching on the input string
	 * 
	 * @param pattern The pattern to regex on 
	 * @param input The input string to match on
	 * @return The RegexMatcher 
	 * 
	 * @throws IllegalArgumentException if the input does not match the pattern  
	 * 
	 */
	public static RegexMatcher match( final String pattern, final String input ) throws IllegalArgumentException {
		final RegexMatcher rm = new RegexMatcher( pattern );
		rm.match( input );
		return rm;
	}
	
	
	/**
	 * Matches the regex on the specified input string. This function immediately
	 * tests the matcher if it succeeded
	 * 
	 * @param input The input string
	 * 
	 * @throws IllegalArgumentException if the string was not matched
	 */
	public void match( final String input ) throws IllegalArgumentException {
		matchstring = "" + input;
		matcher = pattern.matcher( matchstring );
		if( !matcher.find( ) ) throw new IllegalArgumentException( "Pattern failed to match the input string: " + matchstring );
	}
	
	/**
	 * Checks whether the matcher is initialised and, if required, that the
	 * string actually matches
	 * 
	 * @param checkMatch True to also check for the string to match
	 * 
	 * @throws NullPointerException if the matcher is not initialised
	 */
	private void checkMatcher( final boolean checkMatch ) throws NullPointerException, IllegalArgumentException {
		if( matcher == null ) throw new NullPointerException( "Matcher has not been initialised" );
	}
	
	
	/**
	 * Returns the match given by the given group index
	 * 
	 * @param group The index of the group
	 * @return The match group from the matcher
	 * 
	 * @throws IllegalArgumentException if the string was not matched
	 * @throws IndexOutOfBoundsException if there is no such group number
	 */
	public String get( final int group ) throws IllegalArgumentException, IndexOutOfBoundsException {
		checkMatcher( true );
		
		if( matcher.groupCount( ) < group ) throw new IndexOutOfBoundsException( "Invalid number of groups in matched input: " + matcher.groupCount( ) );
		
		return matcher.group( group );		
	}
	
	/**
	 * Returns the match given by the group index as an integer value
	 * 
	 * @param group The index of the group
	 * @return The match group from the matcher
	 * 
	 * @throws IllegalArgumentException if the string was not matched
	 * @throws NumberFormatException if the group did not describe a number
	 * @throws IndexOutOfBoundsException if there is no such group number
	 */
	public int getInt( final int group ) throws IllegalArgumentException, NumberFormatException, IndexOutOfBoundsException {
		return Integer.parseInt( get( group ) );
	}
	
	/**
	 * Returns the match given by the group index as an long value
	 * 
	 * @param group The index of the group
	 * @return The match group from the matcher
	 * 
	 * @throws IllegalArgumentException if the string was not matched
	 * @throws NumberFormatException if the group did not describe a number
	 * @throws IndexOutOfBoundsException if there is no such group number
	 */
	public long getLong( final int group ) throws IllegalArgumentException, NumberFormatException, IndexOutOfBoundsException {
		return Long.parseLong( get( group ) );
	}
	
	/**
	 * Convenient function to perform a simple regex and extract a single group
	 * from the string
	 * 
	 * @param pattern The regex pattern to match
	 * @param input The input string to extract the group from
	 * @return The part of the String that was matched
	 * 
	 * @throws IllegalArgumentException if the string was not matched
	 * @throws IndexOutOfBoundsException if there is no such group number
	 */
	public static String extract( final String pattern, final String input ) {
		final RegexMatcher rm = RegexMatcher.match( pattern, input );
		return rm.get( 1 );
	}
	
	/**
	 * Uses the matcher to find and return an array of integers for every capture
	 * group in the match
	 * 
	 * @param matcher The matcher that contains the results
	 * @return An array of ints for every capture group in the string
	 * 
	 * @throws IllegalArgumentException if the string was not matched
	 * @throws NumberFormatException if the group did not describe a number
	 * @throws IndexOutOfBoundsException if there is no such group number
	 */
	public int[] getInts( ) {
		final int[] indexes = new int[ matcher.groupCount( ) ];
		for( int i = 0; i < indexes.length; i++ ) indexes[i] = i + 1;
		return getInts( indexes );
	}
	
	/**
	 * Uses the matcher to find and return an array of integers for every capture
	 * group in the match. Returns only groups at the specified indexes
	 * 
	 * @param matcher The matcher that contains the results
	 * @param groupIndexes The indexes of the groups to return, in user specified
	 *   order. Note that group numbers start at 1 as is typical for the matcher
	 * @return An array of ints for every capture group in the string
	 * 
	 * @throws IllegalArgumentException if the string was not matched
	 * @throws NumberFormatException if the group did not describe a number
	 * @throws IndexOutOfBoundsException if there is no such group number
	 */
	public int[] getInts( final int... groupIndexes ) {
		checkMatcher( true );
		
		// build result array
		final int[] result = new int[ groupIndexes.length ];
		for( int i = 0; i < groupIndexes.length; i++  ) result[i] = getInt( groupIndexes[ i ] );
		return result;
	}
}
