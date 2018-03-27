package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Warning;

/**
 * 预警设置服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月28日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IWarningService extends IBaseService<Warning> {

	/**
	 * 添加预警设置
	 * @date  2017年3月28日 上午10:08:07
	 * @return void
	 * @param project
	 * @param warning
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月28日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void addWarning(Project project, Warning warning) throws Exception;

	/**
	 * 获得对应项目，对应监测项下的预警设置
	 * @date  2017年3月28日 上午10:14:02
	 * @return List<Warning>
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
	 * <p>2017年3月28日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Warning> getWarnings(String projectUuid, String monitorItemUuid);

	/**
	 * 更新预警信息
	 * @date  2017年4月7日 下午3:10:19
	 * @return void
	 * @param project
	 * @param warning
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
	void updateWarning(Project project, Warning warning) throws Exception;

	/**
	 * 删除预警设置
	 * @date  2017年4月7日 下午3:18:23
	 * @return void
	 * @param warningUuid
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
	void deleteWarning(String warningUuid) throws Exception;

}
