package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IWarningDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPointService;
import com.southgt.smosplat.project.service.IWarningService;

/**
 * 预警信息服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月28日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("warningService")
public class WarningServiceImpl extends BaseServiceImpl<Warning> implements IWarningService {

	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	ISurveyPointService surveyPointService;
	
	@Resource(name="warningDao")
	@Override
	public void setDao(IBaseDao<Warning> dao) {
		super.setDao(dao);
	}

	@Override
	public void addWarning(Project project, Warning warning) throws Exception {
		//预警名称不能重复
		long number=((IWarningDao)getDao()).getWarningsNumber(project.getProjectUuid(),warning.getMonitorItem().getMonitorItemUuid(),warning.getWarningName());
		if(number>0){
			throw new Exception("名称已经存在！");
		}
		warning.setProject(project);
		getDao().saveEntity(warning);
	}

	@Override
	public List<Warning> getWarnings(String projectUuid, String monitorItemUuid) {
		return ((IWarningDao)getDao()).getWarnings(projectUuid,monitorItemUuid);
	}

	@Override
	public void updateWarning(Project project, Warning warning) throws Exception {
		//预警名称不能重复
		long number=((IWarningDao)getDao()).getWarningsNumberExceptSelf(project.getProjectUuid(),warning.getMonitorItem().getMonitorItemUuid(),warning.getWarningName(),warning.getWarningUuid());
		if(number>0){
			throw new Exception("名称已经存在！");
		}
		warning.setProject(project);
		getDao().updateEntity(warning);
	}

	@Override
	public void deleteWarning(String warningUuid) throws Exception {
		//如果有监测点在使用该预警信息，则不允许删除
		long number=surveyPointService.getSPNumbersByWarning(warningUuid);
		if(number>0){
			throw new Exception("预警信息正在被使用，不允许删除！");
		}
		getDao().deleteEntity(getDao().getEntity(warningUuid));
	}

}
