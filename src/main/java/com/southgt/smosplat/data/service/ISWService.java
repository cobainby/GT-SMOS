package com.southgt.smosplat.data.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.project.entity.Project;

/**
 * 
 * 水位数据服务接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月2日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISWService extends IBaseService<SW_Data>{
	/**
	 * 
	 * 获取最新10条水位数据
	 * @date  2017年5月3日 下午2:55:01
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
	 * <p>2017年5月3日     姚家俊      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	Map<String, Object> getLatestSWDatasByProject(Project project);
	/**
	 * 
	 * 获取最新11条水位计数据
	 * @date  2017年5月3日 下午2:55:40
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
	 * <p>2017年5月3日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SW_Data> getLatestSWDatasBySurveyPoints(List<String> surveyPointUuids);
	/**
	 * 
	 * 第一条水位计数据
	 * @date  2017年5月3日 下午3:01:41
	 * 
	 * @param surveyPointUuids
	 * @return
	 * List<SW_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SW_Data> getFirstOneSWDatasBySurveyPoints(List<String> surveyPointUuids);
	
	/**
	 * 
	 * 取最新一条水位计数据 
	 * @date  2017年5月3日 下午3:00:13
	 * 
	 * @param surveyPointUuids
	 * @return
	 * List<SW_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SW_Data> getLatestOneSWDatasBySurveyPoints(List<String> surveyPointUuids);
	/**
	 * 删除监测点的水位数据
	 * 
	 * @date  2017年5月15日 上午11:34:30
	 * @param @param surveyPointUuid
	 * @return void
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月15日      杨杰     	   v1.0          create</p>
	 *
	 */
	void deleteSW_DataBySurveyPoint(String surveyPointUuid);
	
}
