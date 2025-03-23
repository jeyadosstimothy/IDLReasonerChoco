package es.us.isa.idlreasonerchoco.utils;

import es.us.isa.idl.generator.ReservedWords;

public class Utils {

  private Utils() {}

  public static String parseIDLParamName(String paramName) {
    String parsedParamName =
        paramName.replaceAll("^\\[|\\]$", "").replaceAll("[\\.\\-\\/\\:\\[\\]]", "_");
    if (ReservedWords.RESERVED_WORDS.contains(parsedParamName)) parsedParamName += "_R";
    return parsedParamName;
  }
}
