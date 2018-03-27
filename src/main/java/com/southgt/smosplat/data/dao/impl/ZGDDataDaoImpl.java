package com.southgt.smosplat.data.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.data.dao.IZGDDataDao;
import com.southgt.smosplat.data.entity.ZGD_Data;

/**
 * 
 * 周边管线竖向位移数据库实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
@Repository("zgdDataDao")
public class ZGDDataDaoImpl extends BaseDaoImpl<ZGD_Data> implements IZGDDataDao{
	
	private Criteria getCriteria(){
		return getSession().createCriteria(ZGD_Data.class).setCacheable(true);
	}

	@Override
	public List<ZGD_Data> getLatestZGD_DataBySurveyPoint(List<String> spUuids) {
		//最新的11个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(11);
		List<Object> latestTimes=c.list();
		
		//找出该点的最新11个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTimes.size() > 0){
			c1.add(Restrictions.in("surveyTime", latestTimes));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
			c1.addOrder(Order.asc("surveyTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<ZGD_Data> getFirstZGD_DataBySurveyPoint(List<String> spUuids) {
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.asc("surveyTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		//找出该点第1个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTime != null){
			c1.add(Restrictions.eq("surveyTime", latestTime));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
			c1.addOrder(Order.asc("surveyTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<ZGD_Data> getLatestOneZGD_DataBySurveyPoint(List<String> spUuids) {
		//最新的1个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		//找出该点的最新1个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTime != null){
			c1.add(Restrictions.eq("surveyTime", latestTime));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", spUuids));
			c1.addOrder(Order.desc("surveyTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public void deleteZGDDataBySurveyPoint(String spUuid) {
		Query query=getSession().createQuery("delete from ZGD_Data d where d.surveyPoint.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", spUuid);
		query.executeUpdate();
	}

	@Override
	public List<ZGD_Data> getTwoZGDDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate) {
		List<ZGD_Data> result = new ArrayList<>();
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("surveyTime", sDate, eDate));
		c.addOrder(Order.asc("surveyTime"));
		Criteria cc1=getCriteria();
		cc1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		cc1.add(Restrictions.between("surveyTime", sDate, eDate));
		cc1.addOrder(Order.asc("surveyTime"));
		cc1.setMaxResults(1);
		//开始日期第一条
		ZGD_Data latestData = (ZGD_Data) c.uniqueResult();
		//开始日期最后一条
		ZGD_Data latestData1 = (ZGD_Data) c.uniqueResult();
		if(latestData != null){
			result.add(latestData1);
			Criteria c1=getCriteria();
			c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
			//le小于等于，lt小于
			c1.add(Restrictions.lt("surveyTime", latestData.getSurveyTime()));
			c1.addOrder(Order.desc("surveyTime"));
			c1.setMaxResults(1);
			ZGD_Data lastData = (ZGD_Data) c1.uniqueResult();
			result.add(lastData);
		}
		return result;
	}

	@Override
	public List<ZGD_Data> getLatestTwoDataBySurveyPoint(String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(2);
		return c.list();
	}
	
	@Override
	public List<ZGD_Data> getFirstThreeDataBySurveyPoint(List<String> surveyPointUuids) {
		//最新的3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.asc("surveyTime"));
		c.setMaxResults(3);
		List<Object> latestTimes=c.list();
		
		
		//找出该点第1个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTimes.size() > 0){
			c1.add(Restrictions.in("surveyTime", latestTimes));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
			c1.addOrder(Order.asc("surveyTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public ZGD_Data getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date monday) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.le("surveyTime", monday));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(1);
		return (ZGD_Data) c.uniqueResult();
	}

	@Override
	public List<ZGD_Data> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("surveyTime", sDate, eDate));
		c.addOrder(Order.desc("surveyTime"));
		return c.list();
	}

	@Override
	public List<ZGD_Data> getTwoDataBySurveyPointInTwoSide(String surveyPointUuid, Date sDate, Date eDate) {
		List<ZGD_Data> result = new ArrayList<>();
		List<ZGD_Data> temp = new ArrayList<>();
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("surveyTime", sDate, eDate));
		c.addOrder(Order.asc("surveyTime"));
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
