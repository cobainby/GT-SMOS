package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Mcu;

/**
 * mcu数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月14日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IMcuDao extends IBaseDao<Mcu> {

	/**
	 * 获得具有对应名称的mcu的记录数 
	 * @date  2017年4月14日 下午3:00:46
	 * @return long
	 * @param projectUuid
	 * @param mcuName
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
	long getMcuNumber(String projectUuid, String mcuName);

	/**
	 * 获得除自己以外具有对应名称的mcu的记录数 
	 * @date  2017年4月14日 下午3:05:02
	 * @return long
	 * @param projectUuid
	 * @param mcuName
	 * @param mcuUuid
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
	long getMcuNumberExceptSelf(String projectUuid, String mcuName, String mcuUuid);

	/**
	 * 获得使用指定网络连接的mcu的数量
	 * @date  2017年4月14日 下午3:17:48
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
	 * 获得项目下的监测项的所有mcu
	 * @date  2017年4月14日 下午3:49:40
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
	 * 获得该网络连接下所有正在采集中的mcu 
	 * @date  2017年4月24日 上午9:28:10
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
	 * <p>2017年4月24日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Mcu> getCollectingMcusByNetwork(String networkUuid);

	/**
	 * 获得使用指定网络连接的mcu
	 * @date  2017年4月26日 下午2:51:24
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
	 * 获得工程下所有mcu
	 * @date  2017年5月5日 下午2:34:10
	 * @return void
	 * @param organUuid
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
	 * 获得工程下所有mcu的数量
	 * @date  2017年5月5日 下午2:34:10
	 * @return void
	 * @param organUuid
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
	long getMCUNumberByProject(String projectUuid);

}
