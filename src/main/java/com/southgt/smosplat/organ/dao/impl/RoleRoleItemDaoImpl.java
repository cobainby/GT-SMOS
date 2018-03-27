package com.southgt.smosplat.organ.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IRoleRoleItemDao;
import com.southgt.smosplat.organ.entity.RoleRoleItem;

/**
 * 角色权限关联关系数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月10日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("roleRoleItemDao")
public class RoleRoleItemDaoImpl extends BaseDaoImpl<RoleRoleItem> implements IRoleRoleItemDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(RoleRoleItem.class).setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RoleRoleItem> getRoleRoleItemsByRole(String roleUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("role.roleUuid", roleUuid));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleRoleItem> getRoleRoleItemsByRoleItem(String roleItemUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("roleItem.roleItemUuid", roleItemUuid));
		return c.list();
	}

}
