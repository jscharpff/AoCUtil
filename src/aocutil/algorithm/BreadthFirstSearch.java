package aocutil.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
		final Map<T, Long> distances = getDistances( initial, targets, nextfunc, true );
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
		return getDistances( initial, targets, nextfunc, true );
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
	 * @param findall True to require all targets to be found 
	 * @return The distance from initial to target in terms of number of BFS iterations
	 */
	public static <T> Map<T, Long> getDistances( final T initial, final Collection<T> targets, final Function<T, Collection<T>> nextfunc, final boolean findall ) {
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
		if( findall )
			throw new RuntimeException( "Failed to find all of the targets " + targets + " from " + initial + " (found " + distances + ")" );
		else
			return distances;
	}

	/**
	 * Finds the set of all values reachable from the given starting value using
	 * the given function to determine the new values to explore
	 * 
	 * @param <T> The type of value to search over
	 * @param startvalue The initial value to start search from
	 * @param nextfunc The function that generates the next values to explore
	 * @return The set of all visited values and their distances
	 */
	public static <T> Map<T, Long> getReachable( final T startvalue, final Function<T, Collection<T>> nextfunc ) {
		final Map<T, Long> visited = new HashMap<>( );
		Stack<T> explore = new Stack<>( );
		explore.add( startvalue );
		
		long dist = 0;
		while( !explore.empty( ) ) {
			final Stack<T> toexplore = new Stack<>( );
			
			while( !explore.empty( ) ) {
				final T exp = explore.pop( );
				if( visited.containsKey( exp ) ) continue;
				visited.put( exp, dist );
				
				toexplore.addAll( nextfunc.apply( exp ) );
				
			}
			dist++;
			explore = toexplore;
		}	
		
		return visited;		
	}
	
	/**
	 * Finds the shortest path from the given starting value to the target value
	 * using the given function to determine the new values to explore
	 * 
	 * @param <T> The type of value to search over
	 * @param startvalue The initial value to start search from
	 * @param targetvalue The target value we are looking for
	 * @param nextfunc The function that generates the next values to explore
	 * @return The shortest path from the start value to the target value
	 */
	public static <T> List<T> getShortestPath( final T startvalue, final T targetvalue, final Function<T, Collection<T>> nextfunc ) {
		// simply return first available shortest path
		return getShortestPaths( startvalue, targetvalue, nextfunc ).get( 0 );
	}
	
	/**
	 * Finds all shortest paths from the given starting value to the target value
	 * using the given function to determine the new values to explore
	 * 
	 * @param <T> The type of value to search over
	 * @param startvalue The initial value to start search from
	 * @param targetvalue The target value we are looking for
	 * @param nextfunc The function that generates the next values to explore
	 * @return The shortest path from the start value to the target value
	 */
	public static <T> List<List<T>> getShortestPaths( final T startvalue, final T targetvalue, final Function<T, Collection<T>> nextfunc ) {
		// keep track of visited nodes
		final Set<T> visited = new HashSet<>( );
		
		// keep track of explored paths
		Stack<List<T>> explore = new Stack<>( );
		explore.add( new ArrayList<>( ) );
		explore.get( 0 ).add( startvalue );
		
		while( !explore.empty( ) ) {
			final Stack<List<T>> toexplore = new Stack<>( );
			final List<List<T>> shortest = new ArrayList<>( );
			final Set<T> newvisited = new HashSet<>( );
			
			while( !explore.empty( ) ) {
				final List<T> exp = explore.pop( );
				
				// 
				final T currvalue = exp.get( exp.size( ) - 1 );
				if( visited.contains( currvalue ) ) continue;
				newvisited.add( currvalue );
				
				// check what new values we can reach from the end of the current path
				final Collection<T> newvalues = nextfunc.apply( currvalue );
				for( final T newvalue : newvalues ) {
					
					final List<T> newpath = new ArrayList<>( exp );
					newpath.add( newvalue );
					
					// if it leads to the target value, add it as a solution, else try
					// extending it
					if( newvalue.equals( targetvalue ) ) 
						shortest.add( newpath );
					else
						toexplore.add( newpath );
				}				
			}
			
			// we found at least one shortest path this iteration. All others will be
			// longer so return it
			if( shortest.size( ) > 0 ) return shortest;
			
			explore = toexplore;
			visited.addAll( newvisited );
		}	
		
		throw new RuntimeException( "Failed to find shortest path from " + startvalue + "  to " + targetvalue );
	}
}
