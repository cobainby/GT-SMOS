package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.WYS_OriginalData;
import com.southgt.smosplat.project.entity.MonitorItem;

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
public interface IWYSCoordDataDao extends IBaseDao<WYS_CoordData> {
	/**
	 * 
	 * 取出所有监测点最新11条解算后坐标数据
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
	List<WYS_CoordData> getLatestWYSCoordDataBySurveyPoint(List<String> surveyPointUuids); 
	
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
	List<WYS_CoordData> getTwoWYSCoordDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate); 
	
	/**
	 * 
	 * 取出监测点一段时间内的数据
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
	List<WYS_CoordData> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate); 
	
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
	List<WYS_CoordData> getLatestTwoWYSCoordDataBySurveyPoint(String surveyPointUuid); 
	
	/**
	 * 
	 * 取出一天当中头一条和最后一条数据
	 * @date  2017年9月25日 上午9:56:56
	 * 
	 * @param surveyPointUuid
	 * @return
	 * List<WYS_CoordData>
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
	List<WYS_CoordData> getTwoWYSCoordDataBySurveyPointInTwoSide(String surveyPointUuid, Date sDate, Date eDate); 
	
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
	WYS_CoordData getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date Monday);
	
	/**
	 * 
	 * 取出所有监测点最新一条解算后坐标数据
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
	List<WYS_CoordData> getLatestOneWYSCoordDataBySurveyPoint(List<String> surveyPointUuids); 

	/**
	 * 
	 * 取出所有监测点第1条解算后的数据 
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
	List<WYS_CoordData> getFirstWYSCoordDataBySurveyPoint(List<String> surveyPointUuids);
	
	/**
	 * 
	 * 取头三条数据
	 * @date  2017年12月4日 上午11:10:01
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
	 * <p>2017年12月4日     姚家俊      v1.0          create</p>
	 *
	 */
	List<WYS_CoordData> getFirstThreeDataBySurveyPoint(String surveyPointUuids);
	
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
	void deleteWYSDataBySurveyPoint(String spUuid);
}
