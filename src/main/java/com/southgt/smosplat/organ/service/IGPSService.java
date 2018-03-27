package com.southgt.smosplat.organ.service;

import java.text.ParseException;
import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
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
public interface IGPSService extends IBaseService<GPSInfo> {

	/**
	 * 获取对应账号上传的gps信息
	 * @date  2017年10月13日 下午1:53:43
	 * @return List<GPSInfo>
	 * @param account
	 * @param startTime
	 * @param endTime
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
	List<GPSInfo> getGPSInfosByAccount(Account account, String startTime, String endTime, String projectUuid);

	/**
	 * 添加gps信息 
	 * @date  2017年10月13日 下午2:14:25
	 * @return void
	 * @param account
	 * @param lon
	 * @param lat
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年10月13日     mohaolin      v1.0          create</p>
	 * @param within 
	 * @param projectUuid 
	 * @throws ParseException 
	 *
	 */
	void addGPSInfo(Account account, Double lon, Double lat, Integer within, String projectUuid) throws ParseException;

}
