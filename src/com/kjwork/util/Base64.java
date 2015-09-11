package com.kjwork.util;

public class Base64 {

    public static String encode(byte[] stringArray) {
        String encoded = "";
        int paddingCount = (3 - (stringArray.length % 3)) % 3;
        stringArray = zeroPad(stringArray.length + paddingCount, stringArray);
        for (int i = 0; i < stringArray.length; i += 3) {
            int j = ((stringArray[i] & 0xff) << 16) +
                ((stringArray[i + 1] & 0xff) << 8) + 
                (stringArray[i + 2] & 0xff);
            encoded = encoded +
            	base64code.charAt((j >> 18) & 0x3f) +
                base64code.charAt((j >> 12) & 0x3f) +
                base64code.charAt((j >> 6) & 0x3f) +
                base64code.charAt(j & 0x3f);
        }
        return splitLines(encoded.substring(0, encoded.length() -
            paddingCount) + "==".substring(0, paddingCount)); 
    }
    
    private static byte[] getAtom(char[] atom) {
    	byte[] bin = new byte[ 4 ];
    	byte[] result;
    	int resultsize = 0;
    	int block = 0;
    	for (int i=0; i < 4; i++) {
    		if (atom[ i ] == '=') {
    			break;
    		} else {
    			bin[ i ] = (byte)base64code.indexOf(atom[ i ]);
    			block += (bin[ i ] << (6 * (3 - i)));
    			resultsize = (int)Math.ceil(((i + 1) * 3) / 4);
    		}
    	}
    	result = new byte[ resultsize ];
    	for (int i=0; i < resultsize; i++) {
    		result[ i ] = (byte)((block >> (8 * ( 2 - i))) & 0xff);
    	}
    	return result;
    }

    private static byte[] appendAtom(byte[] source, char[] atom) {
    	byte[] bin = getAtom(atom);
    	byte[] result = new byte[ source.length + bin.length ];
    	System.arraycopy(source,  0, result, 0, source.length);
    	System.arraycopy(bin, 0, result, source.length, bin.length);
    	return result;
    }

    public static byte[] decode(String base64) {
    	char[] atom = new char[ 4 ];
    	int atomindex = 0;
    	byte[] result = new byte[ 0 ];
    	for (int i=0; i < base64.length(); i++) {
    		char cur = base64.charAt(i);
    		if (cur == '=' || base64code.indexOf(cur) >= 0) {
    			atom[ atomindex++ ] = cur;
    			if (atomindex == 4) {
    				result = appendAtom(result, atom);
    				atomindex = 0;
    			}
    		}
    	}
    	//result = appendAtom(result, atom);
    	return result;
    }
    
    private static final String base64code =
    	"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    	"abcdefghijklmnopqrstuvwxyz" +
    	"0123456789" +
    	"+/";
 
    private static final int splitLinesAt = 40;
 
    private static byte[] zeroPad(int length, byte[] bytes) {
        byte[] padded = new byte[length];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        return padded;
    }
     
    private static String splitLines(String string) {
        String lines = "";
        for (int i = 0; i < string.length(); i += splitLinesAt) {
            lines += string.substring(i, Math.min(string.length(), i + splitLinesAt));
            lines += "\r\n";
        }
        return lines;
    }
}