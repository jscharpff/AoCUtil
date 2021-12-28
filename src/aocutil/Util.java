package aocutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Util {
	
	/**
	 * Generates all permutations of the input set
	 * 
	 * @param <T> The object type of the set 
	 * @param set The set to generate permutations of
	 * @return The list of permutations (as list)
	 */
	public static <T> List<List<T>> generatePermutations( final Set<T> set ) {
		final List<List<T>> result = new ArrayList<>( );
		generatePermutations( result, set, new Stack<T>( ), 0 );
		return result;
	}

	/**
	 * Generates all permutations of the input array
	 * 
	 * @param <T> The object type of the array 
	 * @param set The set to generate permutations of
	 * @return The list of permutations (as list)
	 */
	public static <T> List<List<T>> generatePermutations( final T[] array ) {
		// convert array to set
		final Set<T> set = new HashSet<>( );
		for( T t : array ) set.add( t );
		
		// generate permutations using set
		return generatePermutations( set );
	}	
	
	protected static <T> void generatePermutations( final List<List<T>> result, final Set<T> set, Stack<T> curr, int curridx  ) {
		// no more elements to add
		if( curridx == set.size( ) ) {
			result.add( new ArrayList<>( curr ) );
			return;
		}
		
		// add remaining elements
		for( T o : set ) {
			// already in the result
			if( curr.contains( o ) ) continue;
			
			// nope, add this and recurse to add the remaining items
			curr.push( o );;
			generatePermutations( result, set, curr, curridx + 1 );
			curr.pop( );
		}
	}
	
	/**
	 * Sleeps for the specified amount of milliseconds, silences potential errors
	 * 
	 * @param sleeptime The sleep time in milliseconds
	 * @return True if the sleep was uninterrupted
	 */
	public static boolean sleep( final long sleeptime ) {		
		try {
			Thread.sleep( sleeptime );
			return true;
		} catch( InterruptedException e ) { /* interrupted */	}
		
		return false;
	}
}
