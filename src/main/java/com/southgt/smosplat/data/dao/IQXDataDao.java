package com.southgt.smosplat.data.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.QX_Data;
import com.southgt.smosplat.data.entity.QX_Data;
import com.southgt.smosplat.data.entity.QX_Data;
/**
 * 
 * 建筑物倾斜数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
public interface IQXDataDao extends IBaseDao<QX_Data>{
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
	List<QX_Data> getOnePointLatestQXDataBySurveyPoint(String spUuid);
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
	List<QX_Data> getOnePointThreeAscQXDataBySurveyPoint(String spUuid);
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
	List<QX_Data> getOnePointLatestOneQXDataBySurveyPoint(String spUuid);
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
	List<QX_Data> getLatestQXDataBySurveyPoint(List<String> spUuids);
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
	List<QX_Data> getThreeAscQXDataBySurveyPoint(List<String> spUuids);
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
	List<QX_Data> getLatestOneQXDataBySurveyPoint(List<String> spUuids);
	/**
	 * 
	 * 根据测量次序和测量点Uuid获取测斜数据
	 * @date  2017年5月12日 下午1:40:16
	 * 
	 * @param surveyOrder
	 * @param surveyPointUuid
	 * @return
	 * List<QX_Data>
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
	List<QX_Data> getQXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid);
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
	/**
	 * 
	 * 删除监测点下的监测点数据
	 * @date  2017年5月15日 下午4:34:55
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
	 * <p>2017年5月15日     姚家俊      v1.0          create</p>
	 *
	 */
	void deleteQXDataBySP(String surveyPointUuid);
}
