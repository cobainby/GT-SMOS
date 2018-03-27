package com.southgt.smosplat.data.service;

import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.data.entity.SM_Data;
import com.southgt.smosplat.project.entity.Project;

/**
 * 
 * 周边建筑物竖向位移服务接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月22日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISMService extends IBaseService<SM_Data>{
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
	Map<String,Object> checkSMData(String jsonOriginalData,String souceData) throws Exception;
	
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
	void saveManualData(Project project,List<SM_Data> data, List<String> gapRateEarlyWarningPoints, List<String> accumEarlyWarningPoints,byte flag)throws Exception;
	
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
	public Map<String, Object> getLatestSMDatasByProject(Project project);
	
	/**
	 * 
	 * 删除监测点
	 * @date  2017年6月22日 上午10:15:34
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
	 * <p>2017年6月22日     姚家俊      v1.0          create</p>
	 *
	 */
	void deleteSMDataBySP(String surveyPointUuid);
}
