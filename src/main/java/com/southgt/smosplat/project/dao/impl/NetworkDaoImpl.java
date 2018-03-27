package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.INetworkDao;
import com.southgt.smosplat.project.entity.Network;

/**
 * 网络连接数据库访问接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("networkDao")
public class NetworkDaoImpl extends BaseDaoImpl<Network> implements INetworkDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(Network.class).setCacheable(true);
	}
	
	@Override
	public long getNetworkNumber(String projectUuid, String networkName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid))
		.add(Restrictions.eq("networkName", networkName));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getNetworkNumberExceptSelf(String projectUuid, String networkName,
			String networkUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid))
		.add(Restrictions.eq("networkName", networkName));
		c.add(Restrictions.not(Restrictions.eq("networkUuid", networkUuid)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Network> getNetworks(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}


}
