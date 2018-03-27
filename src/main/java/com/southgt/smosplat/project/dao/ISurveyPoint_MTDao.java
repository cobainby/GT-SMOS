package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;

public interface ISurveyPoint_MTDao extends IBaseDao<SurveyPoint_MT> {

	List<SurveyPoint_MT> getExistedSurveyPoint_MTsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_MT> getSurveyPoint_MTs(String projectUuid, String monitorItemUuid);
	
	long getSP_mtNumByCode(String projectUuid, String monitorItemUuid,String code);
	
	long getSP_mtNumByCodeExceptSelf(String projectUuid, String monitorItemUuid,String code,String surveyPointUuid);
}
