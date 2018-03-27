package com.southgt.smosplat.data.dao.impl;

import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.data.dao.IQXDataDao;
import com.southgt.smosplat.data.entity.QX_Data;
import com.southgt.smosplat.data.entity.QX_Data;
@Repository("qxDao")
public class QXDataDaoImpl extends BaseDaoImpl<QX_Data> implements IQXDataDao{

	private Criteria getCriteria(){
		return getSession().createCriteria(QX_Data.class).setCacheable(true);
	}
	
	@Override
	public List<QX_Data> getLatestQXDataBySurveyPoint(List<String> spUuids) {
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
		List<QX_Data> dataList = (List<QX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<QX_Data>() {
			@Override
			public int compare(QX_Data sp1, QX_Data sp2) {  
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
	public List<QX_Data> getThreeAscQXDataBySurveyPoint(List<String> spUuids) {
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
		
		List<QX_Data> dataList = (List<QX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<QX_Data>() {
			@Override
			public int compare(QX_Data sp1, QX_Data sp2) {  
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
	public List<QX_Data> getLatestOneQXDataBySurveyPoint(List<String> spUuids) {
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
		List<QX_Data> dataList = (List<QX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<QX_Data>() {
			@Override
			public int compare(QX_Data sp1, QX_Data sp2) {  
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
	public List<QX_Data> getQXDatasBySurveyOrderAndSurveyPoint(int surveyOrder, String surveyPointUuid) {
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
	public void deleteQXDataBySP(String surveyPointUuid) {
		Query query=getSession().createQuery("delete from QX_Data d where d.surveyPoint.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", surveyPointUuid);
		query.executeUpdate();
		
	}

	@Override
	public List<QX_Data> getOnePointLatestQXDataBySurveyPoint(String spUuid) {
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
		List<QX_Data> dataList = (List<QX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<QX_Data>() {
			@Override
			public int compare(QX_Data sp1, QX_Data sp2) {  
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
	public List<QX_Data> getOnePointThreeAscQXDataBySurveyPoint(String spUuid) {
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
		
		List<QX_Data> dataList = (List<QX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<QX_Data>() {
			@Override
			public int compare(QX_Data sp1, QX_Data sp2) {  
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
	public List<QX_Data> getOnePointLatestOneQXDataBySurveyPoint(String spUuid) {
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
		List<QX_Data> dataList = (List<QX_Data>)c1.list();
		//根据深度排序
		dataList.sort(new Comparator<QX_Data>() {
			@Override
			public int compare(QX_Data sp1, QX_Data sp2) {  
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

}
