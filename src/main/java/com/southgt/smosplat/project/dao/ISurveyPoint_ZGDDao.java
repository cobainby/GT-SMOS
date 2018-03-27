package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_ZGD;

public interface ISurveyPoint_ZGDDao extends IBaseDao<SurveyPoint_ZGD> {

	List<SurveyPoint_ZGD> getExistedSurveyPoint_ZGDsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_ZGD> getSurveyPoint_ZGDs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_ZGD> getSurveyPoint_ZGDs(String projectUuid);
	
	long getZGDNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String code,String surveyPointUuid);
}
