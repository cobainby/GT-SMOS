package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;

public interface ISurveyPoint_WYDDao extends IBaseDao<SurveyPoint_WYD> {

	List<SurveyPoint_WYD> getExistedSurveyPoint_WYDsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_WYD> getSurveyPoint_WYDs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_WYD> getSurveyPoint_WYDs(String projectUuid);

	long getWYDNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar, String code,
			String surveyPointUuid);
}
