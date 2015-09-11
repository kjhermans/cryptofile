package com.kjwork.util;

import java.io.*;

public class FileGrab {

  public static byte[] getContent
    (File file)
  {
    try {
      FileInputStream fin = new FileInputStream(file);
      int r, c = 0;
      byte[] result = new byte[ 1024 ];
      while ((r = fin.read()) >= 0) {
        result[ c++ ] = (byte)r;
        if (c == result.length) {
          byte[] copy = new byte[ result.length + 1024 ];
          System.arraycopy(result, 0, copy, 0, result.length);
          result = copy;
        }
      }
      if (c != result.length) {
        byte[] copy = new byte[ c ];
        System.arraycopy(result, 0, copy, 0, c);
        result = copy;
      }
      return result;
    } catch (IOException ioe) {
      return null;
    }
  }

  public static byte[] getContent
    (String path)
  {
    return getContent(new File(path));
  }

  public static String getContentAsString
    (String path, String encoding)
  {
    try {
      return new String(getContent(path), encoding);
    } catch (UnsupportedEncodingException uee) {
      return null;
    }
  }

  public static String getContentAsString
    (String path)
  {
    return getContentAsString(path, "ASCII");
  } 

  public static String getContentAsString
    (File file, String encoding)
  {
    try {
      return new String(getContent(file), encoding);
    } catch (UnsupportedEncodingException uee) {
      return null;
    }
  }

  public static String getContentAsString
    (File file)
  {
    return getContentAsString(file, "ASCII");
  } 
}
