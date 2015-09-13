package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CFLogPane extends Panel implements ActionListener {

  private static CFLogPane instance = null;

  private CFLogPane
    ()
  {
    populate();
  }

  public static CFLogPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFLogPane();
    }
    return instance;
  }

  public void actionPerformed
    (ActionEvent e)
  {
  }

  public void log
    (String s)
  {
    String t = explainer.getText();
    t += s;
    explainer.setText(t);
    System.err.print(s);
  }

  public void logline
    (String s)
  {
    this.log(s + "\n");
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

    Label label = new Label(CFMenu.mtxt_log);
    gbl.setConstraints(label, c);
    this.add(label);

    explainer = new TextArea(
      ""
      , 6, 80, TextArea.SCROLLBARS_VERTICAL_ONLY
    );
    explainer.setEditable(false);
    gbl.setConstraints(explainer, c);
    this.add(explainer);
  }

  TextArea explainer;
}
