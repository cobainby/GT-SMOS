package com.southgt.smosplat.organ.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IDeviceTypeDao;
import com.southgt.smosplat.organ.entity.DeviceType;

/**
 * 设备类型数据库访问接口实现类
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月29日     YANG       v1.0.0        create</p>
 *
 */
@Repository("devTypeDao")
public class DeviceTypeDaoImpl extends BaseDaoImpl<DeviceType> implements IDeviceTypeDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(DeviceType.class).setCacheable(true);
	}
	
	@Override
	public DeviceType getDeviceTypeByName(String name) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("devTypeName", name));
		return (DeviceType) c.list().get(0);
	}

}
