package com.southgt.smosplat.project.service.autoCollect;

import java.util.HashMap;
import java.util.Map;

import com.southgt.smosplat.project.service.autoCollect.mcu.Impl.LRK_McuCollectLogic;
import com.southgt.smosplat.project.service.autoCollect.mcu.Impl.Speed_McuCollectLogic;

/**
 * 保存不同设备对应的不同处理逻辑的适配信息
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年8月17日     mohaolin       v1.0.0        create</p>
 *
 */
public class DeviceMapping {
	
	public static Map<String, String> operatorMapping=new HashMap<String, String>();

	static{
//		operatorMapping.put("全站仪-徕卡", Tps1200_COM.class.getName());
//		operatorMapping.put("全站仪-LEICA_1200", Tps1200_COM.class.getName());
//		operatorMapping.put("全站仪-LEICA_TS30", Tps1200_COM.class.getName());
//		operatorMapping.put("全站仪-LEICA_1800", Tps1000_COM.class.getName());
//		operatorMapping.put("全站仪-LEICA_2003", Tps1000_COM.class.getName());
//		operatorMapping.put("全站仪-SokkiaNET05", SokkiaSRXX_COM.class.getName());
//		operatorMapping.put("全站仪-SokkiaNET05AX", SokkiaSRXX_COM.class.getName());
//		operatorMapping.put("全站仪-SokkiaSRXX", SokkiaSRXX_COM.class.getName());
//		operatorMapping.put("全站仪-TopconMS05A", SokkiaSRXX_COM.class.getName());
		//后缀为0，采集前先所有mcu排队设置采集周期
		operatorMapping.put("mcu-斯比特_0", Speed_McuCollectLogic.class.getName());
		//后缀为1，第一次回数据的窗口时间进行设置
		operatorMapping.put("mcu-联睿科_1", LRK_McuCollectLogic.class.getName());
//		operatorMapping.put("轴力计", Speed_McuCollectLogic.class.getName());
//		operatorMapping.put("水位计", Speed_McuCollectLogic.class.getName());
//		operatorMapping.put("测斜仪", Speed_McuCollectLogic.class.getName());
	}
	
}
