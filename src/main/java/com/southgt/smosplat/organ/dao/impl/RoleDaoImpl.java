package com.southgt.smosplat.organ.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IRoleDao;
import com.southgt.smosplat.organ.entity.Role;

/**
 * 角色数据库访问层接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role> implements IRoleDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(Role.class).setCacheable(true);
		return c;
	}
	
	@Override
	public long getRolesNumber(String roleName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("roleName", roleName));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public byte getMaxMarkNumber() {
		Criteria c=getCriteria();
		c.setProjection(Projections.max("mark"));
		return (byte) c.uniqueResult();
	}

	@Override
	public Role getRoleByMark(byte i) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("mark", i));
		return (Role) c.uniqueResult();
	}

}
