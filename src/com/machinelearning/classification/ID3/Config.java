package com.machinelearning.classification.ID3;
import java.io.FileInputStream;
import java.util.Properties;

public class Config {
	
	public static Properties config;
	
	public static void initialize() {
		config = new Properties();
		try {
			config.load(new FileInputStream("./config/config.properties"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public static String readConfig(String key) {
		String propertyValue = null;
		try {
			if (config == null) {
				initialize();
			}

			propertyValue = config.getProperty(key);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return propertyValue;
	}

}