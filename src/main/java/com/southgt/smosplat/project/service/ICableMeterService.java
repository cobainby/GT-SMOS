package com.southgt.smosplat.project.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.CableMeter;

public interface ICableMeterService extends IBaseService<CableMeter> {

	/**
	 * 根据项目获取锚索计
	 * 
	 * @date  2017年5月3日 上午10:23:34
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
	List<CableMeter> getCableMeterByProject(String projectUuid);
	
	/**
	 * 获取锚索监测点下的锚索设备
	 * 
	 * @date  2017年5月3日 上午10:23:54
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
	List<CableMeter> getCableMeterBySP_mt(String sp_MTUuid);
	
	void addCableMeter(CableMeter cableMeter,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid)throws Exception;
	
	void updateCableMeter(CableMeter cableMeter,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid)throws Exception;
	
	void delCableMeter(String cableMeterUuid)throws Exception;
	
	void delCableMeterBySurveyPoint(String surveyPointUuid);

	/**
	 * 根据mcu获取mcu连接的锚索设备
	 * 
	 * @date  2017年5月3日 上午10:12:10
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
	 * 根据设备获取锚索计
	 * @date  2017年5月24日 上午11:34:21
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
	CableMeter getCableMeterByDevice(String deviceUuid);
}
