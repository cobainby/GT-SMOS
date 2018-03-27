package com.southgt.smosplat.organ.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IDeviceModelDao;
import com.southgt.smosplat.organ.entity.DeviceModel;
import com.southgt.smosplat.organ.entity.DeviceType;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月2日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("deviceModelDao")
public class DeviceModelDaoImpl extends BaseDaoImpl<DeviceModel> implements IDeviceModelDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(DeviceModel.class).setCacheable(true);
	}
	
	@Override
	public DeviceModel getDeviceModelByName(String devModelName) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("devModelName", devModelName));
		return (DeviceModel) c.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceModel> getDeviceModelByDevType(String devTypeName) {
		Query query=getSession().createQuery("select m from DeviceModel m, DeviceType t where m.deviceType=t and t.devTypeName=:devTypeName");
		query.setString("devTypeName", devTypeName);
		return query.list();
	}


}
