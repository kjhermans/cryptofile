package com.kjwork.gui;

import java.awt.*;
import java.awt.event.*;

public class CFMenu extends MenuBar implements ActionListener {

  private static CFMenu instance = null;

  public static final String mtxt_opendec = "Open Encrypted File";
  public static final String mtxt_openenc = "Open File to Encrypt";
  public static final String mtxt_openkey = "Manage Public Keys";
  public static final String mtxt_openprv = "Manage Private Key";
  public static final String mtxt_status  = "Status";
  public static final String mtxt_log     = "Log";
  public static final String mtxt_random  = "Random";
  public static final String mtxt_opensec = "Secure This Application";
  public static final String mtxt_close   = "Quit Application";

  private CFMenu
    ()
  {
    populate();
  }

  public static CFMenu getInstance
    ()
  {
    if (instance == null) {
      instance = new CFMenu();
    }
    return instance;
  }

  private void populate
    ()
  {
    Menu menu_file = new Menu("File");
    menu_file.setShortcut(new MenuShortcut(KeyEvent.VK_F, false));
    MenuItem menuitem_openenc = new MenuItem(mtxt_openenc);
    MenuItem menuitem_opendec = new MenuItem(mtxt_opendec);
    MenuItem menuitem_openkey = new MenuItem(mtxt_openkey);
    MenuItem menuitem_openprv = new MenuItem(mtxt_openprv);
    MenuItem menuitem_close = new MenuItem(mtxt_close);
    this.add(menu_file);
    menu_file.add(menuitem_openenc);
    menu_file.add(menuitem_opendec);
    menu_file.addSeparator();
    menu_file.add(menuitem_openkey);
    menu_file.add(menuitem_openprv);
    menu_file.addSeparator();
    menu_file.add(menuitem_close);
    menuitem_openenc.addActionListener(this);
    menuitem_opendec.addActionListener(this);
    menuitem_openkey.addActionListener(this);
    menuitem_openprv.addActionListener(this);
    menuitem_close.addActionListener(this);

    Menu menu_tools = new Menu("Tools");
    MenuItem menuitem_status = new MenuItem(mtxt_status);
    MenuItem menuitem_log = new MenuItem(mtxt_log);
    MenuItem menuitem_random = new MenuItem(mtxt_random);
    MenuItem menuitem_opensec = new MenuItem(mtxt_opensec);
    menu_tools.add(menuitem_status);
    menu_tools.add(menuitem_log);
    menu_tools.add(menuitem_random);
    menu_tools.add(menuitem_opensec);
    menuitem_status.addActionListener(this);
    menuitem_log.addActionListener(this);
    menuitem_random.addActionListener(this);
    menuitem_opensec.addActionListener(this);
    this.add(menu_tools);

    Menu menu_help = new Menu("Help");
    menu_help.setShortcut(new MenuShortcut(KeyEvent.VK_H, false));
    MenuItem menu_about = new MenuItem("About");
    menu_help.add(menu_about);
    Menu menu_helpon = new Menu("On:");
    menu_help.add(menu_helpon);
    MenuItem menu_helpon_openenc = new MenuItem("Help on " + mtxt_openenc);
    MenuItem menu_helpon_opendec = new MenuItem("Help on " + mtxt_opendec);
    MenuItem menu_helpon_openkey = new MenuItem("Help on " + mtxt_openkey);
    MenuItem menu_helpon_openprv = new MenuItem("Help on " + mtxt_openprv);
    MenuItem menu_helpon_opensec = new MenuItem("Help on " + mtxt_opensec);
    menu_helpon.add(menu_helpon_openenc);
    menu_helpon.add(menu_helpon_opendec);
    menu_helpon.add(menu_helpon_openkey);
    menu_helpon.add(menu_helpon_openprv);
    menu_helpon.add(menu_helpon_opensec);
    menu_helpon_openenc.addActionListener(this);
    this.add(menu_help);
  }

  public void actionPerformed
    (ActionEvent e)
  {
    if (e.getSource() instanceof MenuItem) {
      MenuItem i = (MenuItem)(e.getSource());
      if (i.getLabel().equals(mtxt_close)) {
        System.exit(0);
      }
      if (i.getLabel().equals(mtxt_status)) {
        MainWindow.getInstance().showSttPane();
      }
      if (i.getLabel().equals(mtxt_opendec)) {
        MainWindow.getInstance().showDecPane();
      }
      if (i.getLabel().equals(mtxt_openenc)) {
        MainWindow.getInstance().showEncPane();
      }
      if (i.getLabel().equals(mtxt_openkey)) {
        MainWindow.getInstance().showKeyPane();
      }
      if (i.getLabel().equals(mtxt_openprv)) {
        MainWindow.getInstance().showPrvPane();
      }
      if (i.getLabel().equals(mtxt_opensec)) {
        MainWindow.getInstance().showSecPane();
      }
      if (i.getLabel().equals(mtxt_random)) {
        MainWindow.getInstance().showRndPane();
      }
      if (i.getLabel().equals(mtxt_log)) {
        MainWindow.getInstance().showLogPane();
      }
      if (i.getLabel().equals("Help on " + mtxt_openenc)) {
        CFHelpWindow.getInstance().doPopup("enc");
      }
    }
  }
}
