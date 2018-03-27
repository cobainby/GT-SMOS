package com.southgt.smosplat.organ.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IAccountProjectDao;
import com.southgt.smosplat.organ.entity.AccountProject;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年7月4日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("accountProjectDao")
public class AccountProjectDaoImpl extends BaseDaoImpl<AccountProject> implements IAccountProjectDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(AccountProject.class).setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountProject> getEntitiesByAccount(String accountUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("account.accountUuid", accountUuid));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountProject> getProjectsByAccount(String accountUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("account.accountUuid", accountUuid));
		return c.list();
	}

	

}
