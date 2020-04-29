package com.github.geekercui.generator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static Properties properties;
    static {
        properties = new Properties();
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream("rap.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return (String) properties.get(key);

    }
}
