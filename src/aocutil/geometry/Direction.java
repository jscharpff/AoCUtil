package aocutil.geometry;

/**
 * Enum of compass directions
 */
public enum Direction {
	/* Do not touch the order of these! */
	North, East, South, West;
	
	/** @return The rotational angle of the direction */
	public int getRotation( ) {
		return ordinal( ) * 90;
	}
	
	/**
	 * Reconstructs the direction from a string value
	 * 
	 *  @param dir The direction name as string (east,left,etc.)
	 *  @return The direction
	 */
	public static Direction fromString( final String dir ) {
		
		switch( dir.toLowerCase( ) ) {
			case "north": case "up": return North;
			case "east": case "right": return East;
			case "south": case "down": return South;
			case "west": case "left": return West;
			
			default:
				throw new IllegalArgumentException( "Invalid direction name: " + dir );
		}
	}	
	/**
	 * Reconstructs the direction from its single-letter representation
	 * 
	 * @param dir The character of the direction (N,E,S,W) or (U,R,D,L)
	 * @return The direction
	 */
	public static Direction fromLetter( final char dir ) {
		switch( dir ) {
			case 'N': case 'U': return North;
			case 'E': case 'R': return East;
			case 'S': case 'D': return South;
			case 'W': case 'L': return West;
			
			default:
				throw new IllegalArgumentException( "Invalid direction letter: " + dir );
		}
	}
	
	/**
	 * Reconstructs the direction from a directional v symbol
	 * 
	 * @param symbol The symbol of the direction (^,>,v,<)
	 * @return The direction
	 */
	public static Direction fromSymbol( final char symbol ) {
		switch( symbol ) {
			case '<': return West;
			case '>': return East;
			case '^': return North;
			case 'v': return South;
			default: throw new IllegalArgumentException( "Invalid direction symbol: " + symbol );
		}
	}
	
	/**
	 * @return The directional symbol
	 */
	public char toSymbol( ) {
		switch( this ) {
			case West: return '<';
			case East: return '>';
			case North: return '^';
			case South: return 'v';
			default: throw new RuntimeException( "Unsupported direction: " + this );
		}
	}
	
	/**
	 * Flips the direction
	 * 
	 * @return The opposite direction
	 */
	public Direction flip( ) {
		return values( )[ (this.ordinal( ) + 2) % 4 ];
	}
	
	/**
	 * Returns the direction that results from turning 90 degrees r times
	 * 
	 * @param r The number of turns to rotate
	 * @return The new direction
	 */
	public Direction turn( int r ) {
		r = (this.ordinal( ) + r) % 4;
		if( r < 0 ) r += 4;
		return values( )[ r ]; 
	}
}