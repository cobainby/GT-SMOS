package com.southgt.smosplat.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.service.autoCollect.ConnectCallback;
import com.southgt.smosplat.project.service.autoCollect.ConnectServiceImpl;
import com.southgt.smosplat.project.service.autoCollect.IConnectService;
import com.southgt.smosplat.project.service.autoCollect.mcu.Impl.Speed_McuCollectLogic;

/**
 * 采集处理池，系统所有已建立的连接都存放到该类中统一处理
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月7日     mohaolin       v1.0.0        create</p>
 *
 */
public class ApplicationUtil {
	
	/**
	 * 应用程序上下文，保存起来用来在应用中获取spring的bean
	 */
	private static WebApplicationContext webApplicationContext;
	
	/**
	 * 消息发送辅助类，在spring的websocket的配置进行初始化时，如果该对象为空，需要将simpMessagingTemplate设置进来，以提供其他地方发送消息使用
	 */
	private static SimpMessagingTemplate simpMessagingTemplate;
	
	/**
	 * 保存所有正在进行中的网络通讯连接，以便在采集过程中可以进行停止采集操作
	 */
	public static Map<Network,IConnectService> workingConnect= new ConcurrentHashMap<Network,IConnectService>();
	
	/**
	 * 所有设备进行工作的定时器必须保存到该对象中，目的是为了停止采集时可以找到对应的定时器并调用cancel方法
	 */
	public static Map<Network, Timer> workingTimer=new ConcurrentHashMap<Network, Timer>();
	
	public static WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	public static void setWebApplicationContext(WebApplicationContext webApplicationContext) {
		ApplicationUtil.webApplicationContext = webApplicationContext;
	}

	public static SimpMessagingTemplate getSimpMessagingTemplate() {
		return simpMessagingTemplate;
	}

	public static void setSimpMessagingTemplate(SimpMessagingTemplate st) {
		simpMessagingTemplate = st;
	}
	
	/**
	 * 执行网络连接操作
	 * @date  2016年10月11日 下午3:43:06
	 * @return void
	 * @param network 网络连接信息
	 * @param handlerCallback 通讯成功后的回调
	 * @param connectTimeMillisParam 本次通讯多长时间，参数值必须大于1000
	 * @throws Exception
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年10月11日     mohaolin      v1.0          create</p>
	 *
	 */
	public static void doConnectAction(Network network,ConnectCallback connectCallback) throws Exception{
		//启动一个线程进行连接
		Thread thread=new Thread(new Runnable() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public synchronized void run() {
				//如果已存在保存的对象，则清除掉
				for (Iterator iterator = workingConnect.keySet().iterator(); iterator.hasNext();) {
					Network networkInMap = (Network) iterator.next();
					Network tempNetwork=network;
					if((networkInMap.getType().equals(tempNetwork.getType()))&&(networkInMap.getIp().equals(tempNetwork.getIp()))
							&&(networkInMap.getPort().equals(tempNetwork.getPort()))){
						//销毁并移除
						workingConnect.get(networkInMap).destroy();
						workingConnect.remove(networkInMap);
					}
				}
				//启动连接
				IConnectService connectService;
				try {
					connectService = ConnectServiceImpl.createInstance(network, connectCallback);
					//保存到内存中
					workingConnect.put(network, connectService);
				} catch (Exception e) {
					LogUtil.error(e.toString());
				}
				
			}
		});
		thread.start();
	}
	
	/**
	 * 本次通讯处理结束，必须掉用该方法释放资源
	 * @date  2016年10月31日 上午10:21:01
	 * @return void
	 * @param network
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年10月31日     mohaolin      v1.0          create</p>
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static synchronized void endConnectAction(Network network){
		for (Iterator iterator = workingConnect.keySet().iterator(); iterator.hasNext();) {
			Network networkInMap = (Network) iterator.next();
			if(networkInMap.getType().equals(network.getType())&&networkInMap.getIp().equals(network.getIp())
					&&networkInMap.getPort().equals(network.getPort())){
				//销毁并移除
				workingConnect.get(networkInMap).destroy();
				workingConnect.remove(networkInMap);
			}
		}
	}
	
	/**
	 * 设备开始采集后，在应用程序范围内保存该设备的timer，以便停止采集时使用 
	 * @date  2016年10月24日 下午2:09:45
	 * @return Timer
	 * @param network
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年10月24日     mohaolin      v1.0          create</p>
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static synchronized Timer addTimer(Network network){
		//先循环现在的timer，看看有没有已经存在正在运行和networ一样的timer，如果有则说明程序出现了问题，打印该异常以便维护时寻找问题
		for (Iterator iterator = workingTimer.keySet().iterator(); iterator.hasNext();) {
			Network networkObj = (Network) iterator.next();
			try {
				if(networkObj.getType().equals(network.getType())&&networkObj.getIp().equals(network.getIp())
						&&networkObj.getPort().equals(network.getPort())){
					//停止通讯连接
					endConnectAction(network);
					//停止timer
					workingTimer.get(networkObj).cancel();
					workingTimer.remove(networkObj);
					//打印异常信息以备进行错误检查
					throw new Exception(networkObj.getIp()+":"+networkObj.getPort()+"对应的设备的timer已经在运行！没有被停止！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Timer timer=new Timer();
		workingTimer.put(network, timer);
		return timer;
	}
	
	@SuppressWarnings("rawtypes")
	public static synchronized void removeTimer(Network network){
		for (Iterator iterator = workingTimer.keySet().iterator(); iterator.hasNext();) {
			Network networkObj = (Network) iterator.next();
			if(networkObj.getType().equals(network.getType())&&networkObj.getIp().equals(network.getIp())
					&&networkObj.getPort().equals(network.getPort())){
				//停止通讯连接
				endConnectAction(network);
				//停止timer
				workingTimer.get(networkObj).cancel();
				//移除保存的timer
				workingTimer.remove(networkObj);
			}
		}
	}
	
}
