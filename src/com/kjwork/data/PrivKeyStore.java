package com.kjwork.data;

import java.io.*;
import java.security.*;
import java.security.interfaces.*;
import com.kjwork.gui.*;

public class PrivKeyStore {

  private static PrivKeyStore instance = null;

  private PrivKeyStore
    ()
  {
    scan();
  }

  public static PrivKeyStore getInstance
    ()
  {
    if (instance == null) {
      instance = new PrivKeyStore();
    }
    return instance;
  }

  public RSAPrivateKey getPrivateKey
    ()
  {
    return privkey;
  }

  public void generate
    ()
    throws GeneralSecurityException,
    IOException
  {
    String path = System.getProperty("user.home");
    String sep = System.getProperty("file.separator");
    String privpath = path + sep + "cryptofile" + sep + "privatekey.pem";
    String pubpath =
      path + sep + "cryptofile" + sep + "publickeys" + sep + "self.pem";
    String datpath =
      path + sep + "cryptofile" + sep + "publickeys" + sep + "self.dat";

    KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
    gen.initialize(4096);
    KeyPair key = gen.generateKeyPair();
    PrivateKey privateKey = key.getPrivate();
    PublicKey publicKey = key.getPublic();

    FileOutputStream pvt = new FileOutputStream(privpath);
    try {
      String pem =
        "-----BEGIN PRIVATE KEY-----\n\n" + 
        com.kjwork.util.Base64.encode(privateKey.getEncoded()) + "\n\n" +
        "-----END PRIVATE KEY-----\n";
      pvt.write(pem.getBytes("ASCII"));
      pvt.flush();
    } finally {
      pvt.close();
    }

    FileOutputStream pub = new FileOutputStream(pubpath);
    try {
      String pem =
        "-----BEGIN PUBLIC KEY-----\n\n" + 
        com.kjwork.util.Base64.encode(publicKey.getEncoded()) + "\n\n" +
        "-----END PUBLIC KEY-----\n";
      pub.write(pem.getBytes("ASCII"));
      pub.flush();
    } finally {
      pub.close();
    }

    FileOutputStream dat = new FileOutputStream(datpath);
    try {
      String pem = "Your own public key";
      dat.write(pem.getBytes("ASCII"));
      dat.flush();
    } finally {
      dat.close();
    }

    rescan();
  }

  public void rescan
    ()
  {
    scan();
  }

  private void scan
    ()
  {
    String path = System.getProperty("user.home");
    String sep = System.getProperty("file.separator");
    path += sep + "cryptofile";
    File privdir = new File(path);
    if (! privdir.exists()) {
      if (! privdir.mkdirs()) {
        System.err.println("Could not create directories.");
        return;
      }
    } else if (! privdir.isDirectory()) {
      System.err.println(path + " exists, but is not a directory.");
      return;
    }
    path += sep + "privatekey.pem";
    File privkeyfile = new File(path);
    if (privkeyfile.exists()) {
      try {
        privkey = com.kjwork.cryptofile.Util.openRSAPrivateKeyPEMFile(
          new FileInputStream(privkeyfile)
        );
        CFLogPane.getInstance().logline("Private key successfully loaded.");
      } catch (IOException ioe) {
        System.err.println("Couldn't load private key " + path + ":" + ioe);
      } catch (GeneralSecurityException gse) {
        System.err.println("Couldn't load private key " + path + ":" + gse);
      }
    } else {
      System.err.println("Private key not found.");
    }
  }

  private RSAPrivateKey privkey = null;
}
