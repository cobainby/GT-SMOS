package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Mobile_BackPoint;

public interface IMobileBackPointDao extends IBaseDao<Mobile_BackPoint> {
 
	List<Mobile_BackPoint> getBackPointByStation(String stationUuid);
	
	void deleteBPByStation(String stationUUid);
}
