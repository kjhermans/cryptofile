package com.kjwork.gui;

import com.kjwork.data.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CFPrvPane extends Panel implements ActionListener {

  private static CFPrvPane instance = null;

  private CFPrvPane
    ()
  {
    populate();
    evaluate();
  }

  public static CFPrvPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFPrvPane();
    }
    return instance;
  }

  public void reEvaluate
    ()
  {
    evaluate();
  }

  private void actionGenerate
    ()
  {
    int result = JOptionPane.showConfirmDialog(
      MainWindow.getInstance(),
      "Would you like to generate a new 4096-bit RSA private key?",
      "Warning",
      JOptionPane.YES_NO_OPTION
    );
    if (result == JOptionPane.YES_OPTION) {
      try {
        PrivKeyStore.getInstance().generate();
        evaluate();
        CFEncPane.getInstance().repopulateRecipients();
        CFDecPane.getInstance().repopulateSigners();
        CFKeyPane.getInstance().repopulateKeys();
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(
          MainWindow.getInstance(),
          "Error during private key generation: " + ex.toString(),
          "Key Generation Warning",
          JOptionPane.WARNING_MESSAGE
        );
      }
    }
  }

  private void actionBrowse
    ()
  {
    JFileChooser chooser = new JFileChooser();
    int returnVal = chooser.showOpenDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      String path = chooser.getSelectedFile().getPath();
      System.err.println(path);
      file_in.setText(path);
    }
  }

  private void actionImport
    ()
  {
  }

  private void actionExport
    ()
  {
    JFileChooser chooser = new JFileChooser();
    int returnVal = chooser.showSaveDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      //..
    }
  }

  public void actionPerformed
    (ActionEvent e)
  {
    if (e.getSource() == button_gen) {
      actionGenerate();
    }
    if (e.getSource() == button_browse) {
      actionBrowse();
    }
    if (e.getSource() == button_import) {
      actionImport();
    }
    if (e.getSource() == button_export) {
      actionExport();
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

    Label label = new Label(CFMenu.mtxt_openprv);
    gbl.setConstraints(label, c);
    this.add(label);

    explainer = new TextArea(
      ""
      , 6, 80, TextArea.SCROLLBARS_NONE
    );
    explainer.setEditable(false);
    gbl.setConstraints(explainer, c);
    this.add(explainer);

    label = new Label("Generate Private Key:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    button_gen = new Button("Generate");
    button_gen.addActionListener(this);
    c.weightx = 0.2;
    c.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(button_gen, c);
    //button_gen.setBackground(Color.RED);
    this.add(button_gen);

    label = new Label("Import Private Key:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    file_in = new TextField();
    c.weightx = 1.0;
    gbl.setConstraints(file_in, c);
    this.add(file_in);

    button_browse = new Button("Browse");
    button_browse.addActionListener(this);
    c.weightx = 0.1;
    gbl.setConstraints(button_browse, c);
    this.add(button_browse);

    button_import = new Button("Import");
    button_import.addActionListener(this);
    c.weightx = 0.1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(button_import, c);
    //button_import.setBackground(Color.RED);
    this.add(button_import);

    label = new Label("Export Public Key:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    button_export = new Button("Export");
    button_export.addActionListener(this);
    c.weightx = 0.2;
    c.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(button_export, c);
    this.add(button_export);

  }

  private void evaluate
    ()
  {
    if (PrivKeyStore.getInstance().getPrivateKey() == null) {
      explainer.setText(
        "This pane contains all information about your private key.\n" +
        "This program has no private key installed. That means:\n" +
        "- You cannot decrypt files from other people.\n" +
        "If you want to decrypt files from other people, either:\n" +
        "- Generate a private key, or\n" +
        "- Import a private key.\n"
      );
      button_export.setEnabled(false);
    } else {
      explainer.setText(
        "This pane contains all information about your private key.\n" +
        "This program has a private key installed. That means:\n" +
        "- You can decrypt files from other people, provided:\n" +
        "- You have sent them your public key, which you can export here."
      );
      button_export.setEnabled(true);
    }
  }

  TextArea explainer;
  TextField file_in;
  Button button_gen;
  Button button_browse; 
  Button button_import; 
  Button button_export; 
}
