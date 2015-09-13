package com.kjwork.gui;

import java.util.Enumeration;
import com.kjwork.data.*;
import javax.swing.JOptionPane;
import java.util.Locale;

public class Main {

  static MainWindow w;

  public static void main
    (String[] args)
  {
    w = MainWindow.getInstance();
    check_publickeys();
    check_privatekey();
    w.setVisible(true);
  }

  private static void check_publickeys
    ()
  {
    Enumeration<PubKeyRecord> e = PubKeyDatabase.getInstance().getEntries();
    if (null == e || false == e.hasMoreElements()) {
      JOptionPane.showMessageDialog(w,
        "No public keys found.",
        "Key Management Warning",
        JOptionPane.WARNING_MESSAGE
      );
      return;
    }
  }

  private static void check_privatekey
    ()
  {
    if (null == PrivKeyStore.getInstance().getPrivateKey()) {
      JOptionPane.showMessageDialog(w,
        "Private key not found.",
        "Key Management Warning",
        JOptionPane.WARNING_MESSAGE
      );
    }
  }
}
