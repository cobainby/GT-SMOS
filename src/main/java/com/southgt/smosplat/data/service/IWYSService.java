package com.southgt.smosplat.data.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.data.entity.WYS_OriginalData;
import com.southgt.smosplat.project.entity.Project;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月8日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IWYSService extends IBaseService<WYS_OriginalData> {

	/**
	 * 
	 * 手动获取全站仪数据并检查
	 * @date  2017年4月17日 下午3:31:21
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
	 * <p>2017年4月17日     姚家俊      v1.0          create</p>
	 *
	 */
	Map<String,Object> checkManualWYSData(String tsData, String sourceData) throws Exception;
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
	void saveManualWYSData(List<WYS_OriginalData> data, List<String> gapRateEarlyWarningPoints, List<String> accumEarlyWarningPoints,byte flag);
	/**
	 * 删除监测点下的 围护墙(边坡)顶部水平位移 原始数据
	 * 
	 * @date  2017年5月15日 下午2:25:40
	 * @param @param spUuid
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
	void deleteWYSLevelDataBySurveyPoint(String spUuid);
	
	/**
	 * 
	 * 检查数据库中有没有这个时间点的数据
	 * @date  2017年11月24日 下午4:24:45
	 * 
	 * @param spUuid
	 * @return
	 * boolean
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年11月24日     姚家俊      v1.0          create</p>
	 *
	 */
	boolean ifDataUnique(Date date,String projectUuid);
}
