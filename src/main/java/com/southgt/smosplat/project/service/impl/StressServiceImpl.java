package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceModelService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.organ.service.IDeviceTypeService;
import com.southgt.smosplat.project.dao.IStressDao;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Stress;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.IStressService;

@Service("stressService")
public class StressServiceImpl extends BaseServiceImpl<Stress> implements IStressService {

	@Resource(name="stressDao")
	@Override
	public void setDao(IBaseDao<Stress> dao) {
		super.setDao(dao);
	}
	
	@Resource
	IDeviceService deviceService;
	
	@Resource
	IDeviceTypeService devTypeService;
	
	@Resource
	IDeviceModelService deviceModelService;
	
	@Resource
	IMcuService mcuService;
	
	@Override
	public List<Stress> getStressByProject(String projectUuid) {
		List<Stress> list=((IStressDao)getDao()).getStressByProject(projectUuid);
		return list;
	}

	@Override
	public void addStress(Stress stress,String deviceSn,String devType,String mcuUuid,
			int moduleNum,int channelNum,String devModelUuid) throws Exception {
		
		long stressNum = ((IStressDao)getDao()).getStressNumByConditions(stress.getSp_ZC().getSurveyPointUuid(), stress.getName(), stress.getDevCode());
		if(stressNum>0){
			throw new Exception("已存在相同的设备名称！");
		}else{
			Mcu mcu=mcuService.getEntity(mcuUuid);
			
			Device device = new Device();
			device.setDevType(devTypeService.getDeviceTypeByName(devType));
			device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
			device.setMcu(mcu);
			device.setModuleNum(moduleNum);
			device.setPointNum(channelNum);
			device.setSn(deviceSn);
			device.setOrgan(stress.getProject().getOrgan());
			device.setProjectUuid(stress.getProject().getProjectUuid());
			deviceService.addDevice(device);
			
			stress.setDevice(device);
			((IStressDao)getDao()).saveEntity(stress);
		}
		
		
		
	}

	@Override
	public void updateStress(Stress stress,String deviceSn,String devType,String mcuUuid,
			int moduleNum,int channelNum,String devModelUuid) throws Exception {
		long num = ((IStressDao)getDao()).getStressNumByConditionsSelfException(stress.getSp_ZC().getSurveyPointUuid(), stress.getName(), stress.getDevCode(),stress.getStressUuid());
		if(num>0){
			throw new Exception("已存在相同的设备名称！");
		}else{
			Mcu mcu=mcuService.getEntity(mcuUuid);
			
			Device device = deviceService.getEntity(stress.getDevice().getDeviceUuid());
			device.setDevType(devTypeService.getDeviceTypeByName(devType));
			device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
			device.setMcu(mcu);
			device.setModuleNum(moduleNum);
			device.setPointNum(channelNum);
			device.setSn(deviceSn);
			device.setOrgan(stress.getProject().getOrgan());
			device.setProjectUuid(stress.getProject().getProjectUuid());
			deviceService.updateDevice(device);
			
			stress.setDevice(device);
			((IStressDao)getDao()).updateEntity(stress);
		}
		
	}

	@Override
	public List<Stress> getStressBySP_zc(String sp_ZCUuid) {
		List<Stress> list=((IStressDao)getDao()).getStressBySP_zc(sp_ZCUuid);
		return list;
	}

	@Override
	public void delStress(String stressUuid) throws Exception {
		Stress stress = ((IStressDao)getDao()).getEntity(stressUuid);
		((IStressDao)getDao()).deleteEntity(stress);
		if(stress.getDevice()!=null){
			deviceService.deleteEntity(stress.getDevice());
		}
		
	}

	@Override
	public Stress getStressByDevice(String deviceUuid) {
		return ((IStressDao)getDao()).getStressByDevice(deviceUuid);
	}

	@Override
	public void deleteStressBySurveyPoint(String surveyPointUuid) {
		((IStressDao)getDao()).deleteStressBySurveyPoint(surveyPointUuid);
		
	}

}
