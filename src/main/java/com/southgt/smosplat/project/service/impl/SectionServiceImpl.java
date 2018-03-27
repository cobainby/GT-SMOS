package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.ISectionDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Section;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISectionService;
import com.southgt.smosplat.project.service.ISurveyPointService;

/**
 * 断面服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月30日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("sectionService")
public class SectionServiceImpl extends BaseServiceImpl<Section> implements ISectionService {
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	ISurveyPointService surveyPointService;

	@Resource(name="sectionDao")
	@Override
	public void setDao(IBaseDao<Section> dao) {
		super.setDao(dao);
	}

	@Override
	public void addSection(Project project, Section section) throws Exception {
		long number=((ISectionDao)getDao()).getSectionsNumber(project.getProjectUuid(),section.getMonitorItem().getMonitorItemUuid(),section.getSectionName());
		if(number>0){
			throw new Exception("名称已经存在！");
		}
		section.setProject(project);
		getDao().saveEntity(section);
	}

	@Override
	public List<Section> getSections(String projectUuid, String monitorItemUuid) {
		return ((ISectionDao)getDao()).getSections(projectUuid, monitorItemUuid);
	}

	@Override
	public void updateSection(Project project, Section section) throws Exception {
		//断面名称不能重复
		long number=((ISectionDao)getDao()).getSectionsNumberExceptSelf(project.getProjectUuid(),section.getMonitorItem().getMonitorItemUuid(),section.getSectionName(),section.getSectionUuid());
		if(number>0){
			throw new Exception("名称已经存在！");
		}
		section.setProject(project);
		getDao().updateEntity(section);
	}

	@Override
	public void deleteSection(String sectionUuid) throws Exception {
		//如果有监测点在使用该预警信息，则不允许删除
		long number=surveyPointService.getSPNumbersBySection(sectionUuid);
		if(number>0){
			throw new Exception("断面信息正在被使用，不允许删除！");
		}
		getDao().deleteEntity(getDao().getEntity(sectionUuid));
		
	}


}
