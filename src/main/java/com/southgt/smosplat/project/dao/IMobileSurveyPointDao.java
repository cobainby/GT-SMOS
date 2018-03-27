package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Mobile_SurveyPoint;

public interface IMobileSurveyPointDao extends IBaseDao<Mobile_SurveyPoint> {

	List<Mobile_SurveyPoint> getSPsByProjectAndMonitorItem(String projectUuid,String monitorItemUuid);
	
	void deleteSPsByProjectAndMonitorItem(String projectUuid,String monitorItemUuid);
}
