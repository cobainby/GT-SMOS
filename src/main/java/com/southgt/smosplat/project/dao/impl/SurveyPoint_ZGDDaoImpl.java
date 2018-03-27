package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_ZGDDao;
import com.southgt.smosplat.project.entity.SurveyPoint_ZGD;

@Repository("sp_ZGDDao")
public class SurveyPoint_ZGDDaoImpl extends BaseDaoImpl<SurveyPoint_ZGD> implements ISurveyPoint_ZGDDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(SurveyPoint_ZGD.class).setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_ZGD> getExistedSurveyPoint_ZGDsByCode(String projectUuid, String monitorItemUuid,
			String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_ZGD> getSurveyPoint_ZGDs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_ZGD> getSurveyPoint_ZGDs(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}
	
	@Override
	public long getZGDNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String code,
			String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		 .add(Restrictions.eq("code", code)).add(Restrictions.not(Restrictions.eq("surveyPointUuid", surveyPointUuid)));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return  number;
	}

}
