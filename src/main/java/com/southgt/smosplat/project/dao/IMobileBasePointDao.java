package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Mobile_BasePoint;

public interface IMobileBasePointDao extends IBaseDao<Mobile_BasePoint> {

	List<Mobile_BasePoint> getBPByProjectAndMonitorItem(String projectUuid,String monitorItemUuid);
	
	void deleteBPByProjectAndMonitorItem(String projectUuid,String monitorItemUuid);
}
