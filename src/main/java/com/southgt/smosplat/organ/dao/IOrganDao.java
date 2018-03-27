package com.southgt.smosplat.organ.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Organ;

/**
 * 机构数据库访问层接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IOrganDao extends IBaseDao<Organ> {
	
	/**
	 *  获取账户下创建的所有机构记录数
	 * @date  2017年3月1日 下午3:12:36
	 * @return long
	 * @param account
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月1日     mohaolin      v1.0          create</p>
	 *
	 */
	long getTotalCount(Account account);
	
	/**
	 * 分页获得账号所创建的所有机构
	 * @date  2017年3月1日 下午3:13:05
	 * @return List<Organ>
	 * @param first
	 * @param max
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月1日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Organ> getOrgans(int first, int max);

	/**
	 * 根据机构id删除机构
	 * @date  2017年3月1日 下午3:16:17
	 * @return void
	 * @param organUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月1日     mohaolin      v1.0          create</p>
	 *
	 */
	void deleteOrgan(String organUuid);

	/**
	 * 根据机构名称获得具有该名称的机构数量 
	 * @date  2017年3月1日 下午4:09:19
	 * @return long
	 * @param name
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月1日     mohaolin      v1.0          create</p>
	 *
	 */
	long getOrgansNumber(String name);

	/**
	 * 根据机构联系人id获得机构记录数
	 * @date  2017年3月6日 下午5:10:39
	 * @return long
	 * @param workerUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 *
	 */
	long getOrgansNumberByContact(String workerUuid);

	/**
	 * 根据机构名获得除了自己外的机构个数
	 * @date  2017年5月11日 上午10:24:47
	 * @return long
	 * @param organName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月11日     mohaolin      v1.0          create</p>
	 * @param organUuid 
	 *
	 */
	long getOrgansNumberExceptSelf(String organName, String organUuid);

}
