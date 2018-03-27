package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IMobileBackPointDao;
import com.southgt.smosplat.project.entity.Mobile_BackPoint;
import com.southgt.smosplat.project.entity.Mobile_Station;
import com.southgt.smosplat.project.service.IMobileBackPointService;
import com.southgt.smosplat.project.service.IMobileStationService;

/**
 * 供手机端使用的后视点服务
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年11月14日     YANG       v1.0.0        create</p>
 *
 */
@Service("mobileBackPointService")
public class MobileBackPointServiceImpl extends BaseServiceImpl<Mobile_BackPoint> implements IMobileBackPointService {
	
	@Resource(name="mobileBackPointDao")
	@Override
	public void setDao(IBaseDao<Mobile_BackPoint> dao) {
		super.setDao(dao);
	}

	@Resource
	IMobileStationService mobileStationService;
	
	@Override
	public List<Mobile_BackPoint> getBackPointByStation(String stationUuid) {
		List<Mobile_BackPoint> bpList=((IMobileBackPointDao)getDao()).getBackPointByStation(stationUuid);
		return bpList;
	}

	@Override
	public void operateBackPoint(List<Mobile_BackPoint> bpList,String projecUuid,String monitorItemUuid) {
		List<Mobile_Station> stationList=mobileStationService.getStationsByProjectAndMonitorItem(projecUuid, monitorItemUuid);
		//把数据库中所有对应项目、监测项下的后视点都删除掉
		for(int i=0;i<stationList.size();i++){
			((IMobileBackPointDao)getDao()).deleteBPByStation(stationList.get(i).getStationUuid());
		}
		
		for(int i=0;i<bpList.size();i++){
			Mobile_BackPoint bp=bpList.get(i);
			bp.setOrderCreate(i);
			((IMobileBackPointDao)getDao()).saveEntity(bp);
		}
		
	}

	
	
}
