package com.southgt.smosplat.project.service.autoCollect;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IConnection.FlushMode;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.project.entity.Network;

/**
 * 作为服务端采集时的接口实现
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月7日     mohaolin       v1.0.0        create</p>
 *
 */
public class ConnectServiceServer extends ConnectServiceImpl
	implements IDataHandler, IConnectHandler, IDisconnectHandler {
	
	private IServer server;
	
	public ConnectServiceServer(Network network,ConnectCallback connectCallback) throws Exception {
		this.network=network;
		this.connectCallback=connectCallback;
		//直接初始化端口监听
		InetAddress address;
		try {
			address = InetAddress.getByName(network.getIp());
			//创建一个服务端的对象，创建完毕之后监听端口，在连接超时或者空闲超时之后需要将server销毁以释放资源
			server=new Server(address, network.getPort(), this);
			//设置当前使用异步模式
			server.setFlushmode(FlushMode.ASYNC);
			//连接超时时间指的是连接持续的时间，这里不设置这个，表示连接一直保持，直到手动终止（程序中当采集完成需要手动终止），默认值是Long.MAX_VALUE
			//注：onConnectionTimeout回调需要返回true才行
			//server.setConnectionTimeoutMillis(3*60*1000);
			//不设置空闲时间，因为进入空闲时间回调后，如果return false，连接就会被关闭，这里让程序手动关闭
			//这个参数的默认值是Long.MAX_VALUE
			//server.setIdleTimeoutMillis(3*60*1000);
			//启动监听
			server.start();
			String msg = "此IP/端口监听正常！" + new SimpleDateFormat("hh:mm:ss").format(new Date());
			System.out.println(msg);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuStartCollect/"+network.getProject().getProjectUuid(), msg);
		} catch (BindException e) {
			connectCallback.onException(new Exception("无法建立监听!，请检查本地地址以及端口是否正确！,@"+new SimpleDateFormat("hh:mm:ss").format(new Date())));
			destroy();
			throw new Exception("无法建立监听!");
		}catch (IOException e){
			connectCallback.onException(new Exception("无法建立监听!，请检查本地地址以及端口是否正确！,@"+new SimpleDateFormat("hh:mm:ss").format(new Date())));
			destroy();
			throw new Exception("无法建立监听!");
		}
	}

	@Override
	public void destroy(){
		//停止重发指令
		if(timeCountRunnable!=null){
			timeCountRunnable.setStop();
		}
		try {
			if(server!=null){
				server.close();
			}
			server=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
		//停止重发指令
		if(timeCountRunnable!=null){
			timeCountRunnable.setStop();
		}
		LogUtil.info(connection.getLocalPort()+":断开");
		//作为服务端时只需要端口一直处于监听即可，所以这里不做任何操作，当网络断开后一直等，等到网络连上后继续进行采集，因为不确定一次采集会在什么时间内结束，如果这一次采集还没完成，下一次采集周期到了，那么这一次采集会被程序终止掉。
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
		LogUtil.info(connection.getLocalPort()+"连上");
		return true;
	}

	@Override
	public boolean onData(INonBlockingConnection connection)
			throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
		System.out.println("onData*******************************");
		//停止指令重发计时
		if(timeCountRunnable!=null){
			timeCountRunnable.setStop();
		}
		//当设备还连着通讯模块时，把通讯模块的网络断开，handler是没有任何反应的，onData也不会回调进来。再接上网络，如果有上一次的数据要返回，则onData会先回调，再回调onConnect，这时候丢弃掉这次数据，而是重发上一条指令
		//当网络还在，设备断开后或者连上的时候都会立刻回调一次onData（如果有数据要返回）在回调onConnect,这时候onData里面的connection不能用来发送指令，只能将数据读取出来，发指令的话需要使用onConnect中的connection
		//通过给设备发送一段假指令，判断是不是要在这里发送指令，如果发送不出去，则留到onConnect回调的时候在发送，而且是发送上一条指令
		try {
			if(connectCallback.isAllowSendTest()){//是否允许发送测试字节
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
