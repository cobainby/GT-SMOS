package com.southgt.smosplat.organ.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IAccountDao;
import com.southgt.smosplat.organ.entity.Account;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDaoImpl<Account> implements IAccountDao {

	private Criteria getCriteria(){
		Criteria criteria = getSession().createCriteria(Account.class).setCacheable(true);
		return criteria;
	}
	
	@Override
	public long getAccountsNumber(String loginName) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("loginName", loginName));
		criteria.setProjection(Projections.rowCount());
		return (long) criteria.uniqueResult();
	}
	
	@Override
	public long getAccountsNumber(String accountUuid, String loginName) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("loginName", loginName));
		criteria.add(Restrictions.not(Restrictions.eq("accountUuid", accountUuid)));
		criteria.setProjection(Projections.rowCount());
		return (long) criteria.uniqueResult();
	}

	@Override
	public Account getAccount(String loginName, String password) {
		Criteria criteria = getCriteria();
		Account account=(Account) criteria.add(Restrictions.eq("loginName", loginName))
		.add(Restrictions.eq("password", password)).uniqueResult();
		return account;
	}

	
	

	

}
