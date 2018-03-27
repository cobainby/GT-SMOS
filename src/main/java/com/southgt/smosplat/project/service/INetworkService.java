package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Project;

/**
 * 网络连接服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
public interface INetworkService extends IBaseService<Network> {

	/**
	 * 添加网络连接
	 * @date  2017年4月14日 上午10:59:36
	 * @return void
	 * @param project
	 * @param network
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void addNetwork(Project project, Network network) throws Exception;

	/**
	 * 更新网络连接
	 * @date  2017年4月14日 上午11:16:30
	 * @return void
	 * @param project
	 * @param network
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void updateNetwork(Project project, Network network) throws Exception;

	/**
	 * 删除网络连接
	 * @date  2017年4月14日 上午11:30:11
	 * @return void
	 * @param networkUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void deleteNetwork(String networkUuid) throws Exception;

	/**
	 * 获取所有网络连接
	 * @date  2017年4月14日 下午3:45:08
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
