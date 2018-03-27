package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Mobile_Station;

public interface IMobileStationService extends IBaseService<Mobile_Station> {

	List<Mobile_Station> getStationsByProjectAndMonitorItem(String projecUuid,String monitorItemUuid);
	
	void operateStations(List<Mobile_Station> bpList);
}
