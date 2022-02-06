package aocutil.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Container for a node based graph
 * 
 * @author Joris
 */
public class Graph {
	/** The node set, indexed by node label */
	protected final Map<String, Node> nodes;
	
	/** The edge set */
	protected final List<Edge> edges;
	
	/**
	 * Creates a new, empty Graph of nodes that is undirected
	 */
	public Graph( ) {
		nodes = new HashMap<String, Node>( );
		edges = new ArrayList<Edge>( );
	}
	
	/**
	 * Adds a node to the graph, gets the exiting one if already in the graph
	 * 
	 * @param nodelabel The node to add by label
	 * @return The node
	 */
	public Node addNode( final String nodelabel ) {
		Node node = this.getNode( nodelabel );
		if( node == null ) node = new Node( nodelabel );
		
		nodes.put( node.getLabel( ), node );
		return node;
	}

	/**
	 * Retrieves a node by its label
	 * 
	 * @param label The node label
	 * @return The node or null if not found in the graph
	 */
	public Node getNode( final String label ) {
		return nodes.get( label );
	}
	
	/**
	 * @return The set of nodes
	 */
	public Collection<Node> getNodes( ) {
		return nodes.values( );
	}
	
	/**
	 * Checks if the node is already present in the graph
	 * 
	 * @param node The node to check
	 * @return True if the graph contains the node
	 */
	public boolean contains( final String label ) {
		return nodes.containsKey( label );
	}
	
	/**
	 * @return The number of nodes in the graph
	 */
	public int size() { return nodes.size( ); }
	
	/**
	 * @return List of edges
	 */
	public List<Edge> getEdges( ) {
		return edges;
	}
	
	/**
	 * Adds an edge to the graph that connects the two specified nodes, if not
	 * already present in the edge list
	 * 
	 * @param edge The edge to add
	 * @return False if the edge was already in the list
	 */
	public boolean addEdge( final Edge edge ) {
		if( edges.contains( edge ) ) return false;
		
		// add the edge and its nodes
		edges.add( edge );
		
		// make sure nodes also know of the arc
		edge.from.addEdge( edge );
		edge.to.addEdge( edge );
		return true;
	}
	
	/**
	 * Finds the root node of a directed graph
	 * 
	 * @return The root node of the tree, null if the graph is empty
	 * @throws RuntimeException if the graph is undirected, i.e. a cycle is 
	 *   encountered 
	 */
	public Node getRoot( ) throws RuntimeException {
		if( size( ) == 0 ) return null;
		
		final Set<Node> visited = new HashSet<>( );

		// pick a random starting node
		Node curr = nodes.values( ).iterator( ).next( );
		while( true ) {
			// chekl if this node has predecessors
			final Collection<Node> pre = curr.getPredecessors( );
			if( !pre.iterator( ).hasNext( ) ) return curr;
			
			// pick any of its predecessors
			curr = pre.iterator( ).next( );
			if( visited.contains( curr ) ) throw new RuntimeException( "Graph contains a cycle!" );
			visited.add( curr );
		}
	}
	
	/**
	 * Returns all isolated subgraphs of this graph, i.e. the cliques of nodes
	 * such that no node outside the clique is connected to it
	 * 
	 * @return The list of subgraph, potentially containing the original graph
	 *   only (if it is a clique) 
	 */
	public List<Graph> getCliques( ) {
		final Stack<Node> remaining = new Stack<>( );
		remaining.addAll( nodes.values( ) );
		final List<Graph> cliques = new ArrayList<>( );
		
		while( remaining.size( ) > 0 ) {
			final Node n = remaining.pop( );
			
			// create clique from this node
			final Stack<Node> nextnodes = new Stack<>( );
			nextnodes.add( n );
			final Graph g = new Graph( );
			while( !nextnodes.empty( ) ) {
				final Node next = nextnodes.pop( );
				if( g.contains( next.label ) ) continue;
				
				g.addNode( next.label );
				
				// and find new nodes to explore
				nextnodes.addAll( next.getNeighbours( ) );
			}
			
			// copy edges from the original graph into the sub graph
			for( final Edge e : edges ) {
				if( g.contains( e.from.label ) && g.contains( e.to.label ) )
					g.addEdge( new Edge( e.from, e.to, e.weight ) );
			}
			
			// add clique and remove clique nodes from the remaining set
			cliques.add( g );
			remaining.removeAll( g.getNodes( ) );
		}
		
		return cliques;
	}
	
	/**
	 * Constructs a graph from a list of Strings that describe its edges
	 * 
	 * @param input The list of edges as strings
	 * @return The graph
	 */
	public static Graph fromStringList( final List<String> input ) {
		final Graph g = new Graph( );

		// parse the edges
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "^(\\w+)\\s*-\\s*(\\w+)$" ).matcher( s.trim( ) );
			if( !m.find( ) ) throw new IllegalArgumentException( "Edge not formatted properly: " + s );

			// first make sure we know the nodes of the edge
			final Node A = g.addNode( m.group( 1 ) );
			final Node B = g.addNode( m.group( 2 ) );
			
			// and add the edge
			g.addEdge( new Edge( A, B, 0 ) );
		}
		
		return g;
	}
	
	/**
	 * @return String representation of the tree
	 */
	public String toString( ) {
		return "[Nodes " + size( ) + "]\n" + nodes.toString( ) + 
				"\n\n[Edges " + edges.size( ) + "]\n" + edges.toString( );
	}
}
