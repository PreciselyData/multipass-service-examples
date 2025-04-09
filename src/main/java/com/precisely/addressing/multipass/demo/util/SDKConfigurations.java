package com.precisely.addressing.multipass.demo.util;

import com.precisely.addressing.multipass.sdk.exception.ConfigLoadFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.precisely.addressing.multipass.sdk.util.Constant.DOT;
import static com.precisely.addressing.multipass.sdk.util.Constant.UNDERSCORE;

public class SDKConfigurations {
    private static final Logger log = LoggerFactory.getLogger(SDKConfigurations.class);
    private static final Properties globalProperties = initiate();

    private static Properties initiate() {
        Properties props = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = null;
        try {
            is = classloader.getResourceAsStream("sdk.properties");
            props.load(is);
            if (is != null) {
                is.close();
            }
            return overrideConfigurations(props);
        } catch (IOException e) {
            log.error("Unable to load properties", e);
            throw new ConfigLoadFailedException("Unable to load properties", e);
        }
    }

    private SDKConfigurations() {
    }

    private static Properties overrideConfigurations(Properties properties) {
        properties.forEach((key, value) -> {
            String systemValue = System.getProperty((String) key); // Check System Properties
            String envValue = System.getenv(((String) key).replace(DOT, UNDERSCORE).toUpperCase()); // Check Env Vars (Converted)

            if (systemValue != null) {
                properties.setProperty((String) key, systemValue);
            } else if (envValue != null) {
                properties.setProperty((String) key, envValue);
            }
        });
        return properties;
    }

    public static String getString(String key) {
        return globalProperties.getProperty(key);
    }

}
