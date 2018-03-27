package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.IMobileBasePointDao;
import com.southgt.smosplat.project.entity.Mobile_BasePoint;
import com.southgt.smosplat.project.service.IMobileBasePointService;

/**
 * 手机基准点服务实现类
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
@Service("mobileBasePointService")
public class MobileBasePointServiceImpl extends BaseServiceImpl<Mobile_BasePoint> implements IMobileBasePointService {

	@Resource(name="mobileBasePointDao")
	@Override
	public void setDao(IBaseDao<Mobile_BasePoint> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Mobile_BasePoint> getBPsByProjectAndMonitorItem(String projectUuid, String monitorItemUuid) {
		List<Mobile_BasePoint> bpList=((IMobileBasePointDao)getDao()).getBPByProjectAndMonitorItem(projectUuid, monitorItemUuid);
		return bpList;
	}

	@Override
	public void operateBasePoint(List<Mobile_BasePoint> bpList) {
		if(bpList.size()>0){
			((IMobileBasePointDao)getDao()).deleteBPByProjectAndMonitorItem(bpList.get(0).getProjectUuid(), bpList.get(0).getMonitorItemUuid());
			for(int i=0;i<bpList.size();i++){
				Mobile_BasePoint bp=bpList.get(i);
				bp.setOrderCreate(i);
				((IMobileBasePointDao)getDao()).saveEntity(bp);
			}
		}
		
	}


}
