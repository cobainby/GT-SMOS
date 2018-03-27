package com.southgt.smosplat.organ.dao;

import java.util.Date;
import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.GPSInfo;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年10月13日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IGPSDao extends IBaseDao<GPSInfo> {

	/**
	 * 获得账户上传的时间段内的gps信息
	 * @date  2017年10月13日 下午2:05:20
	 * @return List<GPSInfo>
	 * @param account
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年10月13日     mohaolin      v1.0          create</p>
	 * @param projectUuid 
	 *
	 */
	List<GPSInfo> getGPSInfosByAccount(Account account, Date startDate, Date endDate, String projectUuid);

}
