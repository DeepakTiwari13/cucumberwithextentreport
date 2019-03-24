package com.citrus.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import com.cucumber.listener.Reporter;

public class Fileutil {

	public Properties prop = null;
	FileInputStream fis = null;

	public Fileutil() {

		if (prop == null) {
			prop = new Properties();
			try {
				FileInputStream fis = new FileInputStream(
						System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties");
				try {
					prop.load(fis);
				} catch (IOException e) {
					Reporter.addStepLog("Error loading in input file " + e.fillInStackTrace());
				}
			} catch (FileNotFoundException e) {
				Reporter.addStepLog("Error loading in property file " + e.fillInStackTrace());
			}
		}
	}
}
