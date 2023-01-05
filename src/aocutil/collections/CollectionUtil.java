package aocutil.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Common helper functions for Java collections
 * 
 * @author Joris
 */
public class CollectionUtil {
	
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
	 * @param array The array to generate permutations of
	 * @return The list of permutations (as list)
	 */
	public static <T> List<List<T>> generatePermutations( final T[] array ) {
		// convert array to set
		final Set<T> set = new HashSet<>( );
		for( T t : array ) set.add( t );
		
		// generate permutations using set
		return generatePermutations( set );
	}	
	
	/**
	 * Recursive function to generate all permutations of the remaining input
	 * 
	 * @param <T> The type of the input
	 * @param result The permutations we have generated so far
	 * @param set The set of items to permute
	 * @param curr The current permutation we are building
	 * @param curridx The index of the item we are considering
	 */
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
	 * Counts all possible unique combinations that can be generated from a
	 * collection of sets. Uses the equals function to enforce uniqueness
	 * 
	 * @param <T> The type of the list elements
	 * @param input The set of items to count combinations of
	 * @param combfunc The function to combine elements of the sets 
	 * @return The number of possible combinations of the items in the input set
	 */
	public static <T> Set<T> generateSetCombinations( final Collection<Set<T>> input, BinaryOperator<T> combfunc ) {
		if( input.size( ) == 0 ) return new HashSet<>( );
		
		Set<T> result = new HashSet<>( );
		final Stack<Set<T>> remaining = new Stack<>( );
		remaining.addAll( input );

		// pop the first and thereafter combine sets until the remaining queue is empty
		result.addAll( remaining.pop( ) );
		while( !remaining.empty( ) ) {
			result = CollectionUtil.combineSets( result, remaining.pop( ), combfunc );
		}
		
		return result;
	}
	
	/**
	 * Generates all possible sub sets of a set of elements
	 * 
	 * @param <T> The type of the set elements
	 * @param items The items in the set
	 * @return A list that contains all possible subsets of the input set 
	 */
	public final static <T> List<Set<T>> generateSubSets( final Set<T> items ) {
		return generateSubSets( items, null );
	}

	/**
	 * Generates all possible sub sets of a set of elements
	 * 
	 * @param <T> The type of the set elements
	 * @param items The items in the set
	 * @param validator Validation function that will discard sub sets if the
	 *   function evaluates to false for that set
	 * @return A list that contains all possible subsets of the input set 
	 */
	public final static <T> List<Set<T>> generateSubSets( final Set<T> items, final Function<Set<T>, Boolean> validator ) {
		final List<Set<T>> subsets = new ArrayList<>( );
		generateSubSets( subsets, new ArrayList<T>( items ), validator, new Stack<T>( ), 0 );
		return subsets;
	}
	
	/***
	 * Actual function that generates all the sub sets in O( 2^n ) by branching
	 * for all items into an included and excluded branch
	 * 
	 * @param <T> The type of set elements
	 * @param subsets The list of subsets produced so far
	 * @param items The original set of items to build subsets of
	 * @param validator Validation function that can discard subsets
	 * @param curr The current subset we are building
	 * @param curridx The index of the current item to consider
	 */
	private final static <T> void generateSubSets( final List<Set<T>> subsets, final List<T> items, final Function<Set<T>, Boolean> validator, final Stack<T> curr, final int curridx ) {
		// are we there yet?
		if( curridx >= items.size( ) ) {
			final Set<T> subset = new HashSet<>( curr );
			if( validator != null && validator.apply( subset ) == false ) return;
			subsets.add( subset );
			return;
		}
		
		// nope, recurse on both options for the next item: include or exclude
		curr.push( items.get( curridx ) );
		generateSubSets( subsets, items, validator, curr, curridx + 1 );
		curr.pop( );
		generateSubSets( subsets, items, validator, curr, curridx + 1 );
	}
	

	/**
	 * Generates all combinations of items in a list of sets
	 * 
	 * @param <T> The type of the list elements
	 * @param input The set of items to generate combinations of
	 * @return All possible combinations of the items in the input set
	 */
	public static <T> List<List<T>> generateCombinations( final Collection<Set<T>> input ) {
		return generateCombinations( input, null );
	}
	
	/**
	 * Generates all combinations of items in the set
	 * 
	 * @param <T> The type of the list elements
	 * @param input The set of items to generate combinations of
	 * @param validator The function to validate a combination, can be used to
	 *   discard combinations 
	 * @return All possible combinations of the items in the input set
	 */
	public static <T> List<List<T>> generateCombinations( final Collection<Set<T>> input, final Function<List<T>, Boolean> validator ) {
		// convert input to lists so that we can generate combinations easily
		final List<List<T>> in = new ArrayList<>( );
		for( final Collection<T> i : input )
			in.add( new ArrayList<>( i ) );
				
		final List<List<T>> results = new ArrayList<>( );
		generateCombinations( results, in, validator, new Stack<>( ), 0 );
		return results;
	}
	
		
	/**
	 * Recursively generates all possible (unique) combinations of items in the input list
	 *  
	 * @param <T> The type of elements in the list
	 * @param results All combinations generated so far
	 * @param input The list of input items
	 * @param validator The function to validate a combination, can be used to
	 *   discard combinations 
	 * @param current The current combination that is being built
	 * @param curridx The index of the next item to add
	 */
	private static <T> void generateCombinations( final List<List<T>> results, final List<List<T>> input, final Function<List<T>, Boolean> validator, final Stack<Integer> current, final int curridx ) {
		// done?
		if( curridx == input.size( ) ) {			
			// map stack indexes to original list items
			final List<T> combination = new ArrayList<>( current.size( ) );
			int idx = -1;
			for( final int i : current )
				combination.add( input.get( ++idx ).get( i ) );
			
			// check if this combination is valid
			if( validator != null && validator.apply( combination ) == false ) return;
			
			results.add( new ArrayList<>( combination ) );
			return;
		}
		
		// nope, generate all combinations for the next item
		final List<T> in = input.get( curridx );
		for( int i = 0; i < in.size( ); i++ ) {		
			// add this item to the combination and continue the process
			current.push( i );
			generateCombinations( results, input, validator, current, curridx + 1 );
			current.pop( );
		}
	}
	
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
