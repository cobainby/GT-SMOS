package com.southgt.smosplat.project.service;

import java.util.List;

import com.southgt.smosplat.common.service.IBaseService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_SM;

public interface ISurveyPoint_SMService extends IBaseService<SurveyPoint_SM>{
	/**
	 * 
	 * 根据项目Uuid和监测项Uuid获取监测点
	 * @date  2017年6月22日 上午9:43:03
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
	List<SurveyPoint_SM> getSP_SMs(String projectUuid, String monitorItemUuid);
	
	List<SurveyPoint_SM> getSP_SMs(String projectUuid);
	
	/**
	 * 
	 * 增加监测点
	 * @date  2017年6月22日 上午9:59:33
	 * 
	 * @param project
	 * @param tempSP
	 * @param spCount
	 * @param beginNum
	 * @return
	 * @throws Exception
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
	List<SurveyPoint_SM> addSP_SM(Project project, SurveyPoint_SM tempSP, int spCount,int beginNum)throws Exception;
	
	/**
	 * 
	 * 更新监测点
	 * @date  2017年6月22日 上午9:59:55
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
	 * <p>2017年6月22日     姚家俊      v1.0          create</p>
	 *
	 */
	void updateSP_SM(Project project, SurveyPoint_SM surveyPoint) throws Exception;
	
	/**
	 * 
	 * 删除监测点
	 * @date  2017年6月22日 上午10:00:05
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
	 * <p>2017年6月22日     姚家俊      v1.0          create</p>
	 *
	 */
	void deleteSP_SM(String surveyPointUuid);
}
