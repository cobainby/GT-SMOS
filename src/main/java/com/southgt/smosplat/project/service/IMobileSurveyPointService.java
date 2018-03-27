package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Mobile_SurveyPoint;

public interface IMobileSurveyPointService extends IBaseService<Mobile_SurveyPoint> {

	List<Mobile_SurveyPoint> getSurveyPointsByProjectAndMonitorItem(String projecUuid,String monitorItemUuid);
	
	void operateSurveyPoints(List<Mobile_SurveyPoint> bpList);
}
