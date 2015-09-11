package com.kjwork.cryptofile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import com.kjwork.util.Base64;

public class Util {
	
	public static void print_binary
		(byte[] array, OutputStream out, boolean neat)
	throws IOException
	{
		String result = "";
		String neatstring = "";
		int i, n = 0;
		for (i=0; i < array.length; i++) {
			Integer arg = (
				array[i] < 0 ? new Integer(array[i] + 256) : new Integer(array[i])
			);
			result += String.format("%02x ", arg);
			if (neat) {
				neatstring += (
					(array[i] >= 32 && array[i] < 127) ? (char)array[i] : "."
				);
				if (++n == 16) {
					result += "        " + neatstring + "\n";
					neatstring = "";
					n = 0;
				}
			}
		}
		if (neat && n != 0) {
			result += "                                             ".substring(0, (16-n)*3)
				+ "        " + neatstring + "\n";
		}
		out.write(result.getBytes());
	}
	
	public static void print_binary
		(byte[] array, OutputStream out)
	throws IOException
	{
		print_binary(array, out, true);
	}
	
	public static void print_binary
		(byte[] array, boolean neat)
	throws IOException
	{
		print_binary(array, System.err, neat);
	}
	
	public static void print_binary
		(byte[] array)
	throws IOException
	{
		print_binary(array, System.err, true);
	}
	
	public static RSAPrivateKey openRSAPrivateKeyPEMFile
		(InputStream fin)
	throws IOException,
		NoSuchAlgorithmException,
		UnrecoverableKeyException,
		InvalidKeySpecException
	{
		byte[] base64 = new byte[ 0 ];
		byte[] buf = new byte[ 1024 ];
		int r;
		while ((r = fin.read(buf)) > 0) {
			byte[] copy = new byte[ base64.length + buf.length ];
			System.arraycopy(base64, 0, copy, 0, base64.length);
			System.arraycopy(buf, 0, copy, base64.length, buf.length);
			base64 = copy;
		}
		fin.close();
		String pemstring = new String(base64, "ASCII");
		int begin = pemstring.indexOf("-----BEGIN RSA PRIVATE KEY-----");
		int end = pemstring.indexOf("-----END RSA PRIVATE KEY-----");
		String base64string;
		if (begin != -1 && end != -1 && end > begin) {
			base64string = pemstring.substring(begin + 31, end);
		} else {
			begin = pemstring.indexOf("-----BEGIN PRIVATE KEY-----");
			end = pemstring.indexOf("-----END PRIVATE KEY-----");
			if (begin != -1 && end != -1 && end > begin) {
				base64string = pemstring.substring(begin + 27, end);
			} else {
				throw new UnrecoverableKeyException("File is not a RSA PRIVATE KEY file.");
			}
		}
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		//System.err.println(base64string);
		byte[] res = Base64.decode(base64string);
		//com.kjwork.cryptofile.Util.print_binary(res);
		PKCS8EncodedKeySpec KeySpec = new PKCS8EncodedKeySpec(res);
		RSAPrivateKey privKey = (RSAPrivateKey)keyFactory.generatePrivate(KeySpec);
		return privKey;
	}
	
	public static RSAPublicKey openRSAPublicKeyPEMFile
		(String pemfile)
	throws IOException,
		NoSuchAlgorithmException,
		UnrecoverableKeyException,
		InvalidKeySpecException
	{
		FileInputStream fin = new FileInputStream(new File(pemfile));
		return openRSAPublicKeyPEMFile(fin);
	}
	
	public static RSAPublicKey openRSAPublicKeyPEMFile
		(InputStream fin)
	throws IOException,
		NoSuchAlgorithmException,
		UnrecoverableKeyException,
		InvalidKeySpecException
	{
		byte[] base64 = new byte[ 0 ];
		byte[] buf = new byte[ 1024 ];
		int r;
		while ((r = fin.read(buf)) > 0) {
			byte[] copy = new byte[ base64.length + buf.length ];
			System.arraycopy(base64, 0, copy, 0, base64.length);
			System.arraycopy(buf, 0, copy, base64.length, buf.length);
			base64 = copy;
		}
		fin.close();
		String pemstring = new String(base64, "ASCII");
		int begin = pemstring.indexOf("-----BEGIN RSA PUBLIC KEY-----");
		int end = pemstring.indexOf("-----END RSA PUBLIC KEY-----");
		String base64string;
		if (begin != -1 && end != -1 && end > begin) {
			base64string = pemstring.substring(begin + 30, end);
		} else {
			begin = pemstring.indexOf("-----BEGIN PUBLIC KEY-----");
			end = pemstring.indexOf("-----END PUBLIC KEY-----");
			if (begin != -1 && end != -1 && end > begin) {
				base64string = pemstring.substring(begin + 26, end);
			} else {
				throw new UnrecoverableKeyException("File is not a RSA PUBLIC KEY file.");
			}
		}
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] res = Base64.decode(base64string);
		X509EncodedKeySpec KeySpec = new X509EncodedKeySpec(res);
		RSAPublicKey pubKey = (RSAPublicKey)keyFactory.generatePublic(KeySpec);
		return pubKey;
	}

}