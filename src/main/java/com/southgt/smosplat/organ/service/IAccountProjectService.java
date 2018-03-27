package com.southgt.smosplat.organ.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.AccountProject;
import com.southgt.smosplat.project.entity.Project;

/**
 * 账户和工程关联关系服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年7月4日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IAccountProjectService extends IBaseService<AccountProject> {

	/**
	 * 获得账户可以查看的所有工程
	 * @date  2017年7月4日 下午4:55:11
	 * @return List<Project>
	 * @param accountUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月4日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Project> getProjectsByAccount(String accountUuid);

	/**
	 * 更新账户可以查看的工程
	 * @date  2017年7月5日 下午2:22:26
	 * @return void
	 * @param accountUuid
	 * @param projectUuids
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月5日     mohaolin      v1.0          create</p>
	 *
	 */
	void updateProjectForAccount(String accountUuid, String projectUuids);

}
