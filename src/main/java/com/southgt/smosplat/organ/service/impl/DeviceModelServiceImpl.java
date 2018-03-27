package com.southgt.smosplat.organ.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IDeviceModelDao;
import com.southgt.smosplat.organ.entity.DeviceModel;
import com.southgt.smosplat.organ.service.IDeviceModelService;

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
@Service("deviceModelService")
public class DeviceModelServiceImpl extends BaseServiceImpl<DeviceModel> implements IDeviceModelService {

	@Resource(name="deviceModelDao")
	@Override
	public void setDao(IBaseDao<DeviceModel> dao) {
		super.setDao(dao);
	}

	@Override
	public DeviceModel getDeviceModelByName(String devModelName) {
		return ((IDeviceModelDao)getDao()).getDeviceModelByName(devModelName);
	}

	@Override
	public List<DeviceModel> getDeviceModelByDeviceType(String devTypeName) {
		return ((IDeviceModelDao)getDao()).getDeviceModelByDevType(devTypeName);
	}


}
