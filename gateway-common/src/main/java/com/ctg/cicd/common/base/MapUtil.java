package com.ctg.cicd.common.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 常用返回类
 *
 * @author huangZhi
 * @date 2023/05/23 19:23
 **/
public class MapUtil {

    public static Map<String, Object> mapData(String key, Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(key, data);
        return result;
    }

}