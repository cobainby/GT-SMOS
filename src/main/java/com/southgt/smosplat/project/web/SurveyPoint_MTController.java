package com.southgt.smosplat.project.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.service.ICableMeterService;
import com.southgt.smosplat.project.service.ISurveyPoint_MTService;

@Controller
public class SurveyPoint_MTController {

	@Resource
	ISurveyPoint_MTService sp_MTService;
	
	@Resource
	ICableMeterService cableMeterService;
	
	@RequestMapping("/addSP_MTs")
	@ResponseBody
	public String addSP_MTs(SurveyPoint_MT tempSP,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_MTService.addSP_MT(project, tempSP);
			List<SurveyPoint_MT> list=new ArrayList<SurveyPoint_MT>();
			list.add(tempSP);
			map.put("result", 0);
			map.put("surveyPoints", list);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_MTs")
	@ResponseBody
	public String updateSP_MTs(SurveyPoint_MT surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_MTService.updateSP_MT(project, surveyPoint);;
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSP_MT")
	@ResponseBody
	public String deleteSP_MT(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_MTService.deleteSP_MT(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getCableMeterBySP_mt")
	@ResponseBody
	public String getCableMeterBySP_mt(String sp_MTUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			
			List<CableMeter>list= cableMeterService.getCableMeterBySP_mt(sp_MTUuid);
			map.put("result", 0);
			map.put("rows", list);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/addCableMeter")
	@ResponseBody
	public String addCableMeter(CableMeter cableMeter,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			cableMeter.setProject(project);
			cableMeterService.addCableMeter(cableMeter,deviceSn, mcuUuid, moduleNum, channelNum, devModelUuid);
			map.put("result", 0);
			map.put("entity", cableMeter);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateCableMeter")
	@ResponseBody
	public String updateCableMeter(CableMeter cableMeter,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		cableMeter.setProject(project);
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			cableMeterService.updateCableMeter( cableMeter, deviceSn, mcuUuid, moduleNum, channelNum, devModelUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteCableMeter")
	@ResponseBody
	public String deleteCableMeter(String stressUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			cableMeterService.delCableMeter(stressUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
