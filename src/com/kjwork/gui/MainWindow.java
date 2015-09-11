package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;

public class MainWindow extends Frame implements WindowListener {

  private static MainWindow instance = null;

  private MainWindow
    ()
  {
    super("CryptoFile");
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

  private void populate
    ()
  {
    this.setMenuBar(CFMenu.getInstance());
    this.add(CFEncPane.getInstance());
    this.add(CFDecPane.getInstance());
    this.add(CFKeyPane.getInstance());
    this.add(CFPrvPane.getInstance());
    this.add(CFSecPane.getInstance());
    this.add(CFRndPane.getInstance());
    lm = new CardLayout();
    this.setLayout(lm);
    lm.addLayoutComponent(CFEncPane.getInstance(), "ENC");
    lm.addLayoutComponent(CFDecPane.getInstance(), "DEC");
    lm.addLayoutComponent(CFKeyPane.getInstance(), "KEY");
    lm.addLayoutComponent(CFPrvPane.getInstance(), "PRV");
    lm.addLayoutComponent(CFSecPane.getInstance(), "SEC");
    lm.addLayoutComponent(CFRndPane.getInstance(), "RND");
  }

  CardLayout lm;
}
