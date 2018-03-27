package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.ISitePicDao;
import com.southgt.smosplat.project.entity.SitePic;
import com.southgt.smosplat.project.service.ISitePicService;
/**
 * 
 * 现场图片类服务实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年9月20日     姚家俊       v1.0.0        create</p>
 *
 */
@Service("sitePicService")
public class SitePicServiceImpl extends BaseServiceImpl<SitePic> implements ISitePicService{

	@Resource(name="sitePicDao")
	@Override
	public void setDao(IBaseDao<SitePic> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SitePic> getAllSitePicByProjectUuid(String projectUuid) {
		List<SitePic> sitePics = ((ISitePicDao)getDao()).getAllSitePicByProjectUuid(projectUuid);
		return sitePics;
	}
}
