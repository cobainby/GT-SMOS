package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;

/**
 * 工程与监测项关联关系实体服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月24日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IProjectMonitorItemService extends IBaseService<ProjectMonitorItem> {

	/**
	 * 获得工程相关的所有监测项
	 * @date  2017年3月24日 下午6:38:21
	 * @return List<ProjectMonitorItem>
	 * @param project
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
	List<ProjectMonitorItem> getMonitorItemsByProject(Project project);

	/**
	 * 为工程增加监测项
	 * @date  2017年3月24日 下午7:40:33
	 * @return void
	 * @param project
	 * @param monitorItemUuids
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
	void addMonitorItemsForProject(Project project, String monitorItemUuids);

	/**
	 * 删除项目下的监测项，同时需要删除所有相关联的数据 
	 * @date  2017年4月7日 上午10:51:40
	 * @return void
	 * @param project
	 * @param monitorItemUuids
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void deleteMonitorItemForProject(Project project, String monitorItemUuid) throws Exception;


}
