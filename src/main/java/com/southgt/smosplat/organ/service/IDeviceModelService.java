package com.southgt.smosplat.organ.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.DeviceModel;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月2日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IDeviceModelService extends IBaseService<DeviceModel> {

	/**
	 * TODO(这里用一句话描述这个方法的作用) 
	 * @date  2017年5月6日 下午3:25:48
	 * @return DeviceModel
	 * @param devModelName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月6日     mohaolin      v1.0          create</p>
	 *
	 */
	DeviceModel getDeviceModelByName(String devModelName);

	/**
	 * 获取名称为**的设备类型的所有设备型号
	 * 
	 * @date  2017年5月8日 上午9:24:17
	 * @param @param devTypeName
	 * @param @return
	 * @return List<DeviceModel>
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月8日      杨杰     	   v1.0          create</p>
	 *
	 */
	List<DeviceModel> getDeviceModelByDeviceType(String devTypeName);
}
