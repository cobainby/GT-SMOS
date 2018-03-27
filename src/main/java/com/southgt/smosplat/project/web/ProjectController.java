package com.southgt.smosplat.project.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.organ.service.IAccountProjectService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.INetworkService;
import com.southgt.smosplat.project.service.IProjectMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;

/**
 * 
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
public class ProjectController {
	
	@Resource
	IProjectService projectService;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IProjectMonitorItemService projectMonitorItemService;
	
	@Resource
	INetworkService networkService;
	
	@Resource
	IMcuService mcuService;
	
	@Resource
	IDeviceService deviceService;
	
	
	@RequestMapping("/projectIndex")
	public String toProjectIndex(HttpServletRequest request,HttpSession session){
		//进入工程列表时设置一下当前所属机构
		Account account=(Account) request.getSession().getAttribute("account");
		String organUuid=account.getWorker().getOrgan().getOrganUuid();
		session.setAttribute("currentOrganUuid", organUuid);
		return "project/view/index";
	}
	/**
	 * 加载工程列表页面
	 * @date  2017年3月19日 下午2:50:11
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月19日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/projectList")
	public String projectList(){
		return "project/view/project";
	}
	/**
	 * 加载地图工程列表
	 * @date  2017年8月4日 下午4:32:36
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
	 * <p>2017年8月4日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/mapProjectList")
	public String mapProjectList(){
		return "project/view/mapProjectList";
	}
	/**
	 * 加载工程设置页面
	 * @date  2017年3月20日 下午5:04:33
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月20日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/projectSetting")
	public String projectSetting(){
		return "project/view/projectSetting";
	}
	/**
	 * 工程详细信息
	 * @date  2017年4月12日 下午3:19:21
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/projectDetail")
	public String projectDetail(){
		return "project/view/projectDetail";
	}
	/**
	 * 工程项目包含信息填写
	 * @date  2017年8月8日 上午10:45:34
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
	 * <p>2017年8月8日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/projectInfo")
	public String projectInfo(){
		return "project/view/projectInfo";
	}
	/**
	 * 工程监测方案相关文档
	 * @date  2017年4月12日 下午3:19:31
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/projectDoc")
	public String projectDoc(){
		return "project/view/projectDoc";
	}
	/**
	 * 
	 * 工程平面图
	 * @date  2017年4月12日 下午3:20:02
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/projectPlan")
	public String projectPlan(){
		return "project/view/projectPlan";
	}
	
	/**
	 * 工程监测项设置
	 * @date  2017年4月12日 下午3:20:17
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月12日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/projectMonitorItem")
	public String projectMonitorItem(){
		return "project/view/projectMonitorItem";
	}
	
	/**
	 * 设置当前的工程
	 * @date  2017年3月20日 下午5:14:46
	 * @return String
	 * @param projectUuid
	 * @param session
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月20日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping(value="/setCurrentProject",method=RequestMethod.POST)
	@ResponseBody
	public String setCurrentProject(String projectUuid,HttpSession session){
		Project project=projectService.getEntity(projectUuid);
		session.setAttribute("currentProject", project);
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/getCurrentProjectInfo")
	@ResponseBody
	public String getCurrentProjectInfo(HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		LogUtil.info("当前工程："+project.getProjectName());
		Map<String, Object> map=new HashMap<String,Object>();
		List<ProjectMonitorItem> pm=project.getProjectMonitorItems();
		project.setProjectMonitorItems(null);
		map.put("result", 0);
		map.put("project", project);
		String s=JsonUtil.beanToJson(map);
		project.setProjectMonitorItems(pm);
		return s;
	}
	
	@RequestMapping(value="/getProjectByUuid")
	@ResponseBody
	public String getProjectByUuid(HttpSession session, String projectUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try{
			Project project = projectService.getEntity(projectUuid);
			List<ProjectMonitorItem> pm=project.getProjectMonitorItems();
			project.setProjectMonitorItems(null);
			map.put("result", 0);
			map.put("project", project);
		}catch(Exception ex){
			map.put("result", -1);
			map.put("msg", ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/getProjectCollectStatus")
	@ResponseBody
	public String getProjectCollectStatus(HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		//必须从数据库获取最新状态
		Project project1=projectService.getEntity(project.getProjectUuid());
		map.put("result", 0);
		map.put("collectStatus", project1.getCollectStatus()==null?-1:project1.getCollectStatus());
		map.put("interval", project1.getCollectInterval());
		map.put("projectUuid", project1.getProjectUuid());
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/updateProjectCollectStatus")
	@ResponseBody
	public String updateProjectCollectStatus(String status,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		project.setCollectStatus(Byte.parseByte(status));
		try{
			projectService.updateEntity(project);
			map.put("result", 0);
		}catch(Exception ex){
			map.put("result", -1);
			map.put("msg",ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/updateProjectInterval")
	@ResponseBody
	public String updateProjectInterval(String projectUuid, String interval,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		try{
			projectService.updateInterval(projectUuid, interval);
			map.put("result", 0);
		}catch(Exception ex){
			ex.printStackTrace();
			map.put("result", -1);
			map.put("msg",ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/addProject",method=RequestMethod.POST)
	@ResponseBody
	public String addProject(Project project,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Account account=(Account) session.getAttribute("account");
		try {
			projectService.addProject(project,account.getWorker().getOrgan());
			map.put("result", 0);
			map.put("project", project);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@Resource
	IAccountProjectService accountProjectService;
	
	@RequestMapping("/getProjects")
	@ResponseBody
	public String getProjects(HttpSession session){
		Account account=(Account) session.getAttribute("account");
		Map<String, Object> map=new HashMap<String,Object>();
		List<Project> projectsData=projectService.getProjects(account);
		//如果当前登录
		//处理一下机构信息
		for (int i = 0; i < projectsData.size(); i++) {
			Organ o=new Organ();
			o.setOrganUuid(projectsData.get(i).getOrgan().getOrganUuid());
			o.setOrganName(projectsData.get(i).getOrgan().getOrganName());
			projectsData.get(i).setOrgan(o);
		}
		//处理一下监测项目信息
		for (int i = 0; i < projectsData.size(); i++) {
			List<ProjectMonitorItem> pms=projectsData.get(i).getProjectMonitorItems();
			for (int j = 0; j < pms.size(); j++) {
				pms.get(j).setProject(null);
				MonitorItem m=new MonitorItem();
				m.setMonitorItemUuid(pms.get(j).getMonitorItem().getMonitorItemUuid());
				m.setMonitorItemName(pms.get(j).getMonitorItem().getMonitorItemName());
				m.setCode(pms.get(j).getMonitorItem().getCode());
				pms.get(j).setMonitorItem(m);
			}
		}
		map.put("rows", projectsData);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getProjectsByOrgan")
	@ResponseBody
	public String getProjectsByOrgan(String organUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Project> projectsData=projectService.getProjectsByOrgan(organUuid);
		for (int i = 0; i < projectsData.size(); i++) {
			Organ o=new Organ();
			o.setOrganUuid(projectsData.get(i).getOrgan().getOrganUuid());
			o.setOrganName(projectsData.get(i).getOrgan().getOrganName());
			projectsData.get(i).setOrgan(o);
		}
		//处理一下监测项目信息
		for (int i = 0; i < projectsData.size(); i++) {
			List<ProjectMonitorItem> pms=projectsData.get(i).getProjectMonitorItems();
			for (int j = 0; j < pms.size(); j++) {
				pms.get(j).setProject(null);
				MonitorItem m=new MonitorItem();
				m.setMonitorItemUuid(pms.get(j).getMonitorItem().getMonitorItemUuid());
				m.setMonitorItemName(pms.get(j).getMonitorItem().getMonitorItemName());
				pms.get(j).setMonitorItem(m);
			}
		}
		map.put("projects", projectsData);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/deleteProject",method=RequestMethod.POST)
	@ResponseBody
	public String deleteProject(String projectUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			projectService.deleteProject(projectUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/updateProject",method=RequestMethod.POST)
	@ResponseBody
	public String updateProject(Project project){
		Map<String, Object> map=new HashMap<String,Object>();
		Project updatedProject;
		try {
			updatedProject=projectService.updateProject(project);
			//处理一下代理对象的序列化问题
			Organ organ=new Organ();
			organ.setOrganUuid(updatedProject.getOrgan().getOrganUuid());
			organ.setOrganName(updatedProject.getOrgan().getOrganName());
			updatedProject.setOrgan(organ);
			map.put("result", 0);
			map.put("entity", updatedProject);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getAllMonitorItems")
	@ResponseBody
	public String getAllMonitorItems(){
		Map<String, Object> map=new HashMap<String,Object>();
		List<MonitorItem> monitorItems=monitorItemService.findAllEntity();
		map.put("monitorItems", monitorItems);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getMonitorItemsByProject")
	@ResponseBody
	public String getMonitorItemsByProject(HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		List<MonitorItem> monitorItems=new ArrayList<MonitorItem>();
		List<ProjectMonitorItem> projectMonitorItems=projectMonitorItemService.getMonitorItemsByProject(project);
		for (int i = 0; i < projectMonitorItems.size(); i++) {
			MonitorItem item=new MonitorItem();
			item.setMonitorItemUuid(projectMonitorItems.get(i).getMonitorItem().getMonitorItemUuid());
			item.setMonitorItemName(projectMonitorItems.get(i).getMonitorItem().getMonitorItemName());
			item.setHasSectionSetting(projectMonitorItems.get(i).getMonitorItem().getHasSectionSetting());
			item.setHasAutoSetting(projectMonitorItems.get(i).getMonitorItem().getHasAutoSetting());
			item.setNumber(projectMonitorItems.get(i).getMonitorItem().getNumber());
			item.setCode(projectMonitorItems.get(i).getMonitorItem().getCode());
			monitorItems.add(item);
		}
		map.put("monitorItems", monitorItems);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/getMonitorItemsForMobileByProject")
	@ResponseBody
	public String getMonitorItemsForMobileByProject(String projectUuid){
		Project project=projectService.getEntity(projectUuid);
		Map<String, Object> map=new HashMap<String,Object>();
		List<MonitorItem> monitorItems=new ArrayList<MonitorItem>();
		List<ProjectMonitorItem> projectMonitorItems=projectMonitorItemService.getMonitorItemsByProject(project);
		for (int i = 0; i < projectMonitorItems.size(); i++) {
			MonitorItem item=new MonitorItem();
			item.setMonitorItemUuid(projectMonitorItems.get(i).getMonitorItem().getMonitorItemUuid());
			item.setMonitorItemName(projectMonitorItems.get(i).getMonitorItem().getMonitorItemName());
			item.setHasSectionSetting(projectMonitorItems.get(i).getMonitorItem().getHasSectionSetting());
			item.setHasAutoSetting(projectMonitorItems.get(i).getMonitorItem().getHasAutoSetting());
			item.setNumber(projectMonitorItems.get(i).getMonitorItem().getNumber());
			item.setCode(projectMonitorItems.get(i).getMonitorItem().getCode());
			monitorItems.add(item);
		}
		map.put("monitorItems", monitorItems);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/addMonitorItemsForProject")
	@ResponseBody
	public String addMonitorItemsForProject(String monitorItemUuids,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			projectMonitorItemService.addMonitorItemsForProject(project,monitorItemUuids);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteMonitorItemForProject")
	@ResponseBody
	public String deleteMonitorItemForProject(String monitorItemUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			projectMonitorItemService.deleteMonitorItemForProject(project,monitorItemUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/addNetwork")
	@ResponseBody
	public String addNetwork(Network network,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			networkService.addNetwork(project,network);
			map.put("result", 0);
			map.put("entity", network);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getNetworks")
	@ResponseBody
	public String getNetworks(HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		List<Network> datas=networkService.getNetworks(project.getProjectUuid());
		map.put("networks", datas);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateNetwork")
	@ResponseBody
	public String updateNetwork(Network network,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			networkService.updateNetwork(project,network);
			map.put("result", 0);
			map.put("entity", network);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/deleteNetwork")
	@ResponseBody
	public String deleteNetwork(String networkUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			networkService.deleteNetwork(networkUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/addMcu")
	@ResponseBody
	public String addMcu(Mcu mcu,String deviceSN,String devModelUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Account account=(Account) session.getAttribute("account");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			mcuService.addMcu(account.getWorker().getOrgan(),project,mcu,deviceSN,devModelUuid);
			map.put("result", 0);
			map.put("entity", mcu);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getMcus")
	@ResponseBody
	public String getMcus(HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		List<Mcu> datas=mcuService.getMcus(project.getProjectUuid());
		List<Device> devices=new ArrayList<Device>();
		for (Mcu mcu : datas) {
			Device d=deviceService.getEntity(mcu.getDeviceUuid());
			d.setMcu(null);
			d.setOrgan(null);
			devices.add(d);
		}
		map.put("mcus", datas);
		map.put("devices", devices);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getMcusByOrgan")
	@ResponseBody
	public String getMcusByOrgan(HttpSession session){
		Account account=(Account) session.getAttribute("account");
		Map<String, Object> map=new HashMap<String,Object>();
		List<Mcu> datas=mcuService.getMcusByOrgan(account.getWorker().getOrgan().getOrganUuid());
		List<Device> devices=new ArrayList<Device>();
		for (Mcu mcu : datas) {
			Device d=deviceService.getEntity(mcu.getDeviceUuid());
			d.setMcu(null);
			d.setOrgan(null);
			devices.add(d);
		}
		map.put("mcus", datas);
		map.put("devices", devices);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateMcu")
	@ResponseBody
	public String updateMcu(Mcu mcu,String deviceUuid,String deviceSN,String devModelUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Account account=(Account) session.getAttribute("account");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			mcuService.updateMcu(account.getWorker().getOrgan(),project,mcu,deviceUuid,deviceSN,devModelUuid);
			map.put("result", 0);
			map.put("entity", mcu);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteMcu")
	@ResponseBody
	public String deleteMcu(String mcuUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			mcuService.deleteMcu(mcuUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
}
