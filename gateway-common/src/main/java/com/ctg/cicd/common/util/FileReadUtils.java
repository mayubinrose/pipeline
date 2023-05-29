package com.ctg.cicd.common.util;

import java.io.InputStream;
import java.util.Scanner;

public class FileReadUtils {

    public static String read(String fileName) {
        InputStream ins = FileReadUtils.class.getClassLoader().getResourceAsStream(fileName);
        Scanner scanner = new Scanner(ins);
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            builder.append(line + "\n");
        }
        scanner.close();
        return builder.toString();
    }
}
