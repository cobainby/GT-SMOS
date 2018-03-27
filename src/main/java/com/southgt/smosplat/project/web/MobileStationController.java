package com.southgt.smosplat.project.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.project.entity.Mobile_BackPoint;
import com.southgt.smosplat.project.entity.Mobile_Station;
import com.southgt.smosplat.project.service.IMobileBackPointService;
import com.southgt.smosplat.project.service.IMobileStationService;

/**
 * 手机端测站控制器类
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年11月14日     YANG       v1.0.0        create</p>
 *
 */
@Controller
public class MobileStationController {

	@Resource
	IMobileStationService mobileStationService;
	
	@Resource
	IMobileBackPointService mobileBackPointService;
	
	//获取测站和后视点列表
	@RequestMapping(value="/getMobileStationsAndBackPointsByProjectAndMonitorItem")
	@ResponseBody
	public String getStationsByProjectAndMonitorItem(String projectUuid,String monitorItemUuid,HttpSession session){
		Map<String,Object> map =new HashMap<>();
		List<Mobile_Station> stationList=mobileStationService.getStationsByProjectAndMonitorItem(projectUuid, monitorItemUuid);
		List<Mobile_BackPoint> bpList=new ArrayList<>();
		if(stationList.size()>0){
			for(int i=0;i<stationList.size();i++){
				List<Mobile_BackPoint> blist=mobileBackPointService.getBackPointByStation(stationList.get(i).getStationUuid());
				bpList.addAll(blist);
			}
		}
		map.put("result", 0);
		map.put("stationList", stationList);
		map.put("backPointList", bpList);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/operateMobileStations")
	@ResponseBody
	public String operateStations(String stationList,HttpSession session){
		Map<String,Object> map =new HashMap<>();
		try {
			List<Mobile_Station> sList=JsonUtil.jsonToList(stationList, Mobile_Station.class);
			mobileStationService.operateStations(sList);
			map.put("result", 0);
			map.put("msg", null);
		} catch (IOException e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return JsonUtil.beanToJson(map);

	}
	
	//同时上传测站列表和后视点列表
	@RequestMapping(value="/operateMobileStationsAndBackPoints")
	@ResponseBody
	public String operateStationsAndBackPoints(String stationList,String backPointList,HttpServletRequest re){
		Map<String,Object> map =new HashMap<>();
		try {
			List<Mobile_Station> sList=JsonUtil.jsonToList(stationList, Mobile_Station.class);
			List<Mobile_BackPoint> bpList=JsonUtil.jsonToList(backPointList, Mobile_BackPoint.class);
			if(sList.size()>0){
				mobileBackPointService.operateBackPoint(bpList,sList.get(0).getProjectUuid(),sList.get(0).getMonitorItemUuid());
			}
			mobileStationService.operateStations(sList);
			map.put("result", 0);
			map.put("msg", null);
		} catch (IOException e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return JsonUtil.beanToJson(map);

	}
	
	
	
}
