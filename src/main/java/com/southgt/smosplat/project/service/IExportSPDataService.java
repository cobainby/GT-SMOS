package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectReport;

public interface IExportSPDataService {

	/**
	 * 周报表导出处理中心
	 * 
	 * @date  2017年12月14日 下午3:07:26
	 * @param @param isDaily
	 * @param @param templatePath
	 * @param @param pjName
	 * @param @param pjAddress
	 * @param @param deviceName
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return String
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年12月14日      杨杰     	   v1.0          create</p>
	 *
	 */
	public String exportExcelCoreService(boolean isDaily,List<String> templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,ProjectReport pr);
	
	/**
	 * 围护墙边坡水平位移数据表导出
	 * 
	 * @date  2017年12月11日 上午11:10:59
	 * @param @param isDaily       //是否日报
	 * @param @param templatePath	//模板路径
	 * @param @param pjName`		//项目名
	 * @param @param pjAddress		//项目地址
	 * @param @param deviceName		//采集仪器
	 * @param @param project		//	项目实体
	 * @param @param specification	//参考标准
	 * @param @param startTime		//开始时间
	 * @param @param endTime		//结束时间
	 * @param @return
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年12月11日      杨杰     	   v1.0          create</p>
	 *
	 */
	public void exportWYSDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportWYDDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportLZataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportSMDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportZGDDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportSWDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportMTDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportZCDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
	public void exportCXDataToExcel(boolean isDaily,String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime,int fulu);
	
}
