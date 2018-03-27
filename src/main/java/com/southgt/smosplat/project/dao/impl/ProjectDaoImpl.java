package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.project.dao.IProjectDao;
import com.southgt.smosplat.project.entity.Project;

/**
 * 工程数据库访问接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月19日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("projectDao")
public class ProjectDaoImpl extends BaseDaoImpl<Project> implements IProjectDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(Project.class).setCacheable(true);
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjects() {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("available", 0));
		List<Project> dataList=c.list();
		return dataList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjects(Organ organ) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organ.getOrganUuid()));
		c.add(Restrictions.eq("available", 0));
		List<Project> dataList=c.list();
		return dataList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjectsByOrgan(String organUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.add(Restrictions.eq("available", 0));
		return c.list();
	}

	@Override
	public long hasProjectName(String projectName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("projectName", projectName));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public long hasProjectCode(String code) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("code", code));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public long hasProjectName(String projectUuid, String projectName) {
		Criteria c=getCriteria();
		c.add(Restrictions.not(Restrictions.eq("projectUuid", projectUuid)));
		c.add(Restrictions.eq("projectName", projectName));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public long hasProjectCode(String projectUuid, String code) {
		Criteria c=getCriteria();
		c.add(Restrictions.not(Restrictions.eq("projectUuid", projectUuid)));
		c.add(Restrictions.eq("code", code));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public long getProjectNumberByOrgan(String organUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.add(Restrictions.eq("available", 0));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		return num;
	}

	@Override
	public void deleteProject(String projectUuid) {
		SQLQuery query=getSession().createSQLQuery("update Project p set p.available=:available where p.project_uuid=:projectUuid");
		query.setParameter("available", -1);
		query.setParameter("projectUuid", projectUuid);
		query.executeUpdate();
	}

}
