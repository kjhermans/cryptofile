package com.kjwork.util.test;

import com.kjwork.util.Base64;

public class TestBase64 {
	
	public static void main(String[] args) {
		for (int i=0; i < 30; i++) {
			int msglen = (int)(Math.random() * 100);
			byte[] msg = new byte[ msglen ];
			for (int j=0; j < msglen; j++) {
				msg[ j ] = (byte)(Math.random() * 256);
			}
			test_message(msg);
			System.err.println("");
		}
	}
	
	private static void test_message(byte[] msg) {
		String base64 = Base64.encode(msg);
		System.err.println("Original message length is " + msg.length);
		System.err.print(base64);
		byte[] bin = Base64.decode(base64);
		if (msg.length != bin.length) {
			System.err.println(
				"Messages are not the same length: " +
				msg.length + " != " + bin.length
			);
			return;
		}
		for (int i=0; i < msg.length; i++) {
			if (msg[ i ] != bin[ i ]) {
				System.err.println("Message differ at byte " + i);
				return;
			}
		}
		System.err.println("Messages are identical.");
	}

}
