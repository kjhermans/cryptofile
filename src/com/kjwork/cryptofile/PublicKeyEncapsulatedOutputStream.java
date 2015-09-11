package com.kjwork.cryptofile;

/*
 * File format description:
 * 
 */

import com.kjwork.util.Base64;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class PublicKeyEncapsulatedOutputStream extends java.io.OutputStream {
	
	private static final int PLAINTEXTUNDERHEADER = 112;
	
	private PublicKeyEncapsulatedOutputStream
		()
	{
	}
	
	public static PublicKeyEncapsulatedOutputStream open
		(String path, RSAPublicKey publicKey, SecureRandom rng)
	throws IOException,
		NoSuchAlgorithmException,
		NoSuchPaddingException
	{
		PublicKeyEncapsulatedOutputStream result =
				new PublicKeyEncapsulatedOutputStream();
		if (rng == null) {
			rng = java.security.SecureRandom.getInstance("SHA1PRNG");
		}
		result.rng = rng;
		result.pubkey = publicKey;
		result.createOutputStream(path);
		result.createSymmetricKey();
		result.cipher =
			Cipher.getInstance("AES/ECB/NoPadding");
		return result;
	}
	
	public static PublicKeyEncapsulatedOutputStream open
		(OutputStream out, RSAPublicKey publicKey, SecureRandom rng)
	throws IOException,
		NoSuchAlgorithmException,
		NoSuchPaddingException
	{
		PublicKeyEncapsulatedOutputStream result =
				new PublicKeyEncapsulatedOutputStream();
		if (rng == null) {
			rng = java.security.SecureRandom.getInstance("SHA1PRNG");
		}
		result.rng = rng;
		result.cipher =
			Cipher.getInstance("AES/ECB/NoPadding");
		result.pubkey = publicKey;
		result.fos = out;
		result.createSymmetricKey();
		return result;
	}

	public void write
		(int arg0)
	throws IOException
	{
		if (null == fosbuf) {
			fosbuf = new byte[1];
			fosbuf[0] = (byte)arg0;
		} else {
			byte[] copy = new byte[ fosbuf.length + 1];
			System.arraycopy(fosbuf, 0, copy, 0, fosbuf.length);
			fosbuf = copy;
			fosbuf[ fosbuf.length - 1 ] = (byte)arg0;
		}
		if (header) {
			if (fosbuf.length >= PLAINTEXTUNDERHEADER) {
				try {
					flushHeader();
				} catch (IOException ioe) {
					throw ioe;
				} catch (GeneralSecurityException gse) {
					throw new IOException(gse);
				}
				fosbuf = null;
			}
		} else {
			if (fosbuf.length >= 16) {
				try {
					flushBlock();
				} catch (IOException ioe) {
					throw ioe;
				} catch (GeneralSecurityException gse) {
					throw new IOException(gse);
				}
				fosbuf = null;
			}
		}
	}
	
	public void close
		()
	throws IOException
	{
		if (header) {
			try {
				flushHeader();
			} catch (IOException ioe) {
				throw ioe;
			} catch (GeneralSecurityException gse) {
				throw new IOException(gse);
			}
		} else {
			int times = 1;
			if (fosbuf != null && fosbuf.length == 16) {
				times = 2;
			}
			for (int i=0; i < times; i++) {
				try {
					flushBlock();
				} catch (IOException ioe) {
					throw ioe;
				} catch (GeneralSecurityException gse) {
					throw new IOException(gse);
				}
			}
		}
	}
	
	private void createOutputStream
		(String path)
		throws IOException
	{
		FileOutputStream myfos = new FileOutputStream(new File(path));
		myfos.getChannel().truncate(0);
		fos = myfos;
	}

	private void createSymmetricKey
		()
	throws NoSuchAlgorithmException,
		IOException
	{
		byte[] key = new byte[32];
		rng.nextBytes(key);
		keySpec = new javax.crypto.spec.SecretKeySpec(key, "AES");
		rng.nextBytes(iv);
		fosbuf = new byte[48];
		System.arraycopy(key, 0, fosbuf, 0, key.length);
		System.arraycopy(iv, 0, fosbuf, key.length, iv.length);
		//com.kjwork.cryptofile.Util.print_binary(fosbuf);
		header = true;
	}
	
	private byte[] padArray
		(byte[] array, int size)
	{
		if (array == null) {
			array = new byte[ 0 ];
		}
		if (array.length < size) {
			byte[] result = new byte[ size ];
			System.arraycopy(array, 0, result, 0, array.length);
			result[ array.length ] = 0;
			for (int i = array.length + 1; i < size; i++) {
				byte[] randomnotzero = new byte[ 1 ];
				while (true) {
					rng.nextBytes(randomnotzero);
					if ((result[ i ] = randomnotzero[ 0 ]) != 0) {
						break;
					}
				}
			}
			return result;
		} else {
			return array;
		}
	}
	
	private void flushHeader
		()
	throws BadPaddingException,
		IllegalBlockSizeException,
		InvalidKeyException,
		NoSuchAlgorithmException,
		NoSuchPaddingException,
		IOException
	{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		byte[] plaintext = padArray(fosbuf, PLAINTEXTUNDERHEADER + 8);
		byte[] cipherData = cipher.doFinal(plaintext);
		fos.write(cipherData);
		header = false;
	}
	
	private byte[] xor
		(byte[] array1, byte[] array2, int length)
	{
		byte[] result = new byte[ length ];
		for (int i=0; i < length; i++) {
			result[ i ] = (byte) (array1[ i ] ^ array2[ i ]);
		}
		return result;
	}
	
	/*
	 * Flushes a 16 byte block to disk when it's full. CBC mode.
	 */
	private void flushBlock
		()
	throws InvalidKeyException,
		BadPaddingException,
		IllegalBlockSizeException,
		IOException
	{
		byte[] plaintext = padArray(fosbuf, 16);
		byte[] intermediate = xor(plaintext, iv, 16);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		iv = cipher.doFinal(intermediate);
		fos.write(iv);
	}
	
	private java.security.SecureRandom rng;
	private byte[] iv = new byte[16];
	private javax.crypto.Cipher cipher;
	private javax.crypto.spec.SecretKeySpec keySpec;
	private boolean header = true;
	private java.security.interfaces.RSAPublicKey pubkey;
	private java.io.OutputStream fos;
	private byte[] fosbuf;
}
