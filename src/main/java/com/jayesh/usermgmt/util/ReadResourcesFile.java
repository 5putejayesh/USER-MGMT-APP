package com.jayesh.usermgmt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReadResourcesFile {

	private static Logger logger=LoggerFactory.getLogger(ReadResourcesFile.class);

	public static String getFileDataAsStringSat(String fileName, String fullName, String password, String url) {
		String fileData = null;
		try (InputStream inputStream = ReadResourcesFile.class.getClassLoader().getResourceAsStream(fileName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			fileData = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			fileData = fileData.replace("{FULL-NAME}", fullName);
			fileData = fileData.replace("{TEMP-PWD}", password);
			fileData = fileData.replace("{URL}", url);
			fileData = fileData.replace("{PWD}", password);
		} catch (IOException e) {
			logger.error("Exception occurred",e);
		}

		return fileData;
	}
	
	public  String getFileDataAsString(String fileName, String fullName, String password, String url) {
		String fileData = null;
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			fileData = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			fileData = fileData.replace("{FULL-NAME}", fullName);
			fileData = fileData.replace("{TEMP-PWD}", password);
			fileData = fileData.replace("{URL}", url);
			fileData = fileData.replace("{PWD}", password);
		} catch (IOException e) {
			logger.error("Exception occurred",e);
		}

		return fileData;
	}
}
