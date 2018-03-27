package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Mobile_BasePoint;

public interface IMobileBasePointService extends IBaseService<Mobile_BasePoint> {

	List<Mobile_BasePoint> getBPsByProjectAndMonitorItem(String projecUuid,String monitorItemUuid);
	
	void operateBasePoint(List<Mobile_BasePoint> bpList);
}
