package aocutil.geometry;

/**
 * A hexadecimal coordinate
 * 
 * @author Joris
 */
public class HexCoord {
	/** Its x and y coordinate on a 2D grid */
	protected final int x, y;
	
	/**
	 * Creates a new Hex coordinate
	 * 
	 * @param x The initial x coordinate
	 * @param y The initial y coordinate
	 */
	public HexCoord( final int x, final int y ) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Moves the hex coordinate in the specified direction, returns the new hex
	 * coord.
	 * 
	 * @param dir The move direction
	 * @return The new hex coordinate
	 */
	public HexCoord move( final HexDir dir ) {
		final int xinc = dir.getXInc( ); 
		final int yinc = dir.getYInc( );
		
		final int newx = x + xinc;
		final int newy;
		if( xinc == 0 ) newy = y + yinc;
		else newy = y + (yinc == -1 ? -(Math.abs( x ) % 2) : Math.abs(x+1) % 2); 
		
		return new HexCoord( newx, newy );
	}
	
	/**
	 * Computes the distance between this and another hex coordinate
	 * 
	 * @param hex The other hex coordinate
	 * @return The (absolute) distance between the two coordinates
	 */
	public int dist( final HexCoord hex ) {
		int steps = 0; 
		HexCoord c = this;

		// move to same y level
		while( c.y < hex.y ) { c = c.move( c.x == hex.x ? HexDir.S : (c.x > hex.x ? HexDir.SW : HexDir.SE) ); steps++; } 
		while( c.y > hex.y ) { c = c.move( c.x == hex.x ? HexDir.N : (c.x > hex.x ? HexDir.NW : HexDir.NE) ); steps++; }
		
		// returns steps plus remaining x distance
		return steps + Math.abs( hex.x - c.x );
	}
	
	/**
	 * @return The hex coordinate as <x, y> string
	 */
	@Override
	public String toString( ) {
		return "<" + x + "," + y + ">";
	}

	/**
	 * Enum of movement values
	 */
	public enum HexDir {
		NW,N,NE,SW,S,SE;
		
		/** @return The x directional increase/decrease */
		private int getXInc( ) {
			switch( this ) {
				case NW: case SW: return -1;
				case N: case S: return 0;			
				case NE: case SE: return 1;
				default: throw new RuntimeException( "Invalid direction: " + this );
			}
		}
		
		/** 
		 * @return The y directional increase/decrease
		 */
		private int getYInc( ) {
			switch( this ) {
				case NW: case N: case NE:	return -1;				
				case SW: case S: case SE: return 1;
				default: throw new RuntimeException( "Invalid direction: " + this );
			}
		}

	}
}
