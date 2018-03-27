package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceModelService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.organ.service.IDeviceTypeService;
import com.southgt.smosplat.project.dao.ICableMeterDao;
import com.southgt.smosplat.project.dao.IStressDao;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Stress;
import com.southgt.smosplat.project.service.ICableMeterService;
import com.southgt.smosplat.project.service.IMcuService;

@Service("cableMeterService")
public class CableMeterServiceImpl extends BaseServiceImpl<CableMeter> implements ICableMeterService {

	@Resource(name="cableMeterDao")
	@Override
	public void setDao(IBaseDao<CableMeter> dao) {
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
	public List<CableMeter> getCableMeterByProject(String projectUuid) {
		List<CableMeter> list=((ICableMeterDao)getDao()).getCableMeterByProject(projectUuid);
		return list;
	}

	@Override
	public List<CableMeter> getCableMeterBySP_mt(String sp_MTUuid) {
		List<CableMeter> list=((ICableMeterDao)getDao()).getCableMeterBySP_mt(sp_MTUuid);
		return list;
	}

	@Override
	public void addCableMeter(CableMeter cableMeter,String deviceSn,String mcuUuid,
			int moduleNum,int channelNum,String devModelUuid) throws Exception {
		Mcu mcu=mcuService.getEntity(mcuUuid);
		
		Device device = new Device();
		device.setDevType(devTypeService.getDeviceTypeByName("锚索计"));
		device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
		device.setMcu(mcu);
		device.setModuleNum(moduleNum);
		device.setPointNum(channelNum);
		device.setSn(deviceSn);
		device.setOrgan(cableMeter.getProject().getOrgan());
		device.setProjectUuid(cableMeter.getProject().getProjectUuid());
		deviceService.addCableMeterDevice(device);
		
		cableMeter.setDevice(device);
		getDao().saveEntity(cableMeter);
	}

	@Override
	public void updateCableMeter(CableMeter cableMeter,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid) throws Exception {
		Mcu mcu=mcuService.getEntity(mcuUuid);
		
		Device device = deviceService.getEntity(cableMeter.getDevice().getDeviceUuid());
		device.setDevType(devTypeService.getDeviceTypeByName("锚索计"));
		device.setDeviceModel(deviceModelService.getEntity(devModelUuid));
		device.setMcu(mcu);
		device.setModuleNum(moduleNum);
		device.setPointNum(channelNum);
		device.setSn(deviceSn);
		device.setOrgan(cableMeter.getProject().getOrgan());
		device.setProjectUuid(cableMeter.getProject().getProjectUuid());
		deviceService.updateCableMeterDevice(device);
		
		cableMeter.setDevice(device);
		getDao().updateEntity(cableMeter);
	}

	@Override
	public List<CableMeter> getCableMeterByMcu(String mcuUuid) {
		List<CableMeter> list=((ICableMeterDao)getDao()).getCableMeterByMcu(mcuUuid);
		return list;
	}

	@Override
	public void delCableMeter(String cableMeterUuid) throws Exception {
		CableMeter cm=((ICableMeterDao)getDao()).getEntity(cableMeterUuid);
		((ICableMeterDao)getDao()).deleteEntity(cm);
		if(cm.getDevice()!=null){
			deviceService.deleteEntity(cm.getDevice());
		}
		
		
	}

	@Override
	public void delCableMeterBySurveyPoint(String surveyPointUuid){
		((ICableMeterDao)getDao()).deleteCaleMeterBySP(surveyPointUuid);
		
	}

	@Override
	public CableMeter getCableMeterByDevice(String deviceUuid) {
		return((ICableMeterDao)getDao()).getCalbeMeterByDevice(deviceUuid);
	}

}
