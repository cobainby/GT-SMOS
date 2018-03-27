package com.southgt.smosplat.project.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IProjectReportDao;
import com.southgt.smosplat.project.entity.ProjectReport;

@Repository("projectReportDao")
public class ProjectReportDaoImpl extends BaseDaoImpl<ProjectReport> implements IProjectReportDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(ProjectReport.class).setCacheable(true);
	}
	
	@Override
	public ProjectReport getProjectReportByProject(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return (ProjectReport) c.uniqueResult();
	}

	@Override
	public boolean existOrNotProjectReport(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		c.setProjection(Projections.rowCount());
		long num=(long) c.uniqueResult();
		if(num>0){
			return true;
		}else{
			return false;
		}
	}

	
	
}
