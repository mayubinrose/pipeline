package com.ctg.cicd.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Random;

public class MachineUtils {

    private static final Logger logger = LoggerFactory.getLogger(MachineUtils.class);

    private static final String machineId;

    static {
        String ip = "";
        try {
            //获得本机IP　
            ip = InetAddress.getLocalHost().getHostAddress();
            logger.info("Succeed to get host ip! ip={}", ip);
        } catch (Exception e) {
            logger.error("Fail to get host ip! ip={}", ip);
        }

        if (ip.isEmpty()) {
            machineId = System.currentTimeMillis() + "" + new Random().nextInt(10);
        } else {
            machineId = ip + "_" + System.currentTimeMillis() + "" + new Random().nextInt(10);
        }
    }

    public static final String getMachineId() {
        return machineId;
    }
}
