package aocutil.collections;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Range implements Comparable<Range> {
	/** The lowest value of the range */
	public final long min;
	
	/** The highest value included in the range */
	public final long max;
	
	/**
	 * Creates a new range
	 * 
	 * @param min The lowest value in the range
	 * @param max The highest value in the range (inclusive)
	 */
	public Range( final long min, final long max ) {
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Checks if the range contains the specified number
	 * 
	 * @param n The number to test
	 * @return True iff lowest <= n <= highest
	 */
	public boolean contains( final long n ) {
		return min <= n && n <= max;
	}

	/**
	 * Compares this range with another to check whether it completely overlaps
	 * the other range
	 * 
	 * @param r2 The other range
	 * @return True if r2 is contained within this range
	 */
	public boolean contains( final Range r2 ) {
		return r2.min >= min && r2.max <= max;
	}
	
	/**
	 * Compares this range against another to check if they share at least a
	 * single common value
	 * 
	 * @param r2 The other range
	 * @return True iff r2 has at least one value spanned by this range
	 */
	public boolean overlaps( final Range r2 ) {
		return r2.min <= max && r2.max >= min;
	}

	/**
	 * Combines this range with another one to create a new range that spans from
	 * the smallest value of both ranges to the biggest value of both. Note that
	 * this function does not test or requires the ranges to be adjacent
	 * 
	 * @param r The range to combine it with
	 * @return The new range as [ min(r1,r2), max(r1,r2) ]
	 */
	public Range combine( final Range r ) {
		return new Range( Math.min( min, r.min ), Math.max( max, r.max ) );
	}
	
	/** @return The size of the range as the values spanned by min and max */
	public long size( ) {
		return Math.abs( max - min ) + 1;
	}

	
	/**
	 * Compares two ranges based upon lowest value in the range
	 * 
	 * @param o The other range to compare against
	 * @return The compare value of lowest - o.lowest
	 */
	@Override
	public int compareTo( final Range o ) {
		return Long.compare( min, o.min );
	}
	
	/** @return The range as a string [l,h] */
	@Override
	public String toString( ) {
		return "[" + min + "," + max + "]";
	}
	
	/**
	 * Parses the range from a string min,max, optionally surrounded by 
	 * whitespace, parentheses are brackets
	 * 
	 * @param input The input string
	 * @return The range
	 */
	public static Range fromString( final String input ) {
		final Matcher m = Pattern.compile( "[\\(\\[\\{]?(-?\\d+)\\s*,\\s*(-?\\d+)\\s*[\\}\\]\\)]?").matcher( input );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid range string: " + input );
		return new Range( Long.parseLong( m.group( 1 ) ), Long.parseLong( m.group( 2 ) ) );
	}
}
