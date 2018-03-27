package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;

public interface ISurveyPoint_CXDao extends IBaseDao<SurveyPoint_CX> {

	List<SurveyPoint_CX> getExistedSurveyPoint_CXsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_CX> getSurveyPoint_CXs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_CX> getSurveyPoint_CXs(String projectUuid);
	
	long getCXNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar,String code,String spUuid);
	
}
