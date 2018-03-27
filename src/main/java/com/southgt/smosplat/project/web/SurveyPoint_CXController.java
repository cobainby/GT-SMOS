package com.southgt.smosplat.project.web;

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
import com.southgt.smosplat.project.entity.Clinometer;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.service.IClinometerService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_CXService;
/**
 * 
 * 测斜控制器
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月15日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller
public class SurveyPoint_CXController {

	@Resource
	ISurveyPoint_CXService sp_CXService;
	
	@Resource
	IClinometerService clinometerService;
	
	@Resource
	IProjectService projectService;	
	
	@RequestMapping(value="/getsp_CXs",method=RequestMethod.POST)
	@ResponseBody
	public String getsp_CXs(String monitorItemUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			List<SurveyPoint_CX> cxList= sp_CXService.getSP_CXs(project.getProjectUuid(), monitorItemUuid);
			map.put("result", 0);
			map.put("surveyPoints", cxList);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping("/addSP_CXs")
	@ResponseBody
	public String addSP_CXs(SurveyPoint_CX tempSP,int spCount,int beginNum,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			
			List<SurveyPoint_CX> sps=sp_CXService.addSP_CX(project, tempSP, spCount,beginNum);
			map.put("result", 0);
			map.put("surveyPoints", sps);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_CXs")
	@ResponseBody
	public String updateSP_CXs(SurveyPoint_CX surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_CXService.updateSurveyPoint_CX(project, surveyPoint);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/deleteSP_CX")
	@ResponseBody
	public String deleteSP_CX(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_CXService.deleteSurveyPoint_CX(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/addClinometer",method=RequestMethod.POST)
	@ResponseBody
	public String addClinometer(Clinometer Clinometer,String deviceSn,String devType,String mcuUuid,String gap,String devModelUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		Clinometer.setProject(project);
		try {
			clinometerService.addClinometer(Clinometer,deviceSn, devType, mcuUuid, gap, devModelUuid);
			map.put("result", 0);
			map.put("entity", Clinometer);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/updateClinometer",method=RequestMethod.POST)
	@ResponseBody
	public String updateClinometer(Clinometer Clinometer,String deviceSn,String devType,String mcuUuid,String gap,String devModelUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		Clinometer.setProject(project);
		try {
			clinometerService.updateClinometer(Clinometer, deviceSn, devType, mcuUuid, gap, devModelUuid);
			map.put("result", 0);
			map.put("entity", Clinometer);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/deleteClinometer",method=RequestMethod.POST)
	@ResponseBody
	public String deleteClinometer(String stressUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			clinometerService.delClinometer(stressUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/getClinometerByProjectUuid")
	@ResponseBody
	public String getClinometerByProjectUuid(String projectUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<Clinometer> stressList=clinometerService.getClinometerByProject(projectUuid);
			map.put("result", 0);
			map.put("rows", stressList);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/getClinometerBysp_CX")
	@ResponseBody
	public String getClinometerBysp_CX(String sp_CXUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<Clinometer> stressList=clinometerService.getClinometerBySP_cx(sp_CXUuid);
			map.put("result", 0);
			map.put("rows", stressList);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
}
