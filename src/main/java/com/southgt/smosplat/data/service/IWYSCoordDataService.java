package com.southgt.smosplat.data.service;

import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.WYS_OriginalData;
import com.southgt.smosplat.data.util.math.Station;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.Warning;

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
public interface IWYSCoordDataService extends IBaseService<WYS_CoordData> {

	
	/**
	 * 
	 * 检查解算后数据
	 * @date  2017年4月25日 下午1:44:18
	 * 
	 * @param lstOriginalData 原始数据列表
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月25日     姚家俊      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	List<WYS_CoordData> checkCoordData(List<WYS_OriginalData> lstOriginalData, Station station);
	
	/**
	 * 
	 * 检查解算后数据
	 * @date  2017年11月29日 下午22:44:18
	 * 
	 * @param lstOriginalData 原始数据列表
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月25日     姚家俊      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	Map<String, Object> calOffset(List<WYS_CoordData> resultData, Warning hWarning)throws Exception;;
	
	/**
	 * 
	 * 保存数据
	 * @date  2017年8月1日 下午2:34:04
	 * 
	 * @param lstOriginalData
	 * @param station
	 * @return
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
	void saveCoordData(List<WYS_CoordData> data, List<String> gapRateEarlyWarningPoints, List<String> accumEarlyWarningPoints, byte flag);
	/**
	 * 
	 * 获取最新10条解算数据 
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
	public Map<String, Object> getLatestWYSCoordDatasByProject(Project project);
	/**
	 * 删除监测点下的 围护墙(边坡)顶部水平位移 数据
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
	

}
