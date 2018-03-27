package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.ZGD_Data;

/**
 * 
 * 周边管线竖向位移数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
public interface IZGDDataDao extends IBaseDao<ZGD_Data>{
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
	List<ZGD_Data> getLatestZGD_DataBySurveyPoint(List<String> spUuids);
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
	List<ZGD_Data> getFirstZGD_DataBySurveyPoint(List<String> spUuids);
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
	List<ZGD_Data> getLatestOneZGD_DataBySurveyPoint(List<String> spUuids);
	
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
	List<ZGD_Data> getTwoZGDDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate); 
	
	/**
	 * 删除监测点下的周边管线竖向位移数据
	 * 
	 * @date  2017年5月15日 下午2:10:43
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
	void deleteZGDDataBySurveyPoint(String spUuid);
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
	List<ZGD_Data> getLatestTwoDataBySurveyPoint(String surveyPointUuid);
	
	/**
	 * 
	 * 取出一段时间内头一条和最后一条数据
	 * @date  2017年9月25日 上午10:23:44
	 * 
	 * @param surveyPointUuid
	 * @param sDate
	 * @param eDate
	 * @return
	 * List<ZGD_Data>
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
	List<ZGD_Data> getTwoDataBySurveyPointInTwoSide(String surveyPointUuid, Date sDate, Date eDate);
	/**
	 * 
	 * 取出所有监测点前3条解算后的数据 
	 * @date  2017年4月19日 下午3:09:54
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
	 * <p>2017年4月19日     姚家俊      v1.0          create</p>
	 *
	 */
	List<ZGD_Data> getFirstThreeDataBySurveyPoint(List<String> surveyPointUuids);
	/**
	 * 
	 *获取某个时间点前最新的一条数据
	 * @date  2017年6月27日 上午9:37:52
	 * 
	 * @param surveyPointUuid
	 * @param d
	 * @return
	 * WYS_CoordData
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月27日     姚家俊      v1.0          create</p>
	 *
	 */
	ZGD_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday);
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
	List<ZGD_Data> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate); 
}
