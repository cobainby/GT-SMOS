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
import com.southgt.smosplat.data.dao.IMTDataDao;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.service.IMTService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_MTService;
@Service("mtService")
public class MTServiceImpl extends BaseServiceImpl<MT_Data> implements IMTService{

	@Resource
	ISurveyPoint_MTService surveyPointMTService;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource(name="mtDao")
	@Override
	public void setDao(IBaseDao<MT_Data> dao) {
		super.setDao(dao);
	}
	
	@Override
	public Map<String, Object> getLatestMTDatasByProject(Project project) {
		Map<String, Object> result=new HashMap<String, Object>();
		Map<String, Object> dataResult = new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids =new ArrayList<>();
		//锚杆内力
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(18);
		// 首先获取对应项目下对应监测项的所有锚杆内力监测点
		List<SurveyPoint_MT> sps;
		//每一个点的最新11个时间点的数据
		Map<String, List<MT_Data>> spLatestDatas = new HashMap<String, List<MT_Data>>();
		//每一个点的第一个时间点的数据
//		Map<String, MT_Data> spFirstData = new HashMap<String, MT_Data>();
		//所有监测点最新11个时间点的数据
		List<MT_Data> latestAllZCData;
		//获得所有监测点第一个时间点的坐标数据
//		List<MT_Data> allFirstZCData;
		List<Date> collectTimes;
		try{
			sps = surveyPointMTService.getSP_MTs(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				dataResult.put(sps.get(i).getCode(), new ArrayList<MT_Data>());
			}
			//数据库没有点
			if(sps.size() == 0){
				return result;
			}
			result.put("surveyPoints", sps);
			sps.forEach(p->spUuids.add(p.getSurveyPointUuid()));
			//最新的测量次数，小于等于11
			collectTimes = ((IMTDataDao)getDao()).getLatestTimes(spUuids);
			//所有监测点最新11个时间点的数据
			latestAllZCData = ((IMTDataDao)getDao()).getLatestMTDatasBySurveyPoints(spUuids);
			//获得所有监测点第一个时间点的坐标数据
//			allFirstZCData = ((IMTDataDao)getDao()).getFirstOneMTDatasBySurveyPoints(spUuids);
		}catch(Exception ex){
			result.put("dataList", dataResult);
			return result;
		}
		
		for (int i = 0; i < sps.size(); i++) {
			//获得最新11个时间点的所有解算数据
			String spUuid = sps.get(i).getSurveyPointUuid();
			List<MT_Data> latestMTData = latestAllZCData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
			if(latestMTData.size() > 0 && latestMTData.size() == collectTimes.size()){
				spLatestDatas.put(sps.get(i).getCode(), latestMTData);
			}else{
				List<String> s = new ArrayList<>();
				s.add(spUuid);
				//这个点的测量次数和总体的最新测量次数不符，单独取一次
				List<MT_Data> mtLastestItem = ((IMTDataDao)getDao()).getLatestMTDatasBySurveyPoints(s);
				if(mtLastestItem != null && mtLastestItem.size() > 0){
					spLatestDatas.put(sps.get(i).getCode(), mtLastestItem);
				}
			}
//			List<MT_Data> tempList = allFirstZCData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
//			if(tempList !=null && tempList.size()>0){
//				MT_Data firstZCData = tempList.get(0);
//				spFirstData.put(sps.get(i).getCode(), firstZCData);
//			}else{
//				//取不到第一条，单独取一次
//				List<String> s = new ArrayList<>();
//				s.add(spUuid);
//				List<MT_Data> mtFirstItem = ((IMTDataDao)getDao()).getFirstOneMTDatasBySurveyPoints(s);
//				if(mtFirstItem != null && mtFirstItem.size()>0){
//					spFirstData.put(sps.get(i).getCode(), mtFirstItem.get(0));
//				}
//			}
		}
		//计算累计位移
		for (int i = 0; i < sps.size(); i++) {
			//一个点的最新11条数据
			List<MT_Data> latestDataAtOnePoint = (List<MT_Data>) spLatestDatas.get(sps.get(i).getCode());
			//只在有数据的情况下才进行计算
			if (latestDataAtOnePoint != null && latestDataAtOnePoint.size() > 0) {
				for (int j = 0; j < latestDataAtOnePoint.size(); j++) {
					if (j < latestDataAtOnePoint.size() - 1) {
						// 轴力计算值单次位移
						double gapOffset = latestDataAtOnePoint.get(j + 1).getCalValue() - latestDataAtOnePoint.get(j).getCalValue();
						//四舍五入保留4位小数
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
						// 轴力计算值累计位移
//						double accumOffset = 0.0;
//						if(spFirstData.get(sps.get(i).getCode()).getCalValue() != null){
//							accumOffset = latestDataAtOnePoint.get(j + 1).getCalValue() - spFirstData.get(sps.get(i).getCode()).getCalValue() + sps.get(i).getOriginalTotalValue();
//						}
//						b = new BigDecimal(accumOffset); 
//						accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
//						latestDataAtOnePoint.get(j + 1).setAccumOffset(accumOffset);
						latestDataAtOnePoint.get(j + 1).setGapOffset(gapOffset);
						latestDataAtOnePoint.get(j + 1).setGapChangeRate(gapChangeRate);
					} else if(latestDataAtOnePoint.size()>10){
						latestDataAtOnePoint.remove(0);
					}
				}
//				List<Integer> timesWithNoData = new ArrayList<>();
//				List<Date> dataTimes = new ArrayList<>();
//				latestDataAtOnePoint.stream().forEach(p -> dataTimes.add(p.getCollectTime()));
//				//这个点最新的数据条数和全体最新的数据时间点个数不一致，证明有的时间点这个点没采到
//					for(int m = 0; m < collectTimes.size(); m ++){
//						if(!dataTimes.contains(collectTimes.get(m))){
//							timesWithNoData.add(m);
//						}
//					}
//					for(int l = 0; l < timesWithNoData.size(); l ++){
//						if(latestDataAtOnePoint.size() == collectTimes.size()){
//							break;
//						}
//						MT_Data mtdata = new MT_Data();
//						mtdata.setCollectTime(collectTimes.get(timesWithNoData.get(l)));
//						latestDataAtOnePoint.add(mtdata);
//					}
				dataResult.put(sps.get(i).getCode(), latestDataAtOnePoint);
			}else{
				dataResult.put(sps.get(i).getCode(), new ArrayList<>());
			}
		}
		result.put("dataList", dataResult);
		return result;
	}

	@Override
	public void deleteMTDataBySurveyPoint(String surveyPointUuid) {
		((IMTDataDao)getDao()).deleteMTDataBySP(surveyPointUuid);
		
	}

	@Override
	public List<MT_Data> getFirstOneMTDatasBySurveyPoints(List<String> surveyPointUuids) {
		return ((IMTDataDao)getDao()).getFirstOneMTDatasBySurveyPoints(surveyPointUuids);
	}

	@Override
	public List<MT_Data> getLatestOneMTDatasBySurveyPoints(List<String> surveyPointUuids) {
		return ((IMTDataDao)getDao()).getLatestOneMTDatasBySurveyPoints(surveyPointUuids);
	}

}
