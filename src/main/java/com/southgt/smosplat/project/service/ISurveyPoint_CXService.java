package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;

public interface ISurveyPoint_CXService extends IBaseService<SurveyPoint_CX> {

	List<SurveyPoint_CX> addSP_CX(Project project, SurveyPoint_CX tempSP, int spCount,int beginNum) throws Exception;
	
	List<SurveyPoint_CX> getSP_CXs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_CX> getSP_CXs(String projectUuid);
	
	void updateSurveyPoint_CX(Project project, SurveyPoint_CX surveyPoint) throws Exception;
	
	void deleteSurveyPoint_CX(String surveyPointUuid);
	
}
