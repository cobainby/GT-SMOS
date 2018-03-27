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

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.dao.IQXDataDao;
import com.southgt.smosplat.data.entity.QX_Data;
import com.southgt.smosplat.data.service.IQXService;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_QX;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_QXService;
@Service("qxService")
public class QXServiceImpl extends BaseServiceImpl<QX_Data> implements IQXService{
//	@Resource
//	ISurveyPoint_QXService surveyPointQXService;
	
	@Resource
	ISurveyPoint_QXService surveyPointQXService;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource(name="qxDao")
	@Override
	public void setDao(IBaseDao<QX_Data> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, Object> saveQXData(String jsonOriginalData) throws Exception {
		Map<String,Object>map =new HashMap();
		List<List<QX_Data>> resultList =new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		SurveyPoint_QX surveyPoint = null;
		//所有解算坐标点的监测点Uuid的List
		List<String> spUuids =new ArrayList<>();
		List<QX_Data> dataList = null;
		//测点列表
//		List<SurveyPoint_WYD> lstSurveyPoint=new ArrayList<>();
		//通过树节点解析json数据
		JsonNode root=null;
		try {
			root = mapper.readTree(jsonOriginalData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String projectUuid = root.get("projectUuid").asText();
		String monitorItemUuid = root.get("monitorItemUuid").asText();
		String deviceUuid = root.get("deviceUuid").asText();
		Date d = new Date();
		//根据项目uuid，监测项uuid获取所有的监测点
		List<SurveyPoint_QX> lstSurveyPoint = surveyPointQXService.getSP_QXs(projectUuid, monitorItemUuid);
		lstSurveyPoint.forEach(p->spUuids.add(p.getSurveyPointUuid()));
		try{
			//去除空值
			lstSurveyPoint.removeAll(Collections.singleton(null));
			if(lstSurveyPoint.size()==0){
				throw(new Exception("请先在后台添加监测点！"));
			}
			//所有点的上一条数据（已根据深度排序,eg:0.5,1,1.5）
			List<QX_Data> latestAllCXData = null;
			//所有点的前3条数据（已根据深度排序,eg:0.5,0.5,0.5,1,1,1,1.5,1.5,1.5）
			List<QX_Data> threeAscAllCXData = null;
//			map.put("surveyPointList", lstSurveyPoint);
			
			JsonNode originalData = root.get("originalData");
			//点名（孔名）
			String pointName = originalData.get("pointName").asText();
			//测斜测站
			JsonNode surveyDatas = originalData.get("surveyDatas");
			
			//数据次数
			int times = 0;
			//站数
			int stationCount =0;
			//累计高差
			double lastPointHeight = 0.0;
			String n = pointName;
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
			//从后台得到的监测点列表匹配点名。匹配不上的，可能是点名错误，也可能是转点，不保存。
	    	List<SurveyPoint_QX> tempList = lstSurveyPoint.stream().filter(p->p.getCode().equals(n)).collect(Collectors.toList());
	    	if(tempList != null && tempList.size()>0){
				//所有点的上一条数据（已根据深度排序,eg:0.5,1,1.5）
				latestAllCXData = ((IQXDataDao)getDao()).getOnePointLatestOneQXDataBySurveyPoint(tempList.get(0).getSurveyPointUuid());
				//所有点的前3条数据（已根据深度排序,eg:0.5,0.5,0.5,1,1,1,1.5,1.5,1.5）
				threeAscAllCXData = ((IQXDataDao)getDao()).getOnePointThreeAscQXDataBySurveyPoint(tempList.get(0).getSurveyPointUuid());
				if (surveyDatas.isArray()) {
				    for (Iterator it =  surveyDatas.elements(); it.hasNext(); ){
				    	times++;
				    	JsonNode s = (JsonNode) it.next();
				    	Date dateTime = sdf.parse(s.get("dateTime").asText());
				    	JsonNode surveyDepthDatas = s.get("surveyDepthDatas");
				    	if (surveyDepthDatas.isArray()) {
				    		dataList = new ArrayList<>();
						    for (Iterator ite =  surveyDepthDatas.elements(); ite.hasNext(); ){
						    	stationCount++;
						    	JsonNode ss = (JsonNode) ite.next();
						    	double a0 = ss.get("a0").asDouble();
						    	double a180 = ss.get("a180").asDouble();
						    	double depth = ss.get("depth").asDouble();
					    		surveyPoint = tempList.get(0);
					    		QX_Data item = new QX_Data();
					    		item.setSurveyPoint(surveyPoint);
					    		item.setDepth(depth);
					    		item.setA0Val(a0);
					    		item.setA180Val(a180);
					    		item.setCollectTime(dateTime);
			//		    		this.getDao().saveEntity(item);
					    		dataList.add(item);
						    }
						    
								for(int i=dataList.size()-1;i>-1;i--){
									if(i<dataList.size()-1){
										double calValue = (dataList.get(i).getA0Val()- dataList.get(i).getA180Val())/400+dataList.get(i+1).getCalValue();
										//保留3位小数
										BigDecimal b = new BigDecimal(calValue); 
										calValue = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();  
										dataList.get(i).setCalValue(calValue);
									}else{
										double calValue = (dataList.get(i).getA0Val()- dataList.get(i).getA180Val())/400;
										//保留3位小数
										BigDecimal b = new BigDecimal(calValue); 
										calValue = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();  
										dataList.get(i).setCalValue(calValue);
									}
								}
						    
							resultList.add(dataList);
				    	}
			    	}
			    }
			}
				if(dataList.size() == 0){
					throw(new Exception("全部点匹配不上，入库失败，请检查！"));
				}
				//初始值列表
				List<Double> originVal =new ArrayList<>();
				//测量次序
				int surveyOrder = 0;
				//计算初始值
				if(threeAscAllCXData == null){
					//数据库中历史数据不足3次，用这次上传的新数据计算初始值
					for(int l = 0;l< resultList.get(0).size();l++){
						if(resultList.size()>=3){
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
					for(int j = 0;j<threeAscAllCXData.size();j++){
						if(depth == threeAscAllCXData.get(j).getDepth()){
							sum += threeAscAllCXData.get(j).getCalValue();
							count++;
							if(j == threeAscAllCXData.size()-1){
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
				if(latestAllCXData!=null && latestAllCXData.size()>0){
					for(int k =0;k< resultList.size();k++){
						for(int l = 0;l< resultList.get(k).size();l++){
							if(k==0){
								//本次数据第一组，和数据库中最后一组对比；其余的用本次数据对比
								double gap = resultList.get(k).get(l).getCalValue() - latestAllCXData.get(l).getCalValue();
								//保留3位小数
								BigDecimal b = new BigDecimal(gap); 
								gap = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								
								long days = (resultList.get(k).get(l).getCollectTime().getTime() - latestAllCXData.get(l).getCollectTime().getTime())/(1000*60*60*24);
								if(days == 0){
									days++;
								}
								double rate = Math.abs(gap/days);
								b = new BigDecimal(rate); 
								rate = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								resultList.get(k).get(l).setChangeRate(rate);
								resultList.get(k).get(l).setGapOffset(gap);
								//测量次序
								resultList.get(k).get(l).setSurveyOrder(latestAllCXData.get(0).getSurveyOrder()+1);
							}else if(k>0){
								double gap = resultList.get(k).get(l).getCalValue() - resultList.get(k-1).get(l).getCalValue();
								//保留3位小数
								BigDecimal b = new BigDecimal(gap); 
								gap = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();
								long days = (resultList.get(k).get(l).getCollectTime().getTime() - resultList.get(k-1).get(l).getCollectTime().getTime())/(1000*60*60*24);
								if(days == 0){
									days++;
								}
								double rate = Math.abs(gap/days);
								b = new BigDecimal(rate); 
								rate = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								resultList.get(k).get(l).setChangeRate(rate);
								resultList.get(k).get(l).setSurveyOrder(resultList.get(k-1).get(l).getSurveyOrder()+1);
								resultList.get(k).get(l).setGapOffset(gap);
							}
							if(originVal !=null && originVal.size()>0){
								double accum = resultList.get(k).get(l).getCalValue() - originVal.get(l);
								//保留3位小数
								BigDecimal b = new BigDecimal(accum); 
								accum = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								resultList.get(k).get(l).setAccumOffset(accum);
							}
						}
					}
				}else{
					//数据库里没有上一次数据
					for(int k =0;k< resultList.size();k++){
						for(int l = 0;l< resultList.get(k).size();l++){
							//测量次序从1开始
							resultList.get(k).get(l).setSurveyOrder(k+1);
							if(k>2){
								//当次数据计算初始值来计算累计值，头三次(组)数据没有累计值
								if(originVal !=null && originVal.size()>0){
									double accum = resultList.get(k).get(l).getCalValue() - originVal.get(l);
									//保留3位小数
									BigDecimal b = new BigDecimal(accum); 
									accum = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();  
									resultList.get(k).get(l).setAccumOffset(accum);
								}
							}
							if(k<resultList.size()-1){
								double gap = resultList.get(k+1).get(l).getCalValue() - resultList.get(k).get(l).getCalValue();
								//保留3位小数
								BigDecimal b = new BigDecimal(gap); 
								gap = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								long days = (resultList.get(k+1).get(l).getCollectTime().getTime() - resultList.get(k).get(l).getCollectTime().getTime())/(1000*60*60*24);
								if(days == 0){
									days++;
								}
								double rate = Math.abs(gap/days);
								b = new BigDecimal(rate); 
								rate = b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();  
								resultList.get(k+1).get(l).setChangeRate(rate);
								resultList.get(k+1).get(l).setGapOffset(gap);
							}
						}
					}
				}
				//入库
				resultList.stream().forEach((p)->{
					p.stream().forEach(q->this.getDao().saveEntity(q));
				});
//			map.put("dataList", dataList);
		}catch(org.hibernate.exception.SQLGrammarException e){
			throw(new Exception("数据库操作失败！请检查数据"));
		}
		return map;
	}

	@Override
	public Map<String, Object> getLatestQXDatasByProject(Project project) throws Exception {
		Map<String, Object> result=new HashMap<String, Object>();
		Map<String, Object> dataResult = new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids =new ArrayList<>();
		//支护结构深层水平位移
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(4);
		// 首先获取对应项目下对应监测项的所有监测点
		List<SurveyPoint_QX> sps = surveyPointQXService.getSP_QXs(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
		//数据库没有点
		if(sps.size() == 0){
//			throw(new Exception("数据库没有点！"));
			return result;
		}
		result.put("surveyPoints", sps);
		//每一个点的最新11个时间点的数据
		Map<String, List<QX_Data>> spLatestDatas=new HashMap<String, List<QX_Data>>();
		//每一个点的第一个时间点的数据
		Map<String, QX_Data> spFirstData=new HashMap<String, QX_Data>();
		
		sps.forEach(p->spUuids.add(p.getSurveyPointUuid()));
		//所有监测点最新4个时间点的数据
		List<QX_Data> latestAllCXData = ((IQXDataDao)getDao()).getLatestQXDataBySurveyPoint(spUuids);
		//获得所有监测点头3个时间点的坐标数据,用来计算初始值
		List<QX_Data> allThreeCXData = ((IQXDataDao)getDao()).getThreeAscQXDataBySurveyPoint(spUuids);
		
		for (int i = 0; i < sps.size(); i++) {
			//获得一个点下最新4个时间点的所有解算数据
			String spUuid = sps.get(i).getSurveyPointUuid();
			List<QX_Data> latestFourCXData = latestAllCXData.stream().filter(p->p.getSurveyPoint().getSurveyPointUuid().equals(spUuid)).collect(Collectors.toList());
			if(latestFourCXData.size()>0){
				spLatestDatas.put(sps.get(i).getCode(), latestFourCXData);
			}
		}
		result.put("cxData", spLatestDatas);
		return result;
	}

	@Override
	public Map<String, Object> getQXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid)
			throws Exception {
		Map<String, Object> result=new HashMap<String, Object>();
		List<QX_Data> cXData = ((IQXDataDao)getDao()).getQXDatasBySurveyOrderAndSurveyPoint(surveyOrder, surveyPointUuid);
		result.put("cxData", cXData);
		return result;
	}

	@Override
	public Map<String, Object> getMaxSurveyOrderBySurveyPoint(Project project) {
		Map<String, Object> result=new HashMap<String, Object>();
		//所有监测点Uuid的List
		List<String> spUuids =new ArrayList<>();
		//支护结构深层水平位移
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(4);
		// 首先获取对应项目下对应监测项的所有监测点
		List<SurveyPoint_QX> sps = surveyPointQXService.getSP_QXs(project.getProjectUuid(),monitorItem.getMonitorItemUuid());
		//数据库没有点
		if(sps.size() == 0){
			return result;
		}
		
		sps.stream().forEach((p)->{
			int maxSurveyOrder = 0;
			try{
			maxSurveyOrder = ((IQXDataDao)getDao()).getMaxSurveyOrderBySurveyPoint(p.getSurveyPointUuid());
			}catch(NullPointerException ex){
				//若捕捉到空指针异常，就是这个点没有数据
			}
			p.setTotalSurveyOrders(maxSurveyOrder);
		});
		result.put("surveyPoints", sps);
		return result;
	}

	@Override
	public void deleteQXDataBysurveyPoint(String surveyPointUuid) {
		((IQXDataDao)getDao()).deleteQXDataBySP(surveyPointUuid);
		
	}
}
