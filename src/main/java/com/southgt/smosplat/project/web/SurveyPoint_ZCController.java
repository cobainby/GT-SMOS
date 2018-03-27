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
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Stress;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.IStressService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZCService;

@Controller
public class SurveyPoint_ZCController {

	@Resource
	ISurveyPoint_ZCService sp_ZCService;
	
	@Resource
	IStressService stressService;
	
	@Resource
	IProjectService projectService;	
	
	@RequestMapping(value="/getSP_ZCs",method=RequestMethod.POST)
	@ResponseBody
	public String getSP_ZCs(String monitorItemUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			List<SurveyPoint_ZC> zcList= sp_ZCService.getSP_ZCs(project.getProjectUuid(), monitorItemUuid);
			map.put("result", 0);
			map.put("surveyPoints", zcList);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/addSP_zc",method=RequestMethod.POST)
	@ResponseBody
	public String addSP_zc(SurveyPoint_ZC sp_ZC,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			sp_ZC.setProject(project);
			sp_ZCService.addSP_zc(sp_ZC);
			List<SurveyPoint_ZC> list= new ArrayList<SurveyPoint_ZC>();
			list.add(sp_ZC);
			map.put("result", 0);
			map.put("surveyPoints", list);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/updateSP_zc",method=RequestMethod.POST)
	@ResponseBody
	public String updateSP_zc(SurveyPoint_ZC sp_ZC,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			sp_ZC.setProject(project);
			sp_ZCService.updateSP_zc(sp_ZC);
			map.put("result", 0);
			map.put("entity", sp_ZC);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/deleteSP_zc",method=RequestMethod.POST)
	@ResponseBody
	public String deleteSP_zc(String surveyPointUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_ZCService.deleteSP_zc(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/addStress",method=RequestMethod.POST)
	@ResponseBody
	public String addStress(Stress stress,String deviceSn,String devType,String mcuUuid,int moduleNum,int channelNum,String devModelUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		stress.setProject(project);
		try {
			stressService.addStress(stress,deviceSn, devType, mcuUuid, moduleNum, channelNum, devModelUuid);
			map.put("result", 0);
			map.put("entity", stress);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/updateStress",method=RequestMethod.POST)
	@ResponseBody
	public String updateStress(Stress stress,String deviceSn,String devType,String mcuUuid,int moduleNum,int channelNum,String devModelUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		stress.setProject(project);
		try {
			stressService.updateStress(stress, deviceSn, devType, mcuUuid, moduleNum, channelNum, devModelUuid);
			map.put("result", 0);
			map.put("entity", stress);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/deleteStress",method=RequestMethod.POST)
	@ResponseBody
	public String deleteStress(String stressUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			stressService.delStress(stressUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/getStressByProjectUuid")
	@ResponseBody
	public String getStressByProjectUuid(String projectUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<Stress> stressList=stressService.getStressByProject(projectUuid);
			map.put("result", 0);
			map.put("rows", stressList);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/getStressBySP_zc")
	@ResponseBody
	public String getStressBySP_zc(String sp_ZCUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<Stress> stressList=stressService.getStressBySP_zc(sp_ZCUuid);
			map.put("result", 0);
			map.put("rows", stressList);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
}
