package com.southgt.smosplat.organ.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Role;
import com.southgt.smosplat.organ.entity.RoleItem;
import com.southgt.smosplat.organ.service.IAccountService;
import com.southgt.smosplat.organ.service.IRoleItemService;
import com.southgt.smosplat.organ.service.IRoleRoleItemService;
import com.southgt.smosplat.organ.service.IRoleService;

/**
 * 角色管理控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月7日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class RoleController {
	
	@Resource
	IRoleService roleService;
	
	@Resource
	IRoleItemService roleItemService;
	
	@Resource
	IRoleRoleItemService roleRoleItemService;
	
	@Resource
	IAccountService accountService;
	
	/**
	 * 加载人员管理页面
	 * @date  2017年2月27日 下午2:44:39
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年2月27日     mohaolin      v1.0          create</p>
	 * @throws IOException 
	 *
	 */
	@RequestMapping("/role")
	public String toRole(HttpSession session,HttpServletResponse response,HttpServletRequest request) throws IOException{
		Account account=(Account) session.getAttribute("account");
		//只有超级管理员才能进入该页面
		if(account.getLoginName().equals("superadmin")){
			return "organ/view/role";
		}else{//非管理员跳过此页，进入首页
			return "common/view/index";
		}
	}
	
	@RequestMapping("/getRoles")
	@ResponseBody
	public String getRoles(HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Role> roles=roleService.getRoles();
		map.put("rows", roles);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping(value="/addRole",method=RequestMethod.POST)
	@ResponseBody
	public String addRole(Role role){
		//只有超级管理员才能新建机构
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			roleService.saveRole(role);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	@RequestMapping(value="/deleteRole",method=RequestMethod.POST)
	@ResponseBody
	public String deleteRole(String roleUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			roleService.deleteRole(roleUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getRoleItems")
	@ResponseBody
	public String getRoleItems(HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		List<RoleItem> roleItems=roleItemService.getRoleItems();
		map.put("rows", roleItems);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping(value="/addRoleItem",method=RequestMethod.POST)
	@ResponseBody
	public String addRoleItem(RoleItem roleItem){
		//只有超级管理员才能新建机构
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			roleItemService.saveRoleItem(roleItem);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/deleteRoleItem",method=RequestMethod.POST)
	@ResponseBody
	public String deleteRoleItem(String roleItemUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			roleItemService.deleteRoleItem(roleItemUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/getRoleItemsByRole",method=RequestMethod.POST)
	@ResponseBody
	public String getRoleItemsByRole(String roleUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<RoleItem> roleItems=roleRoleItemService.getRoleItemsByRole(roleUuid);
			map.put("result", 0);
			map.put("roleItems", roleItems);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/updateRoleItemForRole",method=RequestMethod.POST)
	@ResponseBody
	public String updateRoleItemForRole(String roleUuid,String roleItemUuids){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			roleRoleItemService.updateRoleItemForRole(roleUuid,roleItemUuids);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getRoleItemsFromSession",method=RequestMethod.POST)
	@ResponseBody
	public String getRoleItemsFromSession(HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Account account=(Account) session.getAttribute("account");
		if(account.getLoginName().equals("superadmin")){
			//超级管理员，返回-1，不做权限控制
			map.put("result", -1);
		}else{
			List<RoleItem> roleItemsForAccount=(List<RoleItem>) session.getAttribute("roleItemsForAccount");
			map.put("result", 0);
			map.put("roleItems", roleItemsForAccount);
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getRoleByAccount",method=RequestMethod.POST)
	@ResponseBody
	public String getRoleByAccount(String accountUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Account account=accountService.getEntity(accountUuid);
		Role role=roleService.getEntity(account.getRole().getRoleUuid());
			map.put("result", 0);
			map.put("role", role);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
