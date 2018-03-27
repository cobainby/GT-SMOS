package com.southgt.smosplat.project.service;

import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectReport;

public interface IExportDataService {

	/**
	 * WYS监测数据
	 * {创建表格的时候，0行开始先建立第一个表头，增加表尾，然后将表尾下移到数据行数的末行，然后插入数据行，插入数据，然后判断当前数据行有没有
	 * 超过打印的行数，超过了要在超过了的行数前面增加表头用来换页，先把所有超过的行数往下移动(表头的行数+上一页页尾行+1)行，然后再插入表头}
	 * @date  2017年7月18日 下午3:12:58
	 * @param @param templatePath
	 * @param @param projectId
	 * @param @param projectName
	 * @param @param projectAddress
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月18日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportWYSDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	
	/**
	 * 围护墙（边坡）顶部竖向位移 WYD监测数据
	 * 
	 * @date  2017年7月21日 上午10:49:58
	 * @param @param templatePath
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月21日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportWYDDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	
	/**
	 * 立柱竖向位移监测数据 LZ
	 * 
	 * @date  2017年7月21日 上午11:41:10
	 * @param @param templatePath
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月21日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportLZDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	
	/**
	 * 周边建筑物竖向位移 SM
	 * 
	 * @date  2017年7月24日 上午8:56:49
	 * @param @param templatePath
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月24日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportSMDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	/**
	 * 周边管线竖向位移 ZGD
	 * 
	 * @date  2017年7月24日 上午9:13:59
	 * @param @param templatePath
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月24日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportZGDDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	/**
	 * 水位 SW
	 * 
	 * @date  2017年7月24日 上午10:29:27
	 * @param @param templatePath
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月24日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportSWDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	
	/**
	 * 锚杆内力 MT
	 * 
	 * @date  2017年7月24日 上午11:17:51
	 * @param @param templatePath
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月24日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportMTDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	
	/**
	 * 支撑内力 ZC
	 * 
	 * @date  2017年7月24日 下午2:23:43
	 * @param @param templatePath
	 * @param @param project
	 * @param @param specification
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return
	 * @return byte[]
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月24日      杨杰     	   v1.0          create</p>
	 *
	 */
	public byte[] exportZCDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
	
	public byte[] exportCXDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime);
		
	public byte[] exportAllSurveyDataToExcel(String templatePath,String pjName,String pjAddress,Project project,ProjectReport pr, String startTime, String endTime);
}
