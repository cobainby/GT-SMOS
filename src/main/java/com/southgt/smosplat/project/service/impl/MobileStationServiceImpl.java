package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IMobileStationDao;
import com.southgt.smosplat.project.entity.Mobile_Station;
import com.southgt.smosplat.project.service.IMobileStationService;

/**
 * 手机测站服务实现类
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
@Service("mobileStationService")
public class MobileStationServiceImpl extends BaseServiceImpl<Mobile_Station> implements IMobileStationService {

	@Resource(name="mobileStationDao")
	@Override
	public void setDao(IBaseDao<Mobile_Station> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Mobile_Station> getStationsByProjectAndMonitorItem(String projecUuid, String monitorItemUuid) {
		List<Mobile_Station> stationsList=((IMobileStationDao)getDao()).getStationsByProjectAndMonitorItem(projecUuid, monitorItemUuid);
		return stationsList;
	}

	@Override
	public void operateStations(List<Mobile_Station> stationList) {
		if(stationList.size()>0){
			((IMobileStationDao)getDao()).deleteStationsByProjectAndMonitorItem(stationList.get(0).getProjectUuid(), stationList.get(0).getMonitorItemUuid());
			for(int i=0;i<stationList.size();i++){
				Mobile_Station station=stationList.get(i);
				station.setOrderCreate(i);
				((IMobileStationDao)getDao()).saveEntity(station);
			}
		}
		
	}

	
}
