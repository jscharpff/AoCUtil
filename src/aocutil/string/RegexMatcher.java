package aocutil.string;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher {
	/** The pattern we are matching on */
	private final Pattern pattern;
	
	/** The current matcher that is being used */
	private Matcher matcher;
	
	/** The input string we are working on */
	private String matchstring;
	
	/** True if the last matching resulted in a successful match */
	protected boolean matched;
	
	/**
	 * Creates a new Regex matcher for the given pattern
	 * 
	 * @param pattern The pattern to regex on 
	 */
	public RegexMatcher( final String pattern ) {
		this.pattern = Pattern.compile( toRegex( pattern ) );
		matched = false;
	}
	
	/**
	 * Creates a new Regex matcher for the given pattern, also immediately
	 * performs matching on the input string
	 * 
	 * @param pattern The pattern to regex on, allows regex+ strings
	 * @param input The input string to match on
	 * @return The RegexMatcher 
	 * 
	 * @throws IllegalArgumentException if the input does not match the pattern  
	 */
	public static RegexMatcher match( final String pattern, final String input ) throws IllegalArgumentException {
		final RegexMatcher rm = new RegexMatcher( toRegex( pattern ) );
		rm.match( input );
		return rm;
	}
	
	/**
	 * Checks if the input string can be matched by the current matcher without
	 * performing the actual matching
	 * 
	 * @param input The input string
	 * @return True if the pattern is found in the input string
	 */
	public boolean matches( final String input ) {
		return pattern.matcher( input ).matches( );
	}
	
	/**
	 * Matches the regex on the specified input string. This function immediately
	 * tests the matcher if it succeeded
	 * 
	 * @param input The input string
	 * @return True if the string was matched
	 */
	public boolean match( final String input ) {
		matchstring = "" + input;
		matcher = pattern.matcher( matchstring );
		matched = matcher.find( );
		return matched;
	}
	
	/**
	 * Returns a list of all matches of the input string
	 * 
	 * @param input The input string to match
	 * @return A list that contains a single match result for every match in
	 *   the input string
	 */
	public List<MatchResult> matchAll( final String input ) {
		matchstring = "" + input;
		matcher = pattern.matcher( matchstring );
		
		return new ArrayList<>( matcher.results( ).toList( ) );
	}
	
	/**
	 * Returns a list of objects of type t that are produced by applying the
	 * given mapping function to each MatchResult in the input string
	 * 
	 * @param input The input string to match
	 * @param mapfunc The mapping function to apply against each result
	 * @return A list of type T that contains the mapped objects
	 */
	public <T> List<T> matchAll( final String input, final Function<MatchResult, T> mapfunc ) {
		matchstring = "" + input;
		matcher = pattern.matcher( matchstring );
		
		return new ArrayList<>( matcher.results( ).map( mapfunc ).toList( ) );
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
		if( !matched ) throw new IllegalArgumentException( "Pattern "+ pattern + " failed to match the input string: " + matchstring );
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
	 * Returns the match given by the group index as a character value
	 * 
	 * @param group The index of the group
	 * @return The match group from the matcher
	 * 
	 * @throws IllegalArgumentException if the string was not matched or the
	 *   match returned multiple characters 
	 * @throws IndexOutOfBoundsException if there is no such group number
	 */
	public char getChar( final int group ) throws IllegalArgumentException, IndexOutOfBoundsException {
		final String m = get( group );
		if( m.length( ) != 1 ) throw new IllegalArgumentException( "The match group does not contain a single character: " + m );
		return m.charAt( 0 );
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
	
	/**
	 * Creates a pattern string from the specified regex+ string. Regex+ offers
	 * the following shorthands:
	 * 
	 * #D  : A number value "(-?\\d+)"
	 * #Ds : A number value optionally surrounded by spaces "(-?\\d+)"
	 * 
	 * @param regex The regex+ string
	 * @return The Java-compatible regex pattern string
	 */
	public static String toRegex( final String regex ) {
		String result = regex;
		result = result.replaceAll( "#Ds", "\\\\s*(-?\\\\d+)\\\\s*" );
		result = result.replaceAll( "#D", "(-?\\\\d+)" );
		return result;
	}
	
	/** @return The current pattern that is matched and the matching state */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( "[pattern=" );
		sb.append( pattern );
		sb.append( ", matched=" );
		sb.append( matched );
		sb.append( "]" );
		return sb.toString( );
	}
}
