package com.southgt.smosplat.organ.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.entity.GPSInfo;
import com.southgt.smosplat.organ.service.IAccountService;
import com.southgt.smosplat.organ.service.IGPSService;
import com.southgt.smosplat.project.service.IProjectService;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年10月13日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class GPSController {
	
	@Resource
	private IAccountService accountService;
	
	@Resource
	private IGPSService gpsService;
	
	@RequestMapping(value="/getGPSInfo")
	@ResponseBody
	public String getGPSInfo(String accountUuid,String startTime,String endTime,String projectUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		Account account=accountService.getEntity(accountUuid);
		List<GPSInfo> gpsInfos=gpsService.getGPSInfosByAccount(account,startTime,endTime,projectUuid);
		map.put("gpsInfo", gpsInfos);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/uploadGPSInfoFromMobile")
	@ResponseBody
	public String uploadGPSInfoFromMobile(String accountUuid,Double lon,Double lat,Integer within,String projectUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			Account account=accountService.getEntity(accountUuid);
			gpsService.addGPSInfo(account,lon,lat,within,projectUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
}
