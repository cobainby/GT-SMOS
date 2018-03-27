package com.southgt.smosplat.project.service.autoCollect;

import com.southgt.smosplat.project.entity.Network;

/**
 * 与设备的网络通讯实现的接口定义
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月7日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IConnectService {
	/**
	 * 关闭网络通讯并释放相关资源 
	 * @date  2016年10月10日 上午9:36:53
	 * @return void
	 * @throws 
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年10月10日     mohaolin      v1.0          create</p>
	 *
	 */
	void destroy();
	/**
	 * 获得连接操作使用的网络连接信息
	 * @date  2016年9月8日 上午9:48:32
	 * @return Network
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年9月8日     mohaolin      v1.0          create</p>
	 *
	 */
	Network getNetwork();
	
}
