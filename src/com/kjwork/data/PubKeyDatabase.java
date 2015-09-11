package com.kjwork.data;

import java.util.Enumeration;
import java.io.File;

public class PubKeyDatabase {

  private static PubKeyDatabase instance = null;

  private PubKeyDatabase
    ()
  {
    scan();
  }

  public static PubKeyDatabase getInstance
    ()
  {
    if (instance == null) {
      instance = new PubKeyDatabase();
    }
    return instance;
  }

  public Enumeration<PubKeyRecord> getEntries
    ()
  {
    return new PubKeyDbEnumeration(path);
  }

  public void addEntry
    (PubKeyRecord r)
  {
  }

  private void scan
    ()
  {
    path = System.getProperty("user.home");
    String sep = System.getProperty("file.separator");
    path += sep + "cryptofile" + sep + "publickeys";
    File pubdir = new File(path);
    if (! pubdir.exists()) {
      if (! pubdir.mkdirs()) {
        System.err.println("Could not create directories.");
        return;
      }
    } else if (! pubdir.isDirectory()) {
      System.err.println(path + " exists, but is not a directory.");
      return;
    }
  }

  String path;
}
