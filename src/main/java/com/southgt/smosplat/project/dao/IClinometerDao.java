package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Clinometer;

/**
 * 
 * 测斜仪数据库dao接口
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月14日     姚家俊       v1.0.0        create</p>
 *
 */
public interface IClinometerDao extends IBaseDao<Clinometer>{
	
	/**
	 * 
	 * 获取项目下所有的测斜仪
	 * @date  2018年1月14日 下午5:33:13
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
	List<Clinometer> getClinometerByProject(String  projectUuid);
	
	/**
	 * 
	 * 获取测点下所有的测斜仪
	 * @date  2018年1月14日 下午5:33:29
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
	List<Clinometer> getClinometerBySP_cx(String  sp_CXUuid);
	
	long getClinometerNumByConditions(String sp_CXUuid,String name,String devCode );
	
	long getClinometerNumByConditionsSelfException(String sp_CXUuid,String name,String devCode,String clinometerUuid);
	/**
	 * 
	 * 通过设备查找测斜仪
	 * @date  2018年1月14日 下午5:30:52
	 * 
	 * @param deviceUuid
	 * @return
	 * Clinometer
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
	Clinometer getClinometerByDevice(String deviceUuid);
	
	/**
	 * 
	 * 删除监测点下的测斜仪和与其关联的设备
	 * @date  2018年1月14日 下午5:31:24
	 * 
	 * @param spUuid
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
	void deleteClinometerBySurveyPoint(String spUuid);
}
