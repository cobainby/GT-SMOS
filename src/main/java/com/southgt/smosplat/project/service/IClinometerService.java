package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Clinometer;

/**
 * 
 * 测斜仪服务层接口
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月14日     姚家俊       v1.0.0        create</p>
 *
 */
public interface IClinometerService extends IBaseService<Clinometer>{
	
	/**
	 * 
	 * 获取项目下所有的测斜仪
	 * @date  2018年1月14日 下午5:56:46
	 * 
	 * @param projectUuid
	 * @return
	 * List<Clinometer>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月14日     姚家俊      v1.0          create</p>
	 *
	 */
	List<Clinometer> getClinometerByProject(String projectUuid);
	
	/**
	 * 
	 * 获取测点下所有的测斜仪
	 * @date  2018年1月14日 下午5:57:00
	 * 
	 * @param sp_ZCUuid
	 * @return
	 * List<Clinometer>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月14日     姚家俊      v1.0          create</p>
	 *
	 */
	List<Clinometer> getClinometerBySP_cx(String sp_CXUuid);
	
	/**
	 * 
	 * 增加测斜仪
	 * @date  2018年1月14日 下午5:57:24
	 * 
	 * @param Clinometer
	 * @param deviceSn
	 * @param devType
	 * @param mcuUuid
	 * @param moduleNum
	 * @param channelNum
	 * @param devModelUuid
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月14日     姚家俊      v1.0          create</p>
	 *
	 */
	void addClinometer(Clinometer Clinometer,String deviceSn,String devType,String mcuUuid,String gap, String devModelUuid)throws Exception;
	
	/**
	 * 
	 * 更新一个测斜仪
	 * @date  2018年1月14日 下午5:59:33
	 * 
	 * @param Clinometer
	 * @param deviceSn
	 * @param devType
	 * @param mcuUuid
	 * @param depth
	 * @param devModelUuid
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月14日     姚家俊      v1.0          create</p>
	 *
	 */
	void updateClinometer(Clinometer Clinometer,String deviceSn,String devType,String mcuUuid,String gap,String devModelUuid)throws Exception;
	
	/**
	 * 
	 * 删除一个测斜仪
	 * @date  2018年1月14日 下午5:59:49
	 * 
	 * @param ClinometerUuid
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月14日     姚家俊      v1.0          create</p>
	 *
	 */
	void delClinometer(String ClinometerUuid)throws Exception;

	/**
	 * 根据device的id获得轴力计
	 * @date  2017年5月8日 下午2:34:12
	 * @return Clinometer
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
	Clinometer getClinometerByDevice(String deviceUuid);
	
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
	void deleteClinometerBySurveyPoint(String surveyPointUuid);
}
