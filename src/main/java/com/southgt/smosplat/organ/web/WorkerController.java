package com.southgt.smosplat.organ.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Worker;
import com.southgt.smosplat.organ.service.IAccountProjectService;
import com.southgt.smosplat.organ.service.IAccountService;
import com.southgt.smosplat.organ.service.IWorkerService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IProjectService;

/**
 * 
 * 人员管理控制器类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年2月27日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class WorkerController {
	
	@Resource
	IWorkerService workerService;
	
	@Resource
	IAccountService accountService;
	
	@Resource
	IAccountProjectService accountProjectService;
	
	@Resource
	IProjectService projectService;
	
	/**
	 * 
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
	 *
	 */
	@RequestMapping("/worker")
	public String toWorker(){
		return "organ/view/worker";
	}
	
	/**
	 * 加载账户管理页面 
	 * @date  2017年2月27日 下午7:00:03
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
	 *
	 */
	@RequestMapping("/account")
	public String toAccount(){
		return "organ/view/account";
	}
	
	/**
	 * 获取所有人员
	 * @date  2017年3月6日 上午11:33:23
	 * @return String
	 * @param options
	 * @param organUuid
	 * @param session
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/getWorkers")
	@ResponseBody
	public String getWorkers(PageCondition options,String organUuid,HttpSession session){
		Account account=(Account) session.getAttribute("account");
		Map<String, Object> map=new HashMap<String,Object>();
		List<Worker> workers=new ArrayList<Worker>();
		if(account.getLoginName().equals("superadmin")){
			//如果超级管理员的情况下，有organUuid参数，则说明要根据机构id进行过滤操作，否则获取所有
			if(organUuid==null||organUuid.equals("0")){
				workers=workerService.getWorkers(options);
			}else{
				workers=workerService.getWorkers(options,organUuid);
			}
		}else{
			//如果不是超级管理员从session中获得当前机构信息
			organUuid=(String) session.getAttribute("currentOrganUuid");
			workers=workerService.getWorkers(options,organUuid);
		}
		map.put("total", options.getTotalCount());
		map.put("rows", workers);
		//避免死循环
		for (int i = 0; i < workers.size(); i++) {
			if(workers.get(i).getAccount()!=null){
				workers.get(i).getAccount().setWorker(null);
			}
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/getWorkersByOrgan")
	@ResponseBody
	public String getWorkersByOrgan(String organUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Worker> workers=new ArrayList<Worker>();
		workers=workerService.getWorkersByOrgan(organUuid);
		map.put("workers", workers);
		//避免死循环
		for (int i = 0; i < workers.size(); i++) {
			if(workers.get(i).getAccount()!=null){
				workers.get(i).getAccount().setWorker(null);
			}
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getWorkerByAccount")
	@ResponseBody
	public String getWorkersByAccount(String accountUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Worker worker=workerService.getWorkerByAccount(accountUuid);
		if(worker.getAccount()!=null){
			worker.getAccount().setWorker(null);
		}
		map.put("worker", worker);
		
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getWorkerByWorkerUuid")
	@ResponseBody
	public String getWorkerByWorkerUuid(String workerUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		try{
			Worker worker=workerService.getEntity(workerUuid);
			if(worker.getAccount()!=null){
				worker.getAccount().setWorker(null);
			}
			map.put("worker", worker);
			map.put("result", 0);
		}catch(Exception ex){
			map.put("result", -1);
			map.put("msg", ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/addWorker",method=RequestMethod.POST)
	@ResponseBody
	public String addWorker(Worker worker,Boolean hasAccount,Account account,String selectOrganUuid,HttpSession session){
		//当前登录的账户
		Account loginAccount=(Account) session.getAttribute("account");
		Map<String, Object> map=new HashMap<String,Object>();
		//获得当前机构id
		String curOrganUuid=(String) session.getAttribute("currentOrganUuid");
		if(loginAccount.getAccountName().equals("超级管理员")){
			curOrganUuid=selectOrganUuid;
		}
		try {
			workerService.addWorker(worker,hasAccount,account,curOrganUuid,loginAccount);
			//避免序列化问题
			if(worker.getAccount()!=null){
				worker.getAccount().setWorker(null);
			}
			map.put("result", 0);
			map.put("entity", worker);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/deleteWorker",method=RequestMethod.POST)
	@ResponseBody
	public String deleteWorker(String workerUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			workerService.deleteWorker(workerUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/updateWorker",method=RequestMethod.POST)
	@ResponseBody
	public String updateWorker(String workerJson) {
		Map<String, Object> map=new HashMap<String,Object>();
		Worker worker=(Worker) JsonUtil.jsonToBean(workerJson, Worker.class);
		try {
			workerService.updateWorker(worker);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	@RequestMapping(value="/updateAccount",method=RequestMethod.POST)
	@ResponseBody
	public String updateAccount(String accountUuid,String accountName,String loginName,Boolean resetPassword,String password) {
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			workerService.updateAccount(accountUuid,accountName,loginName,resetPassword,password);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	@RequestMapping(value="/getAccountById",method=RequestMethod.POST)
	@ResponseBody
	public String getAccountById(String accountUuid) {
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			Account account=workerService.getAccountById(accountUuid);
			//不需要其他信息
			account.setRole(null);
			account.getWorker().setAccount(null);
			account.getWorker().setOrgan(null);
			map.put("entity", account);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	//手机端，电脑端都有调这个接口
	@RequestMapping(value="/getProjectsByAccount",method=RequestMethod.POST)
	@ResponseBody
	public String getProjectsByAccount(String accountUuid) {
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			Account account=accountService.getEntity(accountUuid);
			String organUuid=account.getWorker().getOrgan().getOrganUuid();
			List<Project> projects=projectService.getProjectsByOrgan(organUuid);
			for (Project project : projects) {
				project.setOrgan(null);
				project.setProjectMonitorItems(null);
			}
			List<Project> projectsCanView=accountProjectService.getProjectsByAccount(account.getAccountUuid());
			for (Project project : projectsCanView) {
				project.setOrgan(null);
				project.setProjectMonitorItems(null);
			}
			map.put("projectsCanView", projectsCanView);
			map.put("allProjects",projects);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/updateProjectForAccount",method=RequestMethod.POST)
	@ResponseBody
	public String updateProjectForAccount(String accountUuid,String projectUuids){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			accountProjectService.updateProjectForAccount(accountUuid,projectUuids);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
}
