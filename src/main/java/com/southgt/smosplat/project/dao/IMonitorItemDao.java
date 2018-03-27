package com.southgt.smosplat.project.dao;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.MonitorItem;

/**
 * 监测项数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月21日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IMonitorItemDao extends IBaseDao<MonitorItem> {

	/**
	 * 根据监测项编号获得监测项 
	 * @date  2017年4月8日 下午4:56:38
	 * @return MonitorItem
	 * @param i
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	MonitorItem getMonitorItemByNumber(int i);
	
	/**
	 * 
	 * 根据监测项code获得监测项信息
	 * @date  2017年11月3日 下午2:31:12
	 * 
	 * @param code
	 * @return
	 * MonitorItem
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年11月3日     姚家俊      v1.0          create</p>
	 *
	 */
	MonitorItem getMonitorItemByCode(String code);

}
