package com.luckypeng.interview.core.util;

/**
 * @author coalchan
 * @since 1.0
 */
public class AssertionUtils {
    private AssertionUtils() {}

    public static void notEmpty(Object obj, String message) {
        isTrue(!ObjectUtils.isEmpty(obj), message);
    }

    public static void isFalse(boolean bool, String message) {
        isTrue(!bool, message);
    }

    public static void isTrue(boolean bool, String message) {
        if (!bool) {
            throw new RuntimeException(message);
        }
    }
}
