package org.pavelf.nevada.api;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;

/**
 * General purpose utility class.
 * @since 1.0
 * @author Pavel F.
 * */
public class HttpUtil {

	public enum Algorithm {
		MD5("MD5"),
		SHA1("SHA-1"),
		SHA256("SHA-256");
		
		String name;
		
		Algorithm(String name) {
			this.name = name;
		}
	}
	
	/**
	 * Returns string digest representation of {@code Serializable} 
	 * or {@code null} if exception occurs. May return an empty string.
	 * Ensure all underlying objects also implement java.io.Serializable.
	 * @param alg algorithm to use.
	 * @param object to hash.
	 * */
	public static char[] hash(Algorithm alg, Serializable object) {
		if (alg == null || object == null) {
			return null;
		}
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			
			MessageDigest md = MessageDigest.getInstance(alg.name);
			oos.writeObject(object);
			
			final byte[] hash = md.digest(baos.toByteArray());
			final StringBuilder digest = new StringBuilder();
			
			for (int i = 0; i < hash.length; i++) {
				int v = hash[i] & 0xFF;
				if (v < 16) {
					digest.append('0');
				}
				digest.append(Integer.toString(v, 16).toUpperCase());
			}
			
			char[] destination = new char[digest.length()];
			digest.getChars(0, digest.length(), destination, 0);
			return destination;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
