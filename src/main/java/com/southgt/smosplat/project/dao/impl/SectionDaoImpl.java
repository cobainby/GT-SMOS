package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISectionDao;
import com.southgt.smosplat.project.entity.Section;

/**
 * 断面数据库访问实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月30日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("sectionDao")
public class SectionDaoImpl extends BaseDaoImpl<Section> implements ISectionDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(Section.class).setCacheable(true);
	}
	
	@Override
	public long getSectionsNumber(String projectUuid, String monitorItemUuid, String sectionName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		.add(Restrictions.eq("sectionName", sectionName));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Section> getSections(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@Override
	public long getSectionsNumberExceptSelf(String projectUuid, String monitorItemUuid, String sectionName,
			String sectionUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		.add(Restrictions.eq("sectionName", sectionName));
		c.add(Restrictions.not(Restrictions.eq("sectionUuid", sectionUuid)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}


}
