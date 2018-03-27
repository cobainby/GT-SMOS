package com.southgt.smosplat.project.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IMonitorItemParamDao;
import com.southgt.smosplat.project.entity.MonitorItemParam;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月12日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("monitorItemParamDao")
public class MonitorItemParamDaoImpl extends BaseDaoImpl<MonitorItemParam> implements IMonitorItemParamDao {
	
	private Criteria getCriteria(){
		return getSession().createCriteria(MonitorItemParam.class).setCacheable(true);
	}

	@Override
	public MonitorItemParam getMonitorItemParam(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		c.add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return (MonitorItemParam) c.uniqueResult();
	}


}
