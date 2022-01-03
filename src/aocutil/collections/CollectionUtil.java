package aocutil.collections;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BinaryOperator;

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
	 */
	public static <T> Set<T> combineSets( final Set<T> s1, final Set<T> s2, final BinaryOperator<T> combfunc ) {
		final Set<T> result = new HashSet<>( );
		for( final T e1 : s1 )
			for( final T e2 : s2 )
				result.add( combfunc.apply( e1, e2 ) );
		return result;
	}

}
