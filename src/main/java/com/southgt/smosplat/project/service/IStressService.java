package com.southgt.smosplat.project.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Stress;

public interface IStressService extends IBaseService<Stress> {

	List<Stress> getStressByProject(String projectUuid);
	
	List<Stress> getStressBySP_zc(String sp_ZCUuid);
	
	void addStress(Stress stress,String deviceSn,String devType,String mcuUuid,int moduleNum,int channelNum,String devModelUuid)throws Exception;
	
	void updateStress(Stress stress,String deviceSn,String devType,String mcuUuid,int moduleNum,int channelNum,String devModelUuid)throws Exception;
	
	void delStress(String stressUuid)throws Exception;

	/**
	 * 根据device的id获得轴力计
	 * @date  2017年5月8日 下午2:34:12
	 * @return Stress
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
	Stress getStressByDevice(String deviceUuid);
	
	/**
	 * 删除监测点下的轴力实体以及关联的设备
	 * 
	 * @date  2017年5月15日 上午11:54:16
	 * @param @param SurveyPointUuid
	 * @return void
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月15日      杨杰     	   v1.0          create</p>
	 *
	 */
	void deleteStressBySurveyPoint(String surveyPointUuid);
}
