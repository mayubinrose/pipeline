package com.ctg.pipeline.execute.exceptions;

import java.util.Collections;
import java.util.Map;

/**
 * @author zhiHuang
 * @date 2022/11/14 21:01
 **/
public interface HasAdditionalAttributes {

    default Map<String, Object> getAdditionalAttributes() {
        return Collections.emptyMap();
    }
}