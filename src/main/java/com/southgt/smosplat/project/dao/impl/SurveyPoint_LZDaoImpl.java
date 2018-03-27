package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_LZDao;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;

@Repository("sp_LZDao")
public class SurveyPoint_LZDaoImpl extends BaseDaoImpl<SurveyPoint_LZ> implements ISurveyPoint_LZDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(SurveyPoint_LZ.class).setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_LZ> getExistedSurveyPoint_LZsByCode(String projectUuid, String monitorItemUuid,String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
		
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_LZ> getSurveyPoint_LZs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@Override
	public long getLZNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar,String code, String spUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar)).add(Restrictions.eq("code", code))
		 . add(Restrictions.not(Restrictions.eq("surveyPointUuid", spUuid)));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return number;
			
	}

	@Override
	public List<SurveyPoint_LZ> getSurveyPoint_LZs(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}

}
