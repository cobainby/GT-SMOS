package com.southgt.smosplat.organ.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.DeviceType;

/**
 * 设备类型服务接口类
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月29日     YANG       v1.0.0        create</p>
 *
 */
public interface IDeviceTypeService extends IBaseService<DeviceType>  {

	List<DeviceType> getAllDevType();

	/**
	 * TODO(这里用一句话描述这个方法的作用) 
	 * @date  2017年5月6日 下午3:19:35
	 * @return DeviceType
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
	DeviceType getDeviceTypeByName(String name);
}
