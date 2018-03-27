package com.southgt.smosplat.data.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
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
public interface IWYSDao extends IBaseDao<WYS_OriginalData> {

	/**
	 * 根据监测点的id获得最新的10个时间点的数据
	 * @date  2017年4月10日 上午10:05:28
	 * @return List<TS_SP_CoordData>
	 * @param surveyPointUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月10日     mohaolin      v1.0          create</p>
	 *
	 */
	List<WYS_OriginalData> getLatestWYSDatas(String surveyPointUuid);

	/**
	 * 获取第一个时间点的所有数据
	 * @date  2017年4月10日 上午11:17:54
	 * @return List<WYS_OriginalData>
	 * @param surveyPointUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月10日     mohaolin      v1.0          create</p>
	 *
	 */
	List<WYS_OriginalData> getFirstWYSDatas(String surveyPointUuid);
	
	/**
	 * 根据监测点的id获得最新的10个时间点
	 * @date  2017年4月10日 上午10:05:28
	 * @return List<TS_SP_CoordData>
	 * @param surveyPointUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月10日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Date> getLatestWYSDates(String surveyPointUuid);
	/**
	 * 
	 * 获取测点第一个测量时间
	 * @date  2017年4月19日 下午12:35:14
	 * 
	 * @param surveyPointUuid
	 * @return
	 * Date
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
	Date getfirstWYSDate(String surveyPointUuid);
	/**
	 * 
	 * 根据测点ID和入库的一段时间获取数据
	 * @date  2017年4月19日 下午12:34:31
	 * 
	 * @param surveyPointUuid
	 * @param date
	 * @return
	 * List<WYS_OriginalData>
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
	List<WYS_OriginalData> getWYSDatesByDate( Date startDate, Date endDate);
	/**
	 * 
	 * 根据测点ID和入库的时间获取数据
	 * @date  2017年4月19日 下午12:34:31
	 * 
	 * @param surveyPointUuid
	 * @param date
	 * @return
	 * List<WYS_OriginalData>
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
	List<WYS_OriginalData> getWYSDatesByPecificDate(String surveyPointUuid, Date date);
	
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
