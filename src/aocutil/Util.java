package aocutil;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Util {
	
	/**
	 * Converts the input string into a hash
	 * 
	 * @param input The input string
	 * @return The hashed string
	 */
	public static String MD5( final String input ) {
		try {
			
			final MessageDigest md = MessageDigest.getInstance("MD5");
			final byte[] messageDigest = md.digest( input.getBytes("UTF-8") );

			// Convert byte array into signum representation
      final BigInteger no = new BigInteger(1, messageDigest);
      
      // Convert message digest into hex value
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
          hashtext = "0" + hashtext;
      }
      return hashtext;
		} catch( Exception e ) {
			throw new RuntimeException( "Failed to produce MD5 hash: " + e.toString( ) );
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
