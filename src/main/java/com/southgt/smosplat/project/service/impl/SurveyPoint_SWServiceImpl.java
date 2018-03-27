package com.southgt.smosplat.project.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.service.ISWService;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceModelService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.organ.service.IDeviceTypeService;
import com.southgt.smosplat.project.dao.ICableMeterDao;
import com.southgt.smosplat.project.dao.ISurveyPoint_SWDao;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.ICableMeterService;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;
import com.southgt.smosplat.project.service.IWarningService;

@Service("sp_SWService")
public class SurveyPoint_SWServiceImpl extends BaseServiceImpl<SurveyPoint_SW> implements ISurveyPoint_SWService {
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IDeviceService deviceService;
	
	@Resource
	IDeviceTypeService devTypeService;
	
	@Resource
	IDeviceModelService deviceModelService;
	
	@Resource
	ICableMeterService cableMeterService;
	
	@Resource
	IWarningService warningService;
	
	@Resource
	IMcuService mcuService;
	
	@Resource
	ISWService swService;
	
	@Resource(name="sp_SWDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_SW> dao) {
		super.setDao(dao);
	}
	

	@Override
	public List<SurveyPoint_SW> getSP_SWs(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_SW> sps=((ISurveyPoint_SWDao)getDao()).getSurveyPoint_SWs(projectUuid,monitorItemUuid);
		//需要按照测点编号进行排序
		
		return sps;
	}

	@Override
	public void updateSP_SW(Project project, SurveyPoint_SW surveyPoint,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid) throws Exception {
		long num=((ISurveyPoint_SWDao)getDao()).getSP_SWByCodeExceptSelf(project.getProjectUuid(), surveyPoint.getMonitorItem().getMonitorItemUuid(), surveyPoint.getCode(),surveyPoint.getSurveyPointUuid());
		if(num>0){
			throw new Exception("已存在相同编号的监测点！");
		}
		surveyPoint.setProject(project);
		Mcu mcu=mcuService.getEntity(mcuUuid);
		Device device=deviceService.getEntity(surveyPoint.getDevice().getDeviceUuid());
		device.setDevType(devTypeService.getDeviceTypeByName("水位计"));
		device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
		device.setSn(deviceSn);
		device.setMcu(mcu);
		device.setModuleNum(moduleNum);
		device.setPointNum(channelNum);
		deviceService.updateDevice(device);
		
		surveyPoint.setDevice(device);
		getDao().updateEntity(surveyPoint);
	}

	@Override
	public void deleteSP_SW(String surveyPointUuid) {
		SurveyPoint_SW sp=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????
		swService.deleteSW_DataBySurveyPoint(surveyPointUuid);
		getDao().deleteEntity(sp);
		if(sp.getDevice()!=null){
			deviceService.deleteEntity(sp.getDevice());
		}
	}

	@Override
	public List<SurveyPoint_SW> addSP_SW(Project project, SurveyPoint_SW tempSP,String deviceSn,
			String mcuUuid,int moduleNum,int channelNum,String devModelUuid) throws Exception {
		long num=((ISurveyPoint_SWDao)getDao()).getSP_SWByCode(project.getProjectUuid(), tempSP.getMonitorItem().getMonitorItemUuid(), tempSP.getCode());
		if(num>0){
			throw new Exception("已存在相同编号的设备！");
		}else{
			Warning w = warningService.getEntity(tempSP.getWarning().getWarningUuid());
			tempSP.setWarning(w);
			tempSP.setProject(project);
			Mcu mcu=mcuService.getEntity(mcuUuid);
			
			Device device = new Device();
			device.setDevType(devTypeService.getDeviceTypeByName("水位计"));
			device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
			device.setMcu(mcu);
			device.setModuleNum(moduleNum);
			device.setPointNum(channelNum);
			device.setSn(deviceSn);
			device.setOrgan(tempSP.getProject().getOrgan());
			device.setProjectUuid(tempSP.getProject().getProjectUuid());
			deviceService.addDevice(device);
			
			tempSP.setDevice(device);
			getDao().saveEntity(tempSP);
		}
		return null;
	}

	@Override
	public List<Device> getDeviceSnByItemName(int number,String stressType) {
		String devTypeName="";
		String monitorItem="";
		List<Device>list=new ArrayList<Device>();
		if(number==1){
			
		}else if(number==4){
			
		}else if(number==6){
			
		}else if(number==10){
			
		}else if(number==12){
			devTypeName="水位计";
			monitorItem="SurveyPoint_SW";
			list= ((ISurveyPoint_SWDao)getDao()).getDeviceSnByItemName(devTypeName, monitorItem);
		}else if(number==15){
			monitorItem="Stress";
			list= ((ISurveyPoint_SWDao)getDao()).getDeviceSnByItemName(stressType, monitorItem);
		}else if(number==18){
			devTypeName="锚索计";
			monitorItem="CableMeter";
			list= ((ISurveyPoint_SWDao)getDao()).getDeviceSnByItemName(devTypeName, monitorItem);
		}
		
		return list;
	}


	@Override
	public List<SurveyPoint_SW> addSP_SW(Project project, SurveyPoint_SW tempSP, int spCount) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SurveyPoint_SW getSPSWByDevice(String deviceUuid) {
		return ((ISurveyPoint_SWDao)getDao()).getSPSWByDevice(deviceUuid);
	}


	@Override
	public List<SurveyPoint_SW> getSP_SWsByProject(String projectUuid) {
		return ((ISurveyPoint_SWDao)getDao()).getSP_SWsByProject(projectUuid);
	}


}
