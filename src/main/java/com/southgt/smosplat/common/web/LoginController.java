package com.southgt.smosplat.common.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.RoleItem;
import com.southgt.smosplat.organ.entity.Worker;
import com.southgt.smosplat.organ.service.IRoleService;
import com.southgt.smosplat.organ.service.IWorkerService;

@Controller
public class LoginController {
	
	@Resource
	IRoleService roleService;
	
	@Resource
	IWorkerService workerService;
	
	@RequestMapping("/loginView")
	public String toLoginView(){
		return "common/view/login";
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public String login(String loginName,String password,HttpServletRequest request){
		//将会话清除，使得登录验证成功时重新生成session，避免出现会话标识未更新问题
		HttpSession session=request.getSession(false);
		if(session!=null){
			System.out.println(session.getId());
			session.invalidate();
			request.getCookies()[0].setMaxAge(0);
		}
		Map<String,Object> map=new HashMap<String, Object>();
		Account account=workerService.validateAccount(loginName,password);
		//如果没有匹配的账户
		if(account==null){
			map.put("result", -1);
			map.put("msg", "登录名或密码错误！");
			String s=JsonUtil.beanToJson(map);
			return s;
		}else{
			//用户登录成功后重新建立session
			session=request.getSession(true);
			System.out.println(session.getId());
			//在session中保存用户信息
			session.setAttribute("account", account);
			//登录成功之后通过登陆的账号获取对应的权限，并保存到session当中，当页面加载之前先加载权限，然后做页面的控制处理
			List<RoleItem> tempRoleItems=roleService.getRoleItemsByAccount(account);
			List<RoleItem> roleItemsForAccount=new ArrayList<RoleItem>();
			for (int i = 0; i < tempRoleItems.size(); i++) {
				RoleItem roleItem=new RoleItem();
				roleItem.setRoleItemName(tempRoleItems.get(i).getRoleItemName());
				roleItem.setNumber(tempRoleItems.get(i).getNumber());
				roleItem.setRoleItemDesc(tempRoleItems.get(i).getRoleItemDesc());
				roleItemsForAccount.add(roleItem);
			}
			request.getSession().setAttribute("roleItemsForAccount", roleItemsForAccount);
			map.put("result", 0);
			map.put("account", account);
		}
		//不需要人员的关联信息
		Worker worker=account.getWorker();
		account.setWorker(null);
		String s=JsonUtil.beanToJson(map);
		account.setWorker(worker);
		return s;
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	public String logout(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String, Object>();
		try{
		//清空session
		Enumeration em = request.getSession().getAttributeNames();
		  while(em.hasMoreElements()){
		   request.getSession().removeAttribute(em.nextElement().toString());
		   map.put("result", 0);
		  }
		}catch(Exception ex){
			map.put("result", -1);
			map.put("msg", ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
