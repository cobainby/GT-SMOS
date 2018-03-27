package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;

public interface ISurveyPoint_ZCService extends IBaseService<SurveyPoint_ZC> {
	
	List<SurveyPoint_ZC> getSP_ZCs(String projectUuid, String monitorItemUuid);
	
	void addSP_zc(SurveyPoint_ZC sp_ZC)throws Exception;
	
	void updateSP_zc(SurveyPoint_ZC sp_ZC)throws Exception;
	
	void deleteSP_zc(String  surveyPointUuid);
}
