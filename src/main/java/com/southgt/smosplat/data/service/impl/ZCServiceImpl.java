package com.southgt.smosplat.data.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.dao.IZCDao;
import com.southgt.smosplat.data.entity.ZC_Data;
import com.southgt.smosplat.data.service.IZCService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZCService;

/**
 * 支撑内力数据服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月28日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("zcService")
public class ZCServiceImpl extends BaseServiceImpl<ZC_Data> implements IZCService {

	@Resource
	ISurveyPoint_ZCService surveyPointZCService;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource(name="zcDao")
	@Override
	public void setDao(IBaseDao<ZC_Data> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, Object> getLatestZCDatasByProject(Project project) {
		Map<String, Object> result=new HashMap<String, Object>();
		Map<String, Object> dataResult = new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids =new ArrayList<>();
		//支撑内力
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(15);
		// 首先获取对应项目下对应监测项的所有支撑轴力监测点
		List<SurveyPoint_ZC> sps;
		//每一个点的最新11个时间点的数据
		Map<String, List<ZC_Data>> spLatestDatas = new HashMap<String, List<ZC_Data>>();
		//每一个点的第一个时间点的数据
//		Map<String, ZC_Data> spFirstData = new HashMap<String, ZC_Data>();
		//所有监测点最新11个时间点的数据
		List<ZC_Data> latestAllZCData;
		//获得所有监测点第一个时间点的坐标数据
//		List<ZC_Data> allFirstZCData;
		try{
			sps = surveyPointZCService.getSP_ZCs(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				dataResult.put(sps.get(i).getCode(), new ArrayList<ZC_Data>());
			}
			result.put("surveyPoints", sps);
			//数据库没有点
			if(sps.size() == 0){
	//			throw(new Exception("数据库没有点！"));
				return result;
			}
			
			sps.forEach(p->spUuids.add(p.getSurveyPointUuid()));
			//所有监测点最新11个时间点的数据
			latestAllZCData = ((IZCDao)getDao()).getLatestZCDatasBySurveyPoints(spUuids);
			//获得所有监测点第一个时间点的坐标数据
//			allFirstZCData = ((IZCDao)getDao()).getFirstOneZCDatasBySurveyPoints(spUuids);
		}catch(Exception ex){
			result.put("dataList", dataResult);
			return result;
		}
		
		for (int i = 0; i < sps.size(); i++) {
			//获得最新11个时间点的所有解算数据
			String spUuid = sps.get(i).getSurveyPointUuid();
			List<ZC_Data> latestZCData = latestAllZCData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
			if(latestZCData.size()>0){
				spLatestDatas.put(sps.get(i).getCode(), latestZCData);
			}
//			List<ZC_Data> tempList = allFirstZCData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
//			if(tempList !=null && tempList.size()>0){
//				ZC_Data firstZCData = tempList.get(0);
//				spFirstData.put(sps.get(i).getCode(), firstZCData);
//			}
		}
		//计算累计位移
		for (int i = 0; i < sps.size(); i++) {
			//一个点的最新11条数据
			List<ZC_Data> dataAtOnePoint = (List<ZC_Data>) spLatestDatas.get(sps.get(i).getCode());
			//只在有数据的情况下才进行计算
			if (dataAtOnePoint!=null && dataAtOnePoint.size() > 0) {
				for (int j = 0; j < dataAtOnePoint.size(); j++) {
					if (j < dataAtOnePoint.size() - 1) {
						// 轴力计算值单次位移
						double gapOffset = dataAtOnePoint.get(j + 1).getCalValue()- dataAtOnePoint.get(j).getCalValue();
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dataAtOnePoint.get(j + 1).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dataAtOnePoint.get(j).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						double gapChangeRate = gapOffset/days;
						
						//四舍五入保留4位小数
						BigDecimal b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						//变化速率
						b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						// 轴力计算值累计位移
//						double accumOffset = dataAtOnePoint.get(j + 1).getCalValue()- spFirstData.get(sps.get(i).getCode()).getCalValue();
//						b = new BigDecimal(accumOffset); 
//						accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
//						dataAtOnePoint.get(j + 1).setAccumOffset(accumOffset);
						dataAtOnePoint.get(j + 1).setGapOffset(gapOffset);
						dataAtOnePoint.get(j + 1).setGapChangeRate(gapChangeRate);
					} else if(dataAtOnePoint.size()>10){
						dataAtOnePoint.remove(0);
					}
				}
				dataResult.put(sps.get(i).getCode(), dataAtOnePoint);
			}else{
				dataResult.put(sps.get(i).getCode(), new ArrayList<ZC_Data>());
			}
		}
		result.put("dataList", dataResult);
		return result;
	}

	@Override
	public void deleteZCDataBySurveyPoint(String spUuid) {
		((IZCDao)getDao()).deleteZCDataBySurveyPoint(spUuid);
		
	}

	@Override
	public List<ZC_Data> getFirstOneZCDatasBySurveyPoints(List<String> surveyPointUuids) {
		return ((IZCDao)getDao()).getFirstOneZCDatasBySurveyPoints(surveyPointUuids);
	}

	@Override
	public List<ZC_Data> getLatestOneZCDatasBySurveyPoints(List<String> surveyPointUuids) {
		return ((IZCDao)getDao()).getLatestOneZCDatasBySurveyPoints(surveyPointUuids);
	}


}
