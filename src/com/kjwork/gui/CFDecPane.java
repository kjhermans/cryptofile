package com.kjwork.gui;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

import java.security.*;
import java.security.interfaces.*;

import com.kjwork.data.*;
import com.kjwork.cryptofile.*;

public class CFDecPane extends Panel implements ActionListener {

  private static CFDecPane instance = null;

  private CFDecPane
    ()
  {
    signers = new Choice();
    signerlist = new ArrayList<File>();
    repopulateSigners();
    populate();
  }

  public static CFDecPane getInstance
    ()
  {
    if (instance == null) {
      instance = new CFDecPane();
    }
    return instance;
  }

  public void actionPerformed
    (ActionEvent e)
  {
    if (e.getSource() == button_browse) {
      actionBrowse();
    } else if (e.getSource() == button_go) {
      actionDecrypt();
    }
  }

  public void repopulateSigners
    ()
  {
    signers.removeAll();
    signerlist.clear();
    signers.add("---- None ----");
    signerlist.add(null);
    Enumeration<PubKeyRecord> e = PubKeyDatabase.getInstance().getEntries();
    while (e.hasMoreElements()) {
      PubKeyRecord r = e.nextElement();
      signers.add(r.getOneliner());
      signerlist.add(r.getFile());
    }
  }

  private void actionBrowse
    ()
  {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter =
      new FileNameExtensionFilter("ENC Files", "enc");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(MainWindow.getInstance());
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      String path = chooser.getSelectedFile().getPath();
      if (path.endsWith(".enc")) {
        file_in.setText(path);
        String output = path.substring(0, path.length() - 4);
        file_out.setText(output);
      }
    }
  }

  private void actionDecrypt
    ()
  {
    String input = file_in.getText();
    String output = file_out.getText();
    RSAPrivateKey key = PrivKeyStore.getInstance().getPrivateKey();
    try {
      int r;
      InputStream in = PublicKeyEncapsulatedInputStream.open(input, key);
      OutputStream out = new FileOutputStream(output);
      while ((r = in.read()) >= 0) {
        out.write(r);
      }
      out.close();
      in.close();
      JOptionPane.showMessageDialog(
        MainWindow.getInstance(),
        "File was successfully decrypted to " + output,
        "Decryption Message",
        JOptionPane.WARNING_MESSAGE
      );
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
        MainWindow.getInstance(),
        "Exception during decryption: " + e,
        "Decryption Message",
        JOptionPane.WARNING_MESSAGE
      );
    } catch (PublicKeyEncapsulationError pe) {
      JOptionPane.showMessageDialog(
        MainWindow.getInstance(),
        "Exception during decryption: " + pe,
        "Decryption Message",
        JOptionPane.WARNING_MESSAGE
      );
    }
  }

  private void populate
    ()
  {
    GridBagLayout gbl = new GridBagLayout();
    this.setLayout(gbl);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;

    Label label = new Label(CFMenu.mtxt_opendec);
    gbl.setConstraints(label, c);
    this.add(label);

    explainer = new TextArea(
      "Open an encrypted file that you that you have received from someone, " +
      "and decrypt it."
      , 6, 80, TextArea.SCROLLBARS_NONE
    );
    explainer.setEditable(false);
    gbl.setConstraints(explainer, c);
    this.add(explainer);

    label = new Label("Encrypted file:");
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

    label = new Label("Require that the file be signed by:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    gbl.setConstraints(signers, c);
    this.add(signers);

    label = new Label("Decrypted file:");
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(label, c);
    this.add(label);

    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    file_out = new Label();
    gbl.setConstraints(file_out, c);
    this.add(file_out);

    button_go = new Button("Decrypt");
    button_go.addActionListener(this);
    //button_go.setBackground(Color.GREEN);
    c.gridwidth = 1;
    c.weightx = 0.2;
    gbl.setConstraints(button_go, c);
    this.add(button_go);
  }

  Choice signers;
  ArrayList<File> signerlist;
  TextArea explainer;
  private TextField file_in;
  private Label file_out;
  private Button button_go;
  private Button button_browse;
}
