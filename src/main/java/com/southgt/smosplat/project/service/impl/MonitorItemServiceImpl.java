package com.southgt.smosplat.project.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IMonitorItemDao;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.service.IMonitorItemService;

/**
 * 监测项服务接口实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月24日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("monitorItemService")
public class MonitorItemServiceImpl extends BaseServiceImpl<MonitorItem> implements IMonitorItemService {

	@Resource(name="monitorItemDao")
	@Override
	public void setDao(IBaseDao<MonitorItem> dao) {
		super.setDao(dao);
	}

	@Override
	public MonitorItem getMonitorItemByNumber(int i) {
		return ((IMonitorItemDao)getDao()).getMonitorItemByNumber(i);
	}

	@Override
	public MonitorItem getMonitorItemByCode(String code) {
		return ((IMonitorItemDao)getDao()).getMonitorItemByCode(code);
	}




}
