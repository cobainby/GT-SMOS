package com.southgt.smosplat.project.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IMonitorItemDao;
import com.southgt.smosplat.project.entity.MonitorItem;

/**
 * 监测项数据库访问实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月21日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("monitorItemDao")
public class MonitorItemDaoImpl extends BaseDaoImpl<MonitorItem> implements IMonitorItemDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(MonitorItem.class).setCacheable(true);
	}
	
	@Override
	public MonitorItem getMonitorItemByNumber(int i) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("number", (byte)i));
		return (MonitorItem) c.uniqueResult();
	}

	@Override
	public MonitorItem getMonitorItemByCode(String code) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("code", code));
		return (MonitorItem) c.uniqueResult();
	}


}
