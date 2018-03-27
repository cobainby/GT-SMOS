package com.southgt.smosplat.project.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.project.entity.Mcu;
import com.southgt.smosplat.project.entity.MonitorItemParam;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Section;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.IMonitorItemParamService;
import com.southgt.smosplat.project.service.INetworkService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISectionService;
import com.southgt.smosplat.project.service.ISurveyPointService;
import com.southgt.smosplat.project.service.IWarningService;

/**
 * 监测项前端控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月28日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class MonitorItemController {

	@Resource
	IWarningService warningService;
	
	@Resource
	ISectionService sectionService;
	
	@Resource
	ISurveyPointService surveyPointService;
	
	@Resource
	IMonitorItemParamService monitorItemParamService;
	
	@Resource
	IProjectService projectService;
	/**
	 * 自动采集设置
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
	@RequestMapping("/autoSetting")
	public String autoSetting(){
		return "project/view/autoSetting";
	}

	@RequestMapping("/addWarning")
	@ResponseBody
	public String addWarning(Warning warning,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			warningService.addWarning(project,warning);
			map.put("result", 0);
			map.put("entity", warning);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/getWarnings")
	@ResponseBody
	public String getWarnings(String monitorItemUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		List<Warning> datas=warningService.getWarnings(project.getProjectUuid(),monitorItemUuid);
		map.put("warnings", datas);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateWarning")
	@ResponseBody
	public String updateWarning(Warning warning,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			warningService.updateWarning(project,warning);
			map.put("result", 0);
			map.put("entity", warning);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/deleteWarning")
	@ResponseBody
	public String deleteWarning(String warningUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			warningService.deleteWarning(warningUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/addSection")
	@ResponseBody
	public String addSection(Section section,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sectionService.addSection(project,section);
			map.put("result", 0);
			map.put("entity", section);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getSections")
	@ResponseBody
	public String getSections(String monitorItemUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		List<Section> datas=sectionService.getSections(project.getProjectUuid(),monitorItemUuid);
		map.put("sections", datas);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSection")
	@ResponseBody
	public String updateSection(Section section,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sectionService.updateSection(project,section);
			map.put("result", 0);
			map.put("entity", section);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/deleteSection")
	@ResponseBody
	public String deleteSection(String sectionUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sectionService.deleteSection(sectionUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
		
	@RequestMapping("/addSurveyPoints")
	@ResponseBody
	public String addSurveyPoints(SurveyPoint_WYS tempSP,int spCount,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			
			List<SurveyPoint_WYS> sps=surveyPointService.addSurveyPoint(project,tempSP,spCount);
			map.put("result", 0);
			map.put("surveyPoints", sps);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getSurveyPointsByProjectAndMonitorItem")
	@ResponseBody
	public String getSurveyPoints(String monitorItemUuid,String projectUuid,HttpSession session){
		Project project=projectService.getEntity(projectUuid);
		Map<String, Object> map=new HashMap<String,Object>();
		List<SurveyPoint_WYS> datas=surveyPointService.getSurveyPoints(project.getProjectUuid(),monitorItemUuid);
		map.put("surveyPoints", datas);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getSurveyPoints")
	@ResponseBody
	public String getSurveyPoints(String monitorItemUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		List<SurveyPoint_WYS> datas=surveyPointService.getSurveyPoints(project.getProjectUuid(),monitorItemUuid);
		map.put("surveyPoints", datas);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSurveyPoint")
	@ResponseBody
	public String updateSurveyPoint(SurveyPoint_WYS surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			surveyPointService.updateSurveyPoint(project,surveyPoint);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSurveyPoint")
	@ResponseBody
	public String deleteSurveyPoint(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			surveyPointService.deleteSurveyPoint(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/getMonitorItemParam")
	@ResponseBody
	public String getMonitorItemParam(String monitorItemUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		MonitorItemParam monitorItemParam=monitorItemParamService.getMonitorItemParam(project.getProjectUuid(),monitorItemUuid);
		if(monitorItemParam!=null){
			monitorItemParam.setMonitorItem(null);
			monitorItemParam.setProject(null);
		}
		map.put("result", 0);
		map.put("entity", monitorItemParam);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/saveOrUpdateMonitorItemParam")
	@ResponseBody
	public String saveOrUpdateMonitorItemParam(MonitorItemParam monitorItemParam,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		monitorItemParamService.saveOrUpdateMonitorItemParam(monitorItemParam,project);
		map.put("result", 0);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
}
