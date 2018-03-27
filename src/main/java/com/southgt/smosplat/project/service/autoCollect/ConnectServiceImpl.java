package com.southgt.smosplat.project.service.autoCollect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xsocket.connection.IConnectionTimeoutHandler;
import org.xsocket.connection.IIdleTimeoutHandler;
import org.xsocket.connection.INonBlockingConnection;

import com.southgt.smosplat.project.entity.Network;

/**
 * 与设备的网络通讯的实现父类（监听端口实现，连接远程地址实现）
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月7日     mohaolin       v1.0.0        create</p>
 *
 */
public abstract class ConnectServiceImpl implements IConnectService, IIdleTimeoutHandler, IConnectionTimeoutHandler {

	/**
	 * 网络连接对象
	 */
	Network network;
	/**
	 * 连接回调处理对象
	 */
	ConnectCallback connectCallback;
	
	/**
	 * 每次发指令后，这个线程就计算时间，到达三分钟后就重复发送上一条指令，发送两次，加上通讯成功时发送的一次，一共要发三次
	 */
	MyTimeCountRunnable timeCountRunnable;
	
	class MyTimeCountRunnable implements Runnable{
		
		private INonBlockingConnection connection;
		private boolean stop=false;
		private int timeCount=0;
		
		public MyTimeCountRunnable(INonBlockingConnection connection) {
			this.connection = connection;
		}
		
		public void setStop() {
			stop=true;
		}

		@Override
		public void run() {
			while(!stop){
				try {
					Thread.sleep(1000);
					timeCount+=1000;
					//每到达三分钟，发送一次
					if((timeCount==3*60*1000)||(timeCount==6*60*1000)){
						//发送一条假指令，验证是否能发送，因为可能在等待的过程中通讯断了，如果能发送，才发送
						try {
							if(connectCallback.isAllowSendTest()){//是否允许发送测试字节
							connection.write("testCommond\r\n");
							System.out.println("testCommond\r\n");
							}
							Map<String, Object> data=new HashMap<String,Object>();
							data.put("msg", "设备没有回应，重新发送指令！");
							connectCallback.broadcastMessage(data);
							System.out.println("设备没有回应，重新发送指令！");
							connectCallback.onConnected(connection,connectCallback.getLastActionFlag(),true);
						} catch (Exception e) {
							//不能发送，就不发送
							System.out.println("connection.write 异常");
						}
					}
					if(timeCount>9*60*1000){
						setStop();
						//重新发送如果还没有结果，则需要终止本次采集
						destroy();
						Map<String, Object> data=new HashMap<String,Object>();
						data.put("msg", "由于给设备发送指令三次都没有回应，本次采集失败！请检查连接是否正常！");
						connectCallback.broadcastMessage(data);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 创建连接处理对象（根据服务端方式接入还是客户端方式接入）
	 * @date  2016年10月12日 下午4:29:52
	 * @return ConnectServiceImpl
	 * @param network
	 * @param handlerCallback
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年10月12日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	public static ConnectServiceImpl createInstance(Network network,ConnectCallback connectCallback) throws Exception{
		if(network.getType()==0){
			return new ConnectServiceServer(network,connectCallback);
		}else{
			return new ConnectServiceClient(network,connectCallback);
		}
	}
	
	@Override
	public Network getNetwork(){
		return this.network;
	}

	@Override
	public boolean onConnectionTimeout(INonBlockingConnection connection) throws IOException {
		//onConnectionTimeout和onIdleTimeout都返回true，即关闭连接由程序手动来处理，而不是由XSocket来关闭
		//onConnectionTimeout和onIdleTimeout都返回false，那么connectTime和idleTime最小值到了后，XSocket会自动关闭连接
		//因为没有设置connectTimeout时间，默认时间就是Long.MAX_VALUE，这个回调方法理论上是不会进来的。
		destroy();
		return true;
	}

	@Override
	public boolean onIdleTimeout(INonBlockingConnection connection) throws IOException {
		//onConnectionTimeout和onIdleTimeout都返回true，即关闭连接由程序手动来处理，而不是由XSocket来关闭
		//onConnectionTimeout和onIdleTimeout都返回false，那么connectTime和idleTime最小值到了后，XSocket会自动关闭连接
		//因为没有设置idleTimeout时间，默认时间就是Long.MAX_VALUE，这个回调方法理论上是不会进来的。
		destroy();
		return true;
	}
}
