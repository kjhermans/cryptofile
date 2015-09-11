package com.kjwork.data;

import java.io.File;
import com.kjwork.util.FileGrab;

public class PubKeyRecord {

  public PubKeyRecord
    (File f)
  {
    file = f;
    oneliner = file.getName();
    if (oneliner.endsWith(".pem")) {
      String record = new String(file.getPath());
      record = record.substring(0, record.length() - 4) + ".dat";
      File data = new File(record);
      if (data.exists()) {
        oneliner = FileGrab.getContentAsString(data);
      }
    }
  }

  public String getOneliner
    ()
  {
    return oneliner;
  }

  public String getPath
    ()
  {
    return file.getPath();
  }

  public File getFile
    ()
  {
    return file;
  }

  private File file;
  String oneliner;
}
