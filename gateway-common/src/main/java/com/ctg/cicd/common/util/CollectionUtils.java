
package com.ctg.cicd.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.*;


/**
 * 集合工具类
 * 
 * 
 */
@Slf4j
public class CollectionUtils {

    /**
     * 判断一个集合对象是否为null或为空
     *
     * @param collection
     *            要检测的集合对象
     * @return 若集合对象为null或为空，则返回true；否则返回false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断一个集合对象是否不为null且不为空
     *
     * @param collection
     *            要检测的集合对象
     * @return 若集合对象不为null且不为空，则返回true；否则返回false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !CollectionUtils.isEmpty(collection);
    }

    /**
     * Return <code>true</code> if the supplied Map is <code>null</code> or
     * empty. Otherwise, return <code>false</code>.
     *
     * @param map
     *            the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     */
    public static String convertToString(final Collection collection, final String separator) {
        StringBuilder outStr = new StringBuilder();
        for (Object obj : collection) {
            if (obj == null) {
                continue;
            }
            if (outStr.length() > 0) {
                outStr.append(separator);
            }
            outStr.append(obj);
        }
        return outStr.toString();
    }

    /**
     * 返回a+b的新List.
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        Set<T> result = new HashSet<>(a);
        if (null != b) {
            result.addAll(b);
        }
        return new ArrayList<>(result);
    }
    
}
