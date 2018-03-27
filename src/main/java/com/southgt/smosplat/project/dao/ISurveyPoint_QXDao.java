package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.entity.SurveyPoint_QX;
/**
 * 
 * 建筑物倾斜数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月15日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISurveyPoint_QXDao extends IBaseDao<SurveyPoint_QX>{
	List<SurveyPoint_QX> getExistedSurveyPoint_QXsByCode(String projectUuid, String monitorItemUuid, String codeChar);
	
	List<SurveyPoint_QX> getSurveyPoint_QXs(String projectUuid, String monitorItemUuid);
	
	long getQXNumByCodeExceptSelf(String projectUuid, String monitorItemUuid, String codeChar,String code,String spUuid);
}
