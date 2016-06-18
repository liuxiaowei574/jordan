/**
 * 
 */
package com.nuctech.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author sunming
 *
 */
public class MemcachedServerProperties {

	public static final String SERVERS = "servers";

	// 属性文件的路径
	static String proFilePath = "/serverconfig.properties";
	/**
	 * 采用静态方法
	 */
	private static Properties props = new Properties();
	static {
		try {
			InputStream in = MemcachedServerProperties.class.getResourceAsStream(proFilePath);
			props.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.exit(-1);
		}
	}

	/**
	 * 读取属性文件中相应键的值
	 * 
	 * @param key
	 *            主键
	 * @return String
	 */
	public static String getKeyValue(String key) {
		return props.getProperty(key);
	}

}
