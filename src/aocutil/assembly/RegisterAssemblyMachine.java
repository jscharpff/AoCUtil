package aocutil.assembly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.object.LabeledObject;

/**
 * Skeleton implementation of an interpreter with registers
 * 
 * @author Joris
 */
public abstract class RegisterAssemblyMachine extends LabeledObject {
	/** The memory registers*/
	private final Map<String, Long> mem;
	
	/** The program that is currently being run */
	private List<String> program;
	
	/** The current instruction pointer index */
	private int IP;
	
	/** The current program state */
	private MachineState state;
	
	/**
	 * Creates a new register-driven assembly interpreter
	 * 
	 * @param label The label of the machine
	 */
	public RegisterAssemblyMachine( final String label ) {
		super( label );
		
		mem = new HashMap<>( );
		IP = -1;
		state = MachineState.Idle;
	}
	
	/**
	 * Runs the specified program
	 * 
	 * @param input The list of instructions to perform
	 */
	public void run( final Collection<String> input ) {
		load( input );
		resume( );
	}
	
	/**
	 * Only loads the program and then suspends the execution, can be used to
	 * prepare several machines before running them alternatively
	 * 
	 * @param input The list of instructions to perform
	 */
	public void load( final Collection<String> input ) {
		if( state != MachineState.Idle ) throw new RuntimeException( "Program not ready to run" );
		
		this.program = new ArrayList<>( input );
		if( program.size( ) == 0 ) throw new RuntimeException( "No instructions in program" );
		
		// do not execute, instead yield the program for now
		state = MachineState.Yielded;
		IP = 0;
	}
		
	/**
	 * Resumes operation from a yielded state
	 */
	public void resume( ) {
		if( state != MachineState.Yielded ) throw new RuntimeException( "Program was not in yielded state" );
		
		state = MachineState.Running;
		
		while( 0 <= IP && IP < program.size( ) ) {
			try {
				execute( program.get( IP++ ) );
			} catch( YieldException ye ) {
				/** Program is interrupted */
				state = MachineState.Yielded;
				return;
			}
		}
			
		// program terminated naturally
		state = MachineState.Ended;		
	}
	
	/**
	 * Executes a single instruction
	 * 
	 * @param instruction The instruction to execute
	 */
	protected abstract void execute( final String instruction ) throws YieldException;
	
	/**
	 * Reads the value from the memory register or returns the literal value if
	 * it is not a register
	 * 
	 * @param r The register name
	 * @return The value in the register, 0 if not initialised
	 */
	public long read( final String r ) {
		try {
			// check if this is a literal value
			return Long.parseLong( r );
		} catch( NumberFormatException nfe ) { /* nope */ }
		
		return mem.getOrDefault( r, 0l );
	}
	
	/**
	 * Performs a relative jump
	 * 
	 * @param offet The offset to jump
	 */
	protected void jump( final long offset ) {
		IP += offset - 1;
	}
	
	/**
	 * Performs a relative jump if the condition evaluates to true
	 * 
	 * @param condition The condition
	 * @param offset The offset to jump
	 */
	protected void jumpIf( final boolean condition, final long offset ) {
		if( condition ) jump( offset );
	}
	
	/**
	 * Writes the value to the memory register
	 * 
	 * @param r The register
	 * @param value The value to write
	 */
	public void write( final String r, final long value ) {
		mem.put( r, value );
	}
		
	/** @return The current memory registers */
	public Map<String, Long> getRegisters( ) { return mem; }
	
	/** @return The current instruction pointer value */
	public int getIP( ) { return IP; }
	
	/** @return The current state of the machine */
	protected MachineState getState( ) { return state; }
	
	/** @return The contents of all memory registers */
	@Override
	public String toString( ) { return mem.toString( ); }

	/**
	 * Available machine states
	 */
	public enum MachineState {
		Idle, Running, Yielded, Ended;
	}
}
