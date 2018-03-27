package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

/**
 * 监测点服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月31日     mohaolin       v1.0.0        create</p>
 *
 */
public interface ISurveyPointService extends IBaseService<SurveyPoint_WYS> {

	/**
	 * 添加监测点
	 * @date  2017年3月31日 上午10:40:10
	 * @return List<SurveyPoint>
	 * @param project
	 * @param monitorItemUuid
	 * @param code
	 * @param spCount
	 * @param warningUuid
	 * @param sectionUuid
	 * @param originalTotalValue
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月31日     mohaolin      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_WYS> addSurveyPoint(Project project, String monitorItemUuid, String code, int spCount,
			String warningUuid, String sectionUuid, Float originalTotalValue);

	/**
	 * 添加监测点
	 * 
	 * @date  2017年4月17日 上午11:06:23
	 * @param @param project
	 * @param @param tempSP
	 * @param @param spCount
	 * @param @return
	 * @return List<SurveyPoint>
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月17日      杨杰     	   v1.0          create</p>
	 *
	 */
	List<SurveyPoint_WYS> addSurveyPoint(Project project, SurveyPoint_WYS tempSP, int spCount);
	
	/**
	 * 获取特定工程下特定监测项中的所有监测点
	 * @date  2017年4月1日 上午9:59:56
	 * @return List<SurveyPoint>
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月1日     mohaolin      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_WYS> getSurveyPoints(String projectUuid, String monitorItemUuid);

	/**
	 * 更新监测点设置
	 * @date  2017年4月6日 下午4:12:08
	 * @return void
	 * @param surveyPoint
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月6日     mohaolin      v1.0          create</p>
	 * @param project 
	 *
	 */
	
	void updateSurveyPoint(Project project, SurveyPoint_WYS surveyPoint);

	/**
	 * 删除监测点
	 * @date  2017年4月6日 下午4:44:22
	 * @return void
	 * @param surveyPointUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月6日     mohaolin      v1.0          create</p>
	 *
	 */
	void deleteSurveyPoint(String surveyPointUuid);

	/**
	 * 获得使用特定预警信息的监测点的数量
	 * @date  2017年4月7日 下午3:21:27
	 * @return long
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 * @param warningUuid 
	 *
	 */
	long getSPNumbersByWarning(String warningUuid);

	/**
	 * 获得使用该断面的监测点的数量 
	 * @date  2017年4月7日 下午6:22:14
	 * @return long
	 * @param sectionUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 *
	 */
	long getSPNumbersBySection(String sectionUuid);

}
