package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.project.entity.Project;

/**
 * 工程数据库访问接口定义
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月19日     mohaolin       v1.0.0        create</p>
 *
 */
public interface IProjectDao extends IBaseDao<Project> {

	/**
	 * 获取所有工程
	 * @date  2017年3月19日 下午3:24:10
	 * @return List<Project>
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月19日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Project> getProjects();
	/**
	 * 获取账户对应的机构下的所有工程
	 * @date  2017年3月19日 下午3:20:39
	 * @return List<Project>
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月19日     mohaolin      v1.0          create</p>
	 * @param organ 
	 *
	 */
	List<Project> getProjects(Organ organ);
	/**
	 * 获得具有工程名的记录数
	 * @date  2017年3月20日 下午3:17:31
	 * @return long
	 * @param projectName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月20日     mohaolin      v1.0          create</p>
	 *
	 */
	long hasProjectName(String projectName);
	/**
	 * 获得具有工程编号的记录数
	 * @date  2017年3月20日 下午3:17:51
	 * @return long
	 * @param code
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月20日     mohaolin      v1.0          create</p>
	 *
	 */
	long hasProjectCode(String code);
	/**
	 * 获得本记录外具有工程名称的记录数
	 * @date  2017年3月20日 下午3:28:42
	 * @return long
	 * @param projectUuid
	 * @param projectName
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月20日     mohaolin      v1.0          create</p>
	 *
	 */
	long hasProjectName(String projectUuid, String projectName);
	/**
	 * 获得本记录外具有编码的工程记录数
	 * @date  2017年3月20日 下午3:29:04
	 * @return long
	 * @param projectUuid
	 * @param code
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月20日     mohaolin      v1.0          create</p>
	 *
	 */
	long hasProjectCode(String projectUuid, String code);
	/**
	 * 获取机构下所有工程
	 * @date  2017年3月31日 下午3:54:23
	 * @return List<Project>
	 * @param organUuid
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
	List<Project> getProjectsByOrgan(String organUuid);
	/**
	 * 获得机构下的工程数量 
	 * @date  2017年5月18日 下午5:16:17
	 * @return long
	 * @param organUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月18日     mohaolin      v1.0          create</p>
	 *
	 */
	long getProjectNumberByOrgan(String organUuid);
	/**
	 * 删除工程，删除工程时保留记录，将工程的available字段设为-1.
	 * @date  2017年11月13日 下午4:42:51
	 * @return void
	 * @param projectUuid
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年11月13日     mohaolin      v1.0          create</p>
	 *
	 */
	public void deleteProject(String projectUuid);

}
