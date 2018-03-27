package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

public interface ISurveyPoint_WYSService extends IBaseService<SurveyPoint_WYS> {
	
	List<SurveyPoint_WYS> addSP_WYS(Project project, SurveyPoint_WYS tempSP, int spCount,int beginNum)throws Exception;
	
	List<SurveyPoint_WYS> getSP_WYSs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_WYS> getSP_WYSs(String projectUuid);
	
	void updateSP_WYS(Project project, SurveyPoint_WYS surveyPoint) throws Exception;
	
	void deleteSP_WYS(String surveyPointUuid);
}
