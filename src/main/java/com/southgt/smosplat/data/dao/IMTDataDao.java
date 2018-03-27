package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.entity.MT_Data;

/**
 * 
 * 锚杆内力数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
public interface IMTDataDao extends IBaseDao<MT_Data> {
	/**
	 * 
	 * 获取最新11条锚杆计数据
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
	List<MT_Data> getLatestMTDatasBySurveyPoints(List<String> surveyPointUuids);
	
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
	 * 取最新一条锚杆计数据 
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
	List<MT_Data> getLatestOneMTDatasBySurveyPoints(List<String> surveyPointUuids);
	
	/**
	 * 
	 * 一个监测点在指定时间段之内最新的两条数据
	 * @date  2017年6月16日 下午5:14:49
	 * 
	 * @param surveyPointUuid
	 * @param sDate
	 * @param eDate
	 * @return
	 * List<MT_Data>
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
	List<MT_Data> getTwoMTDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate);
	
	/**
	 * 
	 * 第一条锚杆计数据
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
	List<MT_Data> getFirstOneMTDatasBySurveyPoints(List<String> surveyPointUuids);
	
	void deleteMTDataBySP(String surveyPointUuid);
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
	List<MT_Data> getLatestTwoDataBySurveyPoint(String surveyPointUuid);
	/**
	 * 
	 * 取出一段时间内头一条和末一条数据
	 * @date  2017年9月25日 上午10:37:01
	 * 
	 * @param surveyPointUuid
	 * @param sDate
	 * @param eDate
	 * @return
	 * List<MT_Data>
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
	List<MT_Data> getTwoDataBySurveyPointInTwoSide(String surveyPointUuid,Date sDate, Date eDate);
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
	MT_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday);
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
	List<MT_Data> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate);
	/**
	 * 
	 * 头3次数据求初始内力值
	 * @date  2017年7月5日 下午4:43:03
	 * 
	 * @param spUuid
	 * @return
	 * List<MT_Data>
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
	List<MT_Data> getFirstThreeDataBySurveyPoint(List<String> spUuid);
}
