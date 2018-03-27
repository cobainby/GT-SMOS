package com.southgt.smosplat.data.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.project.entity.Project;

/**
 * 
 * 侧些数据服务接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月10日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ICXService extends IBaseService<CX_Data>{
	/**
	 * 
	 * 检查水准数据
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
	Map<String,Object> checkCXData(String jsonOriginalData, String sourceData) throws Exception;
	
	/**
	 * 
	 * 保存数据
	 * @date  2017年8月1日 下午2:34:51
	 * 
	 * @param tsData
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
	 * <p>2017年8月1日     姚家俊      v1.0          create</p>
	 *
	 */
	void saveManualData(Project project, List<CX_Data> data, List<String> gapRateEarlyWarningPoints, List<String> accumEarlyWarningPoints,byte flag)throws Exception;
	
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
	Map<String, Object> getLatestCXDatasByProject(Project project) throws Exception;
	/**
	 * 
	 * 获取点最新的时间
	 * @date  2017年6月7日 下午9:24:36
	 * @return Date
	 * @param spUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月7日     mohaolin      v1.0          create</p>
	 *
	 */
	Date getLatestTimeBySurveyPoint(String spUuid);
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
	Map<String, Object> getCXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid) throws Exception;
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
	
	void deleteCXDataBysurveyPoint(String surveyPointUuid);
	
	/**
	 * 
	 * 获取一个点下上一次的数据
	 * @date  2018年1月15日 上午9:59:33
	 * 
	 * @param spUuid
	 * @return
	 * List<CX_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月15日     姚家俊      v1.0          create</p>
	 *
	 */
	public List<CX_Data> getOnePointLatestOneCXDataBySurveyPoint(String spUuid);
	
	/**
	 * 
	 * 获取一个点下所有深度的头三条数据
	 * @date  2018年1月15日 上午10:07:23
	 * 
	 * @param spUuids
	 * @return
	 * List<CX_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月15日     姚家俊      v1.0          create</p>
	 *
	 */
	public List<CX_Data> getThreeAscCXDataBySurveyPoint(List<String> spUuids);
}
