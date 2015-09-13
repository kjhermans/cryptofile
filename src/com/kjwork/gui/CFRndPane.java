package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

public class CFRndPane extends Panel implements ActionListener, MouseMotionListener {

  private static CFRndPane instance = null;
  private TextField randomstring;
  private SecureRandom rng;
  private java.util.Random pseudo_rng;

  private CFRndPane
    ()
  {
    populate();
    try {
      rng = java.security.SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException nsae) {
      rng = new java.security.SecureRandom();
    }
    pseudo_rng = new java.util.Random();
    addMouseMotionListener(this);
  }

  public static CFRndPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFRndPane();
    }
    return instance;
  }

  public void actionPerformed
    (ActionEvent e)
  {
  }

  public SecureRandom getRNG
    ()
  {
    return rng;
  }

  public void mouseDragged
    (MouseEvent e)
  {
    enter_random(e.getX());
    enter_random(e.getY());
  }

  public void mouseMoved
    (MouseEvent e)
  {
    enter_random(e.getX());
    enter_random(e.getY());
  }

  private void enter_random
    (int n)
  {
    byte[] b = { (byte)n };
    rng.setSeed(b);
    String s = randomstring.getText();
    if (s.length() > 32) {
      s = s.substring(s.length() - 32, 32);
    }
    s += (char)((pseudo_rng.nextInt() % 26) + 64);
    randomstring.setText(s);
  }

  private void populate
    ()
  {
    GridBagLayout gbl = new GridBagLayout();
    this.setLayout(gbl);
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 10, 2, 10);
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;

    Label label = new Label(CFMenu.mtxt_random);
    gbl.setConstraints(label, c);
    this.add(label);
    label.addMouseMotionListener(this);

    explainer = new TextArea(
      "You can increase the entropy pool of the application," +
      " by moving your mouse randomly around inside the pane.\n" +
      "Progress of a pseudorandom bytestream (not your actual random)" +
      " will be displayed inside the textfield."
      , 6, 80, TextArea.SCROLLBARS_NONE
    );
    explainer.setEditable(false);
    gbl.setConstraints(explainer, c);
    this.add(explainer);
    explainer.addMouseMotionListener(this);

    label = new Label("Your random::");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);
    label.addMouseMotionListener(this);

    randomstring = new TextField();
    c.weightx = 1.0;
    gbl.setConstraints(randomstring, c);
    randomstring.setEditable(false);
    this.add(randomstring);
    randomstring.addMouseMotionListener(this);

  }

  TextArea explainer;
  Button button_import;
}
