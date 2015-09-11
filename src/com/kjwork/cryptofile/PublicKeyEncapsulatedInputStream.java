/**
 * 
 */
package com.kjwork.cryptofile;

import java.io.*;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.*;

import com.kjwork.util.Base64;

/**
 * @author kees
 *
 */
public class PublicKeyEncapsulatedInputStream extends InputStream {
		
	public static PublicKeyEncapsulatedInputStream open
		(String path, RSAPrivateKey privateKey)
	throws IOException,
		NoSuchAlgorithmException,
		NoSuchPaddingException,
		BadPaddingException,
		IllegalBlockSizeException,
		InvalidKeyException,
		PublicKeyEncapsulationError
	{
		InputStream in = new FileInputStream(new File(path));
		return open(in, privateKey);
	}

	public static PublicKeyEncapsulatedInputStream open
		(InputStream in, RSAPrivateKey privateKey)
	throws IOException,
		NoSuchAlgorithmException,
		NoSuchPaddingException,
		BadPaddingException,
		IllegalBlockSizeException,
		InvalidKeyException,
		PublicKeyEncapsulationError
	{
		PublicKeyEncapsulatedInputStream result =
			new PublicKeyEncapsulatedInputStream();
		int nbytes_header = get_key_length(privateKey.getPrivateExponent().bitLength());
		byte header[] = new byte[ nbytes_header ];
		int r = in.read(header);
		if (r != nbytes_header) {
			throw new PublicKeyEncapsulationError("Ciphertext too short.");
		}
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] paddedplaintext = cipher.doFinal(header);
		nbytes_header = paddedplaintext.length;
		/*
		while (--nbytes_header > 0) {
			if (paddedplaintext[ nbytes_header ] == 0) {
				break;
			}
		}
		*/
		if (nbytes_header < 48) {
			throw new PublicKeyEncapsulationError("Plaintext too short.");
		}
		result.inputstream = in;
		result.finbuf = new byte[ nbytes_header - 48 ];
		byte[] key = new byte[ 32 ];
		result.iv = new byte[ 16 ];
		System.arraycopy(paddedplaintext, 0, key, 0, 32);
		result.keySpec = new javax.crypto.spec.SecretKeySpec(key, "AES");
		result.cipher = Cipher.getInstance("AES/ECB/NoPadding");
		System.arraycopy(paddedplaintext, 32, result.iv, 0, 16);
		System.arraycopy(paddedplaintext, 48, result.finbuf, 0, result.finbuf.length);
		result.cryptoblock = new byte[ 16 ];
		int n = in.read(result.cryptoblock);
		if (n < 16) {
			result.cryptoblock = null;
		} else {
			result.unpad_plaintext();
		}
		return result;
	}

	public int read
		()
	throws IOException
	{
		try {
			if (offset == finbuf.length) {
				offset = 0;
				if (cryptoblock != null) {
					byte[] newcryptoblock = new byte[ 16 ];
					int n = inputstream.read(newcryptoblock);	
					decrypt_cryptoblock();
					if (n == -1) {
						cryptoblock = null;
					} else if (n != 16) {
						throw new IOException(
							new PublicKeyEncapsulationError(
								"File size must be multiple of 16."
							)
						);
					} else {
						cryptoblock = newcryptoblock;
					}
				} else {
					return -1;
				}
			}
			if (offset == 0 && cryptoblock == null) {
				unpad_plaintext();
			}
			if (offset < finbuf.length) {
				return (int)(finbuf[ offset++ ] & 0xff);
			} else {
				return -1;
			}
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse);
		} catch (PublicKeyEncapsulationError pkee) {
			throw new IOException(pkee);
		}
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
	
	private void unpad_plaintext
		()
	throws PublicKeyEncapsulationError
	{
		int i = finbuf.length;
		while (--i >= 0) {
			if (finbuf[ i ] == 0) {
				byte[] copy = new byte[ i ];
				System.arraycopy(finbuf, 0, copy, 0, copy.length);
				finbuf = copy;
				return;
			}
		}
		throw new PublicKeyEncapsulationError("Bad padding on last block");
	}
	
	private void decrypt_cryptoblock
		()
		throws BadPaddingException,
			IllegalBlockSizeException,
			IOException,
			PublicKeyEncapsulationError,
			InvalidKeyException
	{
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] intermediate = cipher.doFinal(this.cryptoblock);
		this.finbuf = xor(intermediate, this.iv, 16);
		this.iv = this.cryptoblock;
	}
	
	private static int get_key_length(int nbits)
		throws PublicKeyEncapsulationError
	{
		nbits /= 8;
		if (nbits > 512) nbits = 1024;
		else if (nbits > 256) nbits = 512;
		else if (nbits > 128) nbits = 128;		
		else if (nbits > 64) nbits = 128;
		else throw new PublicKeyEncapsulationError("Only support RSA 1024 - 8096");
		return nbits;
	}

	private java.io.InputStream inputstream = null;
//	private boolean eof = true;
	private byte[] finbuf = null;
	private int offset = 0;
	private javax.crypto.Cipher cipher;
	private javax.crypto.spec.SecretKeySpec keySpec;
	private byte[] iv = null;
	private byte[] cryptoblock;
}
