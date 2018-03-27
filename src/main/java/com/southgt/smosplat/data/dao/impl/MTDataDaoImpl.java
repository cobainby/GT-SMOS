package com.southgt.smosplat.data.dao.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.data.dao.IMTDataDao;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.entity.SW_Data;

/**
 * 
 * 锚杆内力数据库实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
@Repository("mtDao")
public class MTDataDaoImpl extends BaseDaoImpl<MT_Data> implements IMTDataDao{

	private Criteria getCriteria(){
		return getSession().createCriteria(MT_Data.class).setCacheable(true);
	}
	
	@Override
	public List<MT_Data> getLatestMTDatasBySurveyPoints(List<String> surveyPointUuids) {
		//最新的11个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(11);
		List<Object> latestTimes=c.list();
		
		
		//找出该点的最新11个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTimes.size() > 0){
			c1.add(Restrictions.in("collectTime", latestTimes));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
			c1.addOrder(Order.asc("collectTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public List<MT_Data> getLatestOneMTDatasBySurveyPoints(List<String> surveyPointUuids) {
		//最新的1个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		
		//找出该点的最新1个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTime != null){
			c1.add(Restrictions.eq("collectTime", latestTime));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
			c1.addOrder(Order.desc("collectTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public List<MT_Data> getFirstOneMTDatasBySurveyPoints(List<String> surveyPointUuids) {
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
		return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public void deleteMTDataBySP(String surveyPointUuid) {
		Query query=getSession().createQuery("delete  from MT_Data d where d.surveyPoint.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", surveyPointUuid);
		query.executeUpdate();
		
	}

	@Override
	public List<Date> getLatestTimes(List<String> surveyPointUuids) {
		//最新的11个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(11);
		List<Date> latestTimes=c.list();
		return latestTimes;
	}

	@Override
	public List<MT_Data> getTwoMTDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate) {
		List<MT_Data> result = new ArrayList<>();
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("collectTime", sDate, eDate));
		c.addOrder(Order.asc("collectTime"));
		c.setMaxResults(1);
		
		Criteria cc1=getCriteria();
		cc1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		cc1.add(Restrictions.between("collectTime", sDate, eDate));
		cc1.addOrder(Order.asc("collectTime"));
		cc1.setMaxResults(1);
		//开始日期第一条数据
		MT_Data latestData = (MT_Data) c.uniqueResult();
		//开始日期最后一条数据
		MT_Data latestData1 = (MT_Data) cc1.uniqueResult();
		if(latestData != null){
			result.add(latestData1);
			Criteria c1=getCriteria();
			c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
			//le小于等于，lt小于
			c1.add(Restrictions.lt("collectTime", latestData.getCollectTime()));
			c1.addOrder(Order.desc("collectTime"));
			c1.setMaxResults(1);
			MT_Data lastData = (MT_Data) c1.uniqueResult();
			result.add(lastData);
		}
		return result;
	}

	@Override
	public List<MT_Data> getLatestTwoDataBySurveyPoint(String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(2);
		return c.list();
	}

	@Override
	public MT_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday) {
//		Query query=getSession().createQuery("FROM MT_Data d where d.surveyPoint.surveyPointUuid=:surveyPointUuid AND d.collectTime < :dateTime order by d.collectTime desc" );
//		query.setParameter("surveyPointUuid", surveyPointUuid);
//		query.setParameter("dateTime", monday);
//		query.setMaxResults(1);
//		return (MT_Data) query.uniqueResult();
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.le("collectTime", monday));
		c.addOrder(Order.desc("collectTime"));
		c.setMaxResults(1);
		return (MT_Data) c.uniqueResult();
	}

	@Override
	public List<MT_Data> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("collectTime", sDate, eDate));
		c.addOrder(Order.desc("collectTime"));
		return c.list();
	}

	@Override
	public List<MT_Data> getFirstThreeDataBySurveyPoint(List<String> spUuid) {
		//最新的3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuid));
		c.setProjection(Projections.distinct(Projections.property("collectTime")));
		c.addOrder(Order.asc("collectTime"));
		c.setMaxResults(3);
		List<Object> latestTimes=c.list();
		
		
		//找出该点第1个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTimes.size() > 0){
			c1.add(Restrictions.in("collectTime", latestTimes));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuid));
			c1.addOrder(Order.asc("collectTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public List<MT_Data> getTwoDataBySurveyPointInTwoSide(String surveyPointUuid, Date sDate, Date eDate) {
		List<MT_Data> result = new ArrayList<>();
		List<MT_Data> temp = new ArrayList<>();
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("collectTime", sDate, eDate));
		c.addOrder(Order.asc("collectTime"));
		temp = c.list();
		if(temp.size() >= 2){
			result.add(temp.get(0));
			result.add(temp.get(temp.size() - 1));
		}else if(temp.size() == 1){
			result.add(temp.get(0));
		}
		return result;
	}

}
