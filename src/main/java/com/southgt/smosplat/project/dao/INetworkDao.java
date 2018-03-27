package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Network;

/**
 * 网络连接数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
public interface INetworkDao extends IBaseDao<Network> {

	/**
	 * 获取具有名称的记录数
	 * @date  2017年4月14日 上午11:10:46
	 * @return long
	 * @param projectUuid
	 * @param networkName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 *
	 */
	long getNetworkNumber(String projectUuid, String networkName);

	/**
	 * 获取除了自己外具有名称的记录数
	 * @date  2017年4月14日 上午11:19:37
	 * @return long
	 * @param projectUuid
	 * @param networkName
	 * @param networkUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 *
	 */
	long getNetworkNumberExceptSelf(String projectUuid, String networkName, String networkUuid);

	/**
	 * 获取工程所有网络连接设置 
	 * @date  2017年4月14日 下午3:46:07
	 * @return List<Network>
	 * @param projectUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Network> getNetworks(String projectUuid);

}
