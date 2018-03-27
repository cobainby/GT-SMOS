package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_ZCDao;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;

@Repository("sp_ZCDao")
public class SurveyPoint_ZCDaoImpl extends BaseDaoImpl<SurveyPoint_ZC> implements ISurveyPoint_ZCDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(SurveyPoint_ZC.class).setCacheable(true);
		return c;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_ZC> getSurveyPoint_ZCs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.addOrder(Order.asc("code"));
		return c.list();
	}

	@Override
	public long getSP_zcNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String code,
			String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		.add(Restrictions.eq("code", code));
		c.add(Restrictions.not(Restrictions.eq("surveyPointUuid", surveyPointUuid)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getSP_zcNumByCode(String projectUuid, String monitorItemUuid,String code) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		.add(Restrictions.eq("code", code));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}
	
}
