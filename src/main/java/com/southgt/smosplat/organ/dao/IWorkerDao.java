package com.southgt.smosplat.organ.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Worker;

/**
 * 人员数据库操作实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IWorkerDao extends IBaseDao<Worker>{


	/**
	 * 获得所有人员
	 * @date  2017年3月6日 上午11:44:29
	 * @return List<Worker>
	 * @param first
	 * @param max
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Worker> getWorkers(int first, int max);

	/**
	 *	获得所有人员
	 * @date  2017年3月6日 上午11:44:58
	 * @return List<Worker>
	 * @param first
	 * @param max
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Worker> getWorkers(int first, int maxs, String organUuid);

	/**
	 * 获得机构下所有人员
	 * @date  2017年3月31日 下午3:41:00
	 * @return List<Worker>
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
	List<Worker> getWorkersByOrgan(String organUuid);
	/**
	 * 
	 * 根据账户取得人员 
	 * @date  2017年4月1日 下午3:39:06
	 * 
	 * @param accountUuid
	 * @return
	 * Worker
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月1日     姚家俊      v1.0          create</p>
	 *
	 */
	Worker getWorkerByAccount(String accountUuid);

	/**
	 * 获得机构下的人员的数量
	 * @date  2017年5月18日 下午5:09:51
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
	long getWorkersNumberByOrgan(String organUuid);

}
