package com.southgt.smosplat.organ.dao;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.RoleItem;

/**
 * 权限项数据库访问层接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IRoleItemDao extends IBaseDao<RoleItem>{

	/**
	 * 找出名称相同的记录数
	 * @date  2017年3月7日 下午7:58:29
	 * @return long
	 * @param roleItemName
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
	long getRoleItemsNumberByName(String roleItemName);

	/**
	 * 找出序号相同的记录数
	 * @date  2017年7月6日 上午10:49:36
	 * @return long
	 * @param number
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
	long getRoleItemsNumberByNumber(int number);

}
