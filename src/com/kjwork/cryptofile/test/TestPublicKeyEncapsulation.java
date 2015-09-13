package com.kjwork.cryptofile.test;

import com.kjwork.cryptofile.*;
import java.io.*;

public class TestPublicKeyEncapsulation {

	private static final int NUMTESTS = 256;
	private static final int MAXLENGTH = 1024;
	
	private static byte[] testbuffer(int len, boolean predictable) {
		byte[] result = new byte[ len ];
		for (int i=0; i < len; i++) {
			if (predictable) {
				result[ i ] = (byte)((i % 64) + 32);
			} else {
				result[ i ] = (byte)(Math.random() * 256);
			}
		}
		return result;
	}
	
	public static void main
		(String[] args)
	{
		for (int i=0; i < NUMTESTS; i++) {
			int msglen = (int)(Math.random() * MAXLENGTH);
			//byte[] msg = testbuffer(msglen, true);
			byte[] msg = testbuffer(i, true);
			byte[] ciphertext = encapsulate(msg);
			if (ciphertext == null) {
				continue;
			}
			byte[] plaintext = deencapsulate(ciphertext);
			
			if (msg.length != plaintext.length) {
				System.err.println("TEST FAILED: Different lengths: " +
					msg.length + " != " + plaintext.length
				);
				return;
			}
			for (int j=0; j < msg.length; j++) {
				if (msg[j] != plaintext[j]) {
					System.err.println("TEST FAILED: Differing chars at " + j);
					return;
				}
			}
			try {
				com.kjwork.cryptofile.Util.print_binary(plaintext);
				System.err.println("TEST SUCCEEDED.");
			} catch (IOException ioe) {
				System.err.println(ioe);
			}
		}
	}
		
	private static byte[] encapsulate(byte[] plaintext)
	{
		String pemfile =
			"-----BEGIN PUBLIC KEY-----\n" +
			"MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAq6/0KY73ByzAjhrOuqZd\n" +
			"zb91hOvPgCuHmShrJiyK7HHAWVoC9ZYolaqsFBjx4TackBX9zZLRG9yUqdMT5i3b\n" +
			"VRVACvz/W2Its9e6DM67QxMLH9pxgnlnEGT+zU/wAWkQjqRpfM/CGG6cKb4U+2dv\n" +
			"3RLaNmTrBfAQ8amb55Mq0+YRc+ltaS1SVrnVnAJp61yCSDsU4f4+1bNKZGYSJw6H\n" +
			"lKKq1+ijWKdgWEoPaA0alaLkc64cLMm8DvDCPBI6w/XIg5P7N8S344iBlBngw/1F\n" +
			"Tbd47cvzuq1PlthjzD3Ze8MDUuP5xB69vxc+5xEZ/u0hKp+PWFDkIB+9Kxu3Yl/u\n" +
			"3AbvDUH75vjzYtBH95p2pq2/IO/Z9roCw5YrDJY/KmKqQviOXESwvnNOq6Lvnczv\n" +
			"6HzKxl/h8hNKHIYppnPUJPZZNtfZNijCsj0nzfJL+JUP5XOcorp6ENJrP8zlfcbv\n" +
			"HDIPmS2mpbvQgrE1rDQc0Dhreq2rl4WZ/lHHj72Qj6PT7MuYaRf9j0WlPexTPVY0\n" +
			"FTp4mm5Se6YrCmf3nYZ1la9BIcj771hxifFUu/CM4T+hIPjEH8qAnGzuAuy+1Sh7\n" +
			"UF0Vkg9Ab720zYCklZ1+IZhGUlN49kZBMzkY1AP/gnzwTnYh2h5c8q2IKH+Is4bd\n" +
			"qUiD5cZGyelyCyXIN349nY8CAwEAAQ==\n" +
			"-----END PUBLIC KEY-----";

		InputStream in = new java.io.ByteArrayInputStream(pemfile.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			java.security.interfaces.RSAPublicKey publicKey =
				com.kjwork.cryptofile.Util.openRSAPublicKeyPEMFile(in);
			
			PublicKeyEncapsulatedOutputStream pkeos =
				PublicKeyEncapsulatedOutputStream.open(out, publicKey, null);
			
			System.err.println("Input file length is " + plaintext.length);
			com.kjwork.cryptofile.Util.print_binary(plaintext);
			
			pkeos.write(plaintext);
			pkeos.close();
			
			byte[] file = out.toByteArray();
			System.err.println("Output file length is " + file.length);
			com.kjwork.cryptofile.Util.print_binary(file);
			System.err.println("Done");
			
			return file;
			
		} catch (Throwable t) {
			System.err.println(t.toString());
			t.printStackTrace(System.err);
			return null;
		}
	}
	
	private static byte[] deencapsulate(byte[] ciphertext)
	{
		String pemfile =
			  "-----BEGIN PRIVATE KEY-----\n" +
			  "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\n" +
			  "Zbct+hu3yf7cpJyGh2XCgIUIglmQNvg=\n" +
			  "-----END PRIVATE KEY-----\n";
			  /*
			"-----BEGIN RSA PRIVATE KEY-----\n" +
			"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\n" +
			"-----END RSA PRIVATE KEY-----\n";
			*/
		
		InputStream keystream = new java.io.ByteArrayInputStream(pemfile.getBytes());
		InputStream cipherstream = new java.io.ByteArrayInputStream(ciphertext);
		
		try {
			java.security.interfaces.RSAPrivateKey privateKey =
				com.kjwork.cryptofile.Util.openRSAPrivateKeyPEMFile(keystream);
			
			com.kjwork.cryptofile.PublicKeyEncapsulatedInputStream eis =
				PublicKeyEncapsulatedInputStream.open(cipherstream, privateKey);
			
			byte[] buf = new byte[ 2048 ];
			int n = eis.read(buf);
			byte[] copy;
			if (n >= 0) {
				copy = new byte[ n ];
				System.arraycopy(buf, 0, copy, 0, copy.length);
			} else {
				copy = new byte[ 0 ];
			}
			return copy;
			
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
}
