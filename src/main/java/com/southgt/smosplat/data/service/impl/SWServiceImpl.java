package com.southgt.smosplat.data.service.impl;

import java.math.BigDecimal;
import java.util.Date;
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
import com.southgt.smosplat.data.dao.ISWDataDao;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.service.ISWService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;

/**
 * 
 * 水位数据服务实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月2日     姚家俊       v1.0.0        create</p>
 *
 */
@Service("swService")
public class SWServiceImpl extends BaseServiceImpl<SW_Data> implements ISWService{
	@Resource
	ISurveyPoint_SWService surveyPointSWService;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource(name="swDao")
	@Override
	public void setDao(IBaseDao<SW_Data> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, Object> getLatestSWDatasByProject(Project project) {
		Map<String, Object> result=new HashMap<String, Object>();
		Map<String, Object> dataResult = new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids =new ArrayList<>();
		//每一个点的最新11个时间点的数据
		Map<String, List<SW_Data>> spLatestDatas=new HashMap<String, List<SW_Data>>();
		//每一个点的第一个时间点的数据
		Map<String, SW_Data> spFirstData=new HashMap<String, SW_Data>();
		//地下水位
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(12);
		List<SurveyPoint_SW> sps;
		//测量的时间点
		List<Date> latestTimes;
		//获得所有监测点第一个时间点的数据
		List<SW_Data> allFirstSWData;
		//所有监测点最新11个时间点的数据
		List<SW_Data> latestAllSWData;
		try{
			// 首先获取对应项目下对应监测项的所有水位监测点
			sps = surveyPointSWService.getSP_SWs(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				dataResult.put(sps.get(i).getCode(), new ArrayList<SW_Data>());
			}
			result.put("surveyPoints", sps);
			//数据库没有点
			if(sps.size() == 0){
	//			throw(new Exception("数据库没有点！"));
				return result;
			}
			sps.forEach(p->spUuids.add(p.getSurveyPointUuid()));
			
			latestAllSWData = ((ISWDataDao)getDao()).getLatestSWDatasBySurveyPoints(spUuids);
			allFirstSWData = ((ISWDataDao)getDao()).getFirstOneSWDatasBySurveyPoints(spUuids);
			//测量的时间点
			latestTimes = ((ISWDataDao)getDao()).getLatestTimes(spUuids);
		}catch(Exception ex){
			result.put("dataList", dataResult);
			return result;
		}
		List<Integer> indexOfNoData = new ArrayList<>();
		for (int i = 0; i < sps.size(); i++) {
			//获得最新11个时间点的所有解算数据
			String spUuid = sps.get(i).getSurveyPointUuid();
			List<SW_Data> latestSWData = latestAllSWData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
			if(latestSWData.size() > 0 && latestSWData.size() == latestTimes.size()){
				spLatestDatas.put(sps.get(i).getCode(), latestSWData);
			}else{
				//单独这个点再取一次
				List<String> s = new ArrayList<>();
				s.add(spUuid);
				List<SW_Data> swLatestItem = ((ISWDataDao)getDao()).getLatestSWDatasBySurveyPoints(s);
				if(swLatestItem != null && swLatestItem.size() > 0){
					spLatestDatas.put(sps.get(i).getCode(), swLatestItem);
				}
			}
			List<SW_Data> tempList = allFirstSWData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
			if(tempList !=null && tempList.size()>0){
				SW_Data firstSWData = tempList.get(0);
				spFirstData.put(sps.get(i).getCode(), firstSWData);
			}else{
				//取不到第一条，单独取一次
				List<String> s = new ArrayList<>();
				s.add(spUuid);
				List<SW_Data> swFirstItem = ((ISWDataDao)getDao()).getFirstOneSWDatasBySurveyPoints(s);
				if(swFirstItem != null && swFirstItem.size() > 0){
					spFirstData.put(sps.get(i).getCode(), swFirstItem.get(0));
				}else{
					//这个点没有第一条数据，就是从来没返回过数据
					indexOfNoData.add(i);
				}
			}
		}
		//计算累计位移
		for (int i = 0; i < sps.size(); i++) {
			int t = i;
			List<Integer> passIndex = indexOfNoData.stream().filter(p -> p.equals(t)).collect(Collectors.toList());
			if(passIndex.size() > 0){
				List<SW_Data> dataAtOnePoint = new ArrayList<>();
				for(int kk = 0; kk < latestTimes.size(); kk ++){
					SW_Data sdata = new SW_Data();
					sdata.setCollectTime(latestTimes.get(kk));
					dataAtOnePoint.add(sdata);
				}
				//没有数据的点，只返回一个时间
				dataResult.put(sps.get(i).getCode(), dataAtOnePoint);
				continue;
			}
			//一个点的最新11条数据
			List<SW_Data> latestDataAtOnePoint = (List<SW_Data>) spLatestDatas.get(sps.get(i).getCode());
			//只在有数据的情况下才进行计算
			if (latestDataAtOnePoint != null && latestDataAtOnePoint.size() > 0) {
				for (int j = 0; j < latestDataAtOnePoint.size(); j++) {
					if (j < latestDataAtOnePoint.size() - 1) {
						// 水位计算值单次位移
						double gapOffset = latestDataAtOnePoint.get(j + 1).getCalValue() - latestDataAtOnePoint.get(j).getCalValue();
						//保留2位小数
						BigDecimal b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
		    			String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestDataAtOnePoint.get(j + 1).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestDataAtOnePoint.get(j).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						double gapChangeRate = gapOffset/days;
						b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						// 水位计算值累计位移
						double accumOffset = latestDataAtOnePoint.get(j + 1).getCalValue()- spFirstData.get(sps.get(i).getCode()).getCalValue() + sps.get(i).getOriginalTotalValue();
						b = new BigDecimal(accumOffset); 
						accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
						latestDataAtOnePoint.get(j + 1).setAccumOffset(accumOffset);
						latestDataAtOnePoint.get(j + 1).setGapOffset(gapOffset);
						latestDataAtOnePoint.get(j + 1).setGapChangeRate(gapChangeRate);
					} else if(latestDataAtOnePoint.size()>10){
						latestDataAtOnePoint.remove(0);
					}
				}
//				List<Integer> timesWithNoData = new ArrayList<>();
//				//这个点最新的数据条数和全体最新的数据时间点个数不一致，证明有的时间点这个点没采到
//				if(latestDataAtOnePoint.size() != latestTimes.size()){
//					for(int m = 0; m < latestTimes.size(); m ++){
//						for(int n = 0; n < latestDataAtOnePoint.size(); n ++){
//							if(!latestDataAtOnePoint.get(n).getCollectTime().equals(latestTimes.get(m)) && !timesWithNoData.contains(m)){
//								//找出全体最新时间点中没采到数据的时间点
//								timesWithNoData.add(m);
//							}
//						}
//					}
//					for(int l = 0; l < timesWithNoData.size(); l ++){
//						SW_Data sdata = new SW_Data();
//						sdata.setCollectTime(latestTimes.get(timesWithNoData.get(l)));
//						latestDataAtOnePoint.add(sdata);
//					}
//				}
				dataResult.put(sps.get(i).getCode(), latestDataAtOnePoint);
			}else{
				dataResult.put(sps.get(i).getCode(), new ArrayList<SW_Data>());
			}
		}
		
		result.put("dataList", dataResult);
		return result;
	}

	@Override
	public List<SW_Data> getLatestSWDatasBySurveyPoints(List<String> surveyPointUuids) {
		return ((ISWDataDao)getDao()).getLatestSWDatasBySurveyPoints(surveyPointUuids);
	}

	@Override
	public List<SW_Data> getFirstOneSWDatasBySurveyPoints(List<String> surveyPointUuids) {
		return ((ISWDataDao)getDao()).getFirstOneSWDatasBySurveyPoints(surveyPointUuids);
	}

	@Override
	public List<SW_Data> getLatestOneSWDatasBySurveyPoints(List<String> surveyPointUuids) {
		return ((ISWDataDao)getDao()).getLatestOneSWDatasBySurveyPoints(surveyPointUuids);
	}

	@Override
	public void deleteSW_DataBySurveyPoint(String surveyPointUuid) {
		((ISWDataDao)getDao()).deleteSW_DataBySurveyPoint(surveyPointUuid);
		
	}
}
