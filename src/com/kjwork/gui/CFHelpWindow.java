package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class CFHelpWindow extends Frame implements WindowListener {

  private static CFHelpWindow instance = null;

  private CFHelpWindow
    ()
  {
    super("Help");
    populate();
    this.setMinimumSize(new Dimension(400, 300));
    this.addWindowListener(this);
  }

  public static CFHelpWindow getInstance
    ()
  {
    if (instance == null) {
      instance = new CFHelpWindow();
    }
    return instance;
  }

  public void doPopup
    (String path)
  {
    String text = "";
    try {
      int r;
      InputStream in = ClassLoader.getSystemResourceAsStream(
        "com/kjwork/help/" + path + ".txt"
      );
      while ((r = in.read()) >= 0) {
        text += (char)r;
      }
    } catch (Exception e) {
    }
    textarea.setText(text);
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
    textarea = new TextArea();
    textarea.setEditable(false);
    this.add(textarea);
  }

  TextArea textarea;
}
