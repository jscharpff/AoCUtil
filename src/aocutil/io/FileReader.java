package aocutil.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
	/** The actual file open for reading */
	private File file;

	/** 
	 * Creates a new FileReader
	 * 
	 * @param url The resource identifier of the file
	 */
	public FileReader( URL url ) {
		this( url.getFile( ) );
	}
	
	/** 
	 * Creates a new FileReader
	 * 
	 * @param path The path of the file to read
	 */
	public FileReader( String path ) {
		this.file = new File( path );
	}
		
	/**
	 * Reads the entire file and parses lines into an Int array
	 *  
	 * @return Array of integers, one per line
	 * @throws IOException 
	 */
	public int[] readIntArray() throws IOException {
		return readIntArray( "," );
	}
	
	/**
	 * Reads the entire file and parses lines into an Int array
	 *  
	 * @param separator The separator used in the input file
	 * @return Array of integers, one per line
	 * @throws IOException 
	 */
	public int[] readIntArray( final String separator ) throws IOException {
		final List<String> lines = this.readLines( );
		final List<Integer> values = new ArrayList<>( lines.size( ) );
		
		for( final String line : lines ) {
			// split within line by comma, if there are any
			for( final String s : line.split( separator ) )
				values.add( Integer.parseInt( s.trim( ) ) );
		}
		
		return values.stream( ).mapToInt( Integer::intValue ).toArray( );
	}
	
	
	/**
	 * Reads the entire file and parses lines into a long array
	 *  
	 * @return Array of longs, one per line
	 * @throws IOException 
	 */
	public long[] readLongArray() throws IOException {
		final List<String> lines = this.readLines( );
		final long[] res = new long[ lines.size( ) ];
		
		for( int i = 0; i < lines.size( ); i++ ) {
			res[ i ] = Long.parseLong( lines.get( i ) );
		}
		
		return res;
	}
	
	/**
	 * Reads the file and returns its contents as a single string
	 *  
	 * @return List of strings, one per new line
	 * @throws IOException
	 */
	public String readAll( ) throws IOException {
		String res = "";
		for( final String s : readLines( ) )
			res += s + "\n";
		return res.substring( 0, res.length( ) - 1 );
	}
	
	/**
	 * Reads file and returns the lines as a list of Strings
	 * 
	 * @return List of strings, one per new line
	 * @throws IOException
	 */
	public List<String> readLines( ) throws IOException {
		return Files.readAllLines( this.file.toPath( ) );
	}
	
	
	/**
	 * Reads line groups, each separated by a blank line
	 * 
	 * @param sep The separator to be used within the group
	 * @return List of strings, one per line group
	 * @throws IOException
	 */
	public List<String> readLineGroups( final String sep ) throws IOException {
		final List<String> input = this.readLines( );
		final List<String> output = new ArrayList<String>( );
		
		// read groups in file, each group ending with a new line
		// add an extra blank line at the end to make sure last group is added
		input.add( "" );
		String curr = "";
		for( String s : input ) {
			if( s.equals( "" ) ) {
				// do not add empty groups
				if( !curr.isEmpty( ) ) output.add( curr );
				curr = "";
				continue;
			}
			curr += s.trim() + sep;
		}
				
		return output;
	}
}