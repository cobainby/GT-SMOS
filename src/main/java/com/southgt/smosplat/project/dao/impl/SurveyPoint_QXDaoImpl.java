package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_QXDao;
import com.southgt.smosplat.project.entity.SurveyPoint_QX;

/**
 * 
 * 建筑物倾斜数据库实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月15日     姚家俊       v1.0.0        create</p>
 *
 */
@Repository("sp_QXDao")
public class SurveyPoint_QXDaoImpl extends BaseDaoImpl<SurveyPoint_QX> implements ISurveyPoint_QXDao{

	
	private Criteria getCriteria(){
		return getSession().createCriteria(SurveyPoint_QX.class).setCacheable(true);
	}
	
	@Override
	public List<SurveyPoint_QX> getExistedSurveyPoint_QXsByCode(String projectUuid, String monitorItemUuid,
			String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
	}

	@Override
	public List<SurveyPoint_QX> getSurveyPoint_QXs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@Override
	public long getQXNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar, String code,
			String spUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar)).add(Restrictions.eq("code", code))
		 . add(Restrictions.not(Restrictions.eq("surveyPointUuid", spUuid)));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return number;
	}

}
