package com.southgt.smosplat.organ.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IGPSDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.GPSInfo;
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
@Service("gpsService")
public class GPSServiceImpl extends BaseServiceImpl<GPSInfo> implements IGPSService {
	
	@Resource
	private IProjectService projectService;

	@Resource(name="gpsDao")
	@Override
	public void setDao(IBaseDao<GPSInfo> dao) {
		super.setDao(dao);
	}

	@Override
	public List<GPSInfo> getGPSInfosByAccount(Account account, String startTime, String endTime, String projectUuid) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date startDate=format.parse(startTime);
			Date endDate=format.parse(endTime);
			List<GPSInfo> gpsList=((IGPSDao)getDao()).getGPSInfosByAccount(account,startDate,endDate,projectUuid);
			return gpsList;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addGPSInfo(Account account, Double lon, Double lat,Integer within, String projectUuid) throws ParseException {
		GPSInfo gpsInfo=new GPSInfo();
		gpsInfo.setAccount(account);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		gpsInfo.setTime(new Timestamp(calendar.getTimeInMillis()));
		gpsInfo.setLon(lon);
		gpsInfo.setLat(lat);
		gpsInfo.setWithin(within);
		gpsInfo.setProjectUuid(projectUuid);
		saveEntity(gpsInfo);
	}

	

}
