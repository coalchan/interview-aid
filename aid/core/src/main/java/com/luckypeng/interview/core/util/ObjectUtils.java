package com.luckypeng.interview.core.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author coalchan
 * @since 1.0
 */
public class ObjectUtils {
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }

    public static <T> T coalesce(T... objs) {
        for (T obj: objs) {
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }
}
