package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IProjectMonitorItemDao;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;

/**
 * 工程与监测项关联关系数据库访问实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月24日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("projectMonitorItemDao")
public class ProjectMonitorItemDaoImpl extends BaseDaoImpl<ProjectMonitorItem> implements IProjectMonitorItemDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(ProjectMonitorItem.class).setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectMonitorItem> getMonitorItemsByProject(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}


}
