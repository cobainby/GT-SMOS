package com.southgt.smosplat.organ.dao;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Account;

/**
 * 账户数据库操作实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IAccountDao extends IBaseDao<Account>{
	
	/**
	 * 获得具有该登录名的账号的记录数
	 * @date  2017年3月1日 下午4:20:09
	 * @return long
	 * @param loginName
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
	long getAccountsNumber(String loginName);
	
	/**
	 * 获得除了自身以外具有该登录名的账号的记录数
	 * @date  2017年4月1日 下午5:15:55
	 * @return long
	 * @param accountUuid
	 * @param loginName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月1日     mohaolin      v1.0          create</p>
	 *
	 */
	long getAccountsNumber(String accountUuid, String loginName);

	/**
	 * 根据登录名和密码获取账号信息
	 * @date  2017年3月2日 上午10:35:48
	 * @return Account
	 * @param loginName
	 * @param password
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月2日     mohaolin      v1.0          create</p>
	 *
	 */
	Account getAccount(String loginName, String password);



	
	
	

}
