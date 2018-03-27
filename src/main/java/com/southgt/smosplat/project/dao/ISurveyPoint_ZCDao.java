package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;

public interface ISurveyPoint_ZCDao extends IBaseDao<SurveyPoint_ZC> {

	List<SurveyPoint_ZC> getSurveyPoint_ZCs(String projectUuid, String monitorItemUuid);
	
	long getSP_zcNumByCode(String projectUuid, String monitorItemUuid,String code);
	
	long getSP_zcNumByCodeExceptSelf(String projectUuid, String monitorItemUuid,String code,String surveyPointUuid);
}
