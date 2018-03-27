package com.southgt.smosplat.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	/**
	 * 
	 * 读取配置文件
	 * @date  2017年3月31日 下午1:04:57
	 * 
	 * @param filename
	 * @return
	 * Properties
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月31日     姚家俊      v1.0          create</p>
	 *
	 */
	public static Properties readProperties(String filename){
		File file = new File(filename);
		Properties properties = new Properties();
		try{
		if (file.exists()) {
		  InputStream inputstream = new FileInputStream(file);
		  properties.load(inputstream);
		  properties.getProperty("key");
		}
		}catch(Exception ex){
			
		}
		return properties;
	}
}