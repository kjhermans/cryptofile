package com.kjwork.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.security.*;
import java.security.interfaces.*;

import com.kjwork.data.*;
import com.kjwork.cryptofile.*;

public class CFEncPane extends Panel implements ActionListener {

  private static CFEncPane instance = null;

  private CFEncPane
    ()
  {
    populate();
    repopulateRecipients();
  }

  public static CFEncPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFEncPane();
    }
    return instance;
  }

  public void actionPerformed
    (ActionEvent e)
  {
    if (e.getSource() == button_browse) {
      actionBrowse();
    } else if (e.getSource() == button_go) {
      actionEncrypt();
    }
  }

  public void repopulateRecipients
    ()
  {
    recipients.removeAll();
    Enumeration<PubKeyRecord> e = PubKeyDatabase.getInstance().getEntries();
    while (e.hasMoreElements()) {
      PubKeyRecord r = e.nextElement();
      recipients.add(r.getOneliner());
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
      file_out.setText(path + ".enc");
    }
  }

  private void actionEncrypt
    ()
  {
    String path = file_in.getText();
    String pubkeypath = recipients.getSelectedItem();
    String outpath = file_out.getText();
    int result = JOptionPane.showConfirmDialog(
      MainWindow.getInstance(),
      "Would you like to encrypt file " + path + " for " + pubkeypath,
      "Warning",
      JOptionPane.YES_NO_OPTION
    );
    if (result == JOptionPane.YES_OPTION) {
      try {
        RSAPublicKey key = Util.openRSAPublicKeyPEMFile(pubkeypath);
        InputStream in = new FileInputStream(path);
        OutputStream out = PublicKeyEncapsulatedOutputStream.open(outpath, key, CFRndPane.getInstance().getRNG());
        int r;
        while ((r = in.read()) >= 0) {
          out.write(r);
        }
        out.close();
        in.close();
        JOptionPane.showMessageDialog(
          MainWindow.getInstance(),
          "File was successfully encrypted to " + outpath,
          "Eccryption Message",
          JOptionPane.WARNING_MESSAGE
        );
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
          MainWindow.getInstance(),
          "Exception during encryption: " + e,
          "Encryption Message",
          JOptionPane.WARNING_MESSAGE
        );
      }
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

    Label label = new Label(CFMenu.mtxt_openenc);
    gbl.setConstraints(label, c);
    this.add(label);

    TextArea t = new TextArea(
      "Open a normal file that you want to encrypt for someone.\n" +
      "Select a recipient from the list, select a file, and press " +
      "the 'Encrypt' button."
      , 5, 80, TextArea.SCROLLBARS_NONE
    );
    t.setEditable(false);
    gbl.setConstraints(t, c);
    this.add(t);

    c.gridwidth = 1;
    c.weightx = 0.2;
    label = new Label("Recipient:");
    gbl.setConstraints(label, c);
    this.add(label);

    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    recipients = new Choice();
    gbl.setConstraints(recipients, c);
    this.add(recipients);

    label = new Label("File to be encrypted:");
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
    c.weightx = 0.2;
    c.gridwidth = GridBagConstraints.REMAINDER;
    gbl.setConstraints(button_browse, c);
    this.add(button_browse);

    label = new Label("Encrypted file:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    file_out = new Label();
    gbl.setConstraints(file_out, c);
    this.add(file_out);

    button_go = new Button("Encrypt");
    button_go.addActionListener(this);
    //button_go.setBackground(Color.RED);
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(button_go, c);
    this.add(button_go);
  }

  private TextField file_in;
  private Label file_out;
  private Choice recipients;
  private Button button_go;
  private Button button_browse;
}
