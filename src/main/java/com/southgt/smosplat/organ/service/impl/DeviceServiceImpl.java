package com.southgt.smosplat.organ.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IDeviceDao;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IMcuService;

@Service("deviceService")
public class DeviceServiceImpl extends BaseServiceImpl<Device> implements IDeviceService {

	@Resource
	private IMcuService mcuService;
	
	@Resource(name="deviceDao")
	@Override
	public void setDao(IBaseDao<Device> dao) {
		super.setDao(dao);
	}
	
	@Override
	public List<Device> getDeviceByOrganUuid(String organUuid, int i) {
		List<Device> deviceList=((IDeviceDao)getDao()).getDeviceByOrganUuid(organUuid,i);
		return deviceList;
	}

	@Override
	public void addDevice(Device device) throws Exception {
		long num=((IDeviceDao)getDao()).getDeviceBySN(device.getOrgan().getOrganUuid(), device.getSn());
		if(num>0){
			throw new Exception("已存在相同的设备编号");
		}else{
			getDao().saveEntity(device);
		}
	}

	@Override
	public void updateDevice(Device device) throws Exception {
		long num=((IDeviceDao)getDao()).getDeviceBySNExceptSelf(device.getOrgan().getOrganUuid(), device.getSn(),device.getDeviceUuid());
		if(num>0){
			throw new Exception("已存在相同的设备编号");
		}else{
			getDao().updateEntity(device);
		}
		
	}

	@Override
	public List<Device> getUsingDeviceByOrganUuid(String organUuid) {
		return ((IDeviceDao)getDao()).getUsingDeviceByOrgan(organUuid);
	}

	@Override
	public List<Device> getDevicesByProject(Project project) {
		return ((IDeviceDao)getDao()).getDevicesByProject(project);
	}

	@Override
	public void updateDeviceForMcu(String deviceUuid, String mcuUuid, String moduleNum, String pointNum) {
		Device d=getEntity(deviceUuid);
		Mcu mcu=mcuService.getEntity(mcuUuid);
		d.setMcu(mcu);
		d.setModuleNum(Integer.parseInt(moduleNum));
		d.setPointNum(Integer.parseInt(pointNum));
		updateEntity(d);
	}

	@Override
	public void deleteDeviceForMcu(String deviceUuid) {
		Device d=getEntity(deviceUuid);
		d.setMcu(null);
		d.setModuleNum(null);
		d.setPointNum(null);
		updateEntity(d);
	}

	@Override
	public long getDeviceNumber(String projectUuid, String sn) {
		return ((IDeviceDao)getDao()).getDeviceNumber(projectUuid,sn);
	}

	@Override
	public long getDeviceNumberExceptSelf(String projectUuid, String sn, String deviceUuid) {
		return ((IDeviceDao)getDao()).getDeviceNumberExceptSelf(projectUuid,sn,deviceUuid);
	}

	@Override
	public long getDeviceNumberByMcu(String mcuUuid) {
		return ((IDeviceDao)getDao()).getDeviceNumberByMcu(mcuUuid);
	}

	@Override
	public List<Device> getUsingMcuMudleAndPointNumByMcu(String mcuUuid) {
		return ((IDeviceDao)getDao()).getUsingMcuMudleAndPointNumByMcu(mcuUuid);
	}

	@Override
	public List<Device> getUsingPointNumByMcuAndModuleNum(String mcuUuid, int moduleNum) {
		return ((IDeviceDao)getDao()).getUsingPointNumByMcuAndModuleNum(mcuUuid,moduleNum);
	}

	@Override
	public List<Device> getDevicesByMcu(String mcuUuid) {
		return ((IDeviceDao)getDao()).getDevicesByMcu(mcuUuid);
	}

	@Override
	public long getDeviceNumberByOrgan(String organUuid) {
		return ((IDeviceDao)getDao()).getDeviceNumberByOrgan(organUuid);
	}

	@Override
	public List<Device> getAutoDevicesByCurrentProject(String projectUuid, int i) {
		return ((IDeviceDao)getDao()).getAutoDevicesByCurrentProject(projectUuid,i);
	}

	@Override
	public void addCableMeterDevice(Device device) {
		getDao().saveEntity(device);
	}

	@Override
	public void updateCableMeterDevice(Device device) {
		getDao().updateEntity(device);
	}

	@Override
	public Map<String, List<Device>> getDeviceMapOrderByDevTypeOfOrgan(String organUuid,int isAuto) {
		Map<String,List<Device>> deviceMap=new HashMap<>();
		List<Device> deviceList=getDeviceByOrganUuid(organUuid, isAuto);
		if(deviceList.size()>0){
			for(Device d:deviceList){
				if(deviceMap.containsKey(d.getDevType().getDevTypeEname())){
					List<Device> dl=deviceMap.get(d.getDevType().getDevTypeEname());
					dl.add(d);
				}else{
					List<Device> dl=new ArrayList<>();
					dl.add(d);
					deviceMap.put(d.getDevType().getDevTypeEname(), dl);
				}
			}
		}
		return deviceMap;
	}
}
