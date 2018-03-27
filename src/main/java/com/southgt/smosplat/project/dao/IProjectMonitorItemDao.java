package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;

/**
 * 工程与监测项关联实体数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月24日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IProjectMonitorItemDao extends IBaseDao<ProjectMonitorItem> {

	/**
	 * 根据工程获得相关的监测项
	 * @date  2017年3月24日 下午6:42:59
	 * @return List<ProjectMonitorItem>
	 * @param projectUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月24日     mohaolin      v1.0          create</p>
	 *
	 */
	List<ProjectMonitorItem> getMonitorItemsByProject(String projectUuid);

}
