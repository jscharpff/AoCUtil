package aocutil.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;

/**
 * Class that captures a of a coordinate based grid
 * 
 * @author Joris
 *
 * @param <T> The data type that of the grid
 */
public class CoordGrid<T> implements Iterable<Coord2D> {	
	/** The map that backs the grid */
	protected final Map<Coord2D, T> map;
	
	/** Current size of the vent grid, given by a minimal and maximal coordinate */
	private Window2D window;
	
	/** Default value for all non-set coordinates */
	protected T defaultValue;

	/**
	 * Creates a new coordinate based grid
	 * 
	 * @param defaultValue The default value for non-assigned coordinates
	 */
	public CoordGrid( final T defaultValue ) {
		map = new HashMap<Coord2D, T>( );
		window = new Window2D( );
		setDefaultValue( defaultValue );
	}
	
	/**
	 * Creates a new coordinate based grid with a fixed size
	 * 
	 * @param width The grid width
	 * @param height The grid height 
	 * @param defaultValue The default value for non-assigned coordinates
	 */
	public CoordGrid( final int width, final int height, final T defaultValue ) {
		map = new HashMap<Coord2D, T>( );
		window = new Window2D( width, height );
		setDefaultValue( defaultValue );
	}
	
	/** @return The current default value for coordinates witout a value */
	public T getDefaultValue( ) {
		return defaultValue;
	}
	
	/**
	 * Sets the default value for grid coordinates that do not have a value
	 * stored.
	 * 
	 * @param newdefault The default value to set
	 */
	public void setDefaultValue( T newdefault ) {
		this.defaultValue = newdefault;
	}
	
	/**
	 * Fixes the window of this coordinate grid to its current size
	 */
	public void fixWindow( ) {
		fixWindow( window );
	}
	
	/**
	 * Fixes the window of this coordinate grid to the specified window
	 * 
	 * @param fixwin The window to use as fixing window
	 */
	public void fixWindow( final Window2D fixwin ) {
		if( fixwin == null || fixwin.empty( ) ) throw new RuntimeException( "Cannot fix an empty window" );
		fixWindow( fixwin.getMinCoord( ), fixwin.getMaxCoord( ) );
	}
	
	/**
	 * Fixes the window of this coordinate grid
	 * 
	 * @param topleft The top left coordinate of the window
	 * @param bottomright The bottom right coordinate of the window
	 */
	public void fixWindow( final Coord2D topleft, final Coord2D bottomright ) {
		window = new Window2D( topleft, bottomright );
	}
	
	/**
	 * Unfixes the window of this coordinate grid, resulting in it again scaling
	 * dynamically with input. 
	 */
	public void unfixWindow( ) {
		// set a new dynamic window and let it recalculate its size
		window = new Window2D( );
		window.resize( getKeys() );
	}
	
	/**
	 * Sets the value of an entire region of coordinates
	 * 
	 * @param region The region as a 2D window
	 * @param value The value to set 
	 */
	public void set( final Window2D region, final T value ) {
		for( final Coord2D c : region ) set( c, value );
	}
	
	/**
	 * Adds a new value to the coordinate grid at the specified coordinate
	 * 
	 * @param coord The coordinate to set the value for
	 * @param value The value to set
	 * @return The previous value that was set, null if it was not set before
	 */
	public T set( final Coord2D coord, final T value ) {
		if( value == null ) throw new NullPointerException( "Value cannot be set to null (use unset)" );
		window.include( coord );
		return map.put( coord, value );
	}
	
	/**
	 * Adds a new value at the specified x and y position. Convenient shorthand
	 * for <code>set( new Coord2D( x, y ) )</code>
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param value The value to set at (x,y)
	 * @return The previous value that was set, null if it was not set before
	 */
	public T set( final int x, final int y, final T value ) {
		return set( new Coord2D( x, y ), value );
	}
	
	/**
	 * Retrieves the value for the given coordinate
	 * 
	 * @param coord The coordinate to get the value for
	 * @return The value that is stored at the coordinate or the grid's default
	 *   value if not found
	 */
	public T get( final Coord2D coord ) {
		return get( coord, defaultValue );
	}
	
	/**
	 * Retrieves the value at the coordinate grid at the given x and y position.
	 * This function offers a more convenient fetch operations when iterating
	 * over coordinates by x and y values.
	 * <br/><br/>
	 * Note that it behaves similar to <code>get( Coord2D )</code> in that it
	 * does not check whether the position is actually on the grid, it just
	 * returns the default value if not found. 
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The value stored at (x,y) or the default value if not set
	 */
	public T get( final int x, final int y ) {
		return get( new Coord2D( x, y ), defaultValue );
	}
	
	/**
	 * Retrieves the value for the given coordinate. Uses specific default value 
	 * if the coordinate was not stored in the map
	 * 
	 * @param coord The coordinate to get the value for
	 * @param valDefault The default value to return if the coordinate has no
	 *   value in the grid, overrides class-level default value
	 * @return The value stored at the coordinate or the default value if no
	 *   value was stored at the specified coordinate
	 */
	public T get( final Coord2D coord, final T valDefault ) {
		return map.getOrDefault( coord, valDefault );
	}
	
	/**
	 * Clears the value for a given coordinate and resizes the grid if necessary
	 * 
	 * @param coord The coordinate to remove from the grid
	 * @return The previous value that was stored at the coordinate, can be null 
	 */
	public T unset( final Coord2D coord ) {
		final T oldvalue = map.remove( coord );
		// does the removal influence the bounds? if yes, update
		if( !window.empty( ) && window.onBorder( coord ) ) window.resize( getKeys( ) );
		return oldvalue;
	}
	
	/**
	 * Clears an entire set of coordinates at once, only recomputes bounds once
	 * 
	 * @param coords The set of coordinates to unset 
	 */
	public void unsetAll( final Collection<Coord2D> coords ) {
		for( final Coord2D c : coords )
			map.remove( c );
		
		window.resize( getKeys( ) );
	}
	
	/**
	 * Retrieves the current value at the coordinate and updates the value using
	 * the specified function
	 * 
	 * @param coord The coordinate to update
	 * @param updatefunc The function to update the value
	 * @return The previous value stored at the coordinate
	 */
	public T update( final Coord2D coord, final Function<T, T> updatefunc ) {
		return set( coord, updatefunc.apply( get( coord ) ) );
	}
	
	/**
	 * Counts the number of coordinates that hold the specified value. If the
	 * value equals the default value, this function will additionally count the
	 * coordinates within the grid bounds that are not in the map.
	 * <br><br>
	 * <b>Note: </b> this function uses the equals function to test whether an
	 * element should be counted.
	 * 
	 * @param value The value to count
	 * @return The number of coordinates that have this value
	 */
	public long count( final T value ) {
		if( value == null ) throw new NullPointerException( "Value to count cannot be null" ); 
		long count = 0;
		
		// count the occurrence in the keys stored in this map
		for( final T v : getValues( ) )
			if( value.equals( v ) ) count++;
		
		// include coordinates within the grid window that have the default value?
		if( value.equals( defaultValue ) ) count += window.count( ) - map.size( );
		
		return count;
	}

	/**
	 * Counts the number of coordinates that meet the condition specified in the
	 * given function. The function will be evaluated for every coordinate in the
	 * grid and the number of truthful evaluations is returned. 
	 * 
	 * @param condition The function that specifies the condition to evaluate
	 * @return The number of coordinates that result in a evaluation of true 
	 */
	public long countIf( final Function<Coord2D, Boolean> condition ) {
		long count = 0;
		for( final Coord2D c : this ) if( condition.apply( c ) == true ) count++;
		return count;
	}

	/** 
	 * @return The collection of <key, value> entries for every key that has a
	 *   set value in the grid
	 */
	public Set<Entry<Coord2D, T>> getEntries( ) {
		return map.entrySet( );
	}
	
	/**
	 * @return The collection of coordinates that have a set value in the grid
	 */
	public Set<Coord2D> getKeys( ) {
		return map.keySet( );
	}
	
	/**
	 * @return The collection of values in the grid
	 */
	public Collection<T> getValues( ) {
		return map.values( );
	}
	
	/**
	 * Checks whether the specified coordinate has a value stored in the grid
	 * 
	 * @param coord The coordinate to check
	 * @return True iff a value is stored at the given coordinate (i.e. not the
	 *   default value)
	 */
	public boolean hasValue( final Coord2D coord ) {
		return map.containsKey( coord );
	}
	
	/**
	 * Finds and returns the coordinates of all grid elements that match the
	 * specified search value
	 * 
	 * @param value The value to find in the grid
	 * @return Collection of coordinates that hold the given value in the grid,
	 *   possibly empty if no such value exists in the grid
	 */
	public Collection<Coord2D> find( final T value ) {
		final Collection<Coord2D> found = new ArrayList<>( );
		for( final Entry<Coord2D, T> e : map.entrySet( ) ) {
			if( e != null && e.getValue( ).equals( value ) )
				found.add( e.getKey( ) );
		}
		return found;
	}
	
	/**
	 * Checks if the given coordinate is within the bounds of the grid. It does
	 * not check whether the coordinate actually holds a value, simply whether it
	 * could be `contained' by the coordinates of this grid. To check whether it
	 * actually stores a value use <code>hasValue( Coord2D )</code>.
	 * 
	 * @param coord The coordinate to test
	 * @return True iff the coordinate's x and y positions are within the bounds
	 *   of this grid
	 */
	public boolean contains( final Coord2D coord ) {
		return window.contains( coord );
	}
	
	/**
	 * Returns the coordinate relative from the top left of the grid. That is,
	 * if the grid spans (a,b)-(c,d) then the relative coordinate (x,y), such that
	 * a <= x <=c and b <= y <= d, will be returned as (x-a, y-b).
	 * 
	 * @param coord The coordinate
	 * @return The relative position of this coordinate in the map
	 */
	public Coord2D getRelative( final Coord2D coord ) {
		return new Coord2D( coord.x - window.getMinX( ), coord.y - window.getMinY( ) );
	}
	
	/**
	 * Retrieves all grid neighbours of the specified coordinate. If the grid
	 * is of fixed size, this function will only return the neighbours that are
	 * within the grid's window.
	 * 
	 * @param coord The coordinate
	 * @param diagonal True to include diagonal neighbours
	 * @return All neighbours within the grid of the given coordinate.
	 * @throw {@link IllegalArgumentException} if the coordinate itself it
	 *   outside the grid
	 */
	public Set<Coord2D> getNeighbours( final Coord2D coord, final boolean diagonal ) {
		return getNeighbours( coord, diagonal, null );
	}
	
	/**
	 * Retrieves all grid neighbours of the specified coordinate. If the grid
	 * is of fixed size, this function will only return the neighbours that are
	 * within the grid's window.
	 * 
	 * @param coord The coordinate
	 * @param diagonal True to include diagonal neighbours
	 * @param validneighbour Validation function that includes/excludes
	 *   neighbouring coords based upon their value, a result of true will
	 *   include the coordinate
	 * @return All neighbours within the grid of the given coordinate.
	 * @throw {@link IllegalArgumentException} if the coordinate itself it
	 *   outside the grid
	 */
	public Set<Coord2D> getNeighbours( final Coord2D coord, final boolean diagonal, final Function<Coord2D, Boolean> validneighbour ) {
		if( !contains( coord ) ) throw new IllegalArgumentException( "The coordinate is not within the grid: " + coord );
		
		final Set<Coord2D> neighbours = new HashSet<>( );
		for( final Coord2D n : coord.getAdjacent( diagonal ) ) {
			if( window.isFixed( ) && !contains( n ) ) continue;
			if( validneighbour != null && !validneighbour.apply( n ) ) continue;
			
			neighbours.add( n );
		}
		
		return neighbours;
	}

	/**
	 * Rotates all the coordinates in the grid in clockwise direction. Uses the
	 * current window size to determine new coordinates
	 * 
	 * @param rotations The number of 90 degree rotations
	 * @return The rotated grid
	 */
	public CoordGrid<T> rotate( final int rotations ) {
		final int r = rotations % 4;
		if( r == 0 ) return copy( );
		
		final CoordGrid<T> grid = copyEmpty( );
		
		for( final Coord2D c : this.getKeys( ) ) {
			final T value = get( c );
			
			if( r == 1 ) grid.set( new Coord2D( size( ).y - 1 - c.y, c.x ), value );
			else if( r == 2 ) grid.set( new Coord2D( size( ).x - 1 - c.x, size( ).y - 1 - c.y ), value );
			else grid.set( new Coord2D( c.y, size( ).x - 1 - c.x ), value );
		}
		return grid;
	}
	
	/**
	 * Flips the coordinates in the grid
	 * 
	 * @param horizontal True for horizontal flip, false for vertical
	 * @return The flipped grid
	 */
	public CoordGrid<T> flip( final boolean horizontal ) {
		final CoordGrid<T> grid = copyEmpty( );
		for( final Coord2D c : this.getKeys( ) ) {
			final T value = get( c );
			
			if( horizontal ) grid.set( new Coord2D( size( ).x - 1 - c.x, c.y ), value );
			else grid.set( new Coord2D( c.x, size( ).y - 1 - c.y ), value );

		}
		return grid;
	}
	
	/** 
	 * Determines and returns the size of the current grid, i.e. the span between
	 * minimal and maximal coordinate
	 * 
	 * @return The size of the current grid as 2D coordinate or null if empty
	 */
	public Coord2D size( ) {
		return window.size( );
	}
	
	/** @return The current window spanned by this grid */
	public Window2D window( ) {
		return window;
	}
	
	/**
	 * Creates an Iterator that goes over all coordinates in the grid, including
	 * those without a value
	 * 
	 * @return An iterator of coordinates that goes over all coordinates in the
	 * grid span, from (minX,minY) to (maxX, maxY). Iteration is performed
	 * horizontally, i.e. increasing x until end of row.
	 */
	@Override
	public Iterator<Coord2D> iterator( ) {
		return window.iterator( );
	}
	
	/**
	 * Returns a stream of the coordinates in this map, based upon the iterator.
	 * That is, the coordinates are returned in order of their column first and
	 * then their row number. This function will return a parallel stream by
	 * default.  
	 * 
	 * @return The stream of coordinates in this grid
	 */
	public Stream<Coord2D> stream( ) {
		return stream( true );
	}
	
	/**
	 * Returns a stream of the coordinates in this map, based upon the iterator.
	 * That is, the coordinates are returned in order of their column first and
	 * then their row number.  
	 * 
	 * @param parallel True to allow parallel streaming of the coordinates in
	 *   the grid
	 * @return The stream of coordinates in this grid
	 */
	public Stream<Coord2D> stream( final boolean parallel ) {
		return StreamSupport.stream( 
				Spliterators.spliterator( iterator( ), map.size( ), Spliterator.DISTINCT |Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.SUBSIZED )
				, parallel );
	}
	
	/**
	 * Constructs a Coordinate grid from a grid, represented by a list of Strings
	 * for each row. Columns are separated by the given separator, which may be
	 * empty for character-based grids that hold a single value per char.  
	 * 
	 * @param <U> The type of the data elements to be contained
	 * @param input The list of strings that describe a n x m grid, one string
	 *   per row
	 * @param separator The column separator as regex, "" or null for simple,
	 *   single character columns
	 * @param mapfunc The function to map each character into a value
	 * @param defaultValue The default value to set for the empty entries
	 * @return The CoordGrid that is constructed from the list of strings
	 */
	public static <U> CoordGrid<U> fromStringList( final List<String> input, final String separator, final Function<String, U> mapfunc, final U defaultValue ) {
		final CoordGrid<U> grid = new CoordGrid<>( defaultValue );
		
		// fill in blank separator iff it is null
		final String sep = separator != null ? separator : ""; 
		
		// parse rows and columns
		int y = -1;
		for( final String row : input ) {
			y++;
			int x = -1;
			for( final String col : row.split( sep ) ) {
				x++;
				final U value = mapfunc.apply( col );
				if( defaultValue != null && !defaultValue.equals( value ) )
					grid.set( new Coord2D( x, y ), value );
			}
		}

		return grid;
	}
	
	/**
	 * Shorthand function to construct an Integer-valued CoordGrid from a char
	 * grid. Can only hold integer values 0-9 (as the input is a char grid) in
	 * parsed elements, defaultValue of non-set coordinates will be -1.
	 * 
	 * @param input The list of row strings that describe the digits on that row 
	 * @return An digit-based CoordGrid with the values as read from the input
	 *   and defaultValue -1 for non-contained coordinates
	 */
	public static CoordGrid<Integer> fromDigitGrid( final List<String> input ) {
		return fromStringList( input, null, Integer::parseInt, -1 );
	}
	
	/**
	 * Shorthand function to construct a boolean grid
	 * 
	 * @param input The list of strings that contains a single-char grid
	 * @param truechar The character that represents a true value
	 * @return The boolean valued CoordGrid
	 */
	public static CoordGrid<Boolean> fromBooleanGrid( final List<String> input, final char truechar ) {
		return fromStringList( input, null, x -> x.equals( "" + truechar ), false );
	}
	
	/**
	 * Shorthand function to construct a single-character grid
	 * 
	 * @param input The list of strings that contains a single-char grid
	 * @param defaultchar The default value for non-set columns
	 * @return The character based valued CoordGrid
	 */
	public static CoordGrid<Character> fromCharGrid( final List<String> input, final char defaultchar ) {
		return fromStringList( input, null, x -> x.charAt( 0 ), defaultchar );
	}
	

	/**
	 * Generates a grid-like output of the CoordGrid, from top-left coordinate to
	 * the bottom-right using the object's own toString function 
	 * 
	 * @return A grid of values
	 */
	@Override
	public String toString( ) {
		return toString( T::toString );
	}
	
	/**
	 * Generates a grid-like output of the CoordGrid, from top-left coordinate to
	 * the bottom-right using a custom Stringify function  
	 * 
	 * @param stringFunc The function to Stringify elements
	 * @return A grid of 
	 */
	public String toString( final Function<T, String> stringFunc ) {
		return toString( stringFunc, null );
	}
	
	/**
	 * Generates a grid-like output of the CoordGrid, from top-left coordinate to
	 * the bottom-right using a custom Stringify function  
	 * 
	 * @param stringFunc The function to Stringify elements
	 * @param special A map of string for specific coordinates, to overrule parts
	 *   of the grid  
	 * @return A grid of 
	 */
	public String toString( final Function<T, String> stringFunc, Map<Coord2D, String> special ) {
		// use empty map as dummy if not supplied 
		if( special == null ) special = new HashMap<>( ); 
			
		final StringBuilder res = new StringBuilder( );
		Coord2D prev = null;
		for( final Coord2D c : this ) {
			// new line after every row end
			if( prev != null && prev.y != c.y ) res.append( '\n' );
			try {
				res.append( special.getOrDefault( c , stringFunc.apply( get( c ) ) ) );
			} catch( final NullPointerException e ) {
				res.append( 'N' );
			}
			prev = c;
		}
		
		// return result minus the last newline
		return res.toString( );
	}

	
	/**
	 * Copies the CoordGrid and all the values it contains. Produces a deep copy
	 * of all coordinates
	 * 
	 * @return The copy of the grid
	 */
	public CoordGrid<T> copy( ) {
		// simply "extract" the entire grid 
		return extract( window.getMinCoord( ), window.getMaxCoord( ) );
	}
	
	/**
	 * Copies the grid with its settings but not its values
	 * 
	 * @return The empty copy of the grid
	 */
	private CoordGrid<T> copyEmpty( ) {
		final CoordGrid<T> copy = new CoordGrid<>( defaultValue );
		
		// set size of the copy by "fixing it", then optionally unfix it again
		copy.fixWindow( window );
		if( !window.isFixed( ) ) copy.unfixWindow( );
		
		return copy;
	}
	
	/**
	 * Extracts the specified region from the CoordGrid and returns it as a new
	 * grid. Note that the coordinates are both inclusive
	 * 
	 * @param topleft The top left coordinate of the area to extract
	 * @param bottomright The bottom right coordinate
	 * @return The area from top left to bottom right as a new grid
	 */
	public CoordGrid<T> extract( final Coord2D topleft, final Coord2D bottomright ) {
		return extract( topleft, bottomright, false );
	}
	
	/**
	 * Extracts the specified region from the CoordGrid and returns it as a new
	 * grid. Note that the coordinates are both inclusive
	 * 
	 * @param topleft The top left coordinate of the area to extract
	 * @param bottomright The bottom right coordinate
	 * @param relative True to return the extracted part with keys relative to
	 *   the topleft cooridnate of the window 
	 * @return The area from top left to bottom right as a new grid
	 */
	public CoordGrid<T> extract( final Coord2D topleft, final Coord2D bottomright, boolean relative ) {
		final CoordGrid<T> ex = new CoordGrid<>( defaultValue );
		final Window2D w = new Window2D( topleft, bottomright );
		
		// fix the window of the part if the parent is also fixed
		if( window.isFixed( ) ) ex.fixWindow( relative ? new Window2D( w.size( ).x, w.size( ).y ) : w );
		
		// include only those coordinates that are within the window
		for( final Coord2D c : getKeys( ) ) {
			if( !w.contains( c ) ) continue; 
			
			ex.set( relative ? topleft.diff( c ) : c, get( c ) );
		}
		
		return ex;
	}
	
	/**
	 * Inserts the values of the coordgrid into this one, using the offset 
	 * coordinate to compute the new coordinate positions of the values. That is,
	 * the values of the grid will be inserted at (offset.x + key.x, offset.y +
	 * key.y). Existing keys within the range are removed before insert.
	 * 
	 * Furthermore, the coordinates in the specified grid will be treated as
	 * relative to the top left corner of the grid so that the values are
	 * inserted in the range (offset.x, offset.y) - 
	 * (grid.size().x, grid.size().y].
	 * 
	 * @param offset The offset coordinate
	 * @param grid The grid data to insert
	 */
	public void insert( final Coord2D offset, final CoordGrid<T> grid ) {
		for( final Coord2D coord : grid ) {
			final Coord2D c = grid.getRelative( coord ).move( offset );
			unset( c );
			if( grid.hasValue( coord ) ) set( c, grid.get( coord ) ); 
		}
	}
	
	/**
	 * Inserts a new, empty column at the specified index, moving all existing
	 * keys after the column by one.
	 * 
	 * @param colidx The column index
	 * @param colsize The width of the column to insert
	 */
	public void insertColumns( final int colidx, final int colsize ) {
		final Set<Coord2D> tomove = new HashSet<>( getKeys( ) );
		tomove.removeIf( c -> c.x < colidx );
		
		for( final Coord2D c : tomove ) set( c.move( colsize, 0 ), unset( c ) );
	}
	
	/**
	 * Inserts a new, empty row at the specified index, moving all existing
	 * keys after the row by one.
	 * 
	 * @param rowidx The row index
	 * @param rowsize The height of the row to insert
	 */
	public void insertRows( final int rowidx, final int rowsize ) {
		final Set<Coord2D> tomove = new HashSet<>( getKeys( ) );
		tomove.removeIf( c -> c.y < rowidx );
		
		for( final Coord2D c : tomove ) set( c.move( 0, rowsize ), unset( c ) );
	}
}
