package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.service.IMTService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.dao.ISurveyPoint_MTDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.ICableMeterService;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_MTService;
import com.southgt.smosplat.project.service.IWarningService;

@Service("sp_MTService")
public class SurveyPoint_MTServiceImpl extends BaseServiceImpl<SurveyPoint_MT> implements ISurveyPoint_MTService {

	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IWarningService warningService;
	
	@Resource
	IMTService mtService;
	
	@Resource
	ICableMeterService cableMeterService;
	
	@Resource
	IDeviceService deviceService;
	
	@Resource(name="sp_MTDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_MT> dao) {
		super.setDao(dao);
	}
	
	@Override
	public void addSP_MT(Project project, SurveyPoint_MT tempSP) throws Exception {
		tempSP.setProject(project);
		Warning w=warningService.getEntity(tempSP.getWarning().getWarningUuid());
		tempSP.setWarning(w);
		long num=((ISurveyPoint_MTDao)getDao()).getSP_mtNumByCode(project.getProjectUuid(), tempSP.getMonitorItem().getMonitorItemUuid(), tempSP.getCode());
		
		if(num>0){
			throw new Exception("已存在相同测点编号的测点！");
		}else{
			long deviceNum=deviceService.getDeviceNumber(project.getProjectUuid(), tempSP.getDevCode());
			if(deviceNum>0){
				throw new Exception("已存在相同设备编号的设备！");
			}else{
				((ISurveyPoint_MTDao)getDao()).saveEntity(tempSP);
			}
		}
	
	}

	@Override
	public List<SurveyPoint_MT> getSP_MTs(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_MT> sps=((ISurveyPoint_MTDao)getDao()).getSurveyPoint_MTs(projectUuid,monitorItemUuid);
		return sps;
	}

	@Override
	public void updateSP_MT(Project project, SurveyPoint_MT surveyPoint) throws Exception {
		surveyPoint.setProject(project);
		long num=((ISurveyPoint_MTDao)getDao()).getSP_mtNumByCodeExceptSelf(project.getProjectUuid(), surveyPoint.getMonitorItem().getMonitorItemUuid(), surveyPoint.getCode(),surveyPoint.getSurveyPointUuid());
		if(num>0){
			throw new Exception("已存在相同测点编号的锚索设备！");
		}else{
			getDao().updateEntity(surveyPoint);
		}
	}

	@Override
	public void deleteSP_MT(String surveyPointUuid) {
		SurveyPoint_MT sp=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????删除数据，删除锚索实体和设备
		mtService.deleteMTDataBySurveyPoint(surveyPointUuid);
		cableMeterService.delCableMeterBySurveyPoint(surveyPointUuid);
		getDao().deleteEntity(sp);
	}


}
