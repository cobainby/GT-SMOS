package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_WYSDao;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

@Repository("sp_WYSDao")
public class SurveyPoint_WYSDaoImpl extends BaseDaoImpl<SurveyPoint_WYS> implements ISurveyPoint_WYSDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(SurveyPoint_WYS.class).setCacheable(true);
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_WYS> getExistedSurveyPoint_WYSsByCode(String projectUuid, String monitorItemUuid,
			String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_WYS> getSurveyPoint_WYSs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_WYS> getSurveyPoint_WYSs(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}

	@Override
	public long getWYSNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String code,String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		 .add(Restrictions.eq("code", code)).add(Restrictions.not(Restrictions.eq("surveyPointUuid", surveyPointUuid)));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return  number;
	}

}
