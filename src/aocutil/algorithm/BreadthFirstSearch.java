package aocutil.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

/**
 * Skeleton implementation of a classic BFS search
 * 
 * @author Joris
 */
public class BreadthFirstSearch {
	
	/**
	 * Simple BFS implementation to find the distance from a given initial object
	 * to a single target
	 *  
	 * @param <T> The type of the objects we are searching over
	 * @param initial The initial object where the search starts from
	 * @param target The target value we are looking for
	 * @param nextfunc The function that generates the set of next values to
	 *   consider
	 * @return The distance from initial to target in terms of number of BFS iterations
	 */
	public static <T> long getDistance( final T initial, final T target, final Function<T, Collection<T>> nextfunc ) {
		final Set<T> targets = new HashSet<>( );
		targets.add( target );
		final Map<T, Long> distances = getDistances( initial, targets, nextfunc );
		return distances.get( target );
	}
	
	/**
	 * Simple BFS implementation to find the distance from a given initial object
	 * to a set of targets.
	 *  
	 * @param <T> The type of the objects we are searching over
	 * @param initial The initial object where the search starts from
	 * @param targets The set of target values we are looking for
	 * @param nextfunc The function that generates the set of next values to
	 *   consider
	 * @return The distance from initial to target in terms of number of BFS iterations
	 */
	public static <T> Map<T, Long> getDistances( final T initial, final Collection<T> targets, final Function<T, Collection<T>> nextfunc ) {
		// initialise explore stack from initial state and keep track of visited
		// values
		final Set<T> visited = new HashSet<>( );
		Stack<T> explore = new Stack<>( );
		explore.push( initial );
		
		// set distance 0 to initial value if it is in the target list
		final Map<T, Long> distances = new HashMap<>( targets.size( ) );
		long dist = 0;
		if( targets.contains( initial ) ) {
			distances.put( initial, dist );
		}
		
		// keep exploring until no new values arise
		while( !explore.empty( ) ) {

			// build stack of values to evaluate in the next iteration
			final Stack<T> explorenext = new Stack<>( );
			while( !explore.empty( ) ) {
				// explore a new value
				final T exp = explore.pop( );
				if( visited.contains( exp ) ) continue;
				visited.add( exp );

				// check if this value is a target value
				if( targets.contains( exp ) ) {
					distances.put( exp, dist );
					if( distances.size( ) == targets.size( ) ) return distances;
				}
				
				
				// generate the next values from it that will be considered in the
				// next iteration
				explorenext.addAll( nextfunc.apply( exp ) );
			}
			
			// swap sets and increase distance before the next iteration
			explore = explorenext;
			dist++;
		}
		
		// not all targets were found
		throw new RuntimeException( "Failed to find all of the targets " + targets + " from " + initial + " (found " + distances + ")" );
	}

}
