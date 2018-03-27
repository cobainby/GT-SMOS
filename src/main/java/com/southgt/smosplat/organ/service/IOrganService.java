package com.southgt.smosplat.organ.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.organ.entity.Worker;

/**
 * 
 * 机构服务层接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IOrganService extends IBaseService<Organ>{
	
	/**
	 * 分页查询相关的所有部门
	 * @date  2017年3月1日 下午3:06:54
	 * @return Map<String,Object>
	 * @param account
	 * @param options
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
	Map<String,Object> getOrgans(Account account, PageCondition options,HttpSession session);
	
	/**
	 * 添加一个机构，同时为机构添加一个管理员账号
	 * @date  2017年3月1日 下午3:07:06
	 * @return 
	 * @param account 机构登陆账号
	 * @param organ
	 * @return
	 * @throws Exception
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月1日     mohaolin      v1.0          create</p>
	 * @param worker 
	 * @param supervisorPassword 
	 * @param supervisorAccountName 
	 * @param supervisorLoginName 
	 * @param supervisorEmail 
	 * @param supervisorPhone 
	 * @param supervisorName 
	 *
	 */
	void addOrgan(Organ organ, Account account, Worker worker) throws Exception;
	
	/**
	 * 根据id删除
	 * @date  2017年3月1日 下午3:07:47
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
	 * <p>2017年3月1日     mohaolin      v1.0          create</p>
	 *
	 */
	void deleteOrgan(String organUuid) throws Exception;

	/**
	 * 修改机构 
	 * @date  2017年3月2日 下午8:09:20
	 * @return 
	 * @param organUuid
	 * @param organName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月2日     mohaolin      v1.0          create</p>
	 * @param accountName 
	 * @param email 
	 * @param phone 
	 * @param workerName 
	 * @param loginName 
	 * @param supervisorPhone 
	 * @param supervisorName 
	 * @throws Exception 
	 *
	 */
	public void updateOrgan(Organ organ, String workerName, String phone,String loginName, String accountName, String supervisorName, String supervisorPhone) throws Exception;

}
