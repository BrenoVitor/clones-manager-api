package br.com.liax.clonesManager.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
	
	private static final String CONNECTION_PROPERTIES = "src/main/resources/connection.properties";
	private static final String APPLICATION_PROPERTIES = "src/main/resources/application.properties";
	private static final String MESSAGES_PROPERTIES = "src/main/resources/messages.properties";
	private static Properties PROPERTIES = null;
	
	private LoadProperties() {
	}
	
	public static Properties getProperties() {
		if(PROPERTIES == null) {
			PROPERTIES = new Properties();
			loadConnectionProperties();
			loadApplicationProperties();
			loadMessagesProperties();
		} 
		return PROPERTIES;
	}
	
	private static void loadConnectionProperties() {
		loadProperties(CONNECTION_PROPERTIES);
	}
	
	private static void loadApplicationProperties() {
		loadProperties(APPLICATION_PROPERTIES);
	}
	
	private static void loadMessagesProperties() {
		loadProperties(MESSAGES_PROPERTIES);
	}

	private static void loadProperties(String path) {
		try (InputStream input = new FileInputStream(path)) {
			PROPERTIES.load(input);
		} catch (Exception e) {
			System.out.println("erro ao carregar as properties => ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
	
	

}
