package com.southgt.smosplat.organ.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IOrganDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Organ;


/**
 * 机构数据库访问实现类
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("organDao")
public class OrganDaoImpl extends BaseDaoImpl<Organ> implements IOrganDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(Organ.class).setCacheable(true);
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Organ> getOrgans(int first, int max) {
		Criteria c=getCriteria();
		List<Organ> dataList=c.setFirstResult(first).setMaxResults(max).list();
		return dataList;
	}

	@Override
	public long getTotalCount(Account account) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public void deleteOrgan(String organUuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getOrgansNumber(String name) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organName", name));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}
	
	@Override
	public long getOrgansNumberExceptSelf(String organName, String organUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organName", organName));
		c.add(Restrictions.not(Restrictions.eq("organUuid", organUuid)));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public long getOrgansNumberByContact(String workerUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("contactWorker", workerUuid));
		return c.list().size();
	}

}
