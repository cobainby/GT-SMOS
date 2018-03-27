package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Section;

/**
 * 断面服务接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月30日     mohaolin       v1.0.0        create</p>
 *
 */
public interface ISectionService extends IBaseService<Section> {

	/**
	 * 新增断面
	 * @date  2017年3月30日 上午9:57:13
	 * @return void
	 * @param project
	 * @param section
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月30日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void addSection(Project project, Section section) throws Exception;

	/**
	 * 获取监测项对应的所有断面
	 * @date  2017年3月30日 上午10:36:39
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
	 * 更新断面
	 * @date  2017年4月7日 下午6:14:56
	 * @return void
	 * @param project
	 * @param section
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void updateSection(Project project, Section section) throws Exception;

	/**
	 * 删除断面 
	 * @date  2017年4月7日 下午6:20:47
	 * @return void
	 * @param sectionUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月7日     mohaolin      v1.0          create</p>
	 * @throws Exception 
	 *
	 */
	void deleteSection(String sectionUuid) throws Exception;

}
