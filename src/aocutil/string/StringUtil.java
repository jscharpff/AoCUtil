package aocutil.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Common helper functions for easy string manipulation
 * 
 * @author Joris
 */
public class StringUtil {
	/**
	 * Creates a string by repeating the a single character for n times
	 * 
	 * @param c The character to repeat
	 * @param n The number of repetitions
	 * @return The string
	 */
	public static String repeat( final char c, final int n ) {
		if( n < 0 ) throw new IllegalArgumentException( "The number of repetitions cannot be negative" );
		final StringBuilder s = new StringBuilder( );
		for( int i = 0; i < n; i++ ) s.append( c );
		return s.toString( );
	}
	
	/**
	 * Creates a string by repeating the string for n times
	 * 
	 * @param str The string to repeat
	 * @param n The number of repetitions
	 * @return The string
	 */
	public static String repeat( final String str, final int n ) {
		if( n < 0 ) throw new IllegalArgumentException( "The number of repetitions cannot be negative" );
		final StringBuilder s = new StringBuilder( );
		for( int i = 0; i < n; i++ ) s.append( str );
		return s.toString( );
	}
	
	/**
	 * Extracts all repeating character sequences from the string
	 * 
	 * @param input The input string
	 * @param minlength The minimal number of repeated characters to include it
	 * @return List of all repeating sequences
	 */
	public static List<String> getRepeatingSequences( final String input, final int minlength ) {
		return getRepeatingSequences( input, minlength, input.length( ) );
	}
	
	/**
	 * Extracts all repeating character sequences from the string
	 * 
	 * @param input The input string
	 * @param minlength The minimal number of repeated characters to include it
	 * @param maxlength The maximal number of repeated characters to include it
	 * @return List of all repeating sequences
	 */
	public static List<String> getRepeatingSequences( final String input, final int minlength, final int maxlength ) {
		final List<String> seqs = new ArrayList<>( );
		if( input.length( ) < minlength ) return seqs;
		
		// start checking
		int len = 0;
		char prev = (char)(input.charAt( 0 ) - 1); /* not equal to first char */
		for( final char c : input.toCharArray( ) ) {
			if( prev == c ) {
				len++;
			} else {
				if( len >= minlength && len <= maxlength ) seqs.add( repeat( prev, len ) );
				len = 1;
			}
			prev = c;
		}
		
		// check last part
		if( len >= minlength && len <= maxlength ) seqs.add( repeat( prev, len ) );
		return seqs;
	}
	
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
	 * Converts an array of doubles into a comma separated string
	 * 
	 * @param values The values
	 * @return The comma separated string of values
	 */
	public static String fromArray( final double[] values ) {
		if( values.length == 0 ) return "";
		String res = "" + values[0];
		for( int i = 1; i < values.length; i++ ) res += "," + values[i];
		return res;
	}
	
	/**
	 * Converts an NxM matrix into a string
	 * 
	 * @param matrix The matrix containing the values
	 * @param N The number of values in dimension 1
	 * @param M The number of values in dimension 2
	 * @return The string 
	 */
	public static String fromMatrix( final int[][] matrix, final int N, final int M ) {
		final long[][] LM = new long[N][M];
		for( int i = 0; i < N; i++ )
			for( int j = 0; j < M; j++ )
				LM[i][j] = matrix[i][j];
		
		return fromMatrix( LM, N, M );
	}
	
	/**
	 * Converts an NxM matrix into a string
	 * 
	 * @param matrix The matrix containing the values
	 * @param N The number of values in dimension 1
	 * @param M The number of values in dimension 2
	 * @return The string 
	 */
	public static String fromMatrix( final long[][] matrix, final int N, final int M ) {
		// determine column size of
		long maxvalue = -1;
		for( int i = 0; i < N; i++ )
			for( int j = 0; j < M; j++ )
				if( matrix[i][j] > maxvalue ) maxvalue = matrix[i][j];
		int colsize = 1;
		while( maxvalue >= 10 ) {
			maxvalue /= 10;
			colsize++;
		}
		
		// now generate strings
		final StringBuilder str = new StringBuilder( );
		for( int r = 0; r < M; r++ ) {
			for( int c = 0; c < N; c++ ) {
				str.append( padLeft( "" + matrix[c][r], colsize ) );
				if( c < N - 1 ) str.append( "  " );
			}
			if( r < M - 1 ) str.append( "\n" );
		}
		return str.toString( );
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
	 * Sorts all the characters in the string alphabetically
	 * 
	 * @param input The string to sort
	 * @return The alphabetically ordered string
	 */
	public static String sort( final String input ) {
		final char[] chars = input.toCharArray( );
		Arrays.sort( chars );
		return String.valueOf( chars );
	}
	
	/**
	 * Checks if the strings are anagrams, i.e. they have exactly the same
	 * characters but in a (potentially) different order
	 * 
	 * @param s1
	 * @param s2
	 * @return True if s1 is an anagram of s2
	 */
	public static boolean isAnagram( final String s1, final String s2 ) {
		if( s1.length( ) != s2.length( ) ) return false;
		
		return sort( s1 ).equals( sort( s2 ) );		
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
	
	/**
	 * Returns a new string that is of the specified length, padded with spaces
	 * in front
	 * 
	 * @param str The string to pad
	 * @param len The string length
	 * @return The left-padded string
	 */
	public static String padLeft( final String str, final int len ) {
		return padLeft( str, len, ' ' );
	}
	
	/**
	 * Returns a new string that is of the specified length, padded with the 
	 * specified padding character
	 *  
	 * @param str The string to pad
	 * @param len The string length
	 * @param padchar The padding character
	 * @return The left-padded string
	 */
	public static String padLeft( final String str, final int len, final char padchar ) {
		return repeat( padchar, Math.max( 0, len - str.length( ) ) ) + str;
	}	
}
