package aocutil.collections;

public class Range implements Comparable<Range> {
	/** The lowest value of the range */
	public final long lowest;
	
	/** The highest value included in the range */
	public final long highest;
	
	/**
	 * Creates a new range
	 * 
	 * @param low The lowest value in the range
	 * @param high The highest value in the range (inclusive)
	 */
	public Range( final long low, final long high ) {
		this.lowest = low;
		this.highest = high;
	}
	
	/**
	 * Checks if the range contains the specified number
	 * 
	 * @param n The number to test
	 * @return True iff lowest <= n <= highest
	 */
	public boolean contains( final long n ) {
		return lowest <= n && n <= highest;
	}
	
	/**
	 * Compares two ranges based upon lowest value in the range
	 * 
	 * @param o The other range to compare against
	 * @return The compare value of lowest - o.lowest
	 */
	@Override
	public int compareTo( final Range o ) {
		return Long.compare( lowest, o.lowest );
	}
	
	/** @return The range as a string [l,h] */
	@Override
	public String toString( ) {
		return "[" + lowest + "," + highest + "]";
	}
}
