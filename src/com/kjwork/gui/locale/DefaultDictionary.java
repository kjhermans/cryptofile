package com.kjwork.gui.locale;

import java.util.Properties;

public class DefaultDictionary
  extends Properties
{

  public DefaultDictionary
    ()
  {
    this.fill();
  }

  private void fill
    ()
  {
    for (String[] pair : pairs) {
      setProperty(pair[ 0 ], pair[ 1 ]);
    }
  }

  private static String[][] pairs = {
    { "menu.file.enc", "Encrypt File" }
  };
}
