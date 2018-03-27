package com.southgt.smosplat.organ.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IDeviceTypeDao;
import com.southgt.smosplat.organ.entity.DeviceType;
import com.southgt.smosplat.organ.service.IDeviceTypeService;

/**
 * 设备类型服务接口实现类
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
@Service("devTypeService")
public class DeviceTypeServiceImpl extends BaseServiceImpl<DeviceType> implements IDeviceTypeService {

	@Resource(name="devTypeDao")
	@Override
	public void setDao(IBaseDao<DeviceType> dao) {
		super.setDao(dao);
	}

	@Override
	public List<DeviceType> getAllDevType() {
		List<DeviceType> devTypeList=((IDeviceTypeDao)getDao()).findAllEntity();
		return devTypeList;
	}

	@Override
	public DeviceType getDeviceTypeByName(String name) {
		return ((IDeviceTypeDao)getDao()).getDeviceTypeByName(name);
	}
	
	
}
