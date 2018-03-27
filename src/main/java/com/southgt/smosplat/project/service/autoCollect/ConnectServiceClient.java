package com.southgt.smosplat.project.service.autoCollect;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.IHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.NonBlockingConnection;
import org.xsocket.connection.NonBlockingConnectionPool;

import com.southgt.smosplat.project.entity.Network;

/**
 * 作为客户端采集时的接口实现
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月7日     mohaolin       v1.0.0        create</p>
 *
 */
public class ConnectServiceClient extends ConnectServiceImpl
	implements IDataHandler, IConnectHandler, IDisconnectHandler{
	
	/**
	 * 内部类，用于循环检测设备连接状态，如果断线需要立刻进行重连
	 * @version v1.0.0
	 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
	 * @author mohaolin
	 * <p>Modification History:</p>
	 * <p> Date         Author      Version     Description</p>
	 * <p>-----------------------------------------------------------------</p>
	 * <p>2016年11月7日     mohaolin       v1.0.0        create</p>
	 *
	 */
	class MyRunnable implements Runnable{
		
		/**
		 * 第一次连接，如果没连上不用进行重连，连接上后才需要进行断线重连
		 */
		private boolean isFirstConnect=true;
		/**
		 * 程序手动停止连接，需要将这个设为true，否则会一直死循环
		 */
		private boolean isDestroyNow=false;
		
		private INonBlockingConnection bc;
		private IHandler handler;

		public void setDestroyNow(boolean isDestroyNow) {
			this.isDestroyNow = isDestroyNow;
		}
		
		public MyRunnable(IHandler handler) {
			this.handler=handler;
		}

		@Override
		public void run() {
			//两种情况：1、连接自己断开，需要重连；2、程序手动停止，终止采集，不需要重连
			while(!isDestroyNow){
				try {
					bc = new NonBlockingConnection(network.getIp(),network.getPort(),handler);
					//进入以下代码说明连接到设备成功
					bc.setEncoding("UTF-8");
					//是否自动清空缓存
					bc.setAutoflush(true);
					//连接超时时间指的是连接持续的时间，这里不设置这个，表示连接一直保持，直到手动终止（程序中当采集完成需要手动终止），默认值是Long.MAX_VALUE
					//注：onConnectionTimeout回调需要返回true才行
					//server.setConnectionTimeoutMillis(3*60*1000);
					//不设置空闲时间，因为进入空闲时间回调后，如果return false，连接就会被关闭，这里让程序手动关闭
					//这个参数的默认值是Long.MAX_VALUE
					//bc.setIdleTimeoutMillis(3*60*1000);
					//做一个循环，保持连接一直在线
					while(bc!=null&&bc.isOpen()){
						try {
							//隔10秒才判断是否连接仍在
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//手动终止时，不进行重连
					if(isDestroyNow==true){
						break;
					}
					Map<String, Object> data=new HashMap<String,Object>();
					data.put("msg", network.getIp()+":"+network.getPort()+"连接断开，10秒后进行重连！");
					//隔10秒进行一次重连
					connectCallback.broadcastMessage(data);
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					if(isFirstConnect){
						isFirstConnect=false;
						Map<String, Object> data=new HashMap<String,Object>();
						data.put("msg", network.getIp()+":"+network.getPort()+"无法连接到设备！");
						connectCallback.broadcastMessage(data);
						destroy();
						break;//第一次都没能连上，直接跳出循环终止。
					}
					Map<String, Object> data=new HashMap<String,Object>();
					data.put("msg", network.getIp()+":"+network.getPort()+"重连失败，10秒后再尝试重连！");
					connectCallback.broadcastMessage(data);
				}
			}
			//手动终止连接
			destroy();
		}
		public void destroy() {
			//停止重发指令
			if(timeCountRunnable!=null){
				timeCountRunnable.setStop();
			}
			//设置停止采集
			isDestroyNow=true;
			try {
				if(bc!=null){
					bc.close();
					//需要使用连接池的destroy方法来销毁连接，才能杀死连接产生的线程
					NonBlockingConnectionPool.destroy(bc);
					bc=null;
					Map<String, Object> data=new HashMap<String,Object>();
					data.put("msg", network.getIp()+":"+network.getPort()+"连接已停止！");
					connectCallback.broadcastMessage(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	MyRunnable myRunnable;
	
	public ConnectServiceClient(Network network,ConnectCallback connectCallback) {
		this.network=network;
		this.connectCallback=connectCallback;
		myRunnable=new MyRunnable(this);
		Thread thread=new Thread(myRunnable);
		thread.start();
	}
	

	@Override
	public void destroy() {
		myRunnable.destroy();
	}
	
	@Override
	public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
		//停止重发指令
		if(timeCountRunnable!=null){
			timeCountRunnable.setStop();
		}
		return true;
	}
	
	@Override
	public boolean onConnect(INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
		//停止指令重发计时
		if(timeCountRunnable!=null){
			timeCountRunnable.setStop();
		}
		//第一次连上时，发送第一条指令和上一条指令是一样的
		//网络断开后重连，会回调这个方法，这时候永远重复发送上一条指令来重新执行
		//传递的参数为上一次指令的标识符，以及告诉调用代码现在为重连状态
		connectCallback.onConnected(connection,connectCallback.getLastActionFlag(),true);
		//开始重发计时，如果3分钟设备没回应，重发上一条指令
		timeCountRunnable=new MyTimeCountRunnable(connection);
		new Thread(timeCountRunnable).start();
		return true;
	}

	@Override
	public boolean onData(INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
		//停止指令重发计时
		if(timeCountRunnable!=null){
			timeCountRunnable.setStop();
		}
		//当设备还连着通讯模块时，把通讯模块的网络断开，handler是没有任何反应的，onData也不会回调进来。再接上网络，如果有上一次的数据要返回，则onData会先回调，再回调onConnect，这时候丢弃掉这次数据，而是重发上一条指令
		//当网络还在，设备断开后或者连上的时候都会立刻回调一次onData（如果有数据要返回）在回调onConnect,这时候onData里面的connection不能用来发送指令，只能将数据读取出来，发指令的话需要使用onConnect中的connection
		//通过给设备发送一段假指令，判断是不是要在这里发送指令，如果发送不出去，则留到onConnect回调的时候在发送，而且是发送上一条指令
		try {
			if(connectCallback.isAllowSendTest()){
			connection.write("testCommond\r\n");
			System.out.println("testCommond\r\n");
			}
		} catch (Exception e) {
			//如果发送不出去，有可能是网络断开后重新连上回调进来的，或者设备断开重连回调进来的，这时候不做任何操作，等到回调onConnect方法的时候发送上一条指令
			//如果是设备一直没连上，那么等到重复发送三次指令后就会自动终止采集
			//如果是网络一直没连上，只能一直等待直到连上或者下一次采集到来为止
			return true;
		}
		//网络还在，设备也连上的时候，正常发送指令，发送的是下一条指令
		connectCallback.onConnected(connection,connectCallback.getActionFlag(),false);
		//开始重发计时，如果3分钟设备没回应，重发上一条指令
		timeCountRunnable=new MyTimeCountRunnable(connection);
		new Thread(timeCountRunnable).start();
		return true;
	}

}
