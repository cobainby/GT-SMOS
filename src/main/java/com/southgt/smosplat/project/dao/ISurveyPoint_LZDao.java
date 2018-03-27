package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;

public interface ISurveyPoint_LZDao extends IBaseDao<SurveyPoint_LZ> {

	List<SurveyPoint_LZ> getExistedSurveyPoint_LZsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_LZ> getSurveyPoint_LZs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_LZ> getSurveyPoint_LZs(String projectUuid);

	/*
	 * 获取相同codechar和相同code的测点数(除本身外)
	 */
	long getLZNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar,String code,String spUuid);
}
