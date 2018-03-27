package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Mobile_BackPoint;

public interface IMobileBackPointService extends IBaseService<Mobile_BackPoint> {

	List<Mobile_BackPoint> getBackPointByStation(String stationUuid);
	
	void operateBackPoint(List<Mobile_BackPoint> bpList,String projecUuid,String monitorItemUuid);
}
