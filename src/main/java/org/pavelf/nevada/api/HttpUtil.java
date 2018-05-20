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
	 * Returns string digest representation of Serializable or null if exception occurs. 
	 * May return an empty string.
	 * Ensure all underlying objects also implement java.io.Serializable.
	 * @param alg algorithm to use.
	 * @param object to hash.
	 * */
	public static String hash(Algorithm alg, Serializable object) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			MessageDigest md = MessageDigest.getInstance(alg.name);
			oos.writeObject(object);
			byte[] hash = md.digest(baos.toByteArray());
			String digest = "";
			for (int i = 0; i < hash.length; i++) {
				int v = hash[i] & 0xFF;
				if (v < 16) {
					digest += '0';
				}
				digest += Integer.toString(v, 16).toUpperCase();
			}
			return digest;
			} catch (Exception e) {
					e.printStackTrace();
					return null;
			}
	}
	
}
