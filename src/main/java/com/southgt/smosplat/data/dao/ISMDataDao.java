package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.SM_Data;
import com.southgt.smosplat.data.entity.WYD_LevelData;

/**
 * 
 * 周边建筑物竖向位移数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月22日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISMDataDao extends IBaseDao<SM_Data>{
	/**
	 * 
	 * 获取所有点最新的11条数据
	 * @date  2017年4月28日 上午9:17:23
	 * 
	 * @param spUuids
	 * @return
	 * List<WYD_LevelData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月28日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SM_Data> getLatestSM_DataBySurveyPoint(List<String> spUuids);
	
	/**
	 * 
	 * 取出监测点制定时间内最新两条数据
	 * @date  2017年4月25日 下午4:39:10
	 * 
	 * @param surveyPointUuid
	 * @return
	 * WYS_CoordData
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月25日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SM_Data> getTwoSMDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate); 
	/**
	 * 
	 * 获取所所有点第一条数据 
	 * @date  2017年4月28日 上午9:17:40
	 * 
	 * @param spUuids
	 * @return
	 * List<WYD_LevelData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月28日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SM_Data> getFirstSM_DataBySurveyPoint(List<String> spUuids);
	/**
	 * 
	 * 获取所有点最新一条数据 
	 * @date  2017年4月28日 上午9:17:56
	 * 
	 * @param surveyPointUuids
	 * @return
	 * List<WYS_CoordData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月28日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SM_Data> getLatestOneSM_DataBySurveyPoint(List<String> spUuids);
	
	void deleteSMDataBySP(String surveyPointUuid);
	/**
	 * 
	 * 取出监测点最新两条数据
	 * @date  2017年4月25日 下午4:39:10
	 * 
	 * @param surveyPointUuid
	 * @return
	 * WYS_CoordData
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月25日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SM_Data> getLatestTwoDataBySurveyPoint(String surveyPointUuid);
	
	List<SM_Data> getTwoDataBySurveyPointInTwoSide(String surveyPointUuid, Date sDate, Date eDate);
	/**
	 * 
	 * 某个时刻前的最新一条数据
	 * @date  2017年6月29日 上午11:19:24
	 * 
	 * @param surveyPointUuid
	 * @param monday
	 * @return
	 * LZ_Data
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月29日     姚家俊      v1.0          create</p>
	 *
	 */
	SM_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday);
	/**
	 * 
	 * 取出监测点最新一周内的数据
	 * @date  2017年4月25日 下午4:39:10
	 * 
	 * @param surveyPointUuid
	 * @return
	 * WYS_CoordData
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月25日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SM_Data> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate);
	/**
	 * 
	 * 取出所有监测点头三条数据
	 * @date  2017年6月28日 上午10:25:50
	 * 
	 * @param surveyPointUuids
	 * @return
	 * List<WYD_LevelData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月28日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SM_Data> getFirstThreeDataBySurveyPoint(List<String> surveyPointUuids);

}
