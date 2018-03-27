package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISurveyPointDao;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

/**
 * 监测点数据库访问接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月31日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("surveyPointDao")
public class SurveyPointDaoImpl extends BaseDaoImpl<SurveyPoint_WYS> implements ISurveyPointDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(SurveyPoint_WYS.class).setCacheable(true);
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_WYS> getExistedSurveyPointsByCode(String projectUuid, String monitorItemUuid, String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_WYS> getSurveyPoints(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@Override
	public long getSPNumbersByWarning(String warningUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("warning.warningUuid", warningUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getSPNumbersBySection(String sectionUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("section.sectionUuid", sectionUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}


}
