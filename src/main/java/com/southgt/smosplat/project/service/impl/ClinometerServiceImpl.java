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
import com.southgt.smosplat.project.dao.IClinometerDao;
import com.southgt.smosplat.project.entity.Clinometer;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.service.IClinometerService;
import com.southgt.smosplat.project.service.IMcuService;

@Service("clinometerService")
public class ClinometerServiceImpl extends BaseServiceImpl<Clinometer> implements IClinometerService{

	@Resource IMcuService mcuService;
	
	@Resource
	IDeviceService deviceService;
	
	@Resource
	IDeviceTypeService devTypeService;
	
	@Resource
	IDeviceModelService deviceModelService;
	
	@Resource(name="clinometerDao")
	@Override
	public void setDao(IBaseDao<Clinometer> dao) {
		super.setDao(dao);
	}
	
	@Override
	public List<Clinometer> getClinometerByProject(String projectUuid) {
		List<Clinometer> list = ((IClinometerDao)getDao()).getClinometerByProject(projectUuid);
		return list;
	}

	@Override
	public List<Clinometer> getClinometerBySP_cx(String sp_CXUuid) {
		List<Clinometer> list=((IClinometerDao)getDao()).getClinometerBySP_cx(sp_CXUuid);
		return list;
	}

	@Override
	public void addClinometer(Clinometer clinometer, String deviceSn, String devType, String mcuUuid, String gap,
			String devModelUuid) throws Exception {
		long clinometerNum = ((IClinometerDao)getDao()).getClinometerNumByConditions(clinometer.getSp_CX().getSurveyPointUuid(), clinometer.getName(), clinometer.getDevCode());
		if(clinometerNum>0){
			throw new Exception("已存在相同的设备名称！");
		}else{
			Mcu mcu=mcuService.getEntity(mcuUuid);
			
			Device device = new Device();
			device.setDevType(devTypeService.getDeviceTypeByName(devType));
			device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
			device.setMcu(mcu);
			device.setModuleNum(1);
			device.setPointNum(5);
			device.setSn(deviceSn);
			device.setOrgan(clinometer.getProject().getOrgan());
			device.setProjectUuid(clinometer.getProject().getProjectUuid());
			deviceService.addDevice(device);
			clinometer.setDevice(device);
			clinometer.setGap(gap);
			((IClinometerDao)getDao()).saveEntity(clinometer);
		}
		
	}

	@Override
	public void updateClinometer(Clinometer clinometer, String deviceSn, String devType, String mcuUuid, String gap,
			String devModelUuid) throws Exception {
		long num = ((IClinometerDao)getDao()).getClinometerNumByConditionsSelfException(clinometer.getSp_CX().getSurveyPointUuid(), clinometer.getName(), clinometer.getDevCode(),clinometer.getClinometerUuid());
		if(num>0){
			throw new Exception("已存在相同的设备名称！");
		}else{
			Mcu mcu=mcuService.getEntity(mcuUuid);
			
			Device device = deviceService.getEntity(clinometer.getDevice().getDeviceUuid());
			device.setDevType(devTypeService.getDeviceTypeByName(devType));
			device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
			device.setMcu(mcu);
			device.setSn(deviceSn);
			device.setOrgan(clinometer.getProject().getOrgan());
			device.setProjectUuid(clinometer.getProject().getProjectUuid());
			deviceService.updateDevice(device);
			
			clinometer.setDevice(device);
			clinometer.setGap(gap);
			((IClinometerDao)getDao()).updateEntity(clinometer);
		}
		
	}

	@Override
	public void delClinometer(String clinometerUuid) throws Exception {
		Clinometer clinometer = ((IClinometerDao)getDao()).getEntity(clinometerUuid);
		((IClinometerDao)getDao()).deleteEntity(clinometer);
		if(clinometer.getDevice()!=null){
			deviceService.deleteEntity(clinometer.getDevice());
		}
		
	}

	@Override
	public Clinometer getClinometerByDevice(String deviceUuid) {
		return ((IClinometerDao)getDao()).getClinometerByDevice(deviceUuid);
	}

	@Override
	public void deleteClinometerBySurveyPoint(String surveyPointUuid) {
		((IClinometerDao)getDao()).deleteClinometerBySurveyPoint(surveyPointUuid);
		
	}

}
