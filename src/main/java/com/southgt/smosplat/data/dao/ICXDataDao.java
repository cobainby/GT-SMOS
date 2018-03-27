package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.CX_Data;
/**
 * 
 * 侧些数据数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月10日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ICXDataDao extends IBaseDao<CX_Data>{
	/**
	 * 
	 * 获取所有点最新的4条数据
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
	List<CX_Data> getLatestCXDataBySurveyPoint(List<String> spUuids);
	
	/**
	 * 
	 * 取出监测点制定时间内最新的各个深度的数据
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
	List<CX_Data> getOneCXDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate); 
	/**
	 * 
	 * 获取所所有点前3条数据 
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
	List<CX_Data> getThreeAscCXDataBySurveyPoint(List<String> spUuids);
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
	List<CX_Data> getLatestOneCXDataBySurveyPoint(List<String> spUuids);
	
	/**
	 * 
	 * 获取点最新一条时间
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
	Date getLatestTimeBySurveyPoint(String spUuid);
	
	
	/**
	 * 
	 * 获取所有点最新的4条数据
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
	List<CX_Data> getOnePointLatestCXDataBySurveyPoint(String spUuid);
	/**
	 * 
	 * 获取所所有点前3条数据 
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
	List<CX_Data> getOnePointThreeAscCXDataBySurveyPoint(String spUuid);
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
	List<CX_Data> getOnePointLatestOneCXDataBySurveyPoint(String spUuid);
	
	/**
	 * 
	 * 根据测量次序和测量点Uuid获取测斜数据
	 * @date  2017年5月12日 下午1:40:16
	 * 
	 * @param surveyOrder
	 * @param surveyPointUuid
	 * @return
	 * List<CX_Data>
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
	List<CX_Data> getCXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid);
	/**
	 * 
	 * 根据监测点IUuid获取最大测量次序
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
	int getMaxSurveyOrderBySurveyPoint(String surveyPointUuid);
	
	void deleteCXDataBySP(String surveyPointUuid);
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
	CX_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday);
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
	List<CX_Data> getDataBySurveyPointInOnePeroid(String surveyPointUuid, Date sDate, Date eDate);
	/**
	 * 
	 * 获取第一条数据
	 * @date  2017年6月30日 上午9:32:12
	 * 
	 * @param surveyPointUuid
	 * @return
	 * List<CX_Data>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月30日     姚家俊      v1.0          create</p>
	 *
	 */
	List<CX_Data> getFirstOneDataBySurveyPoint(List<String> surveyPointUuids);
}
