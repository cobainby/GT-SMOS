package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;

public interface ISurveyPoint_LZService extends IBaseService<SurveyPoint_LZ> {
	
	List<SurveyPoint_LZ> addSP_LZ(Project project, SurveyPoint_LZ tempSP, int spCount,int beginNum)throws Exception;
	
	List<SurveyPoint_LZ> getSP_LZs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_LZ> getSP_LZs(String projectUuid);
	
	void updateSP_LZ(Project project, SurveyPoint_LZ surveyPoint) throws Exception;
	
	void deleteSP_LZ(String surveyPointUuid);
}
