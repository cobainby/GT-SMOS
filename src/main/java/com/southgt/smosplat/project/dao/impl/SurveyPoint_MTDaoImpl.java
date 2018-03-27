package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_MTDao;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;

@Repository("sp_MTDao")
public class SurveyPoint_MTDaoImpl extends BaseDaoImpl<SurveyPoint_MT> implements ISurveyPoint_MTDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(SurveyPoint_MT.class).setCacheable(true);
		return c;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_MT> getExistedSurveyPoint_MTsByCode(String projectUuid, String monitorItemUuid,
			String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_MT> getSurveyPoint_MTs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.addOrder(Order.asc("code"));
		return c.list();
	}


	@Override
	public long getSP_mtNumByCode(String projectUuid, String monitorItemUuid, String code) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		 .add(Restrictions.eq("code", code));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}


	@Override
	public long getSP_mtNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String code,
			String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.not(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid)))
		 .add(Restrictions.eq("code", code)).add(Restrictions.eq("surveyPointUuid", surveyPointUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

}
