package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_QX;
/**
 * 
 * 建筑物倾斜监测点服务接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月15日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISurveyPoint_QXService extends IBaseService<SurveyPoint_QX>{
	List<SurveyPoint_QX> addSP_QX(Project project, SurveyPoint_QX tempSP, int spCount,int beginNum) throws Exception;
	
	List<SurveyPoint_QX> getSP_QXs(String projectUuid, String monitorItemUuid);
	
	void updateSurveyPoint_QX(Project project, SurveyPoint_QX surveyPoint) throws Exception;
	
	void deleteSurveyPoint_QX(String surveyPointUuid);
}
