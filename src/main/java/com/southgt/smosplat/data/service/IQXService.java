package com.southgt.smosplat.data.service;

import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.data.entity.QX_Data;
import com.southgt.smosplat.project.entity.Project;

/**
 * 
 * 建筑物倾斜服务接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
public interface IQXService extends IBaseService<QX_Data>{
	/**
	 * 
	 * 保存水准数据
	 * @date  2017年4月27日 上午10:50:07
	 * 
	 * @param jsonOriginalData json格式的水准原始数据
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月27日     姚家俊      v1.0          create</p>
	 *
	 */
	Map<String,Object> saveQXData(String jsonOriginalData) throws Exception;
	
	/**
	 * 
	 * 获取最新10条水准数据 
	 * @date  2017年4月26日 上午9:57:22
	 * 
	 * @param project
	 * @return
	 * Map<String,Object>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月26日     姚家俊      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	Map<String, Object> getLatestQXDatasByProject(Project project) throws Exception;
	/**
	 * 
	 * 根据测量次序、测量点ID获取测斜数据
	 * @date  2017年5月12日 下午1:39:15
	 * 
	 * @param surveyOrder
	 * @param surveyPointUuid
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月12日     姚家俊      v1.0          create</p>
	 *
	 */
	Map<String, Object> getQXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid) throws Exception;
	/**
	 * 
	 * 获取项目中所有测斜点最大测量次序
	 * @date  2017年5月12日 下午1:46:33
	 * 
	 * @param surveyPointUuid
	 * @return
	 * int
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月12日     姚家俊      v1.0          create</p>
	 *
	 */
	Map<String, Object> getMaxSurveyOrderBySurveyPoint(Project project);
	/**
	 * 
	 * 删除建筑物倾斜监测点下的监测数据
	 * @date  2017年5月15日 下午4:33:36
	 * 
	 * @param surveyPointUuid
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月15日     姚家俊      v1.0          create</p>
	 *
	 */
	void deleteQXDataBysurveyPoint(String surveyPointUuid);
}
