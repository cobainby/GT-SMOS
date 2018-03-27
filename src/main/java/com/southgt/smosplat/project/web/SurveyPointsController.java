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
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.entity.SurveyPoint_SM;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.entity.SurveyPoint_ZGD;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_CXService;
import com.southgt.smosplat.project.service.ISurveyPoint_LZService;
import com.southgt.smosplat.project.service.ISurveyPoint_MTService;
import com.southgt.smosplat.project.service.ISurveyPoint_SMService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYDService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZGDService;

@Controller
public class SurveyPointsController {

	@Resource
	IProjectService projectService;
	
	@Resource
	IMonitorItemService monitorItemService;

	@Resource
	IMcuService mcuService;

	@Resource
	IDeviceService deviceService;
	
	@Resource
	ISurveyPoint_LZService sp_LZService;
	
	@Resource
	ISurveyPoint_WYDService sp_WYDService;
	
	@Resource
	ISurveyPoint_CXService sp_CXService;
	
	@Resource
	ISurveyPoint_MTService sp_MTService;
	
	@Resource
	ISurveyPoint_SWService sp_SWService;
	
	@Resource
	ISurveyPoint_WYSService sp_WYSService;
	
	@Resource
	ISurveyPoint_ZGDService sp_ZGDService;
	
	@Resource
	ISurveyPoint_SMService sp_SMService;
	
	
	@RequestMapping("/getSPs")
	@ResponseBody
	public String getSP_SWs(String monitorItemUuid,int number,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		if(number==1){
			List<SurveyPoint_WYS> datas=sp_WYSService.getSP_WYSs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}else if(number==4){
			List<SurveyPoint_CX> datas=sp_CXService.getSP_CXs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}else if(number==5){
			List<SurveyPoint_WYD> datas=sp_WYDService.getSP_WYDs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}else if(number==6){
			List<SurveyPoint_LZ> datas=sp_LZService.getSP_LZs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}else if(number==8){
			List<SurveyPoint_SM> datas=sp_SMService.getSP_SMs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}else if(number==10){
			List<SurveyPoint_ZGD> datas=sp_ZGDService.getSP_ZGDs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}else if(number==12){
			List<SurveyPoint_SW> datas=sp_SWService.getSP_SWs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}else if(number==18){
			List<SurveyPoint_MT> datas=sp_MTService.getSP_MTs(project.getProjectUuid(), monitorItemUuid);
			map.put("surveyPoints", datas);
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getsns")
	@ResponseBody
	public String getsns(int number,String stressType){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Device> list=sp_SWService.getDeviceSnByItemName(number,stressType);
		for (Device device : list) {
			device.setMcu(null);
		}
		map.put("devices", list);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getSP_SWsByProjectAndMonitorItem")
	@ResponseBody
	public String getSP_SWsByProjectAndMonitorItem(String monitorItemUuid,String projectUuid,HttpSession session){
//		Project project=projectService.getEntity(projectUuid);
		Map<String, Object> map=new HashMap<String,Object>();
		List<SurveyPoint_SW> datas=sp_SWService.getSP_SWs(projectUuid, monitorItemUuid);
		map.put("surveyPoints", datas);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/addSP_SWs")
	@ResponseBody
	public String addSP_SWs(SurveyPoint_SW tempSP,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<SurveyPoint_SW> ls=new ArrayList<SurveyPoint_SW>();
			sp_SWService.addSP_SW(project, tempSP,deviceSn,mcuUuid,moduleNum,channelNum,devModelUuid);
			ls.add(tempSP);
			map.put("result", 0);
			map.put("surveyPoints", ls);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_SWs")
	@ResponseBody
	public String updateSP_SWs(SurveyPoint_SW surveyPoint,String deviceSn,String mcuUuid,int moduleNum,int channelNum,String devModelUuid,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_SWService.updateSP_SW(project, surveyPoint,deviceSn,mcuUuid,moduleNum,channelNum,devModelUuid);;
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSP_SW")
	@ResponseBody
	public String deleteSP_SW(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_SWService.deleteSP_SW(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}

	
	
	@RequestMapping("/addSP_WYDs")
	@ResponseBody
	public String addSP_WYDs(SurveyPoint_WYD tempSP,int spCount,int beginNum,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<SurveyPoint_WYD> sps=sp_WYDService.addSP_WYD(project, tempSP, spCount,beginNum);
			map.put("result", 0);
			map.put("surveyPoints", sps);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_WYDs")
	@ResponseBody
	public String updateSP_WYDs(SurveyPoint_WYD surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_WYDService.updateSP_WYD(project, surveyPoint);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSP_WYD")
	@ResponseBody
	public String deleteSP_WYD(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_WYDService.deleteSP_WYD(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	
	
	@RequestMapping("/addSP_LZs")
	@ResponseBody
	public String addSP_LZs(SurveyPoint_LZ tempSP,int spCount,int beginNum,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<SurveyPoint_LZ> sps=sp_LZService.addSP_LZ(project, tempSP, spCount,beginNum);
			map.put("result", 0);
			map.put("surveyPoints", sps);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_LZs")
	@ResponseBody
	public String updateSP_LZs(SurveyPoint_LZ surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_LZService.updateSP_LZ(project, surveyPoint);;
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSP_LZ")
	@ResponseBody
	public String deleteSP_LZ(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_LZService.deleteSP_LZ(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/addSP_SMs")
	@ResponseBody
	public String addSP_SMs(SurveyPoint_SM tempSP,int spCount,int beginNum,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<SurveyPoint_SM> sps=sp_SMService.addSP_SM(project, tempSP, spCount,beginNum);
			map.put("result", 0);
			map.put("surveyPoints", sps);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_SMs")
	@ResponseBody
	public String updateSP_SMs(SurveyPoint_SM surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_SMService.updateSP_SM(project, surveyPoint);;
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSP_SM")
	@ResponseBody
	public String deleteSP_SM(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_SMService.deleteSP_SM(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/addSP_WYSs")
	@ResponseBody
	public String addSP_WYSs(SurveyPoint_WYS tempSP,int spCount,int beginNum,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			
			List<SurveyPoint_WYS> sps=sp_WYSService.addSP_WYS(project, tempSP, spCount,beginNum);
			map.put("result", 0);
			map.put("surveyPoints", sps);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_WYSs")
	@ResponseBody
	public String updateSP_WYSs(SurveyPoint_WYS surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_WYSService.updateSP_WYS(project, surveyPoint);;
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSP_WYS")
	@ResponseBody
	public String deleteSP_WYS(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_WYSService.deleteSP_WYS(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/addSP_ZGDs")
	@ResponseBody
	public String addSP_ZGDs(SurveyPoint_ZGD tempSP,int spCount,int beginNum,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			
			List<SurveyPoint_ZGD> sps=sp_ZGDService.addSP_ZGD(project, tempSP, spCount,beginNum);
			map.put("result", 0);
			map.put("surveyPoints", sps);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/updateSP_ZGDs")
	@ResponseBody
	public String updateSP_ZGDs(SurveyPoint_ZGD surveyPoint,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_ZGDService.updateSP_ZGD(project, surveyPoint);;
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	@RequestMapping("/deleteSP_ZGD")
	@ResponseBody
	public String deleteSP_ZGD(String surveyPointUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			sp_ZGDService.deleteSP_ZGD(surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
//	@RequestMapping("/addSP_CXs")
//	@ResponseBody
//	public String addSP_CXs(SurveyPoint_CX tempSP,int spCount,int beginNum,HttpSession session){
//		Project project=(Project) session.getAttribute("currentProject");
//		Map<String, Object> map=new HashMap<String,Object>();
//		try {
//			
//			List<SurveyPoint_CX> sps=sp_CXService.addSP_CX(project, tempSP, spCount,beginNum);
//			map.put("result", 0);
//			map.put("surveyPoints", sps);
//		} catch (Exception e) {
//			map.put("result", -1);
//			map.put("msg", e.getMessage());
//		}
//		String s=JsonUtil.beanToJson(map);
//		return s;
//	}
	
//	@RequestMapping("/updateSP_CXs")
//	@ResponseBody
//	public String updateSP_CXs(SurveyPoint_CX surveyPoint,HttpSession session){
//		Project project=(Project) session.getAttribute("currentProject");
//		Map<String, Object> map=new HashMap<String,Object>();
//		try {
//			sp_CXService.updateSurveyPoint_CX(project, surveyPoint);
//			map.put("result", 0);
//		} catch (Exception e) {
//			map.put("result", -1);
//			map.put("msg", e.getMessage());
//		}
//		String s=JsonUtil.beanToJson(map);
//		return s;
//	}
//	@RequestMapping("/deleteSP_CX")
//	@ResponseBody
//	public String deleteSP_CX(String surveyPointUuid){
//		Map<String, Object> map=new HashMap<String,Object>();
//		try {
//			sp_CXService.deleteSurveyPoint_CX(surveyPointUuid);
//			map.put("result", 0);
//		} catch (Exception e) {
//			map.put("result", -1);
//			map.put("msg", e.getMessage());
//		}
//		String s=JsonUtil.beanToJson(map);
//		return s;
//	}
}
