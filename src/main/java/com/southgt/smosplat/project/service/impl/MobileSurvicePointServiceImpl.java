package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IMobileSurveyPointDao;
import com.southgt.smosplat.project.entity.Mobile_SurveyPoint;
import com.southgt.smosplat.project.service.IMobileSurveyPointService;

/**
 * 手机监测点服务实现类
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年11月14日     YANG       v1.0.0        create</p>
 *
 */
@Service("mobileSPService")
public class MobileSurvicePointServiceImpl extends BaseServiceImpl<Mobile_SurveyPoint> implements IMobileSurveyPointService {

	@Resource(name="mobileSPDao")
	@Override
	public void setDao(IBaseDao<Mobile_SurveyPoint> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Mobile_SurveyPoint> getSurveyPointsByProjectAndMonitorItem(String projectUuid, String monitorItemUuid) {
		List<Mobile_SurveyPoint> spList=((IMobileSurveyPointDao)getDao()).getSPsByProjectAndMonitorItem(projectUuid, monitorItemUuid);
		return spList;
	}

	@Override
	public void operateSurveyPoints(List<Mobile_SurveyPoint> spList) {
		if(spList.size()>0){
			((IMobileSurveyPointDao)getDao()).deleteSPsByProjectAndMonitorItem(spList.get(0).getProjectUuid(), spList.get(0).getMonitorItemUuid());
			for(int i=0;i<spList.size();i++){
				Mobile_SurveyPoint sp=spList.get(i);
				sp.setOrderCreate(i);
				((IMobileSurveyPointDao)getDao()).saveEntity(sp);
			}
		}
		
	}
	

}
