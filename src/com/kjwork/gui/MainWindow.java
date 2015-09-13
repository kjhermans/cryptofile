package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class MainWindow extends Frame implements WindowListener {

  private static MainWindow instance = null;

  private MainWindow
    ()
  {
    super("CryptoFile");
    this.load_dictionary();
    populate();
    this.setMinimumSize(new Dimension(600, 400));
    this.addWindowListener(this);
  }

  public static MainWindow getInstance
    ()
  {
    if (instance == null) {
      instance = new MainWindow();
    }
    return instance;
  }

  public void showSttPane
    ()
  {
    lm.show(this, "STT");
  }

  public void showEncPane
    ()
  {
    lm.show(this, "ENC");
  }

  public void showDecPane
    ()
  {
    lm.show(this, "DEC");
  }

  public void showKeyPane
    ()
  {
    lm.show(this, "KEY");
  }

  public void showPrvPane
    ()
  {
    lm.show(this, "PRV");
  }

  public void showSecPane
    ()
  {
    lm.show(this, "SEC");
  }

  public void showRndPane
    ()
  {
    lm.show(this, "RND");
  }

  public void showLogPane
    ()
  {
    lm.show(this, "LOG");
  }

  public void windowOpened
    (WindowEvent we)
  {
  }

  public void windowActivated
    (WindowEvent we)
  {
  }

  public void windowDeactivated
    (WindowEvent we)
  {
  }

  public void windowIconified
    (WindowEvent we)
  {
  }

  public void windowDeiconified
    (WindowEvent we)
  {
  }

  public void windowClosed
    (WindowEvent we)
  {
  }

  public void windowClosing
    (WindowEvent we)
  {
    System.exit(0);
  }

  private void load_dictionary
    ()
  {
    try {
      String localestring = Locale.getDefault().toString();
      String dictfile = "com/kjwork/gui/locale/" + localestring + ".dict";
      InputStream in = getClass().getClassLoader().getResourceAsStream(dictfile);
      if (in != null) {
        dict.load(in);
        System.err.println("Successfully loaded dictionary " + dictfile);
        return;
      }
    } catch (IOException ioe) {
      System.err.println(ioe);
    }
    System.err.println("Loading default dictionary");
    dict = new com.kjwork.gui.locale.DefaultDictionary();
  }

  private void populate
    ()
  {
    this.setMenuBar(CFMenu.getInstance());
    this.add(CFSttPane.getInstance());
    this.add(CFLogPane.getInstance());
    this.add(CFEncPane.getInstance());
    this.add(CFDecPane.getInstance());
    this.add(CFKeyPane.getInstance());
    this.add(CFPrvPane.getInstance());
    this.add(CFSecPane.getInstance());
    this.add(CFRndPane.getInstance());
    lm = new CardLayout();
    this.setLayout(lm);
    lm.addLayoutComponent(CFSttPane.getInstance(), "STT");
    lm.addLayoutComponent(CFLogPane.getInstance(), "LOG");
    lm.addLayoutComponent(CFEncPane.getInstance(), "ENC");
    lm.addLayoutComponent(CFDecPane.getInstance(), "DEC");
    lm.addLayoutComponent(CFKeyPane.getInstance(), "KEY");
    lm.addLayoutComponent(CFPrvPane.getInstance(), "PRV");
    lm.addLayoutComponent(CFSecPane.getInstance(), "SEC");
    lm.addLayoutComponent(CFRndPane.getInstance(), "RND");
  }

  private Properties dict = new Properties();
  private CardLayout lm;
}
