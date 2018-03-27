package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPoint_SMDao;
import com.southgt.smosplat.project.entity.SurveyPoint_SM;
/**
 * 
 * 周边建筑物竖向位移数据库实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月22日     姚家俊       v1.0.0        create</p>
 *
 */
@Repository("sp_SMDao")
public class SurveyPoint_SMDaoImpl extends BaseDaoImpl<SurveyPoint_SM> implements ISurveyPoint_SMDao{
	
	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(SurveyPoint_SM.class).setCacheable(true);
		return c;
	}
	
	@Override
	public List<SurveyPoint_SM> getExistedSurveyPoint_SMsByCode(String projectUuid, String monitorItemUuid,
			String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
	}

	@Override
	public List<SurveyPoint_SM> getSurveyPoint_SMs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}
	
	@Override
	public List<SurveyPoint_SM> getSurveyPoint_SMs(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}

	@Override
	public long getSMNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar, String code,
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
