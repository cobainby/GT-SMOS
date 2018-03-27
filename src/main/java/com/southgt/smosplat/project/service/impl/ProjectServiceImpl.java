package com.southgt.smosplat.project.service.impl;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xsocket.connection.INonBlockingConnection;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.data.dao.ICXDataDao;
import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.entity.ZC_Data;
import com.southgt.smosplat.data.service.ICXService;
import com.southgt.smosplat.data.service.IMTService;
import com.southgt.smosplat.data.service.ISWService;
import com.southgt.smosplat.data.service.IZCService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.data.vo.CabelMeterMeasureData;
import com.southgt.smosplat.data.vo.CxMessureData;
import com.southgt.smosplat.data.vo.StressMeasureData;
import com.southgt.smosplat.data.vo.WaterLevelMeasureData;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.organ.service.IAccountProjectService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.dao.IMonitorItemDao;
import com.southgt.smosplat.project.dao.IProjectDao;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Clinometer;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Stress;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;
import com.southgt.smosplat.project.service.ICableMeterService;
import com.southgt.smosplat.project.service.IClinometerService;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.INetworkService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.IStressService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;
import com.southgt.smosplat.project.service.IWarnningDataService;
import com.southgt.smosplat.project.service.autoCollect.ConnectCallback;
import com.southgt.smosplat.project.service.autoCollect.DeviceMapping;
import com.southgt.smosplat.project.service.autoCollect.mcu.IMcuCollectLogic;
import com.southgt.smosplat.project.service.autoCollect.mcu.Impl.Speed_McuCollectLogic;

/**
 * 工程接口服务实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月19日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl<Project> implements IProjectService {
	
	@Resource
	IStressService stressService;
	
	@Resource
	ICableMeterService cableService;
	
	@Resource
	ISurveyPoint_SWService swSurveyPointService;
	
	@Resource
	IMcuService mcuService;
	
	@Resource
	INetworkService networkService;
	
	@Resource
	IMonitorItemDao monitorItemDao;
	
	@Resource
	IZCService zcService;
	
	@Resource
	IMTService mtService;
	
	@Resource
	ISWService swService;
	
	@Resource
	ICXService cxService;
	
	@Resource
	IDeviceService deviceService;
	
	@Resource
	IWarnningDataService warningDataService;
	
	
	@Resource
	IAccountProjectService accountProjectService;
	
	
	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;

	@Resource(name="projectDao")
	@Override
	public void setDao(IBaseDao<Project> dao) {
		super.setDao(dao);
	}

	@Override
	public void addProject(Project project, Organ organ) throws Exception {
		//名称和编号不能重复
		long number=((IProjectDao)getDao()).hasProjectName(project.getProjectName());
		if(number>0){
			throw new Exception("工程名称已经存在！");
		}
//		long number1=((IProjectDao)getDao()).hasProjectCode(project.getCode());
//		if(number1>0){
//			throw new Exception("工程编号已经存在！");
//		}
		project.setOrgan(organ);
		getDao().saveEntity(project);
	}
	
	@Override
	public List<Project> getProjectsByOrgan(String organUuid) {
		return ((IProjectDao)getDao()).getProjectsByOrgan(organUuid);
	}

	@Override
	public List<Project> getProjects(Account account) {
		List<Project> projects=new ArrayList<Project>();
		//判断是否超级管理员
//		boolean isSuperAdmin=account.getLoginName().equals("superadmin")?true:false;
		//如果当前账户是超级管理员，则分页获取所有的工程
//		if(isSuperAdmin){
//			projects=((IProjectDao)getDao()).getProjects(account.getWorker().getOrgan());
//		}else{
//			//如果当前账户不是超级管理员，则根据当前账户所属机构获取工程
//			//如果是访客，则只需要保留有权限的工程，其他角色不做限制
//			if(account.getRole().getMark()==3){
//				projects =accountProjectService.getProjectsByAccount(account.getAccountUuid());
//			}else{
//				projects=((IProjectDao)getDao()).getProjects(account.getWorker().getOrgan());
//			}
//		}
		if(account.getRole().getMark()==3){
			projects =accountProjectService.getProjectsByAccount(account.getAccountUuid());
		}else{
			projects=((IProjectDao)getDao()).getProjects(account.getWorker().getOrgan());
		}
		return projects;
	}

	@Override
	public void deleteProject(String projectUuid) {
		//删除工程要将工程相关的设置，数据之类的删除掉
		//删除账户与可查看工程之间的关联记录
		//accountProjectService.batchEntityByHQL("delete from AccountProject ap where ap.project.projectUuid=?", projectUuid);
		//删除工程时不从数据库删除记录，因为涉及的关联关系太多了，只是将该记录设置为无效即可。
		//getDao().deleteEntity(getDao().getEntity(projectUuid));
		((IProjectDao)getDao()).deleteProject(projectUuid);
	}

	@Override
	public Project updateProject(Project project) throws Exception {
		//名称和编号不能跟其他记录重复
		long number=((IProjectDao)getDao()).hasProjectName(project.getProjectUuid(),project.getProjectName());
		if(number>0){
			throw new Exception("工程名称已经存在！");
		}
		long number1=((IProjectDao)getDao()).hasProjectCode(project.getProjectUuid(),project.getCode());
		if(number1>0){
			throw new Exception("工程编号已经存在！");
		}
		Project projectFromDB=getEntity(project.getProjectUuid());
		getDao().evictEntity(projectFromDB);
		project.setOrgan(projectFromDB.getOrgan());
		getDao().updateEntity(project);
		return project;
	}
	
	@Override
	public long getProjectNumberByOrgan(String organUuid) {
		return ((IProjectDao)getDao()).getProjectNumberByOrgan(organUuid);
	}

	@Override
	public Map<String, Object> getAllAutoCollectDevices(Project project) {
		Map<String, Object> map=new HashMap<String, Object>();
		//获取工程下所有设备
		List<Device> devices=deviceService.getDevicesByProject(project);
		map.put("devices", devices);
		return map;
	}

	@Override
	public void projectStartCollect(Project project,String networkUuids, int interval,int connectMcuTime) throws Exception {
//		//1、开始采集之前先自动测试一下所有的网络连接是否可用，如果有一个连接不通，则中止启动采集
//		//？？？？？？？？？？？？
//		//2、开始采集之前先自动测试一下所有的mcu是否已经连通了，如果有一个没连通，则中止启动采集
//		//？？？？？？？？？？？？？？？
//		//先对所有mcu进行设置，设置成功后才能获取数据
//		Map<String,Object> result = setMcuBeforeCollect(project,networkUuids, interval,connectMcuTime);
//		if((result.get("result") != null)&&Integer.parseInt(result.get("result").toString()) == -1){
//			//设置不成功，直接返回
//			return;
//		}
//		Calendar firstGetDataTime = (Calendar) result.get("firstGetDataTime");
//		// 同时启动多个线程让所有network同时开始接收数据
//		String[] networkIds = networkUuids.split(",");
//		for (String networkId : networkIds) {
//			Network network = networkService.getEntity(networkId);
//			//获得该网络连接所有mcu
//			List<Mcu> mcus = mcuService.getMcusByNetwork(network.getNetworkUuid());
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						projectStartCollectByNetwork(network,mcus,firstGetDataTime,interval,connectMcuTime);
//					} catch (Exception e) {
//						String msg = "出现异常："+e.getMessage()+"，时间："+new SimpleDateFormat("hh:mm:ss").format(new Date());
//						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
//					}
//				}
//			}).start();
//		}
	}
	
	@Override
	public void projectStartCollect_NEW(Project project,String networkUuids, int interval,int connectMcuTime) throws Exception {
		//1、开始采集之前先自动测试一下所有的网络连接是否可用，如果有一个连接不通，则中止启动采集
		//？？？？？？？？？？？？
		//2、开始采集之前先自动测试一下所有的mcu是否已经连通了，如果有一个没连通，则中止启动采集
		//？？？？？？？？？？？？？？？
		Calendar firstGetDataTime = Calendar.getInstance();
		//只有斯比特要进行这一步
		//先对所有mcu进行设置，设置成功后才能获取数据
		Map<String,Object> result = setMcuBeforeCollect(project,networkUuids, interval,connectMcuTime);
		if((result.get("result") != null)&&Integer.parseInt(result.get("result").toString()) == -1){
			//设置不成功，直接返回
			return;
		}
		firstGetDataTime.setTime(((Calendar) result.get("firstGetDataTime")).getTime());

		// 同时启动多个线程让所有network同时开始接收数据
		String[] networkIds = networkUuids.split(",");
		for (String networkId : networkIds) {
			Network network = networkService.getEntity(networkId);
			//获得该网络连接所有mcu
			List<Mcu> mcus = mcuService.getMcusByNetwork(network.getNetworkUuid());
			for(int i = 0; i < mcus.size(); i++){
				String mcuSn = mcus.get(i).getSn();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							List<Mcu> mcu = mcus.stream().filter(p -> p.getSn().equals(mcuSn)).collect(Collectors.toList());
							if(mcu.size() == 0){
								return;
							}
							Device tempD = deviceService.getEntity(mcu.get(0).getDeviceUuid());
							String className = DeviceMapping.operatorMapping.get(tempD.getDeviceModel().getDevModelName());
							IMcuCollectLogic mcuCollectLogic = null;
							try {
								mcuCollectLogic = (IMcuCollectLogic) Class.forName(className).newInstance();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							mcuCollectLogic.projectStartCollectByNetwork(network, mcu, firstGetDataTime, interval, connectMcuTime, uploadFileSrc);
						} catch (Exception e) {
							String msg = "出现异常："+e.getMessage()+"，时间："+new SimpleDateFormat("hh:mm:ss").format(new Date());
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
						}
					}
				}).start();
			}
		}
	}
	
	@Override
	public void projectStartCollect_LRK(Project project,String networkUuids, int interval,int connectMcuTime) throws Exception {
		Calendar firstGetDataTime = Calendar.getInstance();

		// 同时启动多个线程让所有network同时开始接收数据
		String[] networkIds = networkUuids.split(",");
		for (String networkId : networkIds) {
			Network network = networkService.getEntity(networkId);
//			//获得该网络连接所有mcu
			List<Mcu> mcus = mcuService.getMcusByNetwork(network.getNetworkUuid());
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Device tempD = deviceService.getEntity(mcus.get(0).getDeviceUuid());
							String className = DeviceMapping.operatorMapping.get(tempD.getDeviceModel().getDevModelName());
							IMcuCollectLogic mcuCollectLogic = null;
							try {
								mcuCollectLogic = (IMcuCollectLogic) Class.forName(className).newInstance();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							mcuCollectLogic.projectStartCollectByNetwork(network, mcus, firstGetDataTime, interval, connectMcuTime, uploadFileSrc);
						} catch (Exception e) {
							String msg = "出现异常："+e.getMessage()+"，时间："+new SimpleDateFormat("hh:mm:ss").format(new Date());
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
						}
					}
				}).start();
			}
		}
	
	
	private Map<String,Object> setMcuBeforeCollect(Project project,String networkUuids, int interval,int connectMcuTime) throws Exception{
		Map<String,Object> result=new HashMap<String,Object>();
		//先对所有mcu进行第一次采集时间以及采集间隔的设置
		String[] networkIds=networkUuids.split(",");
		//获得工程下mcu的数量
//		long mcuNumber=mcuService.getMCUNumberByProject(project);
		
		int mcuNumber = 0;
		for (String networkId : networkIds) {
			//得到网络连接
			Network network=networkService.getEntity(networkId);
			//获得该网络连接所有mcu
			List<Mcu> mcus=mcuService.getMcusByNetwork(network.getNetworkUuid());
			mcuNumber += mcus.size();
		}
		
		//从当前时间开始算出每个mcu的第一次采集时间以及获取开始数据的时间
		Calendar nowTime=Calendar.getInstance();
		nowTime.setTime(new Date());
		//计算开始获取数据的时间
		Calendar firstGetDataTime=Calendar.getInstance();
		firstGetDataTime.setTime(nowTime.getTime());
		//获取数据的时间为当前时间+每个mcu的设置时间的总和
		firstGetDataTime.add(Calendar.SECOND, (int) (mcuNumber*connectMcuTime));
		//开始采集时间往后延了1分钟，所以取数据的时间也往后延1分钟
		firstGetDataTime.add(Calendar.SECOND, 60);
		//接收数据时的时间,比采集时间提前30秒，尽量保证采数据采完自报后能提前打开端口接收
		firstGetDataTime.add(Calendar.SECOND,-30);//提前30秒钟打开端口，以免数据自报时端口还没打开导致丢数据
		for (String networkId : networkIds) {
			//得到网络连接
			Network network=networkService.getEntity(networkId);
			//获得该网络连接所有mcu
			List<Mcu> mcus=mcuService.getMcusByNetwork(network.getNetworkUuid());
			//为每个mcu设置系统时间、第一次采集时间、采集间隔时间
			//因为设置每个mcu也需要一点时间（假如设置一个mcu最长的时间为3分钟，那么设置同一个网络连接下的第一个mcu的第一次采集时间往后延:mcu数量*3分钟，目的是为了设置完所有mcu后再开始采数据）
			//所以开始采集的时间为当前时间+设置mcu需要的总时间
			Calendar firstCollectTime=Calendar.getInstance();
			firstCollectTime.setTime(nowTime.getTime());
			firstCollectTime.add(Calendar.SECOND, (int) (mcuNumber*connectMcuTime));
			for (int j = 0; j < mcus.size(); j++) {
				Mcu mcu=mcus.get(j);
				//因为现在所有mcu的采集间隔设为一样，并且所有mcu同时启动，所以只要一个dtu下的所有mcu的采集时间错开同样的时间即可
				//每个mcu延迟一定时间（这个时间是每个mcu给定的连接时间），每个mcu都延迟同样的时间，这样接收数据的task就每隔这个时间接收数据即可
				firstCollectTime.add(Calendar.SECOND, j*connectMcuTime);
				Device tempD=deviceService.getEntity(mcu.getDeviceUuid());
				String className = DeviceMapping.operatorMapping.get(tempD.getDeviceModel().getDevModelName());
				IMcuCollectLogic mcuCollectLogic=(IMcuCollectLogic) Class.forName(className).newInstance();
				List<Mcu> mcuL = new ArrayList<>();
				mcuL.add(mcus.get(j));
				mcuCollectLogic.setMcus(mcuL);
				
				mcuCollectLogic.init(firstCollectTime, interval);
				//设置mcu，因为设置需要一定时间，需要等待一下
				//超过给定的一个mcu的设置时间还没有设置成功，整个采集启动失败
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "开始设置【"+mcu.getSn()+"】,大约需要"+connectMcuTime+"秒，请等待设置完成后再操作！！！！！！@"+new SimpleDateFormat("hh:mm:ss").format(new Date())+"...");
				try {
					Thread.sleep(connectMcuTime*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(mcuCollectLogic.isGetDataOver()){
					//取第一次数据成功
					saveAutoReportData(mcuCollectLogic, network, Calendar.getInstance());
					
					firstCollectTime.setTime(mcuCollectLogic.getFirstCollectTime().getTime());
					firstGetDataTime.setTime(firstCollectTime.getTime());
					firstGetDataTime.add(Calendar.SECOND, -30);
				}
				
				if(!mcuCollectLogic.isSetOver()){
					//给前台推送消息，告诉前台哪个mcu设置失败
					ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "设置【"+mcu.getSn()+"】失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
					ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "【自动采集启动失败！】,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
					//断开连接
					ApplicationUtil.endConnectAction(mcu.getNetwork());
					//这里直接在这里修改工程状态会报错，需要在前台那边调用接口来修改采集状态
					//这里直接返回，不再设置下一mcu
					result.put("result", -1);
					return result;
				}
				
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "mcu:"+mcu.getSn()+"首次采集时间"+ firstCollectTime.getTime() +"@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "设置【"+mcu.getSn()+"】成功,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
			}
		}
		
		result.put("result", 0);
		//返回获取数据的时间
		result.put("firstGetDataTime", firstGetDataTime);
		return result;
	}
	
	public void projectStartCollectByNetwork(Network network, List<Mcu> mcus,Calendar firstGetDataTime, int interval,int connectMcuTime) throws Exception {
		//读取数据计数，用来算获取数据的时间
		int[] index = new int[]{0};
		//启动定时器，根据间隔打开端口获取数据
		//所有设备采集间隔一样，所以所有设备采集时间也统一设成一样的
		//设置mcu跟获取数据分开处理，是为了统一管理采集时间，因为设置需要耗费时间，不能保证时间一致
		//每个网络连接启动一个定时器,每隔采集间隔时间就打开端口依次接收mcu发回来的数据并存到数据库
		Timer timer = ApplicationUtil.addTimer(network);
		timer.scheduleAtFixedRate(new TimerTask() { //使用scheduleAtFixedRate以保证定时器运行的频率
			
			@Override
			public void run() {
					//异步去读取数据
					//循环所有mcu，每隔一定时间（mcu的通讯时间）读取一个mcu的数据,直到所有mcu循环读取完毕后手动关闭连接
					//读取数据时的时间就是所有mcu的采集时间
					Calendar collectTime=Calendar.getInstance();
					collectTime.setTime(firstGetDataTime.getTime());
					//除了第一次，每取一次数据，采集时间都加上间隔
					collectTime.add(Calendar.SECOND, index[0]*interval*6*10);
					//次数+1
					index[0]++;
					for (int j = 0; j < mcus.size(); j++) {
						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】开始读取数据！,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
						Device tempD = deviceService.getEntity(mcus.get(j).getDeviceUuid());
						String className = DeviceMapping.operatorMapping.get(tempD.getDeviceModel().getDevModelName());
						IMcuCollectLogic mcuCollectLogic = null;
						try {
							mcuCollectLogic = (IMcuCollectLogic) Class.forName(className).newInstance();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						mcuCollectLogic.setMcus(mcus);
						mcuCollectLogic.setUploadFileSrc(uploadFileSrc);
						//得到mcu连着的所有设备
						IDeviceService deviceService = (IDeviceService) ApplicationUtil.getWebApplicationContext().getBean("deviceService");
						List<Device> deviceList=deviceService.getDevicesByMcu(mcus.get(j).getMcuUuid());
						mcuCollectLogic.setDeviceList(deviceList);
						mcuCollectLogic.setInterval(interval);
						//读取数据是异步的，在mcu的连接时间内，无论取没取完数据，都把连接断开以免影响下一个mcu接收数据
						mcuCollectLogic.getAutoReportData();
						try {
							Thread.sleep(connectMcuTime*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//如果读取完毕后将数据存到数据库中
						if(mcuCollectLogic.isGetDataOver()){
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】从设备读取数据成功,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
							saveAutoReportData(mcuCollectLogic,network,collectTime);
						}else{
							ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(j).getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】从设备读取数据失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
							LogUtil.info("mcu【"+mcus.get(j).getSn()+"】从设备读取数据失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
						}
						//无论数据有没有读到（没有读到就丢弃），mcu连接时间到了后就要关闭连接以准备接受下一个mcu的数据
						ApplicationUtil.endConnectAction(mcus.get(j).getNetwork());
					}
					//回收一下垃圾，并调用cancel，让task结束掉
					//System.gc();
					//this.cancel();
					ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "网络连接【"+network.getNetworkName()+"】本次读取数据完成************************@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
			}
		}, firstGetDataTime.getTime(),interval*6*10*1000);//mcu设置自报、采集时间以及采集间隔后就会立刻进行采集，再次之前端口要先打开，准备好接收数据
	}
	
	private void saveAutoReportData(IMcuCollectLogic mcuCollectLogic,Network network,Calendar collectTime){
		//存数据到数据库，并发送通知到前台
		//存轴力计数据
		if(mcuCollectLogic.getStressDataList().size() > 0){
			//根据所有轴力计找出这些轴力计对应的测点
			List<SurveyPoint_ZC> sps=new ArrayList<SurveyPoint_ZC>();
			IStressService stressService=(IStressService) ApplicationUtil.getWebApplicationContext().getBean("stressService");
			List<Stress> sList = stressService.getStressByProject(network.getProject().getProjectUuid());
			for(Stress s : sList){
				SurveyPoint_ZC sp=s.getSp_ZC();
				if(!sps.contains(sp)){
					sps.add(sp);
				}
			}
			//存放推送到前台的数据
			List<ZC_Data> dataList =new ArrayList<>();
			//根据测点，算出测点对应的数据，并保存
			for (SurveyPoint_ZC sp : sps) {
				//一个点下的所有轴力计的数据
				List<StressMeasureData> sDataList = new ArrayList<StressMeasureData>();
				for (Stress s : sList) {
					if(s.getSp_ZC().getSurveyPointUuid().equals(sp.getSurveyPointUuid())){
						for (StressMeasureData sData : mcuCollectLogic.getStressDataList()) {
							if(sData.getStressUuid().equals(s.getStressUuid())){
								sDataList.add(sData);
							}
						}
					}
				}
				//算出这个点的数据(求平均？)
				double dataAverage = 0.0;
				if(sDataList != null && sDataList.size()>0){
					List<String> tempSp = new ArrayList<>();
					tempSp.add(sp.getSurveyPointUuid());
					//最新一条数据
					List<ZC_Data> latestZCData = zcService.getLatestOneZCDatasBySurveyPoints(tempSp);
					double gapOffset = 0.0;
					double gapChangeRate = 0.0;
					List<Stress> stressList = new ArrayList<>();
					
					for(int i = 0;i < sDataList.size();i++){
						stressList.add(stressService.getEntity(sDataList.get(i).getStressUuid()));
					}
					
					/*******************计算平均值********************/
					dataAverage = mcuCollectLogic.calZC(stressList, sDataList);
					
					ZC_Data zcData = new ZC_Data();
					
					if(latestZCData != null && latestZCData.size() > 0){
						gapOffset = dataAverage - latestZCData.get(0).getCalValue();
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectTime.getTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestZCData.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						gapChangeRate = gapOffset/days;
						//保留4位小数
						BigDecimal b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					}
					//保留2位小数
					BigDecimal b = new BigDecimal(dataAverage); 
					dataAverage = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					zcData.setCalValue(dataAverage);
					zcData.setGapOffset(gapOffset);
					//测量时间
					zcData.setCollectTime(collectTime.getTime());
					zcData.setSurveyPoint(sp);
					zcData.setGapChangeRate(gapChangeRate);
					dataList.add(zcData);
					//存数据库
					zcService.saveEntity(zcData);
				}
			}
			//把最新一次轴力数据推往前台
			String msg = JsonUtil.beanToJson(dataList);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuZCStartCollect/"+network.getProject().getProjectUuid(), msg);
			
		}
		if (mcuCollectLogic.getCableDataList().size() > 0) {
			// 锚索
			// 根据所有轴力计找出这些轴力计对应的测点
			List<SurveyPoint_MT> sps = new ArrayList<SurveyPoint_MT>();
			ICableMeterService cableService = (ICableMeterService) ApplicationUtil.getWebApplicationContext().getBean("cableMeterService");
			List<CableMeter> cList = cableService.getCableMeterByProject(network.getProject().getProjectUuid());
			for (CableMeter c : cList) {
				SurveyPoint_MT sp = c.getSp_MT();
				if (!sps.contains(sp)) {
					sps.add(sp);
				}
			}
			//存放推送到前台的数据
			List<MT_Data> dataList =new ArrayList<>();
			//根据测点，算出测点对应的数据，并保存
			for (SurveyPoint_MT sp : sps) {
				//一个点下的所有轴力计的数据
				List<CabelMeterMeasureData> cDataList=new ArrayList<>();
				for (CableMeter c : cList) {
					if(c.getSp_MT().getSurveyPointUuid().equals(sp.getSurveyPointUuid())){
						List<CabelMeterMeasureData> data = mcuCollectLogic.getCableDataList();
						for (CabelMeterMeasureData cData : data) {
							if(cData.getCableMeter().getCableMeterUuid().equals(c.getCableMeterUuid())){
								cDataList.add(cData);
							}
						}
					}
				}
				//算出这个点的数据
				double dataAverage = 0.0;
				if(cDataList != null && cDataList.size()>0){
					List<String> tempSp = new ArrayList<>();
					tempSp.add(sp.getSurveyPointUuid());
					//最新一条数据
					List<MT_Data> latestMTData = mtService.getLatestOneMTDatasBySurveyPoints(tempSp);
					double gapOffset = 0.0;
					double gapChangeRate = 0.0;
					//采集模数值，用','分隔
					String moduleData = "";
					for(int i = 0;i < cDataList.size();i++){
						//模数值字符串
						moduleData += Double.toString(cDataList.get(i).getModuleData()) + ',';
					}
					
					/*******************计算平均值********************/
					dataAverage = mcuCollectLogic.calMT(cDataList);
					
					MT_Data mtData = new MT_Data();
					if(latestMTData != null && latestMTData.size() > 0){
						gapOffset = dataAverage - latestMTData.get(0).getCalValue();
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(collectTime.getTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestMTData.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						//保留4位小数
						BigDecimal b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					}
					//保留2位小数
					BigDecimal b = new BigDecimal(dataAverage); 
					dataAverage = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					mtData.setCalValue(dataAverage);
					mtData.setGapOffset(gapOffset);
					//测量时间
					mtData.setCollectTime(collectTime.getTime());
					mtData.setSurveyPoint(sp);
					mtData.setGapChangeRate(gapChangeRate);
					
					LogUtil.info("mt模数：--------"+moduleData);
					
					mtData.setModuleData(moduleData);
					dataList.add(mtData);
					//存数据库
					mtService.saveEntity(mtData);
				}
			}
			//把最新一次轴力数据推往前台
			String msg = JsonUtil.beanToJson(dataList);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuMTStartCollect/"+network.getProject().getProjectUuid(), msg);
		}
		if(mcuCollectLogic.getWaterLevelDataList().size() > 0){
			//根据所有水位计找出这些水位计对应的测点
			List<SurveyPoint_SW> sps=new ArrayList<>();
			ISurveyPoint_SWService sp_SWService=(ISurveyPoint_SWService) ApplicationUtil.getWebApplicationContext().getBean("sp_SWService");
			List<SurveyPoint_SW> wList = sp_SWService.getSP_SWsByProject(network.getProject().getProjectUuid());
			for(SurveyPoint_SW s : wList){
				if(!sps.contains(s)){
					sps.add(s);
				}
			}
			//根据测点，算出测点对应的数据，并保存
			List<WaterLevelMeasureData> waterDataList = mcuCollectLogic.getWaterLevelDataList();
			if(waterDataList != null && waterDataList.size() > 0){
				//存放推往前台的数据
				List<SW_Data> dataList = new ArrayList<>();
				for (SurveyPoint_SW sp : sps) {
					//一个点下的水位计的数据
					WaterLevelMeasureData waterLevelData = new WaterLevelMeasureData();
					for (WaterLevelMeasureData wData : waterDataList) {
						if(wData.getWlSurveyPointUuid().equals(sp.getSurveyPointUuid())){
							waterLevelData = wData;
						}
					}
					if(waterLevelData.getCalculateValue() == null){
						//循环完没有匹配到点，这个点没采到数据
						continue;
					}
					SW_Data swData=new SW_Data();
					swData.setCalValue(waterLevelData.getCalculateValue());
					//采集模数值
					swData.setModuleData(waterLevelData.getModuleData());
					//测量时间
					swData.setCollectTime(collectTime.getTime());
					swData.setSurveyPoint(sp);
					
					//计算单次变化量和累计变化量，并推到前台
					
					List<String> tempSp = new ArrayList<>();
					tempSp.add(sp.getSurveyPointUuid());
					//最新一条数据
					List<SW_Data> latestSWData = swService.getLatestOneSWDatasBySurveyPoints(tempSp);
					//第一个时间点的数据
					List<SW_Data> firstSWData = swService.getFirstOneSWDatasBySurveyPoints(tempSp);
					double gapOffset = 0.0;
					double gapChangeRate = 0.0;
					//计算累计位移
					//只在有数据的情况下才进行计算
					// 水位计算值单次位移
					if(latestSWData != null && latestSWData.size() > 0){
						gapOffset = swData.getCalValue()- latestSWData.get(0).getCalValue();
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(swData.getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestSWData.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					}
					//保留2位小数
					BigDecimal b = new BigDecimal(gapOffset); 
					gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					// 水位计算值累计位移
					double accumOffset = 0.0;
					if (firstSWData !=  null && firstSWData.size() > 0) {
						accumOffset= latestSWData.get(0).getCalValue()- firstSWData.get(0).getCalValue() + sp.getOriginalTotalValue();
					}
					b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					swData.setAccumOffset(accumOffset);
					swData.setGapOffset(gapOffset);
					swData.setGapChangeRate(gapChangeRate);
					dataList.add(swData);
					
					//存数据库
					swService.saveEntity(swData);
				}
				//把最新一次数据推往前台
				String msg = JsonUtil.beanToJson(dataList);
//				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuSWStartCollect/"+network.getProject().getProjectUuid(), msg);
			} else {
				String msg="水位测点数据列表为空！";
				System.out.println(msg+"\r\n");
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
			}
		} if(mcuCollectLogic.getCxDataList().size() > 0) {
			//存放推送到前台的数据
			double gap = 0.0;
			double accum = 0.0;
			double rate = 0.0;
			//一个点下的所有测斜的数据
			List<CxMessureData> cDataList = mcuCollectLogic.getCxDataList();
			Clinometer clinometer = cDataList.get(0).getClinometer();
			cDataList = mcuCollectLogic.calCX(clinometer, cDataList);
			List<CX_Data> lastAllCxData = cxService.getOnePointLatestOneCXDataBySurveyPoint(clinometer.getSp_CX().getSurveyPointUuid());
			List<String> temp = new ArrayList<>();
			temp.add(clinometer.getSp_CX().getSurveyPointUuid());
			//头三条数据
			List<CX_Data> first3CxData = cxService.getThreeAscCXDataBySurveyPoint(temp);
			//头三条平均数据
			List<CX_Data> firstAveCxData = new ArrayList<>();
			if(first3CxData != null && first3CxData.size() > 0){
				List<Double> depths = new ArrayList<>();
				first3CxData.stream().forEach(p -> depths.add(p.getDepth()));
				HashSet h = new HashSet(depths);   
				depths.clear();   
				//去除depths重复的值
				depths.addAll(h);  
				for(int kk = 0; kk < depths.size(); kk++){
					double d = depths.get(kk);
					List<CX_Data> dataAtOneDepth = first3CxData.stream().filter(p -> p.getDepth() == d).collect(Collectors.toList());
					double aveCal = 0.0;
					for(CX_Data cd : dataAtOneDepth){
						aveCal += cd.getCalValue();
					}
					dataAtOneDepth.get(0).setCalValue(aveCal);
					firstAveCxData.add(dataAtOneDepth.get(0));
				}
			}
			CX_Data cxData = new CX_Data();
			for(int k = 0; k < cDataList.size(); k++){
				cxData.setDepth((k+1)*(Double.parseDouble(clinometer.getGap())));
				cxData.setCalValue(cDataList.get(k).getCalculateValue());
				cxData.setCollectTime(collectTime.getTime());
				cxData.setSurveyPoint(clinometer.getSp_CX());
				double depth = cxData.getDepth();
				//测量次序
				if(lastAllCxData != null && lastAllCxData.size() > 0){
					cxData.setSurveyOrder(lastAllCxData.get(0).getSurveyOrder() + 1);
					List<CX_Data> dataAtDepth = lastAllCxData.stream().filter(p -> p.getDepth() == depth).collect(Collectors.toList());
					if(dataAtDepth.size() > 0){
						gap = cxData.getCalValue() - dataAtDepth.get(0).getCalValue();
						
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cxData.getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dataAtDepth.get(0).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days++;
						}
						rate = Math.abs(gap / days);
						BigDecimal b = new BigDecimal(rate); 
						rate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						b = new BigDecimal(gap); 
						gap = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					}
				}else{
					cxData.setSurveyOrder(1);
				}
				
				if(firstAveCxData.size() > 0){
					List<CX_Data> firstDataAtDepth = firstAveCxData.stream().filter(p -> p.getDepth() == depth).collect(Collectors.toList());
					if(firstDataAtDepth.size() > 0){
						accum = cxData.getCalValue() - firstDataAtDepth.get(0).getCalValue();
						BigDecimal b = new BigDecimal(accum); 
						accum = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
					}
				}
				cxData.setGapOffset(gap);
				cxData.setAccumOffset(accum);
				cxData.setChangeRate(rate);
				cxService.saveEntity(cxData);
			}
		}
		
		
		
		//发短信给项目监测人员和监测负责人。
		String phones = "";
		String contacts = network.getProject().getMonitorWorker();
		contacts += network.getProject().getMonitorLeader();
		while(contacts.contains("(")||contacts.contains(")")){
			String temp = contacts.substring(contacts.indexOf("(")+1,contacts.indexOf(")"));
			if(!phones.contains(temp)){
				phones += temp+",";
			}
			contacts = contacts.substring(contacts.indexOf(")")+1);
		}
		//计算是否超限并更新工程状态。
		warningDataService.calWarnningOffsetByMonitorItem("SW", network.getProject(),phones);
		warningDataService.calWarnningOffsetByMonitorItem("ZC", network.getProject(),phones);
		warningDataService.calWarnningOffsetByMonitorItem("MT", network.getProject(),phones);
		warningDataService.calWarnningOffsetByMonitorItem("CX", network.getProject(),phones);
	}

	@Override
	public void projectStopCollect(Project project,String networkUuids) {
		String[] networkIds=networkUuids.split(",");
		for (int i = 0; i < networkIds.length; i++) {
			Network network=networkService.getEntity(networkIds[i]);
			//移除接收数据的线程
			ApplicationUtil.removeTimer(network);
			//断开所有端口的监听
			ApplicationUtil.endConnectAction(network);
		}
		String msg="【自动采集停止！】采集将在mcu获取最后一次数据之后停止！！！！！!"+"@"+new SimpleDateFormat("hh:mm:ss").format(new Date());
		ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+project.getProjectUuid(), msg);
	}
	
	@Override
	public void projectStartCallCollect(String networkUuids,int connectMcuTime) {
		//1、开始采集之前先自动测试一下所有的网络连接是否可用，如果有一个连接不通，则中止启动采集
		//？？？？？？？？？？？？
		//2、开始采集之前先自动测试一下所有的mcu是否已经连通了，如果有一个没连通，则中止启动采集
		//？？？？？？？？？？？？？？？
		// 利用线程让所有network同时开始
		String[] networkIds = networkUuids.split(",");
		for (String networkId : networkIds) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Network network = networkService.getEntity(networkId);
					try {
						callCollect(network, connectMcuTime);
					} catch (Exception e) {
						ApplicationUtil.endConnectAction(network);
						String msg = "出现异常：" + e.getMessage()+"，时间："+new SimpleDateFormat("hh:mm:ss").format(new Date());
						ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
					}
				}
			}).start();
		}
	}
	
	
	public void callCollect(Network network,int connectMcuTime) throws Exception {
		//获得该网络连接所有mcu
		List<Mcu> mcus = mcuService.getMcusByNetwork(network.getNetworkUuid());
		//读取数据时的时间就是所有mcu的采集时间
		Calendar collectTime = Calendar.getInstance();
		collectTime.setTime(new Date());
		for (int j = 0; j < mcus.size(); j++) {
			Device tempD=deviceService.getEntity(mcus.get(j).getDeviceUuid());
			String className = DeviceMapping.operatorMapping.get(tempD.getDeviceModel().getDevModelName());
			IMcuCollectLogic mcuCollectLogic = (IMcuCollectLogic) Class.forName(className).newInstance();
			List<Mcu> mcuL = new ArrayList<>();
			mcuL.add(mcus.get(j));
			mcuCollectLogic.setMcus(mcuL);
			//得到mcu连着的所有设备
			IDeviceService deviceService = (IDeviceService) ApplicationUtil.getWebApplicationContext().getBean("deviceService");
			List<Device> deviceList = deviceService.getDevicesByMcu(mcus.get(j).getMcuUuid());
			mcuCollectLogic.setDeviceList(deviceList);
			mcuCollectLogic.beginCollect();
			//mcu通讯时间采集跟获取数据对半分，mcu采集数据
			try {
				Thread.sleep((connectMcuTime / 2) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//获取采集数据
			mcuCollectLogic.getCallCollectData();
			try {
				Thread.sleep((connectMcuTime / 2) * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//如果读取完毕后将数据存到数据库中
			if(mcuCollectLogic.isGetDataOver()){
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】从设备读取数据成功,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
				saveAutoReportData(mcuCollectLogic,network,collectTime);
			}else{
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcus.get(j).getProject().getProjectUuid(), "mcu【"+mcus.get(j).getSn()+"】从设备读取数据失败！（原因：1、网络不通，2、mcu通讯时间太短，3、mcu故障）,@"+new SimpleDateFormat("hh:mm:ss").format(new Date()));
			}
			//无论数据有没有读到（没有读到就丢弃），mcu连接时间到了后就要关闭连接以准备接受下一个mcu的数据
			ApplicationUtil.endConnectAction(mcus.get(j).getNetwork());
		}
		
	}

	@Override
	public void testConnect(String networkUuid) throws Exception {
		Network network = networkService.getEntity(networkUuid);
		String msg = "正在测试网络..." + new SimpleDateFormat("hh:mm:ss").format(new Date());
		System.out.println(msg);
		ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
		ApplicationUtil.doConnectAction(network, new ConnectCallback(network) {
			@Override
			public void onConnected(INonBlockingConnection connection,String flag,boolean b) {
				
			}
		});
		Thread.sleep(3*1000);
		ApplicationUtil.endConnectAction(network);
		
	}

	@Override
	public void testMcu(String mcuUuid) throws Exception {
		Mcu mcu = mcuService.getEntity(mcuUuid);
		Device tempD=deviceService.getEntity(mcu.getDeviceUuid());
		String className = DeviceMapping.operatorMapping.get(tempD.getDeviceModel().getDevModelName());
		IMcuCollectLogic mcuCollectLogic = (IMcuCollectLogic) Class.forName(className).newInstance();
		List<Mcu> mcuL = new ArrayList<>();
		mcuL.add(mcu);
		mcuCollectLogic.setMcus(mcuL);
		String msg = "mcu" + mcu.getSn() + "正在测试mcu，预计需要90s..." + new SimpleDateFormat("hh:mm:ss").format(new Date());
		System.out.println(msg);
		ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcu.getProject().getProjectUuid(), msg);
		mcuCollectLogic.testMcu();
		//最多等待90s
		int count = 0;
		while(!mcuCollectLogic.isTestOver()){
			if(count < 30){
				try {
					Thread.sleep(3 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count ++;
			}else{
				break;
			}
		}
		if(!mcuCollectLogic.isTestOver()){
			msg = "mcu" + mcu.getSn() + "mcu在90s内测试失败" + new SimpleDateFormat("hh:mm:ss").format(new Date());
			System.out.println(msg);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcu.getProject().getProjectUuid(), msg);
			ApplicationUtil.endConnectAction(mcu.getNetwork());
		}else{
			msg = "mcu" + mcu.getSn() + "mcu测试成功" + new SimpleDateFormat("hh:mm:ss").format(new Date());
			System.out.println(msg);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+mcu.getProject().getProjectUuid(), msg);
		}
	}

	@Override
	public void updateInterval(String projectUuid, String interval) {
		Project project = getDao().getEntity(projectUuid);
		project.setCollectInterval(interval);
		getDao().updateEntity(project);
		
	}

	@Override
	public void updateWarningStatus(String projectUuid, String warningStatus) {
		Project project = getDao().getEntity(projectUuid);
		if(project.getWarningStatus() == null || !project.getWarningStatus().equals(warningStatus)){
			project.setWarningStatus(warningStatus);
			getDao().updateEntity(project);
		}
		
	}

	@Override
	public Map<String, Object> mcuFilter(String networkUuids) {
		Map<String, Object> map = new HashMap();
		List<Mcu> mcuList1 = new ArrayList<>();
		List<Mcu> mcuList2 = new ArrayList<>();
		List<Mcu> mcus = new ArrayList<>();
		String[] networkIds = networkUuids.split(",");
		for (String networkId : networkIds) {
			Network network = networkService.getEntity(networkId);
			//获得该网络连接所有mcu
			List<Mcu> mcu = mcuService.getMcusByNetwork(network.getNetworkUuid());
			mcus.addAll(mcu);
		}
		for(int i = 0; i< mcus.size(); i++) {
			Device tempD=deviceService.getEntity(mcus.get(i).getDeviceUuid());
			String name = tempD.getDeviceModel().getDevModelName();
			if(name.substring(name.lastIndexOf("_") + 1).equals("0")){
				mcuList1.add(mcus.get(i));
			}else if(name.substring(name.lastIndexOf("_") + 1).equals("1")){
				mcuList2.add(mcus.get(i));
			}
		}
		map.put("mcus1", mcuList1);
		map.put("mcus2", mcuList2);
		return map;
	}
	

}
