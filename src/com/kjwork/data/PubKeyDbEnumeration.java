package com.kjwork.data;

import java.io.File;
import java.util.Enumeration;

class PubKeyDbEnumeration implements Enumeration<PubKeyRecord> {

  public PubKeyDbEnumeration
    (String path)
  {
    files = new File(path).listFiles();
    index = 0;
    scan();
  }

  public boolean hasMoreElements
    ()
  {
    return (index < files.length) ? true : false;
  }

  public PubKeyRecord nextElement
    ()
  {
    PubKeyRecord result =
      (index < files.length) ? new PubKeyRecord(files[index]) : null;
    ++index;
    scan();
    return result;
  }

  private void scan
    ()
  {
    for (; index < files.length; index++) {
      if (files[index].isFile() && files[index].getPath().endsWith(".pem")) {
        break;
      }
    }
  }

  private File[] files = null;
  private int index = 0;
}
