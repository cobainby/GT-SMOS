package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;

public interface ISurveyPoint_SWDao extends IBaseDao<SurveyPoint_SW> {
	
	List<SurveyPoint_SW> getExistedSurveyPoint_SWsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_SW> getSurveyPoint_SWs(String projectUuid, String monitorItemUuid);
	
	long getSP_SWByCode(String projectUuid, String monitorItemUuid, String code);
	
	long getSP_SWByCodeExceptSelf(String projectUuid, String monitorItemUuid, String code,String spUuid);
	
	List<Device> getDeviceSnByItemName(String devTypeName,String monitorItem);

	/**
	 * 根据设备id获得水位计
	 * @date  2017年5月8日 下午2:50:10
	 * @return SurveyPoint_SW
	 * @param deviceUuid
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
	SurveyPoint_SW getSPSWByDevice(String deviceUuid);

	/**
	 * 获得项目下所有水位计
	 * @date  2017年5月8日 下午3:32:59
	 * @return List<SurveyPoint_SW>
	 * @param projectUuid
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
	List<SurveyPoint_SW> getSP_SWsByProject(String projectUuid);
}
