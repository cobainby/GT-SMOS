package com.southgt.smosplat.data.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.Arith;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.data.dao.IWYSCoordDataDao;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.WYS_OriginalData;
import com.southgt.smosplat.data.service.IWYSCoordDataService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.data.util.math.Station;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Section;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPointService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月8日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("wysCoordDataService")
public class WYSCoordDataServiceImpl extends BaseServiceImpl<WYS_CoordData> implements IWYSCoordDataService {
	
	@Resource
	ISurveyPointService surveyPointServcie;
	
	@Resource
	ISurveyPoint_WYSService sp_WYSService;
	
	@Resource
	IWYSCoordDataDao wysCoordDataDao;
	
	@Resource
	IMonitorItemService monitorItemService;

	@Resource(name="wysCoordDataDao")
	@Override
	public void setDao(IBaseDao<WYS_CoordData> dao) {
		super.setDao(dao);
	}
	
	
	
	@Override
	public Map<String, Object> getLatestWYSCoordDatasByProject(Project project) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> dataResult = new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids = new ArrayList<>();
		//水平位移
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(1);
		// 首先获取对应项目下对应监测项的所有监测点
		List<SurveyPoint_WYS> sps = surveyPointServcie.getSurveyPoints(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
		for(int i = 0; i < sps.size(); i++){
			dataResult.put(sps.get(i).getCode(), new ArrayList<WYS_CoordData>());
		}
		//数据库没有点
		if(sps == null || sps.size() == 0){
//			throw(new Exception("数据库没有点！"));
			return result;
		}
		result.put("surveyPoints", sps);
		//每一个点的第一个时间点的数据
		Map<String, WYS_CoordData> spFirstThreeData = new HashMap<String, WYS_CoordData>();
		try{
			sps.forEach(p->spUuids.add(p.getSurveyPointUuid()));
			//所有监测点最新11个时间点的数据
		}catch(Exception ex){
			System.out.println(ex);
			result.put("spDatas", dataResult);
			return result;
		}
		
		for (int i = 0; i < sps.size(); i++) {
			String spUuid = sps.get(i).getSurveyPointUuid();
				
			List<String> spId = new ArrayList<String>();
			spId.add(spUuid);
			//头三条数据
			List<WYS_CoordData> firstData = ((IWYSCoordDataDao)getDao()).getFirstWYSCoordDataBySurveyPoint(spId);
			if(firstData.size() > 0){
				spFirstThreeData.put(sps.get(i).getCode(), firstData.get(0));
			}
		}
		//计算累计位移
		for (int i = 0; i < sps.size(); i++) {
			List<String> spId = new ArrayList<>();
			spId.add(sps.get(i).getSurveyPointUuid());
			
			double aveX = 0.0;
			double aveY = 0.0;
			//头三条数据
			List<WYS_CoordData> firstThreeData = ((IWYSCoordDataDao)getDao()).getFirstThreeDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
			List<String> firstThreeSpId = new ArrayList<>();
			
			if(firstThreeData.size() > 0){
				for(int kk = 0; kk < firstThreeData.size(); kk ++){
					aveX += firstThreeData.get(kk).getCaculateN();
					aveY += firstThreeData.get(kk).getCaculateE();
//					aveY = Arith.add(aveY, firstThreeData.get(kk).getCaculateE());
					firstThreeSpId.add(firstThreeData.get(kk).getSurveyPoint().getSurveyPointUuid());
				}
				//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
				firstThreeData.get(0).setCaculateE(aveY / firstThreeData.size());
				firstThreeData.get(0).setCaculateN(aveX / firstThreeData.size());
			}
			//一个点的最新11条数据
			List<WYS_CoordData> dataAtOnePoint = ((IWYSCoordDataDao)getDao()).getLatestWYSCoordDataBySurveyPoint(spId);
			
			//只在有数据的情况下才进行计算
			if (dataAtOnePoint!=null && dataAtOnePoint.size() > 0) {
				for (int j = 0; j < dataAtOnePoint.size(); j++) {
					if (j < dataAtOnePoint.size() - 1) {
						Section section = dataAtOnePoint.get(j).getSurveyPoint().getSection();
						//根据断面设置的点名得到以第一次测量为基准的断面的起始点和终止点的坐标数据
						//端面起始点
						WYS_CoordData start = null;
						start = (WYS_CoordData)spFirstThreeData.get(section.getStartPointName());
						WYS_CoordData end=null;
						//断面终止点
						end = (WYS_CoordData)spFirstThreeData.get(section.getEndPointName());
						if(start == null || end == null){
							//如果断面选择的起始点和终止点没有测到数据
							continue;
						}
						double sectionAzimuth = GtMath.calculateSectionAngle(start, end);
						// 东累计位移，化为毫米
						double accumulateEOffset = GtMath.y_Displacement(dataAtOnePoint.get(j + 1).getCaculateN(),
								dataAtOnePoint.get(j + 1).getCaculateE(), sectionAzimuth,
								firstThreeData.get(0).getCaculateN(), firstThreeData.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
						// 东单次位移,化为毫米
						double gapEOffset = GtMath.y_Displacement(dataAtOnePoint.get(j + 1).getCaculateN(),
								dataAtOnePoint.get(j + 1).getCaculateE(), sectionAzimuth,
								dataAtOnePoint.get(j).getCaculateN(), dataAtOnePoint.get(j).getCaculateE())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dataAtOnePoint.get(j + 1).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dataAtOnePoint.get(j).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						double changeRate = gapEOffset/days;
						// 保留3位小数
						String b = new java.text.DecimalFormat("#.000").format(changeRate);
						changeRate = Double.parseDouble(b);
						
						// 保留1位小数
						b = new java.text.DecimalFormat("#.0").format(accumulateEOffset);
						accumulateEOffset = Double.parseDouble(b);
						b = new java.text.DecimalFormat("#.0").format(gapEOffset);
						gapEOffset = Double.parseDouble(b);
						// 高程单次位移
						double gapHOffset = dataAtOnePoint.get(j + 1).getCaculateH()
								- dataAtOnePoint.get(j).getCaculateH();
						// 高程累计位移
						double accumHOffset = dataAtOnePoint.get(j + 1).getCaculateH()
								- firstThreeData.get(0).getCaculateH();
						b = new java.text.DecimalFormat("#.000").format(accumHOffset);
						accumHOffset = Double.parseDouble(b);
						// 北单次变化量
						double gapNOffset = GtMath.y_Displacement(dataAtOnePoint.get(j + 1).getCaculateN(),
								dataAtOnePoint.get(j + 1).getCaculateE(), sectionAzimuth,
								dataAtOnePoint.get(j).getCaculateN(), dataAtOnePoint.get(j).getCaculateE());
						b = new java.text.DecimalFormat("#.000").format(gapNOffset);
						gapNOffset = Double.parseDouble(b);
						// 北累计变化量
						double accumNOffset = GtMath.y_Displacement(dataAtOnePoint.get(j + 1).getCaculateN(),
								dataAtOnePoint.get(j + 1).getCaculateE(), sectionAzimuth,
								firstThreeData.get(0).getCaculateN(), firstThreeData.get(0).getCaculateE());
						b = new java.text.DecimalFormat("#.000").format(accumNOffset);
						accumNOffset = Double.parseDouble(b);
						dataAtOnePoint.get(j + 1).setAccumEOffset(accumulateEOffset);
						dataAtOnePoint.get(j + 1).setAccumHOffset(accumHOffset);
						dataAtOnePoint.get(j + 1).setAccumNOffset(accumNOffset);
						dataAtOnePoint.get(j + 1).setGapEOffset(gapEOffset);
						dataAtOnePoint.get(j + 1).setChangeRate(changeRate);
						dataAtOnePoint.get(j + 1).setGapNOffset(gapNOffset);
						dataAtOnePoint.get(j + 1).setGapHOffset(gapHOffset);
					} else if(dataAtOnePoint.size()>10){
						dataAtOnePoint.remove(0);
					}
				}
				dataResult.put(sps.get(i).getCode(), dataAtOnePoint);
			}else{
				dataResult.put(sps.get(i).getCode(), new ArrayList<WYS_CoordData>());
			}
		}
		result.put("spDatas", dataResult);
		return result;
	
	}

	@Override
	public List<WYS_CoordData> checkCoordData(List<WYS_OriginalData> lstOriginalData, Station station){
		List<WYS_OriginalData> uniqueList = new ArrayList<>();
		uniqueList.addAll(lstOriginalData);
		//得到唯一的点列表
		uniqueList = removeDuplicate(uniqueList);
		List<WYS_CoordData> resultData = new ArrayList<>();
		//找出第一次和第二次观测的后视点(由于现场观测人员顺序问题，有可能不止4个。
		List<WYS_OriginalData> first = lstOriginalData.stream().filter(p->p.getPointType().equals((byte)1)).collect(Collectors.toList());
		WYS_CoordData firstCoord = new WYS_CoordData();
		List<WYS_OriginalData> second = lstOriginalData.stream().filter(p->p.getPointType().equals((byte)2)).collect(Collectors.toList());
		WYS_CoordData secondCoord = new WYS_CoordData();
		//水平角闭合差平差。无论几个测回，都算成平均一次
		double delAngel = 0.0;
		if(first.size() >= 2 && second.size() >= 2){
			firstCoord = new WYSServiceImpl().average(first);
			secondCoord = new WYSServiceImpl().average(second);
			delAngel = secondCoord.getCaculateHA() - firstCoord.getCaculateHA();
		}
		for(int i=0; i < lstOriginalData.size(); i++){
			double ha = lstOriginalData.get(i).getHa();
			lstOriginalData.get(i).setHa(ha + delAngel/uniqueList.size());
		}
		//根据角度距离求坐标
		for(int j = 0; j < uniqueList.size(); j++){
			String name = uniqueList.get(j).getSurveyPoint().getCode();
			List<WYS_OriginalData> samePointData = lstOriginalData.stream().filter(p->p.getSurveyPoint().getCode().equals(name)).collect(Collectors.toList());
			WYS_CoordData coordData = new WYSServiceImpl().average(samePointData);
			coordData.setSurveyTime(samePointData.get(0).getSurveyTime());
			//一个点的数据，根据角度和距离求出东北高
			coordData = GtMath.calulateManualCoord(station, coordData, samePointData.get(0).getPrismH());
			coordData.setSurveyPoint(uniqueList.get(j).getSurveyPoint());
			resultData.add(coordData);
		}
		return resultData;
	}
	

	@Override
	public Map<String, Object> calOffset(List<WYS_CoordData> resultData ,  Warning hWarning) throws Exception {
		Map<String,Object> map = new HashMap();
		//超过报警值部分
		List<String> gapRateWarningPoints = new ArrayList<>();
		List<Double> gapRateWarningOffset = new ArrayList<>();
		List<Float>  gapRateWarningVal = new ArrayList<>();
		//超过报警值部分
		List<String> accumWarningPoints = new ArrayList<>();
		List<Double> accumWarningOffset = new ArrayList<>();
		List<Float>  accumWarningVal = new ArrayList<>();
		//超过控制值部分
		List<String> accumControlWarningPoints = new ArrayList<>();
		List<Double> accumControlWarningOffset = new ArrayList<>();
		List<Float>  accumControlWarningVal = new ArrayList<>();
		
		//所有解算坐标点的监测点Uuid的List
		List<String> spUuids = new ArrayList<>();
		List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(hWarning.getProject().getProjectUuid());
		resultData.forEach(p->spUuids.add(p.getSurveyPoint().getSurveyPointUuid()));
	    //根据断面设置的点名得到同一个测量时间的断面的起始点和终止点的坐标数据
		for(int k = 0; k < resultData.size(); k++){
			List<String> spId = new ArrayList<>();
			spId.add(resultData.get(k).getSurveyPoint().getSurveyPointUuid());
			double aveX = 0.0;
			double aveY = 0.0;
			//头三条数据
			List<WYS_CoordData> firstThreeData = ((IWYSCoordDataDao)getDao()).getFirstThreeDataBySurveyPoint(resultData.get(k).getSurveyPoint().getSurveyPointUuid());
			List<String> firstThreeSpId = new ArrayList<>();
			if(firstThreeData.size() > 0){
				for(int kk = 0; kk < firstThreeData.size(); kk ++){
					aveX += firstThreeData.get(kk).getCaculateN();
					aveY += firstThreeData.get(kk).getCaculateE();
					firstThreeSpId.add(firstThreeData.get(kk).getSurveyPoint().getSurveyPointUuid());
				}
				//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
				firstThreeData.get(0).setCaculateE(aveY / firstThreeData.size());
				firstThreeData.get(0).setCaculateN(aveX / firstThreeData.size());
			}
			
			//上一条数据
			List<WYS_CoordData> latestAllCoordData = ((IWYSCoordDataDao)getDao()).getLatestOneWYSCoordDataBySurveyPoint(spId);
			
			String startName = resultData.get(k).getSurveyPoint().getSection().getStartPointName();
			String endName = resultData.get(k).getSurveyPoint().getSection().getEndPointName();
			if(startName == null){
				throw(new Exception("【"+resultData.get(k).getSurveyPoint().getSection().getSectionName()+"】"+"断面起始点设置出错，请检查！"));
			}
			if(endName == null){
				throw(new Exception("【"+resultData.get(k).getSurveyPoint().getSection().getSectionName()+"】"+"断面终止点设置出错，请检查！"));
			}
			if(startName.equals(endName)){
				throw(new Exception("【"+resultData.get(k).getSurveyPoint().getSection().getSectionName()+"】"+"起始/终止点不能为同一个！"));
			}
			List<SurveyPoint_WYS> startPoint = sps.stream().filter(p->p.getCode().equals(startName)).collect(Collectors.toList());
			List<SurveyPoint_WYS> endPoint = sps.stream().filter(p->p.getCode().equals(endName)).collect(Collectors.toList());
			List<String> spId1 = new ArrayList<>();
			spId1.add(startPoint.get(0).getSurveyPointUuid());
			List<String> spId2 = new ArrayList<>();
			spId2.add(endPoint.get(0).getSurveyPointUuid());
			WYS_CoordData start = null;
			//第一条数据
			List<WYS_CoordData> tempList1 = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spId1);
			//本次测回来的数据
			List<WYS_CoordData> tempList11 = resultData.stream().filter(p->p.getSurveyPoint().getCode().equals(startName)).collect(Collectors.toList());
			if(tempList1.size() > 0){
				start = tempList1.get(0);
			}else if(tempList11.size() > 0){
				start = tempList11.get(0);
			}
			WYS_CoordData end = null;
			//第一条数据
			List<WYS_CoordData> tempList2 = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spId2);
			//本次测回来的数据
			List<WYS_CoordData> tempList22 = resultData.stream().filter(p->p.getSurveyPoint().getCode().equals(endName)).collect(Collectors.toList());
			if(tempList2.size() > 0){
				end = tempList2.get(0);
			}else if(tempList22.size() > 0){
				end = tempList22.get(0);
			}
			if(start == null || end == null){
				LogUtil.error("这个点断面出错："+resultData.get(k).getSurveyPoint().getCode()+";"+"起始点:"+startName+";"+"终止点:"+endName+";");
				if(start == null){
					throw(new Exception("【"+resultData.get(k).getSurveyPoint().getSection().getSectionName()+"】"+"断面起始点"+startName+"测量失败，数据库中也没有起始点信息。请重测/更换起始点"));
				}
				if(end == null){
					throw(new Exception("【"+resultData.get(k).getSurveyPoint().getSection().getSectionName()+"】"+"断面终止点"+endName+"测量失败，数据库中也没有起始点信息。请重测/更换终止点"));
				}
			}
			if(latestAllCoordData != null && latestAllCoordData.size() > 0){
				//最新一条数据
				WYS_CoordData latestData = latestAllCoordData.get(0);
				//预警限差
				Warning warning = latestData.getSurveyPoint().getWarning();
				//第一条数据
				if(firstThreeData != null && firstThreeData.size()>0){
					WYS_CoordData firstData = firstThreeData.get(0);
					double sectionAzimuth = GtMath.calculateSectionAngle(start, end);
					// 东累计位移
					double accumulateEOffset = GtMath.y_Displacement(resultData.get(k).getCaculateN(),
							resultData.get(k).getCaculateE(), sectionAzimuth,
							firstData.getCaculateN(), firstData.getCaculateE())*1000 + resultData.get(k).getSurveyPoint().getOriginalTotalValue();
					// 东单次位移
					double gapEOffset = GtMath.y_Displacement(resultData.get(k).getCaculateN(),
							resultData.get(k).getCaculateE(), sectionAzimuth,
							latestData.getCaculateN(), latestData.getCaculateE())*1000;
					
					String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultData.get(k).getSurveyTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestData.getSurveyTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					//取整数位，进1法。1.1->2,0.9->1;
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					double changeRate = gapEOffset/days;
					// 保留3位小数
					String b = new java.text.DecimalFormat("#.0").format(changeRate);
					changeRate = Double.parseDouble(b);
					
					// 保留1位小数
					b = new java.text.DecimalFormat("#.0").format(accumulateEOffset);
					accumulateEOffset = Double.parseDouble(b);
					b = new java.text.DecimalFormat("#.0").format(gapEOffset);
					gapEOffset = Double.parseDouble(b);
					// 高程单次位移
					double gapHOffset = resultData.get(k).getCaculateH()
							- latestData.getCaculateH();
					// 高程累计位移
					double accumHOffset = resultData.get(k).getCaculateH()
							- firstData.getCaculateH();
					b = new java.text.DecimalFormat("#.000").format(accumHOffset);
					accumHOffset = Double.parseDouble(b);
					//高程单次变化量
					double gapHChangeRate = gapHOffset/days;
					// 北单次变化量
					double gapNOffset = GtMath.y_Displacement(resultData.get(k).getCaculateN(),
							resultData.get(k).getCaculateE(), sectionAzimuth,
							latestData.getCaculateN(), latestData.getCaculateE());
					b = new java.text.DecimalFormat("#.000").format(gapNOffset);
					gapNOffset = Double.parseDouble(b);
					// 北累计变化量
					double accumNOffset = GtMath.y_Displacement(resultData.get(k).getCaculateN(),
							resultData.get(k).getCaculateE(), sectionAzimuth,
							firstData.getCaculateN(), firstData.getCaculateE());
					b = new java.text.DecimalFormat("#.000").format(accumNOffset);
					accumNOffset = Double.parseDouble(b);
					resultData.get(k).setAccumEOffset(accumulateEOffset);
					resultData.get(k).setAccumHOffset(accumHOffset);
					resultData.get(k).setAccumNOffset(accumNOffset);
					resultData.get(k).setGapEOffset(gapEOffset);
					resultData.get(k).setChangeRate(changeRate);
					resultData.get(k).setGapNOffset(gapNOffset);
					resultData.get(k).setGapHOffset(gapHOffset);
					//高程单次变化量
					resultData.get(k).sethChangeRate(gapHChangeRate);
					SurveyPoint_WYS surveyPoint = resultData.get(k).getSurveyPoint();
					//单次变化速率绝对值超过单次变化速率报警值绝对值
					if(Math.abs(resultData.get(k).getChangeRate()) > Math.abs(warning.getWarnSingleRate()) || Math.abs(resultData.get(k).gethChangeRate()) > Math.abs(hWarning.getWarnSingleRate())){
						gapRateWarningPoints.add(surveyPoint.getCode());
						gapRateWarningOffset.add(Math.abs(resultData.get(k).getChangeRate()));
						gapRateWarningVal.add(warning.getWarnSingleRate());
					}
					if(Math.abs(accumulateEOffset) > Math.abs(warning.getWarnAccum()) || Math.abs(resultData.get(k).getAccumHOffset()) > Math.abs(hWarning.getWarnAccum())){
						accumWarningPoints.add(surveyPoint.getCode());
						accumWarningOffset.add(Math.abs(accumulateEOffset));
						accumWarningVal.add(warning.getWarnAccum());
						//累计变化量绝对值超过累计变化量控制值绝对值
						if(Math.abs(accumulateEOffset) > Math.abs(warning.getControlAccum()) || Math.abs(resultData.get(k).getAccumHOffset()) > Math.abs(hWarning.getControlAccum())){
							accumControlWarningPoints.add(surveyPoint.getCode());
							accumControlWarningOffset.add(Math.abs(accumulateEOffset));
							accumControlWarningVal.add(warning.getControlAccum());
						}
					}
				}else{
					resultData.get(k).setAccumEOffset(0.0);
					resultData.get(k).setAccumHOffset(0.0);
					resultData.get(k).setAccumNOffset(0.0);
					resultData.get(k).setGapEOffset(0.0);
					resultData.get(k).setChangeRate(0.0);
					resultData.get(k).setGapNOffset(0.0);
					resultData.get(k).setGapHOffset(0.0);
				}
			}
		}
		map.put("gapRateWarningPoints", gapRateWarningPoints);
//		map.put("gapRateWarningOffset", gapRateWarningOffset);
//		map.put("gapRateWarningVal", gapRateWarningVal);
		
		map.put("accumWarningPoints", accumWarningPoints);
//		map.put("accumWarningOffset", accumWarningOffset);
//		map.put("accumWarningVal", accumWarningVal);
		
		map.put("accumControlWarningPoints", accumControlWarningPoints);
//		map.put("accumControlWarningOffset", accumControlWarningOffset);
//		map.put("accumControlWarningVal", accumControlWarningVal);
		
		map.put("coordData", resultData);
		
		//把最新上传的一次数据推送到前台
		String msg1 = JsonUtil.beanToJson(resultData);
		ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/projectWYSCollect/"+hWarning.getProject().getProjectUuid(), msg1);
		return map;
	}
	

	
	/**
	 * 
	 * 除去点名重复的项
	 * @date  2017年4月25日 下午3:03:30
	 * 
	 * @param list
	 * @return
	 * List<WYS_OriginalData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月25日     姚家俊      v1.0          create</p>
	 *
	 */
	public  static List<WYS_OriginalData> removeDuplicate(List<WYS_OriginalData> list)  {       
		  for  ( int  i = 0; i < list.size() - 1; i ++)  {       
		      for  ( int j = list.size() - 1 ; j > i; j--)  {       
		           if  (list.get(j).getSurveyPoint().getCode().equals(list.get(i).getSurveyPoint().getCode()))  {       
		              list.remove(j);       
		            }        
		        }        
		      }        
		    return list;       
		}

	@Override
	public void deleteWYSLevelDataBySurveyPoint(String spUuid) {
		((IWYSCoordDataDao)getDao()).deleteWYSDataBySurveyPoint(spUuid);
		
	}

	@Override
	public void saveCoordData(List<WYS_CoordData> data, List<String> gapRateEarlyWarningPoints, List<String> accumEarlyWarningPoints, byte flag) {
 		if(flag == 0){
			//只上传不超限部分
			for(int i = 0; i< data.size(); i++){
				String pn = data.get(i).getSurveyPoint().getCode();
				List<String> overrunChangeRatePoints = gapRateEarlyWarningPoints.stream().filter(p -> p.equals(pn)).collect(Collectors.toList());
				List<String> overrunAccumPoints = accumEarlyWarningPoints.stream().filter(p -> p.equals(pn)).collect(Collectors.toList());
				if(overrunChangeRatePoints.size() > 0 || overrunAccumPoints.size() > 0){
					continue;
				}
				//原始数据入库
				this.saveEntity(data.get(i));
			}
		}else if(flag == 1){
			//全部上传
			for(int i = 0; i< data.size(); i++){
				//原始数据入库
				this.saveEntity(data.get(i));
			}
		}
	}






}
