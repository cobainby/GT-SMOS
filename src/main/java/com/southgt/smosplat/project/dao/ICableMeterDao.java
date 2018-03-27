package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Stress;

public interface ICableMeterDao extends IBaseDao<CableMeter> {

	/**
	 * 获取项目下的所有锚索计设备元件
	 * 
	 * @date  2017年5月3日 上午10:05:13
	 * @param @param projectUuid
	 * @param @return
	 * @return List<CableMeter>
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日      杨杰     	   v1.0          create</p>
	 *
	 */
	List<CableMeter> getCableMeterByProject(String  projectUuid);
	
	/**
	 * 
	 * 获取锚索计监测点下的所有锚索计设备元件
	 * @date  2017年5月3日 上午10:05:50
	 * @param @param sp_MTUuid
	 * @param @return
	 * @return List<CableMeter>
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日      杨杰     	   v1.0          create</p>
	 *
	 */
	List<CableMeter> getCableMeterBySP_mt(String  sp_MTUuid);
	
	/**
	 * 获取同监测点下的同名或同编号的设备数
	 * 
	 * @date  2017年5月3日 上午10:06:35
	 * @param @param sp_MTUuid
	 * @param @param name
	 * @param @param devCode
	 * @param @return
	 * @return long
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日      杨杰     	   v1.0          create</p>
	 *
	 */
	long getCableMeterNumByConditions(String sp_MTUuid,String name);
	
	/**
	 * 获取同监测点下的同名或同编号的设备数(除本身外)
	 * 
	 * @date  2017年5月3日 上午10:07:27
	 * @param @param sp_MTUuid
	 * @param @param name
	 * @param @param devCode
	 * @param @param cableMeterUuid
	 * @param @return
	 * @return long
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日      杨杰     	   v1.0          create</p>
	 *
	 */
	long getCableMeterNumByConditionsSelfException(String sp_MTUuid,String name,String cableMeterUuid);
	
	/**
	 * 获取关联MCU的锚索设备
	 * 
	 * @date  2017年5月3日 上午10:07:55
	 * @param @param mcuUuid
	 * @param @return
	 * @return List<Stress>
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月3日      杨杰     	   v1.0          create</p>
	 *
	 */
	List<CableMeter> getCableMeterByMcu(String mcuUuid);
	/**
	 * 
	 * 根据设备Uuid获取锚索计
	 * @date  2017年5月24日 上午11:35:44
	 * 
	 * @param deviceUuid
	 * @return
	 * CableMeter
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月24日     姚家俊      v1.0          create</p>
	 *
	 */
	CableMeter getCalbeMeterByDevice(String deviceUuid);
	
	void deleteCaleMeterBySP(String spUUid);
}
