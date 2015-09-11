package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CFSecPane extends Panel implements ActionListener {

  private static CFSecPane instance = null;

  private CFSecPane
    ()
  {
    populate();
  }

  public static CFSecPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFSecPane();
    }
    return instance;
  }

  public void actionPerformed
    (ActionEvent e)
  {
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

    Label label = new Label(CFMenu.mtxt_opensec);
    gbl.setConstraints(label, c);
    this.add(label);

    explainer = new TextArea(
      ""
      , 6, 80, TextArea.SCROLLBARS_NONE
    );
    explainer.setEditable(false);
    gbl.setConstraints(explainer, c);
    this.add(explainer);

    label = new Label("Import Public Key:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    button_import = new Button("Import");
    button_import.addActionListener(this);
    c.weightx = 0.2;
    c.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(button_import, c);
    this.add(button_import);

  }

  TextArea explainer;
  Button button_import;
}
