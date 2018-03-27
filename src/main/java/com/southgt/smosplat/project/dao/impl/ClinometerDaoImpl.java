package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.IClinometerDao;
import com.southgt.smosplat.project.entity.Clinometer;
/**
 * 
 * 测斜仪数据库dao实现类
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年1月14日     姚家俊       v1.0.0        create</p>
 *
 */
@Repository("clinometerDao")
public class ClinometerDaoImpl extends BaseDaoImpl<Clinometer> implements IClinometerDao {

	
	private Criteria getCriteria(){
		return getSession().createCriteria(Clinometer.class,"c").setCacheable(true);
	}
	
	@Override
	public List<Clinometer> getClinometerByProject(String projectUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}

	@Override
	public List<Clinometer> getClinometerBySP_cx(String sp_CXUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("sp_CX.surveyPointUuid", sp_CXUuid));
		return c.list();
	}

	@Override
	public long getClinometerNumByConditions(String sp_CXUuid, String name, String devCode) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("sp_CX.surveyPointUuid", sp_CXUuid));
		c.add(Restrictions.or(Restrictions.eq("name",name),Restrictions.eq("devCode",devCode)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getClinometerNumByConditionsSelfException(String sp_CXUuid, String name, String devCode,
			String clinometerUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("sp_CX.surveyPointUuid", sp_CXUuid)).add(Restrictions.not(Restrictions.eq("clinometerUuid", clinometerUuid)));
		c.add(Restrictions.or(Restrictions.eq("name",name),Restrictions.eq("devCode",devCode)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public Clinometer getClinometerByDevice(String deviceUuid) {
		Criteria c = getCriteria();
		c.createAlias("c.device", "d");
		c.add(Restrictions.eq("d.deviceUuid", deviceUuid));
		return (Clinometer) c.list().get(0);
	}

	@Override
	public void deleteClinometerBySurveyPoint(String spUuid) {
			List<Clinometer> list= getClinometerBySP_cx(spUuid);
			Query query=getSession().createQuery("delete from Clinometer c where c.sp_CX.surveyPointUuid=:surveyPointUuid");
			query.setParameter("surveyPointUuid", spUuid);
			query.executeUpdate();
			
			for(Clinometer c:list){
				Query query1=getSession().createQuery("delete from Device d where d.deviceUuid=:deviceUuid");
				query1.setParameter("deviceUuid", c.getDevice().getDeviceUuid());
				query1.executeUpdate();
			}
		
	}

}
