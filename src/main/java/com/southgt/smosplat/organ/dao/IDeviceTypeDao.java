package com.southgt.smosplat.organ.dao;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.DeviceType;

/**
 * 设备类型数据库访问接口类
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
public interface IDeviceTypeDao extends IBaseDao<DeviceType> {

	/**
	 * TODO(这里用一句话描述这个方法的作用) 
	 * @date  2017年5月6日 下午3:23:24
	 * @return DeviceType
	 * @param name
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
