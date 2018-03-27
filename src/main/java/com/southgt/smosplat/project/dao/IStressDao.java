package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.Stress;

public interface IStressDao extends IBaseDao<Stress> {

	List<Stress> getStressByProject(String  projectUuid);
	
	List<Stress> getStressBySP_zc(String  sp_ZCUuid);
	
	long getStressNumByConditions(String sp_ZCUuid,String name,String devCode );
	
	long getStressNumByConditionsSelfException(String sp_ZCUuid,String name,String devCode,String stressUuid);

	/**
	 * 根据设备id获得轴力计 
	 * @date  2017年5月8日 下午2:35:02
	 * @return Stress
	 * @param deviceUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月8日     mohaolin      v1.0          create</p>
	 *
	 */
	Stress getStressByDevice(String deviceUuid);
	
	/**
	 * 删除监测点下的轴力计和与其关联的设备
	 * 
	 * @date  2017年5月15日 上午11:50:34
	 * @param @param spUuid
	 * @return void
	 * @throws null
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月15日      杨杰     	   v1.0          create</p>
	 *
	 */
	void deleteStressBySurveyPoint(String spUuid);
}
