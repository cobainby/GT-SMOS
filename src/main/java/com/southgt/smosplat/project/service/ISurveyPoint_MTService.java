package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;

public interface ISurveyPoint_MTService extends IBaseService<SurveyPoint_MT> {
	
	void addSP_MT(Project project, SurveyPoint_MT tempSP)throws Exception;
	
	List<SurveyPoint_MT> getSP_MTs(String projectUuid, String monitorItemUuid);
	
	void updateSP_MT(Project project, SurveyPoint_MT surveyPoint)throws Exception;
	
	void deleteSP_MT(String surveyPointUuid);
}
