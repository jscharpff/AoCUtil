package aocutil.object;

/**
 * Abstract base class for simple objects that can be hashed using only a 
 * unique label
 * 
 * @author Joris
 */
public abstract class LabeledObject {
	/** The label used in the hashing */
	protected final String label;
	
	/** The one-time cached hash code for the label */
	private final int hashcode;
	
	/**
	 * Creates a new LabeledObject that can be compared and hashed based upon a
	 * simple label
	 * 
	 * @param label The label to hash the object by
	 */
	public LabeledObject( final String label ) {
		if( label == null || label.length( ) == 0 ) throw new IllegalArgumentException( "Invalid label for LabeledObject" );
		this.label = "" + label;
		this.hashcode = label.hashCode( );
	}
	
	/**
	 * Simple equals function that simply checks labels
	 * 
	 * @param obj The object to compare against
	 * @return True iff the object is valid and its label is the same
	 */
	@Override
	public boolean equals( final Object obj ) {
		if( obj == null || !(obj instanceof LabeledObject) ) return false;
		return label.equals( ((LabeledObject)obj).label );
	}
	
	/** @return The cached hash code */
	@Override
	public int hashCode( ) {
		return hashcode;
	}

	
	/** @return The label of the object */
	@Override
	public String toString( ) {
		return label;
	}
}
