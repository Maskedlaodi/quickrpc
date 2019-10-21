package com.github.util;

import java.util.Optional;

/**
 * @author xiaobobo
 */
public class StringUtil {

    public static boolean isBank(String str) {
        if (!Optional.ofNullable(str).isPresent() || 0==str.length() || "".equals(str) || "null".equalsIgnoreCase(str))
            return true;
        return false;
    }

    public static boolean isNotBank(String str) {
        return !isBank(str);
    }
}
