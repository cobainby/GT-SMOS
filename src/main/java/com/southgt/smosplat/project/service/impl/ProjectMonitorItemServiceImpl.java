package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IMonitorItemDao;
import com.southgt.smosplat.project.dao.IProjectMonitorItemDao;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;
import com.southgt.smosplat.project.service.IProjectMonitorItemService;
import com.southgt.smosplat.project.service.ISectionService;
import com.southgt.smosplat.project.service.ISurveyPointService;
import com.southgt.smosplat.project.service.IWarningService;

/**
 * 工程与监测项关联关系实体服务实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月24日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("projectMonitorItemService")
public class ProjectMonitorItemServiceImpl extends BaseServiceImpl<ProjectMonitorItem> implements IProjectMonitorItemService {
	
	@Resource
	IWarningService warningService;
	
	@Resource
	ISectionService sectionService;
	
	@Resource
	ISurveyPointService surveyPointService;
	
	@Resource
	IMonitorItemDao monitorItemDao;

	@Resource(name="projectMonitorItemDao")
	@Override
	public void setDao(IBaseDao<ProjectMonitorItem> dao) {
		super.setDao(dao);
	}

	@Override
	public List<ProjectMonitorItem> getMonitorItemsByProject(Project project) {
		return ((IProjectMonitorItemDao)getDao()).getMonitorItemsByProject(project.getProjectUuid());
	}

	@Override
	public void addMonitorItemsForProject(Project project, String monitorItemUuids) {
		String[] monitorItemUuidArray=monitorItemUuids.split(",");
		for (int i = 0; i < monitorItemUuidArray.length; i++) {
			MonitorItem monitorItem=monitorItemDao.getEntity(monitorItemUuidArray[i]);
			ProjectMonitorItem projectMonitorItem=new ProjectMonitorItem();
			projectMonitorItem.setProject(project);
			projectMonitorItem.setMonitorItem(monitorItem);
			getDao().saveEntity(projectMonitorItem);
		}
	}

	@Override
	public void deleteMonitorItemForProject(Project project, String monitorItemUuid) throws Exception {
		try {
			//?????????????????删除所有监测测点的数据
			//删除所有监测点
			surveyPointService.batchEntityByHQL("delete from SurveyPoint sp where sp.project.projectUuid=? and sp.monitorItem.monitorItemUuid=?", project.getProjectUuid(),monitorItemUuid);
			//删除所有预警设置
			warningService.batchEntityByHQL("delete from Warning w where w.project.projectUuid=? and w.monitorItem.monitorItemUuid=?", project.getProjectUuid(),monitorItemUuid);
			//删除所有断面设置
			sectionService.batchEntityByHQL("delete from Section s where s.project.projectUuid=? and s.monitorItem.monitorItemUuid=?", project.getProjectUuid(),monitorItemUuid);
			//????????????????删除所有监测项设置
			//删除监测项目
			getDao().batchEntityByHQL("delete from ProjectMonitorItem pm where pm.project.projectUuid=? and pm.monitorItem.monitorItemUuid=?", project.getProjectUuid(),monitorItemUuid);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}


}
