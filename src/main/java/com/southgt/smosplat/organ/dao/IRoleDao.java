package com.southgt.smosplat.organ.dao;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Role;

/**
 * 角色数据库访问层接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IRoleDao extends IBaseDao<Role>{

	/**
	 * 根据角色名称获得记录数
	 * @date  2017年3月7日 下午7:07:33
	 * @return long
	 * @param roleName
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
	long getRolesNumber(String roleName);

	/**
	 * 获得权限标识的最大值
	 * @date  2017年3月11日 下午4:19:57
	 * @return byte
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
	byte getMaxMarkNumber();

	/**
	 * 根据角色标识获得角色
	 * @date  2017年3月11日 下午4:29:22
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
	public Role getRoleByMark(byte i);

}
