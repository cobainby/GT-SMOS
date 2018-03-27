package com.southgt.smosplat.organ.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IRoleItemDao;
import com.southgt.smosplat.organ.entity.RoleItem;

/**
 * 权限项数据库访问层实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("roleItemDao")
public class RoleItemDaoImpl extends BaseDaoImpl<RoleItem> implements IRoleItemDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(RoleItem.class).setCacheable(true);
		return c;
	}
	
	@Override
	public long getRoleItemsNumberByName(String roleItemName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("roleItemName", roleItemName));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public long getRoleItemsNumberByNumber(int number) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("number", number));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

}
