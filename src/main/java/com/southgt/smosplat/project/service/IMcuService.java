package com.southgt.smosplat.project.service;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.Project;

/**
 * mcu服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IMcuService extends IBaseService<Mcu> {

	/**
	 * 添加mcu 
	 * @date  2017年4月14日 下午2:59:21
	 * @return void
	 * @param project
	 * @param mcu
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 * @param organ 
	 * @param devModelUuid 
	 * @param deviceSN 
	 * @throws Exception 
	 *
	 */
	void addMcu(Organ organ, Project project, Mcu mcu, String deviceSN, String devModelUuid) throws Exception;

	/**
	 * 更新mcu 
	 * @date  2017年4月14日 下午3:03:21
	 * @return void
	 * @param project
	 * @param mcu
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 * @param organ 
	 * @param devModelUuid 
	 * @param deviceSN 
	 * @param deviceUuid 
	 * @throws Exception 
	 *
	 */
	void updateMcu(Organ organ, Project project, Mcu mcu, String deviceUuid, String deviceSN, String devModelUuid) throws Exception;

	/**
	 * 删除mcu
	 * @date  2017年4月14日 下午3:15:15
	 * @return void
	 * @param mcuUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void deleteMcu(String mcuUuid) throws Exception;

	/**
	 * 根据网络连接id获得使用该网络连接的mcu的数量
	 * @date  2017年4月14日 下午3:16:39
	 * @return long
	 * @param networkUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 *
	 */
	long getMcusCountByNetwork(String networkUuid);

	/**
	 * 获得工程下的对应监测项的所有mcu
	 * @date  2017年4月14日 下午3:48:35
	 * @return List<Mcu>
	 * @param projectUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月14日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Mcu> getMcus(String projectUuid);

	/**
	 * 根据同一个网络连接下所有mcu的采集时间点，算出该mcu的第一个采集时间点（错开2分钟）
	 * 假如每个mcu的采集间隔不一样，并且单独启动，可以使用该方法得到合适的开始采集时间点
	 * （现在系统是所有mcu一起启动，间隔设置为一样，所以该方法暂时用不上）
	 * @date  2017年4月21日 下午5:27:51
	 * @return Date
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月21日     mohaolin      v1.0          create</p>
	 * @param mcu 
	 * @throws Exception 
	 *
	 */
	Date setFirstCollectTime(Mcu mcu,int interval) throws Exception;

	/**
	 * 根据网络连接获取使用该网络连接的mcu
	 * @date  2017年4月26日 下午2:49:38
	 * @return List<Mcu>
	 * @param networkUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月26日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Mcu> getMcusByNetwork(String networkUuid);

	/**
	 * 获得机构下所有的启用状态的mcu
	 * @date  2017年5月5日 下午2:33:29
	 * @return List<Mcu>
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月5日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Mcu> getMcusByOrgan(String organUuid);

	/**
	 * 获得工程下mcu的数量
	 * @date  2017年5月5日 下午2:33:29
	 * @return List<Mcu>
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月5日     mohaolin      v1.0          create</p>
	 *
	 */
	long getMCUNumberByProject(Project project);

}
