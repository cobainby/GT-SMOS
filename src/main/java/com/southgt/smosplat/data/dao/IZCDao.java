package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.ZC_Data;

/**
 * 支撑内力数据数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月28日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IZCDao extends IBaseDao<ZC_Data> {
	/**
	 * 
	 * 获取最新11条轴力计数据
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
	List<ZC_Data> getLatestZCDatasBySurveyPoints(List<String> surveyPointUuids);
	
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
	List<ZC_Data> getTwoZCDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate);
	
	/**
	 * 
	 * 取最新一条轴力计数据 
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
	
	/**
	 * 
	 * 第一条轴力计数据
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
	 * 删除监测点下的轴力数据
	 * 
	 * @date  2017年5月15日 上午11:42:12
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
	void deleteZCDataBySurveyPoint(String spUuid);
	/**
	 * 
	 * 获取最新两条记录
	 * @date  2017年6月23日 下午3:11:43
	 * 
	 * @param surveyPointUuid
	 * @return
	 * List<ZC_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月23日     姚家俊      v1.0          create</p>
	 *
	 */
	List<ZC_Data> getLatestTwoDataBySurveyPoint(String surveyPointUuid);
	/**
	 * 
	 * 获取一段时间内头一条数据和最后一条数据
	 * @date  2017年9月25日 上午10:32:06
	 * 
	 * @param surveyPointUuid
	 * @param sDate
	 * @param eDate
	 * @return
	 * List<ZC_Data>
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
	List<ZC_Data> getTwoDataBySurveyPointInTwoSide(String surveyPointUuid, Date sDate, Date eDate);
	/**
	 * 
	 * 获取头3次数据
	 * @date  2017年7月5日 下午4:39:26
	 * 
	 * @param spUuid
	 * @return
	 * List<ZC_Data>
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
	List<ZC_Data> getFirstThreeDataBySurveyPoint(List<String> surveyPointUuids);
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
	 ZC_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday);
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
	List<ZC_Data> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate);
	 
}
