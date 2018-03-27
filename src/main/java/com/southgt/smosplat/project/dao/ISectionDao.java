package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Section;

/**
 * 断面数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月30日     mohaolin       v1.0.0        create</p>
 *
 */
public interface ISectionDao extends IBaseDao<Section> {

	/**
	 * 获得具有相同名称的记录数
	 * @date  2017年3月30日 上午10:04:02
	 * @return long
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @param sectionName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月30日     mohaolin      v1.0          create</p>
	 *
	 */
	long getSectionsNumber(String projectUuid, String monitorItemUuid, String sectionName);

	/**
	 * 获取所有断面
	 * @date  2017年3月30日 上午10:39:36
	 * @return List<Section>
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
	 * <p>2017年3月30日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Section> getSections(String projectUuid, String monitorItemUuid);

	/**
	 * 获得除了自己以外具有相同名称的记录数
	 * @date  2017年4月7日 下午6:16:43
	 * @return long
	 * @param projectUuid
	 * @param monitorItemUuid
	 * @param sectionName
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
	long getSectionsNumberExceptSelf(String projectUuid, String monitorItemUuid, String sectionName,
			String sectionUuid);

}
