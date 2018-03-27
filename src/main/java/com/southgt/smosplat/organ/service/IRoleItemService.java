package com.southgt.smosplat.organ.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.RoleItem;

/**
 * 权限服务层接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("roleItemService")
public interface IRoleItemService extends IBaseService<RoleItem>{
	
	/**
	 * 获得所有权限项
	 * @date  2017年3月7日 下午5:36:23
	 * @return List<Role>
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月7日     mohaolin      v1.0          create</p>
	 *
	 */
	List<RoleItem> getRoleItems();
	
	/**
	 * 根据id删除权限项
	 * @date  2017年3月7日 下午5:37:32
	 * @return void
	 * @param organUuid
	 * @throws Exception
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月7日     mohaolin      v1.0          create</p>
	 *
	 */
	void deleteRoleItem(String roleItemUuid) throws Exception;

	/**
	 * 添加权限项 
	 * @date  2017年3月7日 下午7:56:17
	 * @return void
	 * @param roleItem
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月7日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void saveRoleItem(RoleItem roleItem) throws Exception;

}
