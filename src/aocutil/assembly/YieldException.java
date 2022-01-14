package aocutil.assembly;

/**
 * Exception to force quit
 */
@SuppressWarnings( "serial" )
public class YieldException extends Exception {
	/** The exit code */
	private final int statuscode;
	
	/**
	 * Creates a new interrupt exception
	 * 
	 * @param code The status code
	 */
	public YieldException( final int code ) {
		this( code, "Interrupted" );
	}
	
	/**
	 * Creates a new interrupt exception
	 * 
	 * @param code The status code
	 * @param message The interrupt message
	 */
	public YieldException( final int code, final String message ) {
		super( message + " (code " + code + ")" );
		this.statuscode = code;
	}
	
	/** @return The status code */
	public int getCode( ) { return statuscode; }
}
