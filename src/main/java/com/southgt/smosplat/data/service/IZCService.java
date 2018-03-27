package com.southgt.smosplat.data.service;

import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.entity.ZC_Data;
import com.southgt.smosplat.project.entity.Project;

/**
 * 支撑内力数据服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月28日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IZCService extends IBaseService<ZC_Data> {
	/**
	 * 
	 * 获取最新10条轴力计数据
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
	Map<String, Object> getLatestZCDatasByProject(Project project);
	
	/**
	 * 
	 * 第一条数据
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
	List<ZC_Data> getFirstOneZCDatasBySurveyPoints(List<String> surveyPointUuids);
	
	/**
	 * 
	 * 取最新一条数据 
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
	List<ZC_Data> getLatestOneZCDatasBySurveyPoints(List<String> surveyPointUuids);
	
	void deleteZCDataBySurveyPoint(String spUuid);
}
