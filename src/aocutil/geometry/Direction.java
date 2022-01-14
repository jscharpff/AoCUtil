package aocutil.geometry;

/**
 * Enum of compass directions
 */
public enum Direction {
	/* Do not touch the order of these! */
	North, East, South, West;
	
	/** @return The rotation of the direction */
	public int getRotation( ) {
		return ordinal( ) * 90;
	}
	
	/**
	 * Reconstructs the direction from its single-letter representation
	 * 
	 * @param dir The character of the direction (N,E,S,W)
	 * @return The direction
	 */
	public static Direction fromLetter( final char dir ) {
		for( final Direction d : values( ) )
			if( d.toString( ).substring( 0, 1 ).equals( ("" + dir).toUpperCase( ) ) ) return d;
		
		throw new IllegalArgumentException( "Invalid direction letter: " + dir );
	}
	
	/**
	 * Returns the direction that results from turning 90 degrees r times
	 * 
	 * @param r The number of turns to rotate
	 * @return The new direction
	 */
	public Direction turn( final int r ) {
		return values( )[ (this.ordinal( ) + r) % 4 ]; 
	}
}