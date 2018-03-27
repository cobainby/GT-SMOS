package com.southgt.smosplat.organ.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.service.IAccountService;

/**
 * 账户服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月28日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("accountService")
public class AccountServiceImpl extends BaseServiceImpl<Account> implements IAccountService {

	@Resource(name="accountDao")
	@Override
	public void setDao(IBaseDao<Account> dao) {
		super.setDao(dao);
	}


}
