package com.along.coash.server.admin.utils;

public class NamingUtils {
    public static String toUnderlineCase(CharSequence str) {
        return toSymbolCase(str, '_');
    }

    public static String toSymbolCase(CharSequence str, char symbol) {
        if (str == null) {
            return null;
        } else {
            int length = str.length();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < length; ++i) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    Character preChar = i > 0 ? str.charAt(i - 1) : null;
                    Character nextChar = i < str.length() - 1 ? str.charAt(i + 1) : null;
                    if (null != preChar) {
                        if (symbol == preChar) {
                            if (null == nextChar || Character.isLowerCase(nextChar)) {
                                c = Character.toLowerCase(c);
                            }
                        } else if (Character.isLowerCase(preChar)) {
                            sb.append(symbol);
                            if (null == nextChar || Character.isLowerCase(nextChar) || CharUtils.isNumber(nextChar)) {
                                c = Character.toLowerCase(c);
                            }
                        } else if (null != nextChar && Character.isLowerCase(nextChar)) {
                            sb.append(symbol);
                            c = Character.toLowerCase(c);
                        }
                    } else if (null == nextChar || Character.isLowerCase(nextChar)) {
                        c = Character.toLowerCase(c);
                    }
                }

                sb.append(c);
            }

            return sb.toString();
        }
    }

    public static String lowerFirst(CharSequence str) {
        if (null == str) {
            return null;
        } else {
            if (!str.isEmpty()) {
                char firstChar = str.charAt(0);
                if (Character.isUpperCase(firstChar)) {
                    return Character.toLowerCase(firstChar) + str.subSequence(0, 1).toString();
                }
            }

            return str.toString();
        }
    }
}
