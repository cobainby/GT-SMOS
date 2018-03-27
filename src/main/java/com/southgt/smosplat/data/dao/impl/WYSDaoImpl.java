package com.southgt.smosplat.data.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.data.dao.IWYSDao;
import com.southgt.smosplat.data.entity.WYS_OriginalData;

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
@Repository("wysDao")
public class WYSDaoImpl extends BaseDaoImpl<WYS_OriginalData> implements IWYSDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(WYS_OriginalData.class).setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WYS_OriginalData> getLatestWYSDatas(String surveyPointUuid) {
		//最新的11个时间点
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.setProjection(Projections.distinct(Projections.property("surveyTime")));
		c.addOrder(Order.desc("surveyTime"));
		c.setMaxResults(11);
		List<Object> latestTimes=c.list();
		//找出该点的最新11个时间点的所有数据
		Criteria c1=getCriteria();
		c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c1.add(Restrictions.in("surveyTime", latestTimes));
		c1.addOrder(Order.desc("surveyTime"));
		return c1.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WYS_OriginalData> getFirstWYSDatas(String surveyPointUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.addOrder(Order.asc("surveyTime"));
		c.setMaxResults(1);
		Date firstTime=((WYS_OriginalData)c.uniqueResult()).getSurveyTime();
		Criteria c1=getCriteria();
		c1.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c1.add(Restrictions.eq("surveyTime", firstTime));
		return c1.list();
	}

	@Override
	public List<Date> getLatestWYSDates(String surveyPointUuid) {
		Query query=getSession().createQuery("select distinct data.surveyTime from WYS_OriginalData data where data.surveyPoint.surveyPointUuid=? order by surveyTime").setParameter(0, surveyPointUuid);
		query.setMaxResults(11);
		return query.list();
	}
	
	@Override
	public List<WYS_OriginalData> getWYSDatesByDate(Date startDate,Date endDate) {
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//格式化开始日期和结束日期
		String start = dd.format(startDate);
		String end = dd.format(endDate);
		String hql = "from WYS_OriginalData where  surveyTime >= '" + start +"' and surveyTime <= '"+end+"'";
		Query query=getSession().createQuery(hql);
		return query.list();

	}

	@Override
	public Date getfirstWYSDate(String surveyPointUuid) {
		Query query=getSession().createQuery("select distinct data.surveyTime from WYS_OriginalData data where data.surveyPoint.surveyPointUuid=? order by surveyTime desc").setParameter(0, surveyPointUuid);
		query.setMaxResults(1);
		return (Date)query.uniqueResult();
	}

	@Override
	public List<WYS_OriginalData> getWYSDatesByPecificDate(String surveyPointUuid, Date date) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("surveyPoint.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.eq("surveyTime", date));
		return c.list();
	}

	@Override
	public void deleteWYSLevelDataBySurveyPoint(String spUuid) {
		Query query=getSession().createQuery("delete from WYS_OriginalData d where d.surveyPoint.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", spUuid);
		query.executeUpdate();
		
	}

	@Override
	public boolean ifDataUnique(Date date, String projectUuid) {
		Criteria c=getSession().createCriteria(WYS_OriginalData.class,"wys").setCacheable(true);
		c.createAlias("wys.surveyPoint", "sp");
		c.createAlias("sp.project", "p");
		c.add(Restrictions.eq("p.projectUuid", projectUuid));
		c.add(Restrictions.eq("surveyTime", date));
		List<Object>t = c.list();
		if(t.size() > 0){
			return false;
		}else{
			return true;
		}
	}

}
