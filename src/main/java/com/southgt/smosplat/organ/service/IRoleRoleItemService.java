package com.southgt.smosplat.organ.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.RoleItem;
import com.southgt.smosplat.organ.entity.RoleRoleItem;

/**
 * 角色权限关联关系服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月10日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IRoleRoleItemService extends IBaseService<RoleRoleItem>{

	/**
	 * 更新角色对应的权限
	 * @date  2017年3月10日 上午8:57:00
	 * @return void
	 * @param roleUuid
	 * @param roleItemUuids
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月10日     mohaolin      v1.0          create</p>
	 *
	 */
	void updateRoleItemForRole(String roleUuid, String roleItemUuids);

	/**
	 * 根据角色id获得角色对应的权限列表
	 * @date  2017年3月10日 上午9:55:35
	 * @return List<RoleItem>
	 * @param roleUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月10日     mohaolin      v1.0          create</p>
	 *
	 */
	List<RoleItem> getRoleItemsByRole(String roleUuid);
	
}
