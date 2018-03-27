package com.southgt.smosplat.organ.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.RoleRoleItem;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月10日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IRoleRoleItemDao extends IBaseDao<RoleRoleItem>{

	/**
	 * 根据角色获得该角色具有的权限列表
	 * @date  2017年3月10日 上午9:02:54
	 * @return List<RoleRoleItem>
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
	List<RoleRoleItem> getRoleRoleItemsByRole(String roleUuid);

	/**
	 * 根据参数获得该角色具有的权限列表
	 * @date  2017年7月6日 上午11:35:06
	 * @return List<RoleRoleItem>
	 * @param roleItemUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月6日     mohaolin      v1.0          create</p>
	 *
	 */
	List<RoleRoleItem> getRoleRoleItemsByRoleItem(String roleItemUuid);

}
