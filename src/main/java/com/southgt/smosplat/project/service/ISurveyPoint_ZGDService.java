package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_ZGD;

public interface ISurveyPoint_ZGDService extends IBaseService<SurveyPoint_ZGD> {
	
	List<SurveyPoint_ZGD> addSP_ZGD(Project project, SurveyPoint_ZGD tempSP, int spCount,int beginNum)throws Exception;
	
	List<SurveyPoint_ZGD> getSP_ZGDs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_ZGD> getSP_ZGDs(String projectUuid);
	
	void updateSP_ZGD(Project project, SurveyPoint_ZGD surveyPoint) throws Exception;
	
	void deleteSP_ZGD(String surveyPointUuid);
}
