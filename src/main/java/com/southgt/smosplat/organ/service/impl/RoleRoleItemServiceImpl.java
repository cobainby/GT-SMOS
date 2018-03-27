package com.southgt.smosplat.organ.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IRoleDao;
import com.southgt.smosplat.organ.dao.IRoleItemDao;
import com.southgt.smosplat.organ.dao.IRoleRoleItemDao;
import com.southgt.smosplat.organ.entity.Role;
import com.southgt.smosplat.organ.entity.RoleItem;
import com.southgt.smosplat.organ.entity.RoleRoleItem;
import com.southgt.smosplat.organ.service.IRoleRoleItemService;

/**
 * 角色和权限关联关系服务实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月10日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("roleRoleItemService")
public class RoleRoleItemServiceImpl extends BaseServiceImpl<RoleRoleItem> implements IRoleRoleItemService {

	@Resource
	IRoleDao roleDao;
	
	@Resource
	IRoleItemDao roleItemDao;
	
	@Resource(name="roleRoleItemDao")
	@Override
	public void setDao(IBaseDao<RoleRoleItem> dao) {
		super.setDao(dao);
	}
	
	@Override
	public List<RoleItem> getRoleItemsByRole(String roleUuid) {
		List<RoleRoleItem> roleRoleItems=((IRoleRoleItemDao)getDao()).getRoleRoleItemsByRole(roleUuid);
		List<RoleItem> roleItems=new ArrayList<RoleItem>();
		for (int i = 0; i < roleRoleItems.size(); i++) {
			RoleItem tempRoleItem=new RoleItem();
			tempRoleItem.setRoleItemUuid(roleRoleItems.get(i).getRoleItem().getRoleItemUuid());
			tempRoleItem.setRoleItemName(roleRoleItems.get(i).getRoleItem().getRoleItemName());
			tempRoleItem.setNumber(roleRoleItems.get(i).getRoleItem().getNumber());
			tempRoleItem.setRoleItemDesc(roleRoleItems.get(i).getRoleItem().getRoleItemDesc());
			roleItems.add(tempRoleItem);
		}
		return roleItems;
	}

	@Override
	public void updateRoleItemForRole(String roleUuid, String roleItemUuids) {
		//获得已有的权限集合
		List<RoleRoleItem> roleRoleItems=((IRoleRoleItemDao)getDao()).getRoleRoleItemsByRole(roleUuid);
		String[] roleItemUuidsArray=roleItemUuids.split(",");
		//根据现在的权限集合判断，如果现在的权限集合没有该权限，先从已有的删掉
		for (int i = 0; i < roleRoleItems.size(); i++) {
			boolean finded=false;
			for (int j = 0; j < roleItemUuidsArray.length; j++) {
				if(roleRoleItems.get(i).getRoleItem().getRoleItemUuid().equals(roleItemUuidsArray[j])){
					//找到后跳出循环
					finded=true;
					break;
				}
			}
			if(!finded){
				//没有找到，从已有权限中删除权限
				((IRoleRoleItemDao)getDao()).deleteEntity(roleRoleItems.get(i));
			}
		}
		//根据现有的权限集合，如果已有的权限集合中没有该权限，则增加到权限
		for (int i = 0; i < roleItemUuidsArray.length; i++) {
			boolean finded=false;
			for (int j = 0; j < roleRoleItems.size(); j++) {
				//如果在已有的权限中找到，则跳过
				if(roleItemUuidsArray[i].equals(roleRoleItems.get(j).getRoleItem().getRoleItemUuid())){
					finded=true;
					break;
				}
			}
			//没有找到，则增加权限
			if(!finded){
				Role role=roleDao.getEntity(roleUuid);
				RoleItem roleItem=roleItemDao.getEntity(roleItemUuidsArray[i]);
				RoleRoleItem roleRoleItem=new RoleRoleItem(role, roleItem);
				((IRoleRoleItemDao)getDao()).saveEntity(roleRoleItem);
			}
		}
		
	}

	
}
