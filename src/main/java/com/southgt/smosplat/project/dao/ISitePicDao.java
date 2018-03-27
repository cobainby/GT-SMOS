package com.southgt.smosplat.project.dao;

import java.util.List;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.project.entity.SitePic;
/**
 * 
 * 现场图片数据库接口
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年9月20日     姚家俊       v1.0.0        create</p>
 *
 */
public interface ISitePicDao extends IBaseDao<SitePic>{
	/**
	 * 
	 * 根据项目id获取所有的现场图片信息
	 * @date  2017年9月20日 下午3:21:20
	 * 
	 * @param projectuUid
	 * @return
	 * List<SitePic>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月20日     姚家俊      v1.0          create</p>
	 *
	 */
	List<SitePic> getAllSitePicByProjectUuid(String projectUuid);
}
