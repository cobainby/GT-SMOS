package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IWarningDao;
import com.southgt.smosplat.project.entity.Warning;

/**
 * 预警信息数据库访问接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月28日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("warningDao")
public class WarningDaoImpl extends BaseDaoImpl<Warning> implements IWarningDao {
	
	private Criteria getCriteria(){
		return getSession().createCriteria(Warning.class).setCacheable(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Warning> getWarnings(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@Override
	public long getWarningsNumber(String projectUuid, String monitorItemUuid, String warningName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		.add(Restrictions.eq("warningName", warningName));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getWarningsNumberExceptSelf(String projectUuid, String monitorItemUuid, String warningName,
			String warningUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		.add(Restrictions.eq("warningName", warningName));
		c.add(Restrictions.not(Restrictions.eq("warningUuid", warningUuid)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

}
