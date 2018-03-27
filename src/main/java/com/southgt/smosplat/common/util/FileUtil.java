package com.southgt.smosplat.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年9月30日     mohaolin       v1.0.0        create</p>
 *
 */
public class FileUtil {
	// 保存原始文件
	public static void saveSourceFile(String organUuid, String projectUuid, String uploadFileSrc, String fileName,
			String monitorItemCode, String sourceData) {
		// 保存原始数据
		try {
			File directory = new File(uploadFileSrc + "/" + organUuid + "/project/" + projectUuid + "/" + "SourceFile"+ "/" + monitorItemCode);
			if (!(directory.exists())) {
				directory.mkdirs();
			}
			String path = directory + "/" + fileName + "-" + monitorItemCode + ".txt";
			// 如果已经有同名文件，删除掉
			if (new File(path).exists()) {
				new File(path).delete();
			}
			File file = new File(path);
			FileWriter fw = new FileWriter(file);
			if (file.exists()) {
				// 文件存在，就写数据进去。
				fw.write(sourceData);
			} else {
				// 文件不存在就创建
				file.createNewFile();
			}
			fw.flush();
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// 保存mcu原始文件
	public static void saveMCUFile(String organUuid, String projectUuid, String uploadFileSrc, String fileName,
			String sn, String sourceData) {
		// 保存原始数据
		try {
			File directory = new File(uploadFileSrc + "/" + organUuid + "/project/" + projectUuid + "/" + "SourceFile" + "/" + "MCU");
			if (!(directory.exists())) {
				directory.mkdirs();
			}
			String path = directory + "/" + fileName + "-" + sn + ".txt";
			// 如果已经有同名文件，删除掉
			if (new File(path).exists()) {
				new File(path).delete();
			}
			File file = new File(path);
			FileWriter fw = new FileWriter(file);
			if (file.exists()) {
				// 文件存在，就写数据进去。
				fw.write(sourceData);
			} else {
				// 文件不存在就创建
				file.createNewFile();
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.error("mcu保存原始文件出错！"+e.toString());
		}
	}
}
