package aocutil.string;

import java.util.stream.Stream;

/**
 * Common helper functions for easy string manipulation
 * 
 * @author Joris
 */
public class StringUtil {
	
	/**
	 * Parses a comma separated string into a long array
	 * 
	 * @param str The string value
	 * @return An array of lnogs
	 */
	public static long[] toLongArray( final String str ) {
		return Stream.of( str.split( "," ) ).mapToLong( x -> Long.valueOf( x ) ).toArray( );
	}
			
	/**
	 * Parses a comma separated string into an int array
	 * 
	 * @param str The string value
	 * @return An array of integers
	 */
	public static int[] toIntArray( final String str ) {
		return Stream.of( str.split( "," ) ).mapToInt( x -> Integer.valueOf( x ) ).toArray( );
	}
		
	/**
	 * Converts a generic object array to string
	 * 
	 * @param objects The object array
	 * @return The comma separated string containing the elements 
	 */
	public static String fromArray( final Object[] objects ) {
		if( objects.length == 0 ) return "";
		String res = "" + objects[0];
		for( int i = 1; i < objects.length; i++ ) res += "," + objects[i];
		return res;
	}
	
	/**
	 * Converts an array of longs into a comma separated string
	 * 
	 * @param values The long values
	 * @return The comma separated string of values
	 */
	public static String fromArray( final long[] values ) {
		if( values.length == 0 ) return "";
		String res = "" + values[0];
		for( int i = 1; i < values.length; i++ ) res += "," + values[i];
		return res;
	}
	
	/**
	 * Converts an array of ints into a comma separated string
	 * 
	 * @param values The ints values
	 * @return The comma separated string of values
	 */
	public static String fromArray( final int[] values ) {
		if( values.length == 0 ) return "";
		String res = "" + values[0];
		for( int i = 1; i < values.length; i++ ) res += "," + values[i];
		return res;
	}
	
	/**
	 * Reverses a string
	 * 
	 * @param str The string to revers
	 * @return The reversed string
	 */
	public static String reverse( final String str ) {
		 return new StringBuffer( str ).reverse( ).toString( );
	}
	
	/**
	 * Returns the union of both strings
	 * 
	 * @param s1
	 * @param s2
	 * @return The characters that occur in both strings
	 */
	public static String union( final String s1, final String s2 ) {
		String result = "";
		for( int i = 0; i < s1.length( ); i++ )
			if( s2.contains( "" + s1.charAt( i ) ) )
				result += "" + s1.charAt( i );
		
		return result;
	}
	
	
	/**
	 * Returns the string difference of the two strings, i.e. the characters that
	 * are not in both strings
	 * 
	 * @param s1
	 * @param s2
	 * @param bothways True to check the characters of both strings, i.e. return
	 *   the characters of both string that occur in either one but not both (s1
	 *   first), false to only return the characters of s1 that are not in s2
	 * @return The string difference
	 */
	public static String diff( final String s1, final String s2, final boolean bothways ) {
		String result = "";
		for( int i =  0 ; i < s1.length( ); i++ )
			if( !s2.contains( "" + s1.charAt( i ) ) ) result += s1.charAt( i );
		if( bothways )
			for( int i =  0 ; i < s2.length( ); i++ )
				if( !s1.contains( "" + s2.charAt( i ) ) ) result += s2.charAt( i );
		return result;
	}
	
	/**
	 * Returns the string difference of s1 - s2, i.e. all characters in s1 that are not in s2
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String diff( final String s1, final String s2 ) {
		return diff( s1, s2, false );
	}
	
	/**
	 * Checks if the input string contains all characters in the check string,
	 * regardless of order
	 * 
	 * @param input The input string to check
	 * @param check The string to check against the input
	 * @return True iff all characters of the check string are in the input
	 */
	public static boolean containsAll( final String input, final String check ) {
		for( int i = 0; i < check.length( ); i++ )
			if( !input.contains( "" + check.charAt( i ) ) ) return false;
		
		return true;
	}
	
	/**
	 * Counts the number of occurrences of the specified character
	 * 
	 * @param input The string to count within
	 * @param chr The character to count
	 * @return The number of times the character occurs in the string
	 */
	public static int count( final String input, final char chr ) {
		int count = 0;
		for( int i = 0; i < input.length( ); i++ )
			count += input.charAt( i ) == chr ? 1 : 0;
		return count;
	}
}
