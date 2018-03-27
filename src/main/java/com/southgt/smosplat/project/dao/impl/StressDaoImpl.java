package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.dao.IStressDao;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Stress;

@Repository("stressDao")
public class StressDaoImpl extends BaseDaoImpl<Stress> implements IStressDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(Stress.class,"s").setCacheable(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Stress> getStressByProject(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}

	@Override
	public long getStressNumByConditions(String surveyPointUuid, String name, String devCode) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("sp_ZC.surveyPointUuid", surveyPointUuid));
		c.add(Restrictions.or(Restrictions.eq("name",name),Restrictions.eq("devCode",devCode)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getStressNumByConditionsSelfException(String surveyPointUuid, String name, String devCode,
			String stressUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("sp_ZC.surveyPointUuid", surveyPointUuid)).add(Restrictions.not(Restrictions.eq("stressUuid", stressUuid)));
		c.add(Restrictions.or(Restrictions.eq("name",name),Restrictions.eq("devCode",devCode)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Stress> getStressBySP_zc(String sp_ZCUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("sp_ZC.surveyPointUuid", sp_ZCUuid));
		return c.list();
	}

	@Override
	public Stress getStressByDevice(String deviceUuid) {
		Criteria c=getCriteria();
		c.createAlias("s.device", "d");
		c.add(Restrictions.eq("d.deviceUuid", deviceUuid));
		return (Stress) c.list().get(0);
	}

	@Override
	public void deleteStressBySurveyPoint(String spUuid) {
		List<Stress> list= getStressBySP_zc(spUuid);
		Query query=getSession().createQuery("delete from Stress s where s.sp_ZC.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", spUuid);
		query.executeUpdate();
		
		for(Stress c:list){
			Query query1=getSession().createQuery("delete from Device d where d.deviceUuid=:deviceUuid");
			query1.setParameter("deviceUuid", c.getDevice().getDeviceUuid());
			query1.executeUpdate();
		}
	}
	
}
