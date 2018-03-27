package com.southgt.smosplat.data.dao.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.data.dao.ICXDataDao;
import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
/**
 * 
 * 测斜数据数据库实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月10日     姚家俊       v1.0.0        create</p>
 *
 */
@Repository("cxDao")
public class CXDataDaoImpl extends BaseDaoImpl<CX_Data> implements ICXDataDao{

	private Criteria getCriteria(){
		return getSession().createCriteria(CX_Data.class).setCacheable(true);
	}
	
	@Override
	public List<CX_Data> getLatestCXDataBySurveyPoint(List<String> spUuids) {
		//最新的3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(4);
		List<Object> latestTimes=c.list();
		
		
		//找出该点的最新11个时间点的所有数据
		Criteria c1=getCriteria();
		c1.add(Restrictions.in("collectTime", latestTimes));
		c1.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c1.addOrder(Order.desc("collectTime"));
		List<CX_Data> dataList = (List<CX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<CX_Data>() {
			@Override
			public int compare(CX_Data sp1, CX_Data sp2) {  
                if(sp1.getDepth()<sp2.getDepth()) {
                	return -1;
                }else if(sp1.getDepth() == sp2.getDepth()){
                	return 0;
                }else{
                	return 1;
                }
                
            }
		});
		return dataList;
	}

	@Override
	public List<CX_Data> getThreeAscCXDataBySurveyPoint(List<String> spUuids) {
		//头3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.asc("collectTime"));
		c.setMaxResults(3);
		List<Object> latestTimes=c.list();
		if(latestTimes.size()<3){
			//不足3次，无法计算初始值，返回空；
			return null;
		}
		
		//找出该点的最新3个时间点的所有数据
		Criteria c1=getCriteria();
		c1.add(Restrictions.in("collectTime", latestTimes));
		c1.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c1.addOrder(Order.desc("collectTime"));
		
		List<CX_Data> dataList = (List<CX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<CX_Data>() {
			@Override
			public int compare(CX_Data sp1, CX_Data sp2) {  
                if(sp1.getDepth()<sp2.getDepth()) {
                	return -1;
                }else if(sp1.getDepth() == sp2.getDepth()){
                	return 0;
                }else{
                	return 1;
                }
                
            }
		});
		return dataList;
	}

	@Override
	public List<CX_Data> getLatestOneCXDataBySurveyPoint(List<String> spUuids) {
		//最新一个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		
		//找出该点第1个时间点的所有数据
		Criteria c1=getCriteria();
		c1.add(Restrictions.eq("collectTime", latestTime));
		c1.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c1.addOrder(Order.asc("collectTime"));
		List<CX_Data> dataList = (List<CX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<CX_Data>() {
			@Override
			public int compare(CX_Data sp1, CX_Data sp2) {  
                if(sp1.getDepth()<sp2.getDepth()) {
                	return -1;
                }else if(sp1.getDepth() == sp2.getDepth()){
                	return 0;
                }else{
                	return 1;
                }
                
            }
		});
		return dataList;
	}

	@Override
	public List<CX_Data> getCXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.eq("surveyOrder", surveyOrder));
		return c.list();
	}

	@Override
	public int getMaxSurveyOrderBySurveyPoint(String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.setProjection(Projections.max("surveyOrder"));
		return (int) c.uniqueResult() ;
	}

	@Override
	public List<CX_Data> getOnePointLatestCXDataBySurveyPoint(String spUuid) {
		//最新的3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", spUuid));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(4);
		List<Object> latestTimes=c.list();
		
		
		//找出该点的最新11个时间点的所有数据
		Criteria c1=getCriteria();
		c1.add(Restrictions.in("collectTime", latestTimes));
		c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", spUuid));
		c1.addOrder(Order.desc("collectTime"));
		List<CX_Data> dataList = (List<CX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<CX_Data>() {
			@Override
			public int compare(CX_Data sp1, CX_Data sp2) {  
                if(sp1.getDepth()<sp2.getDepth()) {
                	return -1;
                }else if(sp1.getDepth() == sp2.getDepth()){
                	return 0;
                }else{
                	return 1;
                }
                
            }
		});
		return dataList;
	}

	@Override
	public List<CX_Data> getOnePointThreeAscCXDataBySurveyPoint(String spUuid) {
		//头3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", spUuid));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.asc("collectTime"));
		c.setMaxResults(3);
		List<Object> latestTimes=c.list();
		if(latestTimes.size()<3){
			//不足3次，无法计算初始值，返回空；
			return null;
		}
		
		//找出该点的最新3个时间点的所有数据
		Criteria c1=getCriteria();
		c1.add(Restrictions.in("collectTime", latestTimes));
		c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", spUuid));
		c1.addOrder(Order.desc("collectTime"));
		
		List<CX_Data> dataList = (List<CX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<CX_Data>() {
			@Override
			public int compare(CX_Data sp1, CX_Data sp2) {  
                if(sp1.getDepth()<sp2.getDepth()) {
                	return -1;
                }else if(sp1.getDepth() == sp2.getDepth()){
                	return 0;
                }else{
                	return 1;
                }
                
            }
		});
		return dataList;
	}

	@Override
	public List<CX_Data> getOnePointLatestOneCXDataBySurveyPoint(String spUuid) {
		//最新一个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", spUuid));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		
		//找出该点第1个时间点的所有数据
		Criteria c1=getCriteria();
		c1.add(Restrictions.eq("collectTime", latestTime));
		c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", spUuid));
		c1.addOrder(Order.asc("collectTime"));
		List<CX_Data> dataList = (List<CX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<CX_Data>() {
			@Override
			public int compare(CX_Data sp1, CX_Data sp2) {  
                if(sp1.getDepth()<sp2.getDepth()) {
                	return -1;
                }else if(sp1.getDepth() == sp2.getDepth()){
                	return 0;
                }else{
                	return 1;
                }
                
            }
		});
		return dataList;
	}

	@Override
	public void deleteCXDataBySP(String surveyPointUuid) {
		Query query=getSession().createQuery("delete from CX_Data d where d.surveyPoint.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", surveyPointUuid);
		query.executeUpdate();
	}

	@Override
	public Date getLatestTimeBySurveyPoint(String spUuid) {
		//头3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", spUuid));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(1);
		Date latestTime = (Date) c.uniqueResult();
		return latestTime;
	}

	@Override
	public List<CX_Data> getOneCXDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("collectTime", sDate, eDate));
		c.addOrder(Order.desc("collectTime"));
		return c.list();
	}

	@Override
	public CX_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.le("collectTime", monday));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(1);
		return (CX_Data) c.uniqueResult();
	}

	@Override
	public List<CX_Data> getDataBySurveyPointInOnePeroid(String surveyPointUuid, Date sDate, Date eDate) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("collectTime", sDate, eDate));
		c.addOrder(Order.desc("collectTime"));
		List<CX_Data> dataList = (List<CX_Data>)c.list();
		if(dataList.size() < 0){
			//根据深度排序
			dataList.sort(new Comparator<CX_Data>() {
				@Override
				public int compare(CX_Data sp1, CX_Data sp2) {  
	                if(sp1.getDepth()<sp2.getDepth()) {
	                	return -1;
	                }else if(sp1.getDepth() == sp2.getDepth()){
	                	return 0;
	                }else{
	                	return 1;
	                }
	                
	            }
			});
		}
		return dataList;
	}

	@Override
	public List<CX_Data> getFirstOneDataBySurveyPoint(List<String> surveyPointUuids) {
		//第一个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.asc("collectTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		
		//找出该点第1个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTime != null){
			c1.add(Restrictions.eq("collectTime", latestTime));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
			c1.addOrder(Order.asc("collectTime"));
			List<CX_Data>  result = c1.list();
			//根据深度排序
			result.sort(new Comparator<CX_Data>() {
				@Override
				public int compare(CX_Data sp1, CX_Data sp2) {  
	                if(sp1.getDepth() < sp2.getDepth()) {
	                	return -1;
	                }else if(sp1.getDepth() == sp2.getDepth()){
	                	return 0;
	                }else{
	                	return 1;
	                }
	                
	            }
			});
		 return result;
		}else{
			return new ArrayList<>();
		}
	}

}
