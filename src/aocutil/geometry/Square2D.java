package aocutil.geometry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class that represents a 2D square
 * 
 * @author Joris
 */
public class Square2D implements Iterable<Coord2D> {
	/** The coordinates that represent the square */
	private final Coord2D topleft, bottomright;
	
	/**
	 * Creates a new square
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public Square2D( final int x1, final int y1, final int x2, final int y2 ) {
		topleft = new Coord2D( Math.min( x1, x2 ), Math.min( y1, y2 ) );
		bottomright = new Coord2D( Math.max( x1, x2 ), Math.max( y1, y2 ) );
	}
	
	/** @return The minimal x value */
	public int getMinX( ) { return topleft.x; }

	/** @return The maximal x value */
	public int getMaxX( ) { return bottomright.x; }

	/** @return The minimal y value */
	public int getMinY( ) { return topleft.y; }

	/** @return The maximal y value */
	public int getMaxY( ) { return bottomright.y; }

	
	/** @return The area of the square */
	public int area( ) {
		final Coord2D diff = bottomright.diffAbs( topleft );
		return (diff.x + 1) * (diff.y + 1);
	}
	
	/**
	 * Tests whether the given coordinate is contained by this square, i.e. its
	 * coordinates are somewhere within the area of the square.
	 * 
	 * @param coord The coordinate to test
	 * @return True iff x in [xmin, xmax] and y in [ymin, ymax]
	 */
	public boolean contains( final Coord3D coord ) {
		return coord.x >= topleft.x && coord.y >= topleft.y && 
				coord.x <= bottomright.x && coord.y <= bottomright.y;
	}
	
	/**
	 * Checks if the square overlaps this one at least somewhere
	 * 
	 * @param square The square to test
	 * @return True iff the square overlaps this one somewhere in its area 
	 */
	public boolean overlaps( final Square2D square ) {
		if( square.getMaxX( ) < getMinX( ) || square.getMinX( ) > getMaxX( ) ) return false;
		if( square.getMaxY( ) < getMinY( ) || square.getMinY( ) > getMaxY( ) ) return false;
		
		return true;
	}
	
	/**
	 * Determine the square that represents the overlapping area between this
	 * and another square
	 * 
	 * @param square The other square
	 * @return The overlapping square, is null in case of no overlap
	 */
	public Square2D getOverlap( final Square2D square ) {
		// perform a quick test whether they overlap at all
		if( !overlaps( square ) ) return null;
		
		// they overlap somewhere, determine the area that is in both squares
		return new Square2D(
				Math.max( getMinX(), square.getMinX( ) ), Math.min( getMaxX(), square.getMaxX( ) ),
				Math.max( getMinY(), square.getMinY( ) ), Math.min( getMaxY(), square.getMaxY( ) )
		);
	}
	
	/**
	 * Subtracts the specified square from this one, resulting in a set that
	 * contains 0, 1 or more new squares depending on the overlapping area.
	 * 
	 * @param square The square to subtract from this one
	 * @return Set of resulting squares. May be empty if the other square
	 *   completely overlaps this one 
	 */
	public Set<Square2D> subtract( final Square2D square ) {
		final Set<Square2D> result = new HashSet<>( );
		
		// if no overlap, subtract has no effect
		if( !overlaps( square ) ) {
			result.add( this );
			return result;
		}
		
		// they do overlap, remove the other cube!
		// first cut in x range before and after the cube and store remaining part
		if( getMinX( ) < square.getMinX( ) ) result.add( new Square2D( getMinX( ), square.getMinX( ) - 1, getMinY( ), getMaxY( ) ) );
		if( getMaxX( ) > square.getMaxX( ) ) result.add( new Square2D( square.getMaxX( ) + 1, getMaxX( ), getMinY( ), getMaxY( ) ) );		
		final int xmin = Math.max( getMinX( ), square.getMinX( ) );
		final int xmax = Math.min( getMaxX( ), square.getMaxX( ) );
		 
		// use the remaining x range to cut in the y range and again keep remaining y range
		if( getMinY( ) < square.getMinY( ) ) result.add( new Square2D( xmin, xmax, getMinY( ), square.getMinY( ) - 1 ) );
		if( getMaxY( ) > square.getMaxY( ) ) result.add( new Square2D( xmin, xmax, square.getMaxY( ) + 1, getMaxY( ) ) );

		return result;		
	}

	/**
	 * Check if this square's coordinates equal that of another square
	 * 
	 * @param obj The other object
	 * @return True iff the other object is a Square2D and has the same 
	 *   top left and bottom right cooridnates
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Square2D) ) return false;
		final Square2D s = (Square2D)obj;
		return topleft.equals( s.topleft ) && bottomright.equals( s.bottomright );
	}
	
	/** @return The hash code of the toString method */
	@Override
	public int hashCode( ) {
		return toString( ).hashCode( );
	}
	
	/**
	 * @return The square as a string (x1,y1)-(x2,y2)
	 */
	public String toString( ) {
		return topleft.toString( ) + "-" + bottomright.toString( );
	}
	
	/**
	 * Creates an Iterator that goes over all coordinates in the window
	 * 
	 * @return An iterator of coordinates that goes over all coordinates in the
	 * window span, from (minX,minY) to (maxX, maxY). Iteration is performed
	 * horizontally, i.e. increasing x until end of row.
	 */
	@Override
	public Iterator<Coord2D> iterator( ) {
		return new Iterator<Coord2D>( ) {
			/** Current coordinate */
			protected Coord2D curr = topleft.move( -1, 0 );
			
			/** Start coordinate */
			protected final Coord2D start = topleft;
			
			/** End coordinate */
			protected final Coord2D end = bottomright;
			
			@Override
			public Coord2D next( ) {
				if( curr.x < end.x ) curr = curr.move( 1, 0 );
				else curr = new Coord2D( start.x, curr.y + 1 );

				return curr;
			}
			
			@Override
			public boolean hasNext( ) {
				return !end.equals( curr );
			}
		};
	}	
}
