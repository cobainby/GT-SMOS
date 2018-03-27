package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;

/**
 * 监测点数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月31日     mohaolin       v1.0.0        create</p>
 *
 */
public interface ISurveyPointDao extends IBaseDao<SurveyPoint_WYS> {

	/**
	 * 根据编号的英文部分获得监测点列表 
	 * @date  2017年3月31日 上午10:49:26
	 * @return List<SurveyPoint>
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @param codeChar
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月31日     mohaolin      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_WYS> getExistedSurveyPointsByCode(String projectUuid, String monitorItemUuid, String codeChar);

	/**
	 * 获取特定工程下的特定监测项的所有监测点
	 * @date  2017年4月1日 上午10:01:33
	 * @return List<SurveyPoint>
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月1日     mohaolin      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_WYS> getSurveyPoints(String projectUuid, String monitorItemUuid);

	/**
	 * 获得使用特定预警信息的监测点的数量
	 * @date  2017年4月7日 下午3:27:37
	 * @return long
	 * @param warningUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 *
	 */
	long getSPNumbersByWarning(String warningUuid);

	/**
	 * 获得使用特定断面的监测点的数量
	 * @date  2017年4月7日 下午6:23:15
	 * @return long
	 * @param sectionUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 *
	 */
	long getSPNumbersBySection(String sectionUuid);

}
