package com.southgt.smosplat.organ.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Role;
import com.southgt.smosplat.organ.entity.RoleItem;

/**
 * 角色服务层接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IRoleService extends IBaseService<Role>{
	
	/**
	 * 获得所有角色
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
	List<Role> getRoles();
	
	/**
	 * 根据id删除角色
	 * @date  2017年3月7日 下午5:37:32
	 * @return void
	 * @param roleUuid
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
	void deleteRole(String roleUuid) throws Exception;

	/**
	 * 增加角色
	 * @date  2017年3月7日 下午7:05:51
	 * @return void
	 * @param role
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
	void saveRole(Role role) throws Exception;

	/**
	 * 获得账号对应的所有权限信息，Map的key表示分组，List<RoleItem>表示这个组下的所有权限列表
	 * @date  2017年3月11日 下午2:15:30
	 * @return Map<String,List<RoleItem>>
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月11日     mohaolin      v1.0          create</p>
	 * @param account 登录的账户
	 *
	 */
	List<RoleItem> getRoleItemsByAccount(Account account);

	/**
	 * 根据角色标识获得角色对象
	 * @date  2017年3月11日 下午4:28:29
	 * @return Role
	 * @param i
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月11日     mohaolin      v1.0          create</p>
	 *
	 */
	Role getRoleByMark(byte i);

}
