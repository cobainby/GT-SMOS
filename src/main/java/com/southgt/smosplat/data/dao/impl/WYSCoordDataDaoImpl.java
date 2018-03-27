package com.southgt.smosplat.data.dao.impl;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.data.dao.IWYSCoordDataDao;
import com.southgt.smosplat.data.entity.WYS_CoordData;

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
@Repository("wysCoordDataDao")
public class WYSCoordDataDaoImpl extends BaseDaoImpl<WYS_CoordData> implements IWYSCoordDataDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(WYS_CoordData.class).setCacheable(true);
	}

	@Override
	public List<WYS_CoordData> getLatestOneWYSCoordDataBySurveyPoint(List<String> surveyPointUuids) {
		//最新的1个时间点
//		Criteria c=getCriteria();
//		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
//		c.addOrder(Order.desc("surveyTime"));
//		c.setMaxResults(1);
//		return (WYS_CoordData) c.uniqueResult();
		
		//最新的1个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		
		//找出该点的最新11个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTime != null){
			c1.add(Restrictions.eq("surveyTime", latestTime));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
			c1.addOrder(Order.desc("surveyTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public List<WYS_CoordData> getFirstWYSCoordDataBySurveyPoint(List<String> surveyPointUuids) {
		//最新的11个时间点
//		Criteria c=getCriteria();
//		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
//		c.addOrder(Order.asc("surveyTime"));
//		c.setMaxResults(1);
//		return (WYS_CoordData) c.uniqueResult();
		
		//最新的11个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.asc("surveyTime"));
		c.setMaxResults(1);
		Object latestTime=c.uniqueResult();
		
		
		//找出该点第1个时间点的所有数据
		Criteria c1=getCriteria();
		if(latestTime != null){
			c1.add(Restrictions.eq("surveyTime", latestTime));
			c1.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
			c1.addOrder(Order.asc("surveyTime"));
			return c1.list();
		}else{
			return new ArrayList<>();
		}
	}

	@Override
	public List<WYS_CoordData> getLatestWYSCoordDataBySurveyPoint(List<String> surveyPointUuids) {
		//最新的1个时间点
//		Criteria c=getCriteria();
//		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
//		c.addOrder(Order.desc("surveyTime"));
//		c.setMaxResults(11);
//		return  c.list();
		
		
		//最新的11个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.in("surveyPoint.surveyPointUuid", surveyPointUuids));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(11);
		List<Object> latestTimes=c.list();
		
		
		//找出该点的最新11个时间点的所有数据
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
	public void deleteWYSDataBySurveyPoint(String spUuid) {
		Query query=getSession().createQuery("delete from WYS_CoordData d where d.surveyPoint.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", spUuid);
		query.executeUpdate();
		
	}

	@Override
	public List<WYS_CoordData> getTwoWYSCoordDataBySurveyPointAndDate(String surveyPointUuid, Date sDate, Date eDate) {
		List<WYS_CoordData> result = new ArrayList<>();
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("surveyTime", sDate, eDate));
		c.addOrder(Order.asc("surveyTime"));
		c.setMaxResults(1);
		
		Criteria cc1=getCriteria();
		cc1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		cc1.add(Restrictions.between("surveyTime", sDate, eDate));
		cc1.addOrder(Order.desc("surveyTime"));
		cc1.setMaxResults(1);
		//开始日期的第一条数据
		WYS_CoordData latestData = (WYS_CoordData) c.uniqueResult();
		//开始日期的最后一条数据
		WYS_CoordData latestData1 = (WYS_CoordData) cc1.uniqueResult();
		if(latestData != null){
			result.add(latestData1);
			Criteria c1=getCriteria();
			c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
			//le小于等于，lt小于
			c1.add(Restrictions.lt("surveyTime", latestData.getSurveyTime()));
			c1.addOrder(Order.desc("surveyTime"));
			c1.setMaxResults(1);
			WYS_CoordData lastData = (WYS_CoordData) c1.uniqueResult();
			if(lastData != null){
				result.add(lastData);
			}
		}
		return result;
	}

	@Override
	public List<WYS_CoordData> getLatestTwoWYSCoordDataBySurveyPoint(String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(2);
		return c.list();
	}
	
	@Override
	public List<WYS_CoordData> getTwoWYSCoordDataBySurveyPointInTwoSide(String surveyPointUuid, Date sDate, Date eDate) {
		List<WYS_CoordData> result = new ArrayList<>();
		List<WYS_CoordData> temp = new ArrayList<>();
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

	@Override
	public WYS_CoordData getLatestDataBySurveyPointBeforeOneDateTime(String surveyPointUuid, Date Monday) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.le("surveyTime", Monday));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(1);
		return (WYS_CoordData) c.uniqueResult();
	}

	@Override
	public List<WYS_CoordData> getDataBySurveyPointInOnePeriod(String surveyPointUuid, Date sDate, Date eDate) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.between("surveyTime", sDate, eDate));
		c.addOrder(Order.desc("surveyTime"));
		return c.list();
//		Query query=getSession().createQuery("FROM WYS_CoordData d where d.surveyPoint.surveyPointUuid=:surveyPointUuid AND d.surveyTime <= :d1 AND d.surveyTime >= :d2 order by d.surveyTime desc" );
//		query.setParameter("surveyPointUuid", surveyPointUuid);
//		query.setParameter("d1", eDate);
//		query.setParameter("d2", sDate);
//		return query.list();
	}

	@Override
	public List<WYS_CoordData> getFirstThreeDataBySurveyPoint(String surveyPointUuid) {
		//最新的3个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
//		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.asc("surveyTime"));
		c.setMaxResults(3);
//		getSession().evict(this);
		return c.list();
	}


	

}
