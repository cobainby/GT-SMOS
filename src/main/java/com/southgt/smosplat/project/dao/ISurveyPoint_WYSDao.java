package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

public interface ISurveyPoint_WYSDao extends IBaseDao<SurveyPoint_WYS> {

	List<SurveyPoint_WYS> getExistedSurveyPoint_WYSsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_WYS> getSurveyPoint_WYSs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_WYS> getSurveyPoint_WYSs(String projectUuid);
	
	long getWYSNumByCodeExceptSelf(String projectUuid, String monitorItemUuid,String code,String surveyPointUuid);
}
