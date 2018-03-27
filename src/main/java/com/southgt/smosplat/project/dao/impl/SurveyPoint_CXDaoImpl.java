package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_CXDao;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;

@Repository("sp_CXDao")
public class SurveyPoint_CXDaoImpl extends BaseDaoImpl<SurveyPoint_CX> implements ISurveyPoint_CXDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(SurveyPoint_CX.class).setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_CX> getExistedSurveyPoint_CXsByCode(String projectUuid, String monitorItemUuid,
			String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_CX> getSurveyPoint_CXs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_CX> getSurveyPoint_CXs(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}
	
	@Override
	public long getCXNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar, String code,String spUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar)).add(Restrictions.eq("code", code))
		 . add(Restrictions.not(Restrictions.eq("surveyPointUuid", spUuid)));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return number;
	}
	
}
