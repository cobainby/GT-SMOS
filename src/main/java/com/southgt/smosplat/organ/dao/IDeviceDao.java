package com.southgt.smosplat.organ.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.Project;

public interface IDeviceDao extends IBaseDao<Device> {

	/**
	 * 根据部门id获取所有设备
	 * 
	 * @date  2017年3月28日 上午11:07:32
	 * 
	 * @param @param organUuid
	 * @param @return
	 * @return List<Device>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  YANG
	 * <p>Modification History:</p>
	 * <p>Date         Author  杨杰    Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月28日     YANG      v1.0          create</p>
	 * @param i 是否是自动化设备
	 *
	 */
	List<Device> getDeviceByOrganUuid(String organUuid, int i);
	
	/**
	 * 获取指定部门下相同SN号设备的个数
	 * 
	 * @date  2017年4月12日 下午3:33:49
	 * @param @param organUuid
	 * @param @param sn
	 * @param @return
	 * @return long
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日      杨杰     	   v1.0          create</p>
	 *
	 */
	long getDeviceBySN(String organUuid,String sn);
	
	long getDeviceBySNExceptSelf(String organUuid,String sn,String deviceUuid);
	
	/**
	 * 根据部门获取启用状态下的设备
	 * 
	 * @date  2017年4月12日 下午3:35:10
	 * @param @param organUuid 部门ＩＤ
	 * @param @return
	 * @return List<Device>　当前部门启用状态下的设备列表
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日      杨杰     	   v1.0          create</p>
	 *
	 */
	List<Device> getUsingDeviceByOrgan(String organUuid);

	/**
	 * 获得工程下所有设备
	 * @date  2017年5月3日 下午4:01:14
	 * @return List<Device>
	 * @param project
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Device> getDevicesByProject(Project project);

	/**
	 * 获得具有该编号的设备数量
	 * @date  2017年5月6日 下午3:03:56
	 * @return long
	 * @param projectUuid
	 * @param sn
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月6日     mohaolin      v1.0          create</p>
	 *
	 */
	long getDeviceNumber(String projectUuid, String sn);
	/**
	 * 获得除了自己以外具有该编号的设备数量
	 * @date  2017年5月6日 下午3:03:56
	 * @return long
	 * @param projectUuid
	 * @param sn
	 * @param deviceUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月6日     mohaolin      v1.0          create</p>
	 *
	 */
	long getDeviceNumberExceptSelf(String projectUuid,String sn,String deviceUuid);

	/**
	 * 获得使用mcu的设备的数量
	 * @date  2017年5月6日 下午3:50:25
	 * @return long
	 * @param mcuUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月6日     mohaolin      v1.0          create</p>
	 *
	 */
	long getDeviceNumberByMcu(String mcuUuid);
	
	List<Device> getUsingMcuMudleAndPointNumByMcu(String mcuUuid);
	
	List<Device> getUsingPointNumByMcuAndModuleNum(String mcuUuid,int mudleNum);

	/**
	 * 获得mcu连接的mcu列表
	 * @date  2017年5月8日 下午3:37:52
	 * @return List<Device>
	 * @param mcuUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月8日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Device> getDevicesByMcu(String mcuUuid);

	/**
	 * 获得机构下的设备数量
	 * @date  2017年5月18日 下午5:13:51
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
	long getDeviceNumberByOrgan(String organUuid);

	/**
	 * 获得工程下所有自动采集设备
	 * @date  2017年5月22日 下午3:27:05
	 * @return List<Device>
	 * @param projectUuid
	 * @param i
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月22日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Device> getAutoDevicesByCurrentProject(String projectUuid, int i);
}
