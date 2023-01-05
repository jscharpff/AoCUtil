package aocutil.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import aocutil.object.LabeledObject;

/**
 * Implementation of a matrix structure with String indexes
 * 
 * @author Joris
 * @param <K> The type of the key elements
 * @param <V> The type of the data elements contained in the matrix
 */
public class LabelMatrix<K extends LabeledObject, V> {
	/** The actual matrix data */
	private final Map<K, Map<K, V>> data;
	
	/**
	 * Creates a new labelled matrix
	 */ 
	public LabelMatrix( ) {
		data = new HashMap<>( );
	}
	
	/**
	 * Sets the value for the specified row and column
	 * 
	 * @param row The row label
	 * @param col the column label
	 * @param value The value to set
	 * @return The previous value, null if no value was present
	 */
	public V set( final K row, final K col, final V value ) {
		final V prev = get( row, col, null );
		if( !data.containsKey( row ) ) data.put( row, new HashMap<>( ) );
		data.get( row ).put( col, value );
		return prev;
	}
	
	/**
	 * Gets the value for the specified row and column
	 * 
	 * @param row The row label
	 * @param col The column label
	 * @return The value
	 * @throws NoSuchElementException if the element is not in the matrix
	 */
	public V get( final K row, final K col ) {
		if( !data.containsKey( row ) || !data.get( row ).containsKey( col ) )
			throw new NoSuchElementException( "The element (" + row + "," + col + ") has no value in the matrix" );
		return get( row, col, null );
	}

	/**
	 * Gets the value for the specified row and column, returns the defValue if
	 * not found
	 * 
	 * @param row The row label
	 * @param col The column label
	 * @param defValue The value to return if the matrix entry does not exist
	 * @return The value or defValue if the entry has no value
	 */
	public V get( final K row, final K col, final V defValue ) {
		if( !data.containsKey( row ) || !data.get( row ).containsKey( col ) ) return defValue;
			
		return data.get( row ).get( col );
	}
	
	/**
	 * Retrieves all the column values of the specified row
	 * 
	 * @param row The row label
	 * @return The column values, can be an empty map if no values exist for the
	 *   row
	 */
	public Map<K, V> getRow( final K row ) {
		return data.getOrDefault( row, new HashMap<>( ) );
	}
	
	/** @returns The current contents of the matrix */
	@Override
	public String toString( ) {
		final StringBuilder sb = new StringBuilder( );
		sb.append( "[\n" );
		for( final Entry<K, Map<K,V>> e : data.entrySet( ) ) {
			sb.append( "  " );
			sb.append( e.getKey( ).toString( ) );
			sb.append( "=" );
			sb.append( e.getValue( ).toString( ) );
			sb.append( "\n" );
		}
		sb.append( "]" );
		return sb.toString( );
	}
}
