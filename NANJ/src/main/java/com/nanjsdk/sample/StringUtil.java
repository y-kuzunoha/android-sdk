package com.nanjsdk.sample;

import java.security.MessageDigest;

import static java.security.MessageDigest.getInstance;

/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/19/18
 * ____________________________________
 */
final class StringUtil {
	static String sha512(String value) throws Exception{
		MessageDigest md = getInstance("SHA-512");
		byte[] digest = md.digest(value.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte aDigest : digest) {
			sb.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
}
