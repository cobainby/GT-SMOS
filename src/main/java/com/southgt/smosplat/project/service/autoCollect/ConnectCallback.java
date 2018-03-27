package com.southgt.smosplat.project.service.autoCollect;

import java.util.HashMap;
import java.util.Map;

import org.xsocket.connection.INonBlockingConnection;

import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.project.entity.Network;

/**
 * 通讯连接回调接口定义
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月28日     mohaolin       v1.0.0        create</p>
 *
 */
public abstract class ConnectCallback {
	
	/**
	 * 网络连接信息，用于广播消息时使用
	 */
	private Network network;
	/**
	 * 上一步的操作标识符，如果出现断线又重新连上的情况，这个标识符用于判断来重复发送上一条指令
	 */
	private String lastActionFlag="connect_end";
	/**
	 * 标识符，用来分别处理不同的通讯操作
	 */
	private String actionFlag="connect_end";
	
	/**
	 * 发送指令时如果设备返回了数据，该属性设为true，如果没返回（onData回调没进去），则重复发同一条指令三次，每隔2分钟发一次
	 */
	private boolean isDataBack=false;
	/**
	 * 是否可以发送测试字节
	 */
	private boolean isAllowSendTest=true;
	
	public boolean isAllowSendTest() {
		return isAllowSendTest;
	}
	public void setAllowSendTest(boolean isAllowSendTest) {
		this.isAllowSendTest = isAllowSendTest;
	}
	public Network getNetwork() {
		return network;
	}
	public void setNetwork(Network network) {
		this.network = network;
	}
	public String getActionFlag() {
		return actionFlag;
	}
	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	
	public ConnectCallback(Network network) {
		this.network = network;
	}
	public boolean isDataBack() {
		return isDataBack;
	}
	public void setDataBack(boolean isDataBack) {
		this.isDataBack = isDataBack;
	}
	public String getLastActionFlag() {
		return lastActionFlag;
	}
	public void setLastActionFlag(String lastActionFlag) {
		this.lastActionFlag = lastActionFlag;
	}
	/**
	 * 与设备进行相互通讯操作
	 * @date  2016年10月11日 下午3:44:35
	 * @return void
	 * @param connection
	 * @param flag 因为所有的通讯操作都在这个方法里面执行，通过这个操作标识符来区分不同的通讯操作。这个标识符在发指令之前需要调用setActionFlag方法设置，并且不能重复
	 * @param b 是否是重连后的回调，如果是的话则需要重复发送上一条指令（最多发三次）。
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年10月11日     mohaolin      v1.0          create</p>
	 * 
	 *
	 */
	public abstract void onConnected(INonBlockingConnection connection,String flag, boolean b);
	/**
	 * 当发生异常后进行回调 ,默认的操做是将异常信息广播到所有客户端，如果不需要广播则覆盖此方法
	 * @date  2016年10月11日 下午3:44:56
	 * @return void
	 * @param e
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
	public void onException(Exception e){
		Map<String, Object> data=new HashMap<String,Object>();
		data.put("msg", e.getMessage());
		broadcastMessage(data);
		LogUtil.error("网络错误：------"+e.getMessage());
		
	};
	
	/**
	 * 广播消息到所有客户端
	 * @date  2016年10月11日 下午3:49:51
	 * @return void
	 * @param message
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
	public void broadcastMessage(Map<String, Object> map){
		//发送推送消息到所有客户端
		ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), map.get("msg").toString());
	}
}
