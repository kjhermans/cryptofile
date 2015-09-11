package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;

public class PubKeyImportDialog extends Dialog implements WindowListener {

  private static PubKeyImportDialog instance = null;

  private PubKeyImportDialog
    ()
  {
    super(MainWindow.getInstance(), "Import Public Key", true);
    populate();
    this.setMinimumSize(new Dimension(400, 300));
    this.addWindowListener(this);
  }

  public static PubKeyImportDialog getInstance
    ()
  {
    if (instance == null) {
      instance = new PubKeyImportDialog();
    }
    return instance;
  }

  public void doPopup
    (String path)
  {
    this.setVisible(true);
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
    this.setVisible(false);
  }

  private void populate
    ()
  {
    //..
  }
}
