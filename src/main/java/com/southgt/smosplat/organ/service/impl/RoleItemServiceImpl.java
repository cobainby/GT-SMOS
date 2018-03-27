package com.southgt.smosplat.organ.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IRoleItemDao;
import com.southgt.smosplat.organ.dao.IRoleRoleItemDao;
import com.southgt.smosplat.organ.entity.RoleItem;
import com.southgt.smosplat.organ.entity.RoleRoleItem;
import com.southgt.smosplat.organ.service.IRoleItemService;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("roleItemService")
public class RoleItemServiceImpl extends BaseServiceImpl<RoleItem> implements IRoleItemService {

	@Resource
	IRoleItemDao roleItemDao;
	
	@Resource
	IRoleRoleItemDao roleRoleItemDao;
	
	@Resource(name="roleItemDao")
	@Override
	public void setDao(IBaseDao<RoleItem> dao) {
		super.setDao(dao);
	}
	
	@Override
	public List<RoleItem> getRoleItems() {
		return getDao().findAllEntity();
	}

	@Override
	public void saveRoleItem(RoleItem roleItem) throws Exception {
		long number=((IRoleItemDao)getDao()).getRoleItemsNumberByName(roleItem.getRoleItemName());
		if(number>0){
			throw new Exception("权限名称已存在！");
		}
		long number1=((IRoleItemDao)getDao()).getRoleItemsNumberByNumber(roleItem.getNumber());
		if(number1>0){
			throw new Exception("权限序号已存在！");
		}
		((IRoleItemDao)getDao()).saveEntity(roleItem);
	}

	@Override
	public void deleteRoleItem(String roleItemUuid) throws Exception {
		//先删除与之相关的角色信息
		List<RoleRoleItem> roleItems=roleRoleItemDao.getRoleRoleItemsByRoleItem(roleItemUuid);
		for (RoleRoleItem roleRoleItem : roleItems) {
			roleItemDao.deleteEntity(roleRoleItem.getRoleItem());
		}
		RoleItem roleItem=getDao().getEntity(roleItemUuid);
		getDao().deleteEntity(roleItem);
	}

}
