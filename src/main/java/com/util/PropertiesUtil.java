

package com.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	
	private static Properties prop= new Properties();
	private static String filePath = "jdbc.properties";
	
	static{
		try {
			prop.load(ClassLoader.getSystemResourceAsStream(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void load(String _filePath){
		try {
			prop.load(ClassLoader.getSystemResourceAsStream(_filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Properties prop(){
		return prop;
	}
	
	public static String get(String key){
		return prop.getProperty(key);
	}
	

}
