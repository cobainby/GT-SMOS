package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Mobile_Station;

public interface IMobileStationDao extends IBaseDao<Mobile_Station> {

	List<Mobile_Station> getStationsByProjectAndMonitorItem(String projectUuid,String monitorItemUuid);
	
	void deleteStationsByProjectAndMonitorItem(String projectUuid,String monitorItemUuid);
}
