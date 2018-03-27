package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IMcuDao;
import com.southgt.smosplat.project.entity.Mcu;

/**
 * mcu数据访问接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("mcuDao")
public class McuDaoImpl extends BaseDaoImpl<Mcu> implements IMcuDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(Mcu.class,"m").setCacheable(true);
	}
	
	@Override
	public long getMcuNumber(String projectUuid, String mcuName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid))
		.add(Restrictions.eq("mcuName", mcuName));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getMcuNumberExceptSelf(String projectUuid, String mcuName, String mcuUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid))
		.add(Restrictions.eq("mcuName", mcuName));
		c.add(Restrictions.not(Restrictions.eq("mcuUuid", mcuUuid)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getMcusCountByNetwork(String networkUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("network.networkUuid", networkUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mcu> getMcus(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Mcu> getMcusByOrgan(String organUuid) {
		Criteria c=getCriteria();
		c.createAlias("m.device", "d");
		c.add(Restrictions.eq("d.status", 0));
		c.createAlias("d.organ", "o");
		c.add(Restrictions.eq("o.organUuid", organUuid));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mcu> getCollectingMcusByNetwork(String networkUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("network.networkUuid", networkUuid));
		c.add(Restrictions.eq("collectStatus", (byte)0));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mcu> getMcusByNetwork(String networkUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("network.networkUuid", networkUuid));
		return c.list();
	}

	@Override
	public long getMCUNumberByProject(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

}
