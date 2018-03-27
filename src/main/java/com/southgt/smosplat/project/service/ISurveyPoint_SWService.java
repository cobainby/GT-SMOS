package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;

public interface ISurveyPoint_SWService extends IBaseService<SurveyPoint_SW> {
	
	List<SurveyPoint_SW> addSP_SW(Project project, SurveyPoint_SW tempSP,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid) throws Exception;
	
	//批量增加
	List<SurveyPoint_SW> addSP_SW(Project project, SurveyPoint_SW tempSP, int spCount);
	
	List<SurveyPoint_SW> getSP_SWs(String projectUuid, String monitorItemUuid);
	
	void updateSP_SW(Project project, SurveyPoint_SW surveyPoint,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid) throws Exception;
	
	void deleteSP_SW(String surveyPointUuid);
	
	List<Device> getDeviceSnByItemName(int number,String stressType);

	/**
	 * 根据设备id获得水位计
	 * @date  2017年5月8日 下午2:48:59
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
	 * 找到工程下所有水位计
	 * @date  2017年5月8日 下午3:30:55
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
