package com.southgt.smosplat.project.service;

import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Project;

/**
 * 工程服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月19日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IProjectService2 extends IBaseService<Project> {

	/**
	 * TODO(这里用一句话描述这个方法的作用) 
	 * @date  2017年6月3日 上午10:22:14
	 * @return void
	 * @param networkUuids
	 * @param interval
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月3日     mohaolin      v1.0          create</p>
	 *
	 */
	void projectStartCollect2(String networkUuids, int interval) throws Exception;
	/**
	 * 
	 * 召测
	 * @date  2017年6月6日 下午1:28:23
	 * @return void
	 * @param networkUuids
	 * @throws Exception
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月6日     mohaolin      v1.0          create</p>
	 *
	 */
	void callCollect(Network network) throws Exception;

	/**
	 * 开始召测
	 * @date  2017年6月6日 下午1:27:41
	 * @return void
	 * @param networkUuids
	 * @param interval
	 * @param connectMcuTime
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月6日     mohaolin      v1.0          create</p>
	 *
	 */
	void projectStartCallCollect(String networkUuids);

}
