package com.southgt.smosplat.project.dao;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectReport;

public interface IProjectReportDao extends IBaseDao<ProjectReport> {

	/**
	 * 获取项目下的报告参数模板
	 * 
	 * @date  2017年9月12日 上午9:25:49
	 * @param @param projectUuid
	 * @param @return
	 * @return ProjectReport
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月12日      杨杰     	   v1.0          create</p>
	 *
	 */
	ProjectReport getProjectReportByProject(String projectUuid);

	/**
	 * 项目下是否存在报告参数模板
	 * 
	 * @date  2017年9月12日 上午9:28:52
	 * @param @param project
	 * @param @return
	 * @return boolean
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月12日      杨杰     	   v1.0          create</p>
	 *
	 */
	boolean existOrNotProjectReport(String projectUuid);
}
