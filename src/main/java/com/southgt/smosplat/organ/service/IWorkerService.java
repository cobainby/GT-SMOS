package com.southgt.smosplat.organ.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Worker;

/**
 * 人员服务层接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月2日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IWorkerService extends IBaseService<Worker>{
	
	/**
	 * 验证登陆账号，返回登陆成功后的账号信息 
	 * @date  2017年3月2日 上午10:31:01
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
	Account validateAccount(String loginName, String password);

	/**
	 * 获得具有登陆名的账号记录数
	 * @date  2017年3月2日 下午8:14:45
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
	 * <p>2017年3月2日     mohaolin      v1.0          create</p>
	 *
	 */
	long getAccountsNumber(String loginName);
	
	/**
	 * 获得除了自身以外具有登陆名的账号记录数
	 * @date  2017年3月2日 下午8:14:45
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
	 * <p>2017年3月2日     mohaolin      v1.0          create</p>
	 *
	 */
	long getAccountsNumber(String accountUuid,String loginName);

	/**
	 * 删除机构下所有人员
	 * @date  2017年3月2日 下午6:39:18
	 * @return void
	 * @param organUuid
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
	void deleteWorkersByOrgan(String organUuid);

	/**
	 * 获取所有人员
	 * @date  2017年3月6日 上午11:37:57
	 * @return List<Worker>
	 * @param options
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
	List<Worker> getWorkers(PageCondition options);

	/**
	 * 获取所有人员
	 * @date  2017年3月6日 上午11:38:53
	 * @return List<Worker>
	 * @param options
	 * @param organUuid
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
	List<Worker> getWorkers(PageCondition options, String organUuid);

	/**
	 * 增加人员，如果有关联账号则同时增加关联账号
	 * @date  2017年3月6日 下午3:08:58
	 * @return 
	 * @param worker
	 * @param hasAccount
	 * @param account
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 * @param curOrganUuid 
	 * @param loginAccount 
	 * @throws Exception 
	 *
	 */
	void addWorker(Worker worker, Boolean hasAccount, Account account, String curOrganUuid, Account loginAccount) throws Exception;

	/**
	 * 保存人员关联的账号
	 * @date  2017年3月6日 下午3:14:51
	 * @return void
	 * @param account
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
	void saveAccount(Account account);

	/**
	 * 更新人员相关联的账号
	 * @date  2017年3月6日 下午3:16:06
	 * @return void
	 * @param account
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
	void updateAccount(Account account);

	/**
	 * 删除人员
	 * @date  2017年3月6日 下午4:47:36
	 * @return void
	 * @param workerUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void deleteWorker(String workerUuid) throws Exception;

	/**
	 * 更新人员信息
	 * @date  2017年3月6日 下午7:21:43
	 * @return void
	 * @param worker
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
	void updateWorker(Worker worker);

	/**
	 * 更新人员对应的账号
	 * @date  2017年3月6日 下午8:28:11
	 * @return void
	 * @param accountUuid
	 * @param accountName
	 * @param loginName
	 * @param resetPassword
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 * @param password 
	 * @throws Exception 
	 *
	 */
	void updateAccount(String accountUuid, String accountName, String loginName, Boolean resetPassword, String password) throws Exception;

	/**
	 * 获得机构下的所有人员列表 
	 * @date  2017年3月31日 下午3:39:56
	 * @return List<Worker>
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月31日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Worker> getWorkersByOrgan(String organUuid);
	/**
	 * 
	 * 根据账户取得人员
	 * @date  2017年4月1日 下午3:41:35
	 * 
	 * @param accountUuid
	 * @return
	 * Worker
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月1日     姚家俊      v1.0          create</p>
	 *
	 */
	Worker getWorkerByAccount(String accountUuid);

	/**
	 * 获得机构下的人员的数量
	 * @date  2017年5月18日 下午5:08:24
	 * @return long
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月18日     mohaolin      v1.0          create</p>
	 *
	 */
	long getWorkersNumberByOrgan(String organUuid);

	/**
	 * 根据账户id获取账户信息
	 * @date  2017年7月3日 下午3:54:06
	 * @return Account
	 * @param accountUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月3日     mohaolin      v1.0          create</p>
	 *
	 */
	Account getAccountById(String accountUuid);
}
