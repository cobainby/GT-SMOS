package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.SM_Data;
import com.southgt.smosplat.data.entity.SW_Data;

/**
 * 
 * 水位数据dao接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月2日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISWDataDao extends IBaseDao<SW_Data>{
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
	 * 
	 * 获取一个监测点下指定时间段内最新的两条数据
	 * @date  2017年6月16日 下午4:51:55
	 * 
	 * @param surveyPointUuid
	 * @return
	 * List<SW_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月16日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SW_Data> getTwoSWDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate);
	
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
	
	void deleteSW_DataBySurveyPoint(String spUuid);
	/**
	 * 
	 * 获取最新最新的数据的次数（小于等于11）
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
	List<Date> getLatestTimes(List<String> surveyPointUuids);
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
	List<SW_Data> getLatestTwoDataBySurveyPoint(String surveyPointUuid);
	/**
	 * 
	 * 一段时间内第一条数据和最后一条数据
	 * @date  2017年9月25日 上午10:29:21
	 * 
	 * @param surveyPointUuid
	 * @param sDate
	 * @param eDate
	 * @return
	 * List<SW_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月25日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SW_Data> getTwoDataBySurveyPointInTwoSide(String surveyPointUuid,Date sDate, Date eDate);
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
	SW_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday);
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
	List<SW_Data> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate);
	/**
	 * 
	 * 头3次数据求初始水位
	 * @date  2017年7月5日 下午4:46:23
	 * 
	 * @param spUuid
	 * @return
	 * List<SW_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月5日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SW_Data> getFirstThreeDataBySurveyPoint(List<String> spUuid);
}
