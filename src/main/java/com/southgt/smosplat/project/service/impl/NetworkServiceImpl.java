package com.southgt.smosplat.project.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.xsocket.connection.INonBlockingConnection;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.project.dao.INetworkDao;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.INetworkService;
import com.southgt.smosplat.project.service.autoCollect.ConnectCallback;

/**
 * 网络连接服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("networkService")
public class NetworkServiceImpl extends BaseServiceImpl<Network> implements INetworkService {
	
	@Resource
	IMcuService mcuService;

	@Resource(name="networkDao")
	@Override
	public void setDao(IBaseDao<Network> dao) {
		super.setDao(dao);
	}

	@Override
	public void addNetwork(Project project, Network network) throws Exception {
		long number=((INetworkDao)getDao()).getNetworkNumber(project.getProjectUuid(),network.getNetworkName());
		if(number>0){
			throw new Exception("名称已存在！");
		}
		network.setProject(project);
		getDao().saveEntity(network);
	}

	@Override
	public void updateNetwork(Project project, Network network) throws Exception {
		//名称不能重复
		long number=((INetworkDao)getDao()).getNetworkNumberExceptSelf(project.getProjectUuid(),network.getNetworkName(),network.getNetworkUuid());
		if(number>0){
			throw new Exception("名称已经存在！");
		}
		network.setProject(project);
		getDao().updateEntity(network);
	}

	@Override
	public void deleteNetwork(String networkUuid) throws Exception {
		//如果有mcu正在使用该网络连接，则不允许删除
		long number=mcuService.getMcusCountByNetwork(networkUuid);
		if(number>0){
			throw new Exception("网络连接正在被使用，不允许删除！");
		}
		getDao().deleteEntity(getDao().getEntity(networkUuid));
	}

	@Override
	public List<Network> getNetworks(String projectUuid) {
		return ((INetworkDao)getDao()).getNetworks(projectUuid);
	}



}
