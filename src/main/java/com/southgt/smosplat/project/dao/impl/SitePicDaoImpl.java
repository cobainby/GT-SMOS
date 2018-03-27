package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ISitePicDao;
import com.southgt.smosplat.project.entity.SitePic;
/**
 * 
 * 现场图片数据库实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年9月20日     姚家俊       v1.0.0        create</p>
 *
 */
@Repository("sitePicDao")
public class SitePicDaoImpl extends BaseDaoImpl<SitePic> implements ISitePicDao {
	
	private Criteria getCriteria(){
		return getSession().createCriteria(SitePic.class,"s").setCacheable(true);
	}
	@Override
	public List<SitePic> getAllSitePicByProjectUuid(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}

}
