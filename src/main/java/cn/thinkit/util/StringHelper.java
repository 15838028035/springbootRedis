package cn.thinkit.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
  private StringHelper () {
	  // 
	  throw new IllegalStateException("Utility class");
  }

  /**
   * 去除空格
   * 
   * @param str
   * @return
   */
  public static String trimBlank(String str) {
    return str == null ? "" : str.trim();
  }

  public static boolean isBlank(String str) {
    return str == null || str.trim().length() == 0;
  }

  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }

  public static String emptyIf(String value, String defaultValue) {
    if ((value == null) || ("".equals(value))) {
      return defaultValue;
    }
    return value;
  }

  public static String defaultString(Object value) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

  public static String defaultIfEmpty(Object value, String defaultValue) {
    if (value == null || "".equals(value)) {
      return defaultValue;
    }
    return value.toString();
  }

  public static String capitalize(String str) {
    return changeFirstCharacterCase(str, true);
  }

  public static String uncapitalize(String str) {
    return changeFirstCharacterCase(str, false);
  }

  private static String changeFirstCharacterCase(String str, boolean capitalize) {
    if ((str == null) || (str.length() == 0)) {
      return str;
    }
    StringBuilder buf = new StringBuilder(str.length());
    if (capitalize) {
      buf.append(Character.toUpperCase(str.charAt(0)));
    } else {
      buf.append(Character.toLowerCase(str.charAt(0)));
    }
    buf.append(str.substring(1));
    return buf.toString();
  }

  public static String join(Object[] array, String seperator) {
    if (array == null)
      return null;
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      result.append(array[i]);
      if (i != array.length - 1) {
        result.append(seperator);
      }
    }
    return result.toString();
  }

  public static String getExtension(String str) {
    if (str == null)
      return null;
    int i = str.lastIndexOf('.');
    if (i >= 0) {
      return str.substring(i + 1);
    }
    return null;
  }

  public static int indexOfByRegex(String input, String regex) {
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    if (m.find()) {
      return m.start();
    }
    return -1;
  }

  /**
   * Test whether the given string matches the given substring at the given index.
   * 
   * @param str
   *          the original string (or StringBuilder)
   * @param index
   *          the index in the original string to start matching against
   * @param substring
   *          the substring to match at the given index
   */
  public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
    for (int j = 0; j < substring.length(); j++) {
      int i = index + j;
      if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
        return false;
      }
    }
    return true;
  }

  /**
	 * Count the occurrences of the substring in string s.
	 * @param str string to search in. Return 0 if this is null.
	 * @param sub string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
}
}