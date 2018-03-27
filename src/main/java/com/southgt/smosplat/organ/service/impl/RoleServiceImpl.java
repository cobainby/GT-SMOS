package com.southgt.smosplat.organ.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IRoleDao;
import com.southgt.smosplat.organ.dao.IRoleRoleItemDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Role;
import com.southgt.smosplat.organ.entity.RoleItem;
import com.southgt.smosplat.organ.entity.RoleRoleItem;
import com.southgt.smosplat.organ.service.IRoleItemService;
import com.southgt.smosplat.organ.service.IRoleService;

/**
 * 角色服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {
	
	@Resource
	IRoleItemService roleItemService;
	
	@Resource
	IRoleRoleItemDao roleRoleItemDao;
	
	@Resource(name="roleDao")
	@Override
	public void setDao(IBaseDao<Role> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Role> getRoles() {
		return getDao().findAllEntity();
	}
	
	@Override
	public void saveRole(Role role) throws Exception {
		long number=((IRoleDao)getDao()).getRolesNumber(role.getRoleName());
		if(number>0){
			throw new Exception("角色名称已存在！");
		}
		//增加的时候指定一个数字，不能重复
		byte maxMark=((IRoleDao)getDao()).getMaxMarkNumber();
		role.setMark((byte) (maxMark+1));
		getDao().saveEntity(role);
	}

	@Override
	public void deleteRole(String roleUuid) throws Exception {
		Role role=getDao().getEntity(roleUuid);
		//删除角色之前先删除与之关联的角色权限关联记录
		List<RoleRoleItem> roleRoleItems=roleRoleItemDao.getRoleRoleItemsByRole(roleUuid);
		for (int i = 0; i < roleRoleItems.size(); i++) {
			roleRoleItemDao.deleteEntity(roleRoleItems.get(i));
		}
		getDao().deleteEntity(role);
	}

	@Override
	public List<RoleItem> getRoleItemsByAccount(Account account) {
		List<RoleItem> roleItems=null;
		//如果是超级管理员，所有权限都具有
		if(account.getLoginName().equals("superadmin")){
			roleItems=roleItemService.findAllEntity();
		}else{
			List<RoleRoleItem> roleRoleItems=account.getRole().getRoleRoleItems();
			roleItems=new ArrayList<RoleItem>();
			for (int i = 0; i < roleRoleItems.size(); i++) {
				roleItems.add(roleRoleItems.get(i).getRoleItem());
			}
		}
		return roleItems;
	}

	@Override
	public Role getRoleByMark(byte i) {
		return ((IRoleDao)getDao()).getRoleByMark(i);
	}

}
