package com.southgt.smosplat.project.service;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.MonitorItemParam;
import com.southgt.smosplat.project.entity.Project;

/**
 * 监测项参数服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月12日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IMonitorItemParamService extends IBaseService<MonitorItemParam> {

	/**
	 * 获得工程下的监测项目的监测项设置
	 * @date  2017年4月12日 下午4:15:21
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

	/**
	 * 更新监测项设置参数，如果没有设置参数，则增加，否则更新 
	 * @date  2017年4月12日 下午5:05:17
	 * @return void
	 * @param monitorItemParam
	 * @param project
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
	void saveOrUpdateMonitorItemParam(MonitorItemParam monitorItemParam, Project project);

}
