package pers.silonest.component.base.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pers.silonest.component.util.StringUtils;

public class StreamUtils {

  private static final String DEFAULT_ENCODING = "utf-8";

  public static String inputStream2String(InputStream in, String charsetName) {
    if (in == null) {
      return null;
    }
    InputStreamReader inReader = null;
    try {
      if (StringUtils.isEmpty(charsetName)) {
        inReader = new InputStreamReader(in, DEFAULT_ENCODING);
      } else {
        inReader = new InputStreamReader(in, charsetName);
      }
      int readLen = 0;
      char[] buffer = new char[1024];
      StringBuffer strBuf = new StringBuffer();
      while ((readLen = inReader.read(buffer)) != -1) {
        strBuf.append(buffer, 0, readLen);
      }
      return strBuf.toString();
    } catch (IOException e) {
    } finally {
      closeStream(inReader);
    }

    return null;
  }

  public static void closeStream(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
      }
    }
  }
}
