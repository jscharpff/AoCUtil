package aocutil.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * Simple print-stream based file writer
 * 
 * @author Joris
 */
public class FileWriter {
	/** The print streamm used to write */
	private final PrintWriter writer;
	
	/**
	 * Creates a new file writer
	 * 
	 * @param filename The name of the file to write to
	 * @throws IOException if the file is not found or unwritable
	 */
	public FileWriter( final String filename ) throws IOException {
		final File file = new File( filename );		
		if( file.exists( ) && !file.canWrite( ) ) throw new IOException( "No permission to write to file " + file );
		writer = new PrintWriter( file );
	}
	
	/**
	 * Creates a new file writer
	 * 
	 * @param uri The resource URI
	 * @throws IOException if the file is not found or unwritable
	 */
	public FileWriter( final URL uri ) throws IOException {
		this( uri.getFile( ) );
	}
	
	/**
	 * Writes a single message
	 * 
	 * @param message The message to write
	 */
	public void write( final String message ) {
		writer.print( message );
	}
	
	/**
	 * Writes a message and adds a new line
	 * 
	 * @param message The message to write
	 */
	public void writeln( final String message ) {
		write( message + "\n" );
	}
	
	/**
	 * Flushes the file
	 */
	public void flush( ) {
		writer.flush( );
	}

	/**
	 * Closes the file
	 */
	public void close( ) {
		writer.flush( );
		writer.close( );
	}
}
