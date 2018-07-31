package com.bc.example;

import java.security.MessageDigest;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/19/18
 * ____________________________________
 */
public final class StringUtil {
	public static String sha512(String value) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] digest = md.digest(value.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte aDigest : digest) {
			sb.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
}
