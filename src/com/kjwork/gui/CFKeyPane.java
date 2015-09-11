package com.kjwork.gui;

import com.kjwork.data.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CFKeyPane extends Panel implements ActionListener {

  private static CFKeyPane instance = null;

  private CFKeyPane
    ()
  {
    list = new List();
    paths = new ArrayList<File>();
    repopulateKeys();
    populate();
  }

  public static CFKeyPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFKeyPane();
    }
    return instance;
  }

  public void repopulateKeys
    ()
  {
    Enumeration<PubKeyRecord> e = PubKeyDatabase.getInstance().getEntries();
    while (e.hasMoreElements()) {
      PubKeyRecord r = e.nextElement();
      list.add(r.getOneliner());
      paths.add(r.getFile());
    }
  }

  private void actionImport
    ()
  {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter =
      new FileNameExtensionFilter("PEM Files", "pem");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      String path = chooser.getSelectedFile().getPath();
      System.err.println("Import public key file " + path);
      PubKeyImportDialog.getInstance().doPopup(path);
    }
  }

  private void actionEdit
    ()
  {
  }

  private void actionRemove
    ()
  {
  }

  public void actionPerformed
    (ActionEvent e)
  {
    if (e.getSource() == button_import) {
      actionImport();
    } else if (e.getSource() == button_edit) {
      actionEdit();
    } else if (e.getSource() == button_remove) {
      actionRemove();
    }
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

    Label label = new Label(CFMenu.mtxt_openkey);
    gbl.setConstraints(label, c);
    this.add(label);

    explainer = new TextArea(
      "This pane provides a way to manage the public keys of your contacts.\n"
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

    label = new Label("Existing Public Keys:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    c.weightx = 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    Panel p1 = new Panel();
    Panel p2 = new Panel();
    p1.setLayout(new BorderLayout());
    p1.add(list, BorderLayout.CENTER);
    p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
    p1.add(p2, BorderLayout.EAST);
    p2.add((button_edit = new Button("Edit")));
    p2.add((button_remove = new Button("Remove")));
    button_edit.addActionListener(this);
    button_remove.addActionListener(this);
    gbl.setConstraints(p1, c);
    this.add(p1);

  }

  List list;
  ArrayList<File> paths;
  TextArea explainer;
  Button button_import;
  Button button_edit;
  Button button_remove;
}
