package aocutil.collections;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Common helper functions for sets
 * 
 * @author Joris
 */
public class CollectionUtil {
	
	/**
	 * Combines the elements of both sets using the specified combination
	 * function, alike a Cartesian product but with a custom combinator function.
	 * 
	 * @param s1 The first set
	 * @param s2 The second set
	 * @param combfunc The function that combines each element of s1 and s2 into
	 *   new single elements, i.e. s3 = combfunc( s1, s2 )
	 * @return A single set that holds the Cartesian product of s1 and s2, where
	 *   this product is defined by combfunc
	 */
	public static <T> Set<T> combineSets( final Set<T> s1, final Set<T> s2, final BinaryOperator<T> combfunc ) {
		final Set<T> result = new HashSet<>( );
		for( final T e1 : s1 )
			for( final T e2 : s2 )
				result.add( combfunc.apply( e1, e2 ) );
		return result;
	}
	
	/**
	 * Retrieves all keys of the map of which the values pass the criteria of 
	 * the filter function
	 * 
	 * @param map The map to filter
	 * @param filter The filter function to apply to the values
	 * @return The set of keys that result after filtering
	 */
	public static <K, V> Set<K> filterKeys( final Map<K, V> map, final Function<V, Boolean> filter ) {
		final Set<K> result = new HashSet<>( );
		for( final K key : map.keySet( ) )
			if( map.get( key ) != null && filter.apply( map.get( key ) ) == true )
				result.add( key );
		return result;
	}

}
