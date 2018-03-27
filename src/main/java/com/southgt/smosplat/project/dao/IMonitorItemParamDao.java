package com.southgt.smosplat.project.dao;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.MonitorItemParam;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月12日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IMonitorItemParamDao extends IBaseDao<MonitorItemParam> {

	/**
	 * 获得工程下对应监测项的监测项参数 
	 * @date  2017年4月12日 下午4:19:31
	 * @return MonitorItemParam
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日     mohaolin      v1.0          create</p>
	 *
	 */
	MonitorItemParam getMonitorItemParam(String projectUuid, String monitorItemUuid);

}
