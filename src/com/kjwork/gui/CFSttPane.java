package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CFSttPane extends Panel implements ActionListener {

  private static CFSttPane instance = null;

  private CFSttPane
    ()
  {
    populate();
  }

  public static CFSttPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFSttPane();
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

    Label label = new Label(CFMenu.mtxt_status);
    gbl.setConstraints(label, c);
    this.add(label);

    explainer = new TextArea(
      ""
      , 6, 80, TextArea.SCROLLBARS_NONE
    );
    explainer.setEditable(false);
    gbl.setConstraints(explainer, c);
    this.add(explainer);

  }

  TextArea explainer;
  Button button_import;
}
