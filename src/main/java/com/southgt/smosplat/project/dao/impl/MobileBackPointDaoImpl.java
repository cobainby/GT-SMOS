package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IMobileBackPointDao;
import com.southgt.smosplat.project.entity.Mobile_BackPoint;

/**
 * 手机后视点数据库访问实现
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
@Repository("mobileBackPointDao")
public class MobileBackPointDaoImpl extends BaseDaoImpl<Mobile_BackPoint> implements IMobileBackPointDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(Mobile_BackPoint.class).setCacheable(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mobile_BackPoint> getBackPointByStation(String stationUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("stationUuid", stationUuid));
		c.addOrder(Order.asc("orderCreate"));
		return c.list();
	}

	@Override
	public void deleteBPByStation(String stationUUid) {
		Query query=getSession().createQuery("delete from Mobile_BackPoint b where b.stationUuid=:stationUuid");
		query.setString("stationUuid", stationUUid);
		query.executeUpdate();
	}

}
