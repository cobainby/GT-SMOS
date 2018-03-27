package com.southgt.smosplat.project.service;

import java.util.List;
import java.util.Map;

import com.southgt.smosplat.project.entity.Project;

public interface IWarnningDataService {
	/**
	 * 
	 * 获取报警值
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
	
	public void calWarnningOffset(Map<String, Object> map, Project project);
	
	/**
	 * 
	 * 根据监测项代码计算超限数据
	 * @date  2017年9月27日 上午11:38:36
	 * 
	 * @param dataList
	 * @param monitorCode
	 * @param project
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月27日     姚家俊      v1.0          create</p>
	 *
	 */
	public void calWarnningOffsetByMonitorItem(String monitorCode,Project project, String phoneNums);
	
	/**
	 * 
	 * 处理超限的点
	 * @date  2017年7月18日 上午9:59:30
	 * 
	 * @param points
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月18日     姚家俊      v1.0          create</p>
	 * @param flag 
	 *
	 */
	public void processPoints(Project project,String point, String flag);
}
