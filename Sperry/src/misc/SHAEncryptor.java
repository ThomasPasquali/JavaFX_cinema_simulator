package misc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHAEncryptor {

	 public static String get_SHA_1_SecurePassword(String passwordToHash, byte[] salt) {
	        String generatedPassword = null;
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            md.update(salt);
	            byte[] bytes = md.digest(passwordToHash.getBytes());
	            StringBuilder sb = new StringBuilder();
	            for(int i=0; i< bytes.length ;i++)
	                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	            generatedPassword = sb.toString();
	        }
	        catch (NoSuchAlgorithmException e){e.printStackTrace();}
	        return generatedPassword;
	    }
	
	public static byte[] generateSalt() {
		try {
			SecureRandom sr=SecureRandom.getInstance("SHA1PRNG");
			byte[] s=new byte[16];
			sr.nextBytes(s);
			return s;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
