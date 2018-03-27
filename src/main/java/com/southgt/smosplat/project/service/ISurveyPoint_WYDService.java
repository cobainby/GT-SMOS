package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;

public interface ISurveyPoint_WYDService extends IBaseService<SurveyPoint_WYD> {

	List<SurveyPoint_WYD> getSP_WYDs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_WYD> getSP_WYDs(String projectUuid);
	/**
	 * 
	 * 增加监测点
	 * @date  2017年8月23日 上午10:29:26
	 * 
	 * @param project
	 * @param tempSP
	 * @param spCount
	 * @param beginNum
	 * @return
	 * List<SurveyPoint_WYD>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月23日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SurveyPoint_WYD> addSP_WYD(Project project, SurveyPoint_WYD tempSP, int spCount, int beginNum) throws Exception;
	/**
	 * 
	 * 更新监测点
	 * @date  2017年8月23日 上午10:36:30
	 * 
	 * @param project
	 * @param surveyPoint
	 * @throws Exception
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月23日     姚家俊      v1.0          create</p>
	 *
	 */
	void updateSP_WYD(Project project, SurveyPoint_WYD surveyPoint) throws Exception;
	/**
	 * 	
	 * 删除监测点
	 * @date  2017年8月23日 上午10:37:13
	 * 
	 * @param surveyPointUuid
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月23日     姚家俊      v1.0          create</p>
	 *
	 */
	void deleteSP_WYD(String surveyPointUuid);
}
