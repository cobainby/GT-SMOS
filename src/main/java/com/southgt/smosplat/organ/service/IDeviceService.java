package com.southgt.smosplat.organ.service;

import java.util.List;
import java.util.Map;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.Project;

public interface IDeviceService extends IBaseService<Device> {

	/**
	 * 获取部门下的设备
	 * 
	 * @date  2017年4月12日 下午3:37:57
	 * @param @param organUuid 部门ID
	 * @param @return
	 * @return List<Device> 部门下的设备列表
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日      杨杰     	   v1.0          create</p>
	 * @param i 是否是自动化设备
	 *
	 */
	List<Device> getDeviceByOrganUuid(String organUuid, int i);
	
	void addDevice(Device device)throws Exception;
	
	void updateDevice(Device device)throws Exception;
	
	List<Device> getUsingDeviceByOrganUuid(String organUuid);

	/**
	 * 获得工程下所有设备
	 * @date  2017年5月3日 下午4:00:23
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
	 * 更新设备连接的mcu信息
	 * @date  2017年5月5日 下午7:15:58
	 * @return void
	 * @param deviceUuid
	 * @param mcuUuid
	 * @param moduleNum
	 * @param pointNum
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
	void updateDeviceForMcu(String deviceUuid, String mcuUuid, String moduleNum, String pointNum);

	/**
	 * 清除设备连接的mcu信息
	 * @date  2017年5月5日 下午7:27:48
	 * @return void
	 * @param deviceUuid
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
	void deleteDeviceForMcu(String deviceUuid);

	/**
	 * 根据编号获得具有该编号的设备数量 
	 * @date  2017年5月6日 下午3:02:49
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
	 * @date  2017年5月6日 下午3:49:20
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
	
	List<Device> getUsingPointNumByMcuAndModuleNum(String mcuUuid,int moduleNum);

	/**
	 * 获得该mcu连接的设备列表
	 * @date  2017年5月8日 下午3:37:09
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
	 * @date  2017年5月18日 下午5:12:14
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
	 * @date  2017年5月22日 下午3:26:04
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
	
	/**
	 * 添加锚索设备（设备编号不惟一）
	 * 
	 * @date  2017年6月8日 上午9:22:53
	 * @param @param device
	 * @param @throws Exception
	 * @return void
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月8日      杨杰     	   v1.0          create</p>
	 *
	 */
	void addCableMeterDevice(Device device);
	
	void updateCableMeterDevice(Device device);
	
	/**
	 * 根据部门ID获取得到一个以设备类型分组的所有设备的Map
	 * 
	 * @date  2017年10月16日 下午2:32:33
	 * @param @param organUuid
	 * @param @return
	 * @return Map<String,List<Device>>
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年10月16日      杨杰     	   v1.0          create</p>
	 *
	 */
	Map<String,List<Device>> getDeviceMapOrderByDevTypeOfOrgan(String organUuid,int isAuto);
}
