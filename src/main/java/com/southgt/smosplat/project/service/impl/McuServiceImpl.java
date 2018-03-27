package com.southgt.smosplat.project.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.entity.DeviceModel;
import com.southgt.smosplat.organ.entity.DeviceType;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.organ.service.IDeviceModelService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.organ.service.IDeviceTypeService;
import com.southgt.smosplat.project.dao.IMcuDao;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.INetworkService;

/**
 * Mcu服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("mcuService")
public class McuServiceImpl extends BaseServiceImpl<Mcu> implements IMcuService {
	
	@Resource
	IDeviceTypeService deviceTypeService;
	
	@Resource
	IDeviceModelService deviceModelService;
	
	@Resource
	IDeviceService deviceService;
	
	@Resource
	INetworkService networkService;
	
	@Resource(name="mcuDao")
	@Override
	public void setDao(IBaseDao<Mcu> dao) {
		super.setDao(dao);
	}

	@Override
	public void addMcu(Organ organ,Project project, Mcu mcu, String deviceSN, String devModelUuid) throws Exception {
		//项目下编号要唯一
		long number=deviceService.getDeviceNumber(project.getProjectUuid(),deviceSN);
		if(number>0){
			throw new Exception("设备编号已存在！");
		}
		//增加mcu的同时增加一个device
		DeviceType dt=deviceTypeService.getDeviceTypeByName("mcu");
		DeviceModel dm=deviceModelService.getEntity(devModelUuid);
		Device d=new Device();
		d.setDevType(dt);
		d.setDeviceModel(dm);
		d.setOrgan(organ);
		d.setProjectUuid(project.getProjectUuid());
		d.setSn(deviceSN);
		deviceService.saveEntity(d);
		mcu.setDeviceUuid(d.getDeviceUuid());
		mcu.setProject(project);
		mcu.setCreateTime(new Date());
		getDao().saveEntity(mcu);
	}

	@Override
	public void updateMcu(Organ organ,Project project, Mcu mcu, String deviceUuid, String deviceSN, String devModelUuid) throws Exception {
		//除了自己外名称不能重复
		long number=deviceService.getDeviceNumberExceptSelf(project.getProjectUuid(),deviceSN,deviceUuid);
		if(number>0){
			throw new Exception("设备编号已存在！");
		}
		//更新device
		DeviceModel dm=deviceModelService.getEntity(devModelUuid);
		Device d=deviceService.getEntity(deviceUuid);
		//mcu对应的device指向的mcu是空的
		d.setMcu(null);
		d.setModuleNum(null);
		d.setPointNum(null);
		d.setDeviceModel(dm);
		d.setSn(deviceSN);
		deviceService.updateEntity(d);
		//更新mcu
		mcu.setDeviceUuid(d.getDeviceUuid());
		mcu.setProject(project);
		getDao().updateEntity(mcu);
	}

	@Override
	public void deleteMcu(String mcuUuid) throws Exception {
		//删除mcu时同时需要删除对应的device
		Mcu m=getDao().getEntity(mcuUuid);
		//删除mcu时需要判断有没有设备在连接着该mcu
		long number=deviceService.getDeviceNumberByMcu(mcuUuid);
		if(number>0){
			throw new Exception("有设备正在连接到此mcu，不允许删除！");
		}
		deviceService.deleteEntity(deviceService.getEntity(m.getDeviceUuid()));
		getDao().deleteEntity(m);
	}

	@Override
	public long getMcusCountByNetwork(String networkUuid) {
		return ((IMcuDao)getDao()).getMcusCountByNetwork(networkUuid);
	}

	@Override
	public List<Mcu> getMcus(String projectUuid) {
		return ((IMcuDao)getDao()).getMcus(projectUuid);
	}

	@Override
	public Date setFirstCollectTime(Mcu mcu,int interval) throws Exception {
//		//找出使用该网络连接的所有正在采集中的mcu
//		Network network=mcu.getNetwork();
//		//获得所有mcu
//		List<Mcu> mcuList=((IMcuDao)getDao()).getCollectingMcusByNetwork(network.getNetworkUuid());
//		//所有采集中的mcu的所有采集时间点
//		List<Long> collectTimes=new ArrayList<Long>();
//		for (int i = 0; i < mcuList.size(); i++) {
//			Date firstCollectTime=mcuList.get(i).getFirstCollectTime();
//			collectTimes.addAll(getAllCollectTimes(firstCollectTime,interval));
//		}
//		//循环对比，如果每一个时间点相差都在2分钟以上，则该时间点可用，否则不可用
//		//以一分钟作为差值做对比，一共对比24*60次，如果仍没有可用时间点，则不能进行采集
//		//当前mcu想要开始采集的第一个时间点
//		Calendar calendar=Calendar.getInstance();
//		calendar.setTime(new Date());
//		boolean canUse=false;
//		int i=0;
//		while(i<1440){
//			//找出当前mcu的所有采集时间点(每个采集时间点)
//			List<Long> mcuCollectTimes=getAllCollectTimes(calendar.getTime(),interval);
//			//循环做对比
//			boolean lessThan2Minute=false;
//			for (int j = 0; j < mcuCollectTimes.size(); j++) {
//				for (int j2 = 0; j2 < collectTimes.size(); j2++) {
//					//如果相差2分钟以内，则不能用该时间点
//					if(Math.abs(mcuCollectTimes.get(j)-collectTimes.get(j2))<(2*60*1000)){
//						lessThan2Minute=true;
//						break;
//					}
//				}
//				//跳出外层循环
//				if(lessThan2Minute){
//					break;
//				}
//			}
//			if(lessThan2Minute){
//				//时间往后移一分钟，看看合不合适
//				calendar.add(Calendar.MINUTE, 1);
//				i++;
//			}else{
//				canUse=true;
//				break;
//			}
//		}
//		if(!canUse){
//			throw new Exception("当前没有合适的采集时间可以使用，请停止其他通讯设备或者增加网络连接！");
//		}
//		mcu.setFirstCollectTime(calendar.getTime());
//		return calendar.getTime();
		return null;
	}

	/**
	 * 根据第一次采集时间以及采集间隔（分钟）获取所有采集时间点 
	 */
	private List<Long> getAllCollectTimes(Date firstCollectTime, int interval) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(firstCollectTime);
		Calendar firstTimeInDay=Calendar.getInstance();
		firstTimeInDay.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
		Calendar lastTimeInDay=Calendar.getInstance();
		lastTimeInDay.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
		List<Long> timeList=new ArrayList<Long>();
		//求出之前的时间点
		Calendar tempCalendar=Calendar.getInstance();
		tempCalendar.setTime(calendar.getTime());
		while(tempCalendar.getTimeInMillis()>=firstTimeInDay.getTimeInMillis()){ 
			timeList.add(tempCalendar.getTimeInMillis());
			tempCalendar.add(Calendar.MINUTE, -interval);
		}
		//求出之后的时间点
		tempCalendar.setTime(calendar.getTime());
		tempCalendar.add(Calendar.MINUTE, interval);
		while(tempCalendar.getTimeInMillis()<=lastTimeInDay.getTimeInMillis()){
			timeList.add(tempCalendar.getTimeInMillis());
			tempCalendar.add(Calendar.MINUTE, interval);
		}
		//排序
		timeList.sort(new Comparator<Long>() {

			@Override
			public int compare(Long o1, Long o2) {
				return (int) (o1-o2);
			}
		});
		return timeList;
	}

	@Override
	public List<Mcu> getMcusByNetwork(String networkUuid) {
		return ((IMcuDao)getDao()).getMcusByNetwork(networkUuid);
	}

	@Override
	public List<Mcu> getMcusByOrgan(String organUuid) {
		return ((IMcuDao)getDao()).getMcusByOrgan(organUuid);
	}

	@Override
	public long getMCUNumberByProject(Project project) {
		return ((IMcuDao)getDao()).getMCUNumberByProject(project.getProjectUuid());
	}

}
