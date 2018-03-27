package com.southgt.smosplat.organ.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.organ.entity.Worker;
import com.southgt.smosplat.organ.service.IAccountService;
import com.southgt.smosplat.organ.service.IOrganService;
import com.southgt.smosplat.organ.service.IWorkerService;
import com.sun.net.httpserver.HttpContext;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

/**
 * 机构管理控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年2月22日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class OrganController {
	
	@Resource
	private IOrganService organService;
	
	@Resource
	IAccountService accountService;
	
	@Resource 
	IWorkerService workerService;
	/**
	 * 
	 *机构管理的主体界面 
	 * @date  2017年3月17日 上午11:30:47
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月17日     白杨      v1.0          create</p>
	 * @throws IOException 
	 *
	 */
	@RequestMapping("/organIndex")
	public String toOrganIndex(HttpServletRequest request,HttpServletResponse response) throws IOException{
		return "organ/view/organIndex";
	}
	/**
	 * 加载机构管理页面
	 * @date  2017年2月24日 上午10:50:55
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年2月24日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/organ")
	public String toOrgan(){
		return "organ/view/organ";
	}
	/**
	 * 非超级管理员进来时进入机构详情页面 
	 * @date  2017年3月6日 上午9:10:08
	 * @return String
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
	@RequestMapping("/organDetail")
	public String toOrganDetail(){
		return "organ/view/organDetail";
	}
	
	@RequestMapping("/getOrgans")
	@ResponseBody
	public String getOrgans(PageCondition options,HttpSession session){
		Account account=(Account) session.getAttribute("account");
		Map<String, Object> map=new HashMap<String,Object>();
		//若不是管理员，设置当前组织到session
		Map<String,Object> organsData=organService.getOrgans(account,options,session);
		map.put("total", options.getTotalCount());
		map.put("rows", organsData.get("organs"));
		//对应的联系人的信息
		map.put("workers", organsData.get("workers"));
		//监督员的信息
		map.put("supervisors", organsData.get("supervisors"));
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getOrgan")
	@ResponseBody
	public String getOrgan(String organUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Organ organData = organService.getEntity(organUuid);
		Worker worker = workerService.getEntity(organData.getContactWorker());
		map.put("organ", organData);
		String loginName = worker.getAccount().getLoginName();
		String accoutName = worker.getAccount().getAccountName();
		worker.setAccount(null);
//		Worker supervisor=workerService.getEntity(organData.getSupervisor());
//		if(supervisor != null && supervisor.getAccount() != null){
//			supervisor.getAccount().setWorker(null);
//		}
		//对应的联系人的信息
		map.put("worker", worker);
		map.put("loginName", loginName);
		map.put("accountName", accoutName);
//		map.put("supervisor", supervisor);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	
	@RequestMapping("/getAllOrgans")
	@ResponseBody
	public String getAllOrgans(){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Organ> organs=organService.findAllEntity();
		map.put("result", 0);
		map.put("organs", organs);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/setCurrentOrgan",method=RequestMethod.POST)
	@ResponseBody
	public String setCurrentOrgan(String organUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		session.setAttribute("currentOrganUuid", organUuid);
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	/**
	 * 
	 * 获取当前机构id
	 * @date  2017年3月17日 下午3:11:13
	 * 
	 * @param session
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月17日     姚家俊      v1.0          create</p>
	 *
	 */
	@RequestMapping(value="/getCurrentOrgan",method=RequestMethod.POST)
	@ResponseBody
	public String getCurrentOrgan(HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		String organUuid=(String) session.getAttribute("currentOrganUuid");
		String workerUuid = organService.getEntity(organUuid).getContactWorker();
		map.put("result", 0);
		map.put("currentOrganUuid", organUuid);
		map.put("workerUuid",workerUuid);
		return JsonUtil.beanToJson(map);
	}
	
	/**
	 * 根据登录账户id获取所属结构信息
	 * @date  2017年3月28日 上午11:24:59
	 * @return String
	 * @param accountUuid
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月28日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping(value="/getOrganInfoByAccount")
	@ResponseBody
	public String getOrganInfoByAccount(String accountUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		Account account=accountService.getEntity(accountUuid);
		Worker worker=account.getWorker();
		Organ organ=worker.getOrgan();
		worker.setOrgan(null);
		if(worker.getAccount()!=null){
			worker.getAccount().setWorker(null);
		}
		map.put("result", 0);
		map.put("organ", organ);
		map.put("worker", worker);
		return JsonUtil.beanToJson(map);
	}
	
	
	@RequestMapping(value="/addOrgan",method=RequestMethod.POST)
	@ResponseBody
	public String addOrgan(Organ organ,Account account,Worker worker,HttpSession session){
		//只有超级管理员才能新建机构
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			organService.addOrgan(organ,account,worker);
			map.put("result", 0);
			map.put("entity", organ);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/deleteOrgan",method=RequestMethod.POST)
	@ResponseBody
	public String deleteOrgan(String organUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			organService.deleteOrgan(organUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/updateOrgan",method=RequestMethod.POST)
	@ResponseBody
	public String updateOrgan(Organ organ,String workerName,String phone,String loginName,String accountName,String supervisorName,String supervisorPhone) {
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			organService.updateOrgan(organ,workerName,phone,loginName,accountName,supervisorName,supervisorPhone);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	

/**
	 * 
	 * 编辑机构信息页面
	 * @date  2017年3月15日 上午11:57:02
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月15日     姚家俊      v1.0          create</p>
	 *
	 */
	@RequestMapping("/organEdit")
	public String toOrganEdit(){
		return "organ/view/organEdit";
	}
	
}
