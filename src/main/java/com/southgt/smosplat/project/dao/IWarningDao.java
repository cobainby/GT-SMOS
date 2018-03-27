package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Warning;

/**
 * 预警信息数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月28日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IWarningDao extends IBaseDao<Warning> {

	/**
	 * 获得对应项目、对应监测项的预警设置
	 * @date  2017年3月28日 上午10:15:12
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
	 * 判断是否已有相同名称的记录
	 * @date  2017年3月30日 上午10:00:40
	 * @return long
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @param warningName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月30日     mohaolin      v1.0          create</p>
	 *
	 */
	long getWarningsNumber(String projectUuid, String monitorItemUuid, String warningName);

	/**
	 * 判断除了自己以外是否具有相同名称的记录
	 * @date  2017年4月7日 下午3:15:13
	 * @return long
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @param warningName
	 * @param warningUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 *
	 */
	long getWarningsNumberExceptSelf(String projectUuid, String monitorItemUuid, String warningName,
			String warningUuid);

}
