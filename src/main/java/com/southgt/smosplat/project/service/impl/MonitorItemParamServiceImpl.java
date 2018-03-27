package com.southgt.smosplat.project.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IMonitorItemParamDao;
import com.southgt.smosplat.project.entity.MonitorItemParam;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IMonitorItemParamService;
import com.southgt.smosplat.project.service.IMonitorItemService;

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
@Service("monitorItemParamService")
public class MonitorItemParamServiceImpl extends BaseServiceImpl<MonitorItemParam> implements IMonitorItemParamService {
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource(name="monitorItemParamDao")
	@Override
	public void setDao(IBaseDao<MonitorItemParam> dao) {
		super.setDao(dao);
	}

	@Override
	public MonitorItemParam getMonitorItemParam(String projectUuid, String monitorItemUuid) {
		return ((IMonitorItemParamDao)getDao()).getMonitorItemParam(projectUuid,monitorItemUuid);
	}

	@Override
	public void saveOrUpdateMonitorItemParam(MonitorItemParam monitorItemParam, Project project) {
		monitorItemParam.setProject(project);
		getDao().saveOrUpdateEntity(monitorItemParam);
	}


}
