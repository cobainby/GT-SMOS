package com.southgt.smosplat.data.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.FileUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.data.dao.ICXDataDao;
import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.data.service.ICXService;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_CXService;
import com.southgt.smosplat.project.service.IWarnningDataService;
/**
 * 
 * 测斜数据服务实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月10日     姚家俊       v1.0.0        create</p>
 *
 */
@Service("cxService")
public class CXServiceImpl extends BaseServiceImpl<CX_Data> implements ICXService{

	@Resource
	ISurveyPoint_CXService surveyPointCXService;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IProjectService projectService;
	
	@Resource
	IWarnningDataService warningDataService;
	
	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;
	
	@Resource(name="cxDao")
	@Override
	public void setDao(IBaseDao<CX_Data> dao) {
		super.setDao(dao);
	}
	@Override
	public Map<String, Object> checkCXData(String jsonOriginalfData, String sourceData) throws Exception {
		Map<String,Object>map = new HashMap();
		List<List<CX_Data>> resultList = new ArrayList<>();
		//这个是resultList降维后的list
		List<CX_Data> cxDataList = new ArrayList<>();
		
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
		//超限的点上一条数据
		List<Double> lastTimeOffset = new ArrayList<>();
		//超限的点第一条数据
		List<Double> firstTimeOffset = new ArrayList<>();
		//本次测量（计算值）
		Map<String, Object> currentValMap = new HashMap();
		ObjectMapper mapper = new ObjectMapper();
		SurveyPoint_CX surveyPoint = null;
		//所有解算坐标点的监测点Uuid的List
		List<String> spUuids = new ArrayList<>();
		List<CX_Data> dataList = null;
		//测点列表
//		List<SurveyPoint_WYD> lstSurveyPoint=new ArrayList<>();
		//通过树节点解析json数据
		JsonNode root = null;
		try {
			root = mapper.readTree(jsonOriginalfData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String projectUuid = root.get("projectUuid").asText();
		Project project = null;
		if(projectUuid != null || projectUuid != ""){
			project = projectService.getEntity(projectUuid);
		}
		String monitorItemUuid = root.get("monitorItemUuid").asText();
		String deviceUuid = root.get("deviceUuid").asText();
		Date d = new Date();
		//根据项目uuid，监测项uuid获取所有的监测点
		List<SurveyPoint_CX> lstSurveyPoint = surveyPointCXService.getSP_CXs(projectUuid, monitorItemUuid);
		lstSurveyPoint.forEach(p->spUuids.add(p.getSurveyPointUuid()));
		try{
			//去除空值
			lstSurveyPoint.removeAll(Collections.singleton(null));
			if(lstSurveyPoint.size() == 0){
				throw(new Exception("请先在后台添加监测点！"));
			}
			//所有点的上一条数据（已根据深度排序,eg:0.5,1,1.5）
			List<CX_Data> latestAllCXData = null;
			//所有点的前3条数据（已根据深度排序,eg:0.5,0.5,0.5,1,1,1,1.5,1.5,1.5）
			List<CX_Data> threeAscAllCXData = null;
//			map.put("surveyPointList", lstSurveyPoint);
			
			JsonNode originalData = root.get("originalData");
			//点名（孔名）
			String pointName = originalData.get("pointName").asText();
			//测斜测站
			JsonNode surveyDatas = originalData.get("surveyDatas");
			
			String millionTime = "";
			
			//数据次数
			int times = 0;
			//站数
			int stationCount =0;
			//累计高差
			double lastPointHeight = 0.0;
			String n = pointName;
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
			//从后台得到的监测点列表匹配点名。匹配不上的，可能是点名错误，不保存。
	    	List<SurveyPoint_CX> tempList = lstSurveyPoint.stream().filter(p->p.getCode().equals(n)).collect(Collectors.toList());
	    	if(tempList != null && tempList.size()>0){
				//所有点的上一条数据（已根据深度排序,eg:0.5,1,1.5）
				latestAllCXData = ((ICXDataDao)getDao()).getOnePointLatestOneCXDataBySurveyPoint(tempList.get(0).getSurveyPointUuid());
				//所有点的前3条数据（已根据深度排序,eg:0.5,0.5,0.5,1,1,1,1.5,1.5,1.5）
				threeAscAllCXData = ((ICXDataDao)getDao()).getOnePointThreeAscCXDataBySurveyPoint(tempList.get(0).getSurveyPointUuid());
				if (surveyDatas.isArray()) {
				    for (Iterator it = surveyDatas.elements(); it.hasNext();){
				    	times++;
				    	JsonNode s = (JsonNode) it.next();
				    	Date dateTime = sdf.parse(s.get("dateTime").asText());
				    	
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
						millionTime = sdf1.format(dateTime);
				    	
				    	JsonNode surveyDepthDatas = s.get("surveyDepthDatas");
				    	if (surveyDepthDatas.isArray()) {
				    		dataList = new ArrayList<>();
						    for (Iterator ite = surveyDepthDatas.elements(); ite.hasNext();){
						    	stationCount++;
						    	JsonNode ss = (JsonNode) ite.next();
						    	double a0 = ss.get("a0").asDouble();
						    	double a180 = ss.get("a180").asDouble();
						    	double depth = ss.get("depth").asDouble();
					    		surveyPoint = tempList.get(0);
					    		CX_Data item = new CX_Data();
					    		item.setSurveyPoint(surveyPoint);
					    		item.setDepth(depth);
					    		item.setA0Val(a0);
					    		item.setA180Val(a180);
					    		item.setCollectTime(dateTime);
			//		    		this.getDao().saveEntity(item);
					    		dataList.add(item);
						    }
						    
								for(int i = dataList.size() - 1; i > -1; i--){
									if(i<dataList.size()-1){
										double calValue = (dataList.get(i).getA0Val() - dataList.get(i).getA180Val())/400 + dataList.get(i+1).getCalValue();
										//保留3位小数
										BigDecimal b = new BigDecimal(calValue); 
										calValue = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
										dataList.get(i).setCalValue(calValue);
										currentValMap.put(dataList.get(i).getSurveyPoint().getCode()+"-"+(i+1)*0.5, calValue);
									}else{
										double calValue = (dataList.get(i).getA0Val()- dataList.get(i).getA180Val())/400;
										//保留3位小数
										BigDecimal b = new BigDecimal(calValue); 
										calValue = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
										dataList.get(i).setCalValue(calValue);
										currentValMap.put(dataList.get(i).getSurveyPoint().getCode()+"-"+(i+1)*0.5, calValue);
									}
								}
						    
							resultList.add(dataList);
				    	}
			    	}
			    }
			}
				if(dataList == null || dataList.size() == 0){
					throw(new Exception("全部点匹配不上，入库失败，请检查！"));
				}
				//初始值列表
				List<Double> originVal = new ArrayList<>();
				//测量次序
				int surveyOrder = 0;
				//计算初始值
				if(threeAscAllCXData == null){
					//数据库中历史数据不足3次，用这次上传的新数据计算初始值
					for(int l = 0;l< resultList.get(0).size(); l++){
						if(resultList.size() >= 3){
							//前三次的计算值求平均作初始值
							double ava = (resultList.get(0).get(l).getCalValue() + resultList.get(1).get(l).getCalValue() +resultList.get(2).get(l).getCalValue())/3;
							originVal.add(ava);
						}
					}
				}else{
			    	//点号有3个时间点的数据，求初始值
					double depth = threeAscAllCXData.get(0).getDepth();
					double sum = 0;
					int count = 0;
					for(int j = 0; j < threeAscAllCXData.size(); j++){
						if(depth == threeAscAllCXData.get(j).getDepth()){
							sum += threeAscAllCXData.get(j).getCalValue();
							count++;
							if(j == threeAscAllCXData.size() - 1){
								//最后一次
								originVal.add(sum/count);
								break;
							}
  						}else{
							originVal.add(sum/count);
							count = 1;
							sum = threeAscAllCXData.get(j).getCalValue();;
  						}
						depth = threeAscAllCXData.get(j).getDepth();
					}
			    }
				//数据库里有上一次数据
				//计算累计变化量和单次变化量
				if(latestAllCXData != null && latestAllCXData.size() > 0){
					//点的数量循环
					for(int k =0; k< resultList.size(); k++){
						//深度的间隔循环
						for(int l = 0;l< resultList.get(k).size(); l++){
							if(k==0){
								//本次数据第一组，和数据库中最后一组对比；其余的用本次数据对比
								double gap = resultList.get(k).get(l).getCalValue() - latestAllCXData.get(l).getCalValue();
								//保留3位小数
								BigDecimal b = new BigDecimal(gap); 
								gap = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								double days = ((resultList.get(k).get(l).getCollectTime().getTime() - latestAllCXData.get(l).getCollectTime().getTime())/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								//取整数位，进1法。1.1->2,0.9->1;
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1;
								}
								double rate = Math.abs(gap/days);
								b = new BigDecimal(rate); 
								rate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								resultList.get(k).get(l).setChangeRate(rate);
								resultList.get(k).get(l).setGapOffset(gap);
								
								Warning warn = resultList.get(k).get(l).getSurveyPoint().getWarning();
								if(Math.abs(rate) > Math.abs(warn.getWarnSingleRate())){
									//单次变化速率超报警值
									gapRateWarningPoints.add(resultList.get(k).get(l).getSurveyPoint().getCode()+"-"+resultList.get(k).get(l).getDepth());
									gapRateWarningOffset.add(rate);
									gapRateWarningVal.add(warn.getWarnSingleRate());
									//上一条数据
									lastTimeOffset.add(latestAllCXData.get(l).getCalValue());
								}
								
								//测量次序
								resultList.get(k).get(l).setSurveyOrder(latestAllCXData.get(0).getSurveyOrder()+1);
							}else if(k > 0){
								double gap = resultList.get(k).get(l).getCalValue() - resultList.get(k-1).get(l).getCalValue();
								//保留3位小数
								BigDecimal b = new BigDecimal(gap); 
								gap = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
								long days = (resultList.get(k).get(l).getCollectTime().getTime() - resultList.get(k-1).get(l).getCollectTime().getTime())/(1000*60*60*24);
								if(days == 0){
									days++;
								}
								double rate = Math.abs(gap/days);
								b = new BigDecimal(rate); 
								rate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								resultList.get(k).get(l).setChangeRate(rate);
								resultList.get(k).get(l).setSurveyOrder(resultList.get(k-1).get(l).getSurveyOrder()+1);
								resultList.get(k).get(l).setGapOffset(gap);
								
								Warning warn = resultList.get(k).get(l).getSurveyPoint().getWarning();
								if(Math.abs(rate) > Math.abs(warn.getWarnSingleRate())){
									//单次变化速率超报警值
									gapRateWarningPoints.add(resultList.get(k).get(l).getSurveyPoint().getCode()+"-"+resultList.get(k).get(l).getDepth());
									gapRateWarningOffset.add(rate);
									gapRateWarningVal.add(warn.getWarnSingleRate());
									//上一条数据
									lastTimeOffset.add(resultList.get(k-1).get(l).getCalValue());
								}
							}
							if(originVal != null && originVal.size()>0){
								double accum = resultList.get(k).get(l).getCalValue() - originVal.get(l);
								//保留3位小数
								BigDecimal b = new BigDecimal(accum); 
								accum = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								resultList.get(k).get(l).setAccumOffset(accum);
								
								Warning warn = resultList.get(k).get(l).getSurveyPoint().getWarning();
								if(Math.abs(accum) > Math.abs(warn.getWarnAccum())){
									//累计变化量超报警值
									accumWarningPoints.add(resultList.get(k).get(l).getSurveyPoint().getCode()+"-"+resultList.get(k).get(l).getDepth());
									accumWarningOffset.add(accum);
									accumWarningVal.add(warn.getWarnAccum());
									//第一次计算值
									firstTimeOffset.add(originVal.get(l));
								}else if(Math.abs(accum) > Math.abs(warn.getControlAccum())){
									//累计变化量超控制值
									accumControlWarningPoints.add(resultList.get(k).get(l).getSurveyPoint().getCode()+"-"+resultList.get(k).get(l).getDepth());
									accumControlWarningOffset.add(accum);
									accumControlWarningVal.add(warn.getControlAccum());
								}
								
							}
						}
					}
				}else{
					//数据库里没有上一次数据
					for(int k = 0; k < resultList.size(); k++){
						for(int l = 0; l < resultList.get(k).size(); l++){
							//测量次序从1开始
							resultList.get(k).get(l).setSurveyOrder(k+1);
							if(k>2){
								//当次数据计算初始值来计算累计值，头三次(组)数据没有累计值
								if(originVal != null && originVal.size()>0){
									double accum = resultList.get(k).get(l).getCalValue() - originVal.get(l);
									//保留3位小数
									BigDecimal b = new BigDecimal(accum); 
									accum = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
									resultList.get(k).get(l).setAccumOffset(accum);
									
									Warning warn = resultList.get(k).get(l).getSurveyPoint().getWarning();
									if(Math.abs(accum) > Math.abs(warn.getWarnAccum())){
										//累计变化量超报警值
										accumWarningPoints.add(resultList.get(k).get(l).getSurveyPoint().getCode()+"-"+resultList.get(k).get(l).getDepth());
										accumWarningOffset.add(accum);
										accumWarningVal.add(warn.getWarnAccum());
										//第一次计算值
										firstTimeOffset.add(originVal.get(l));
									}else if(Math.abs(accum) > Math.abs(warn.getControlAccum())){
										//累计变化量超控制值
										accumControlWarningPoints.add(resultList.get(k).get(l).getSurveyPoint().getCode()+"-"+resultList.get(k).get(l).getDepth());
										accumControlWarningOffset.add(accum);
										accumControlWarningVal.add(warn.getControlAccum());
									}
								}
							}
							if(k < resultList.size() - 1){
								double gap = resultList.get(k+1).get(l).getCalValue() - resultList.get(k).get(l).getCalValue();
								//保留3位小数
								BigDecimal b = new BigDecimal(gap); 
								gap = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								double days = (resultList.get(k + 1).get(l).getCollectTime().getTime() - resultList.get(k).get(l).getCollectTime().getTime())/(1000*60*60*24);
								BigDecimal bb = new BigDecimal(days); 
								//取整数位，进1法。1.1->2,0.9->1;
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days++;
								}
								double rate = Math.abs(gap / days);
								b = new BigDecimal(rate); 
								rate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								resultList.get(k+1).get(l).setChangeRate(rate);
								resultList.get(k+1).get(l).setGapOffset(gap);
								
								Warning warn = resultList.get(k).get(l).getSurveyPoint().getWarning();
								if(Math.abs(rate) > Math.abs(warn.getWarnSingleRate())){
									//单次变化速率超报警值
									gapRateWarningPoints.add(resultList.get(k + 1).get(l).getSurveyPoint().getCode()+"-"+resultList.get(k + 1).get(l).getDepth());
									gapRateWarningOffset.add(rate);
									gapRateWarningVal.add(warn.getWarnSingleRate());
									//上一条数据
									lastTimeOffset.add(resultList.get(k).get(l).getCalValue());
								}
							}
						}
					}
				}
				
				resultList.stream().forEach((p)->{
//					p.stream().forEach(q->this.getDao().saveEntity(q)); 
					p.stream().forEach(q->cxDataList.add(q));
				});
				
				map.put("data", cxDataList);
				map.put("gapRateEarlyWarningPoints", gapRateWarningPoints);
				//该超限点上一次测量的值
				map.put("lastTimeOffset", lastTimeOffset);
				//超单次变化速率的点的单次变化速率列表
				map.put("gapRateWarningOffset", gapRateWarningOffset);
				//单次变化报警值
				map.put("gapRateWarningVal", gapRateWarningVal);
				//超累计变化报警值的点名列表
				map.put("accumWarningPoints", accumWarningPoints);
				//超累计变化报警值点的累计变化量
				map.put("accumWarningOffset", accumWarningOffset);
				//累计变化量报警值
				map.put("accumWarningVal", accumWarningVal);
				//超累计变化量控制值的点名列表
				map.put("accumControlWarningPoints", accumControlWarningPoints);
				//超累计变化量控制值点的累计变化量列表
				map.put("accumControlWarningOffset", accumControlWarningOffset);
				//累计变化量控制值
				map.put("accumControlWarningVal", accumControlWarningVal);
				//该超限点第一次测量的值
				map.put("firstTimeOffset", firstTimeOffset);
				//点名，深度，本次测量值
				map.put("currentVal", currentValMap);

//			map.put("dataList", dataList);
				String organUuid = project.getOrgan().getOrganUuid();
				//保存原始数据源文件
				FileUtil.saveSourceFile(organUuid, projectUuid, uploadFileSrc, millionTime,"CX", sourceData);
				//把最新上传的一次数据推送到前台
				String msg = JsonUtil.beanToJson(resultList);
				System.out.println(msg+"\r\n");
				ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/projectCXCollect/"+dataList.get(0).getSurveyPoint().getProject().getProjectUuid(), msg);
		}catch(org.hibernate.exception.SQLGrammarException e){
			throw(new Exception("数据库操作失败！请检查数据"));
		}
		return map;
	}

	@Override
	public Map<String, Object> getLatestCXDatasByProject(Project project) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> dataResult = new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids = new ArrayList<>();
		//支护结构深层水平位移
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(4);
		// 首先获取对应项目下对应监测项的所有监测点
		List<SurveyPoint_CX> sps = surveyPointCXService.getSP_CXs(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
		//数据库没有点
		if(sps.size() == 0){
//			throw(new Exception("数据库没有点！"));
			return result;
		}
		result.put("surveyPoints", sps);
		//每一个点的最新11个时间点的数据
		Map<String, List<CX_Data>> spLatestDatas = new HashMap<String, List<CX_Data>>();
		//每一个点的第一个时间点的数据
		Map<String, CX_Data> spFirstData = new HashMap<String, CX_Data>();
		
		sps.forEach(p->spUuids.add(p.getSurveyPointUuid()));
		//所有监测点最新4个时间点的数据
		List<CX_Data> latestAllCXData = ((ICXDataDao)getDao()).getLatestCXDataBySurveyPoint(spUuids);
		//获得所有监测点头3个时间点的坐标数据,用来计算初始值
		List<CX_Data> allThreeCXData = ((ICXDataDao)getDao()).getThreeAscCXDataBySurveyPoint(spUuids);
		
		for (int i = 0; i < sps.size(); i++) {
			//获得一个点下最新4个时间点的所有解算数据
			String spUuid = sps.get(i).getSurveyPointUuid();
			List<CX_Data> latestFourCXData = latestAllCXData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
			if(latestFourCXData.size()>0){
				spLatestDatas.put(sps.get(i).getCode(), latestFourCXData);
			}
			//一个点下的头3次数据
//			List<CX_Data> threeAscAllCXData = allThreeCXData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
//			if(threeAscAllCXData !=null && threeAscAllCXData.size()>0){
//				CX_Data firstCoordData = threeAscAllCXData.get(0);
//				spFirstData.put(sps.get(i).getCode(), firstCoordData);
//		    }
		}
		//计算累计位移
//		for (int i = 0; i < sps.size(); i++) {
//			//一个点的最新4个时间点的数据
//			List<CX_Data> dataAtOnePoint = (List<CX_Data>) spLatestDatas.get(sps.get(i).getCode());
//			//只在有数据的情况下才进行计算
//			if (dataAtOnePoint!=null && dataAtOnePoint.size() > 0) {
//				for (int j = 0; j < dataAtOnePoint.size(); j++) {
//					if (j < dataAtOnePoint.size() - 1) {
//						
//					}
//				}
//				dataResult.put(sps.get(i).getCode(), dataAtOnePoint);
//			}
//		}
		result.put("cxData", spLatestDatas);
		return result;
	
	
	}
	@Override
	public Map<String, Object> getCXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		List<CX_Data> cXData = ((ICXDataDao)getDao()).getCXDatasBySurveyOrderAndSurveyPoint(surveyOrder, surveyPointUuid);
		result.put("cxData", cXData);
		return result;
	}
	@Override
	public Map<String, Object> getMaxSurveyOrderBySurveyPoint(Project project) {
		Map<String, Object> result = new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids = new ArrayList<>();
		//支护结构深层水平位移
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(4);
		// 首先获取对应项目下对应监测项的所有监测点
		List<SurveyPoint_CX> sps = surveyPointCXService.getSP_CXs(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
		//数据库没有点
		if(sps.size() == 0){
			return result;
		}
		
		sps.stream().forEach((p)->{
			int maxSurveyOrder = 0;
			try{
			maxSurveyOrder = ((ICXDataDao)getDao()).getMaxSurveyOrderBySurveyPoint(p.getSurveyPointUuid());
			}catch(NullPointerException ex){
				//若捕捉到空指针异常，就是这个点没有数据
			}
			p.setTotalSurveyOrders(maxSurveyOrder);
		});
		result.put("surveyPoints", sps);
		return result;
	}
	@Override
	public void deleteCXDataBysurveyPoint(String surveyPointUuid) {
		((ICXDataDao)getDao()).deleteCXDataBySP(surveyPointUuid);
	}

	@Override
	public Date getLatestTimeBySurveyPoint(String spUuid) {
		return ((ICXDataDao)getDao()).getLatestTimeBySurveyPoint(spUuid);
	}
	@Override
	public void saveManualData(Project project,List<CX_Data> data, List<String> gapRateEarlyWarningPoints, List<String> accumEarlyWarningPoints, byte flag) throws Exception {
		try{
			//可上传的数据
			List<CX_Data> acceptedData = new ArrayList<>();
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
					acceptedData.add(data.get(i));
				}
				}else if(flag == 1){
					//全部上传
					for(int i = 0; i< data.size(); i++){
						for(int j = 0; j < data.size(); j ++){
							//原始数据入库
							this.saveEntity(data.get(i));
							acceptedData.add(data.get(i));
						}
					}
				}
			
			//把最新上传的一次数据推送到前台
			String msg = JsonUtil.beanToJson(acceptedData);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/projectCXCollect/"+project.getProjectUuid(), msg);
			//发短信给项目监测人员和监测负责人。
			String phones = "";
			String contacts = project.getMonitorWorker();
			contacts += project.getMonitorLeader();
			while(contacts.contains("(")||contacts.contains(")")){
				String temp = contacts.substring(contacts.indexOf("(")+1,contacts.indexOf(")"));
				if(!phones.contains(temp)){
					phones += temp+",";
				}
				contacts = contacts.substring(contacts.indexOf(")")+1);
			}
			//计算是否超限并更新工程状态。
			warningDataService.calWarnningOffsetByMonitorItem("CX", project,phones);
		}catch(org.hibernate.exception.SQLGrammarException e){
			throw(new Exception("数据库操作失败！请检查数据"));
		}
		
	}
	@Override
	public List<CX_Data> getOnePointLatestOneCXDataBySurveyPoint(String spUuid) {
		List<CX_Data> lastAllCxData = ((ICXDataDao)getDao()).getOnePointLatestOneCXDataBySurveyPoint(spUuid);
		return lastAllCxData;
	}
	@Override
	public List<CX_Data> getThreeAscCXDataBySurveyPoint(List<String> spUuids) {
		List<CX_Data> allThreeCXData = ((ICXDataDao)getDao()).getThreeAscCXDataBySurveyPoint(spUuids);
		return allThreeCXData;
	}
}
