package com.southgt.smosplat.project.service;

import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.project.entity.Project;

/**
 * 工程服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月19日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IProjectService extends IBaseService<Project> {

	/**
	 * 增加工程
	 * @date  2017年3月19日 下午3:08:25
	 * @return void
	 * @param project
	 * @param organ
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月19日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void addProject(Project project, Organ organ) throws Exception;

	/**
	 * 根据条件查询工程列表
	 * @date  2017年3月19日 下午3:16:51
	 * @return List<Project>
	 * @param account
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月19日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Project> getProjects(Account account);

	/**
	 * 删除工程
	 * @date  2017年3月19日 下午4:58:15
	 * @return void
	 * @param projectUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月19日     mohaolin      v1.0          create</p>
	 *
	 */
	void deleteProject(String projectUuid);

	/**
	 * 修改工程信息 
	 * @date  2017年3月20日 下午3:08:29
	 * @return Project
	 * @param project
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月20日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	Project updateProject(Project project) throws Exception;

	/**
	 * 获取机构下所有工程 
	 * @date  2017年3月31日 下午3:52:15
	 * @return List<Project>
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月31日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Project> getProjectsByOrgan(String organUuid);

	/**
	 * 获得工程下所用到的所有自动采集设备 
	 * 针对每种设备类型，这里都要去对应的数据库取一下
	 * @date  2017年4月25日 下午4:28:38
	 * @return Map<String,Object>
	 * @param project
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月25日     mohaolin      v1.0          create</p>
	 *
	 */
	Map<String, Object> getAllAutoCollectDevices(Project project);

	/**
	 * 工程开始采集
	 * @date  2017年6月3日 下午2:07:59
	 * @return void
	 * @param networkUuids
	 * @param interval
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月3日     mohaolin      v1.0          create</p>
	 * @param project 
	 * @param connectMcuTime 每个mcu操作给定的时间
	 * @throws Exception 
	 *
	 */
	void projectStartCollect(Project project, String networkUuids, int interval, int connectMcuTime) throws Exception;
	
	/**
	 * 
	 * 联睿科采集模块
	 * @date  2018年1月30日 下午5:09:29
	 * 
	 * @param project
	 * @param networkUuids
	 * @param interval
	 * @param connectMcuTime
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月30日     姚家俊      v1.0          create</p>
	 *
	 */
	void projectStartCollect_NEW(Project project, String networkUuids, int interval, int connectMcuTime) throws Exception;
	
	void projectStartCollect_LRK(Project project, String networkUuids, int interval, int connectMcuTime) throws Exception;
	
	
	/**
	 * 
	 * 区分开两种采集逻辑的mcu
	 * @date  2018年1月24日 上午10:24:07
	 * 
	 * @param networkUuids
	 * @return
	 * Map<String,Object>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月24日     姚家俊      v1.0          create</p>
	 *
	 */
	Map<String, Object> mcuFilter(String networkUuids);
	
	
	/**
	 * 获得机构下的工程数量
	 * @date  2017年5月18日 下午5:15:05
	 * @return long
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月18日     mohaolin      v1.0          create</p>
	 *
	 */
	long getProjectNumberByOrgan(String organUuid);

	/**
	 * 工程停止自动采集 
	 * @date  2017年5月23日 下午2:47:24
	 * @return void
	 * @param networkUuids
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月23日     mohaolin      v1.0          create</p>
	 * @param project 
	 *
	 */
	void projectStopCollect(Project project, String networkUuids);

	/**
	 * 开始召测
	 * @date  2017年7月10日 下午3:39:56
	 * 
	 * @param networkUuid
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月10日     姚家俊      v1.0          create</p>
	 *
	 */
	void projectStartCallCollect(String networkUuids,int connectMcuTime);
	/**
	 * 
	 * 测试连接
	 * @date  2017年7月10日 下午3:39:56
	 * 
	 * @param networkUuid
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月10日     姚家俊      v1.0          create</p>
	 *
	 */
	void testConnect(String networkUuid) throws Exception;
	
	/**
	 * 
	 * 测试mcu
	 * @date  2017年7月10日 下午3:39:56
	 * 
	 * @param networkUuid
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月10日     姚家俊      v1.0          create</p>
	 *
	 */
	void testMcu(String mcuUuid) throws Exception;
	/**
	 * 
	 * 更新采集间隔
	 * @date  2017年7月13日 下午2:03:22
	 * 
	 * @param projectUuid
	 * @param interval
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月13日     姚家俊      v1.0          create</p>
	 *
	 */
	void updateInterval(String projectUuid, String interval);
	/**
	 * 
	 * 更新超限状态
	 * @date  2017年9月27日 下午4:19:44
	 * 
	 * @param projectUuid
	 * @param interval
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
	void updateWarningStatus(String projectUuid, String warningStatus);

}
