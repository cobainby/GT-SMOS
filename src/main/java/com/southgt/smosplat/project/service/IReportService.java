package com.southgt.smosplat.project.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectReport;

/**
 * 报表导出接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月14日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IReportService {
	/**
	 * 
	 * 获取日报数据
	 * @date  2017年9月22日 上午10:48:48
	 * 
	 * @param data
	 * @param project
	 * @param dateStr
	 * @throws ParseException
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月22日     姚家俊      v1.0          create</p>
	 *
	 */
	void getDailyReportDate(Map<String, Object> data,Project project, String dateStr) throws ParseException;
	
	/**
	 * 获取报告参数模板
	 * 
	 * @date  2017年9月12日 上午9:50:50
	 * @param @param projectUuid
	 * @param @return
	 * @return ProjectReport
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月12日      杨杰     	   v1.0          create</p>
	 *
	 */
	ProjectReport getProjectReportByProject(String projectUuid);
	
	/**
	 * 增加或修改报告参数模板
	 * 
	 * @date  2017年9月12日 上午9:50:16
	 * @param @param projectReport
	 * @return void
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月12日      杨杰     	   v1.0          create</p>
	 *
	 */
	void operateProjectReport(ProjectReport projectReport,String projectUuid);
	
	/**
	 * 导出周报 
	 * @date  2017年6月14日 下午3:45:04
	 * @return void
	 * @param project
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月14日     mohaolin      v1.0          create</p>
	 * @param dateStr 
	 *
	 */
	void weeklyReport(Map<String, Object> data, Project project, String bDate, String endDate) throws ParseException;
	
	/**
	 * 导出日报
	 * @date  2017年6月14日 下午3:45:04
	 * @return void
	 * @param project
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月14日     mohaolin      v1.0          create</p>
	 * @param dateStr 
	 *
	 */
	String dailyReport(Project project, Date sDate, Date eDate,ProjectReport pr);
	/**
	 * 导出周报
	 * 
	 * @date  2017年6月28日 上午11:13:08
	 * @param @param project
	 * @param @param dateStr
	 * @param @return
	 * @return String
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月28日      杨杰     	   v1.0          create</p>
	 *
	 */
	String weeklyReport(Project project, String bDate, String endDate,ProjectReport pr);
	
	/**
	 * 导出一段时间的报表
	 * 
	 * @date  2017年6月28日 上午11:13:08
	 * @param @param project
	 * @param @param dateStr
	 * @param @return
	 * @return String
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月28日      杨杰     	   v1.0          create</p>
	 *
	 */
	void timeReport(Map<String, Object> data,Project project, String bDate, String endDate,String flag) throws Exception;
	
	void weeklyReport(Map<String, Object> data,Project project) throws Exception;

	/**
	 * 
	 * 获取指定的一天中测量累计变化量最大最小值和变化速率最大值的监测点
	 * @date  2017年6月21日 上午10:54:37
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
	 * <p>2017年6月21日     姚家俊      v1.0          create</p>
	 *
	 */
	public Map<String, Object> getLimitAccumOffsetAndBiggestChangeRateByPeriod(Project project, Date sDate, Date eDate) throws ParseException;
	
	/**
	 * 
	 * 获取最新一次测量累计变化量最大最小值和变化速率最大值的监测点
	 * @date  2017年6月21日 上午10:54:37
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
	 * <p>2017年6月21日     姚家俊      v1.0          create</p>
	 *
	 */
	public Map<String, Object> getLimitAccumOffsetAndBiggestChangeRate(Project project);
	
}
