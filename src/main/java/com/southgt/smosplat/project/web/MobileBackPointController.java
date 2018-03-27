package com.southgt.smosplat.project.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.project.entity.Mobile_BackPoint;
import com.southgt.smosplat.project.service.IMobileBackPointService;

/**
 * 手机端后视点controller
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
public class MobileBackPointController {

	@Resource
	IMobileBackPointService mobileBackPointService;
	
	@RequestMapping(value="/getMobileBackPointsByStation")
	@ResponseBody
	public String getMobileBackPointsByStation(String stationUuid,HttpSession session){
		Map<String,Object> map =new HashMap<>();
		List<Mobile_BackPoint> bpList=mobileBackPointService.getBackPointByStation(stationUuid);
		map.put("result", 0);
		map.put("data", bpList);
		return JsonUtil.beanToJson(map);
	}
	
//	@RequestMapping(value="/operateMobileBackPoints")
//	@ResponseBody
//	public String operateMobileBackPoints(String backPointList,HttpSession session){
//		Map<String,Object> map =new HashMap<>();
//		try {
//			List<Mobile_BackPoint> bpList=JsonUtil.jsonToList(backPointList, Mobile_BackPoint.class);
//			mobileBackPointService.operateBackPoint(bpList);
//			map.put("result", 0);
//			map.put("msg", null);
//		} catch (IOException e) {
//			map.put("result", -1);
//			map.put("msg", e.getMessage());
//			e.printStackTrace();
//		}
//		return JsonUtil.beanToJson(map);
//
//	}
}
