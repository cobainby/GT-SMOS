package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IMobileSurveyPointDao;
import com.southgt.smosplat.project.entity.Mobile_SurveyPoint;

/**
 * 手机监测点数据库访问实现
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年11月14日     YANG       v1.0.0        create</p>
 *
 */
@Repository("mobileSPDao")
public class MobileSurveyPointSDaoImpl extends BaseDaoImpl<Mobile_SurveyPoint> implements IMobileSurveyPointDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(Mobile_SurveyPoint.class).setCacheable(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mobile_SurveyPoint> getSPsByProjectAndMonitorItem(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("projectUuid", projectUuid)).add(Restrictions.eq("monitorItemUuid", monitorItemUuid));
		c.addOrder(Order.asc("orderCreate"));
		return c.list();
	}

	@Override
	public void deleteSPsByProjectAndMonitorItem(String projectUuid, String monitorItemUuid) {
		Query query=getSession().createQuery("delete from Mobile_SurveyPoint b where b.projectUuid=:projectUuid and b.monitorItemUuid=:monitorItemUuid");
		query.setString("projectUuid", projectUuid).setString("monitorItemUuid", monitorItemUuid);
		query.executeUpdate();
	}

}
