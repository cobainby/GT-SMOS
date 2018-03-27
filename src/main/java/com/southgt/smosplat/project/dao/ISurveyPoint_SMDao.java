package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_SM;

/**
 * 
 * 周边建筑物竖向位移数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月22日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISurveyPoint_SMDao extends IBaseDao<SurveyPoint_SM>{
	/**
	 * 
	 * 根据项目uuid,监测项uuid,codeChar获取现有的监测点
	 * @date  2017年6月22日 上午9:47:18
	 * 
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @param codeChar
	 * @return
	 * List<SurveyPoint_SM>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月22日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_SM> getExistedSurveyPoint_SMsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	/**
	 * 
	 * 根据项目uuid,监测项uuid获取监测点
	 * @date  2017年6月22日 上午9:48:01
	 * 
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @return
	 * List<SurveyPoint_SM>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月22日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_SM> getSurveyPoint_SMs(String projectUuid, String monitorItemUuid);
	
	/**
	 * 
	 * 根据项目uuid,监测项uuid获取监测点
	 * @date  2017年6月22日 上午9:48:01
	 * 
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @return
	 * List<SurveyPoint_SM>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月22日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_SM> getSurveyPoint_SMs(String projectUuid);
	
	/*
	 * 获取相同codechar和相同code的测点数(除本身外)
	 */
	long getSMNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar,String code,String spUuid);
}
