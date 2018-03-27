package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.project.dao.ICableMeterDao;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Stress;

@Repository("cableMeterDao")
public class CableMeterDaoImpl extends BaseDaoImpl<CableMeter> implements ICableMeterDao {

	private Criteria getCriteria(){
		return getSession().createCriteria(CableMeter.class,"c").setCacheable(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CableMeter> getCableMeterByProject(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CableMeter> getCableMeterBySP_mt(String sp_MTUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("sp_MT.surveyPointUuid", sp_MTUuid));
		return c.list();
	}

	@Override
	public long getCableMeterNumByConditions(String sp_MTUuid, String name) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("sp_MT.surveyPointUuid", sp_MTUuid));
		c.add(Restrictions.eq("name",name));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@Override
	public long getCableMeterNumByConditionsSelfException(String sp_MTUuid, String name,String cableMeterUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("sp_MT.surveyPointUuid", sp_MTUuid)).add(Restrictions.not(Restrictions.eq("cableMeterUuid", cableMeterUuid)));
		c.add(Restrictions.eq("name",name));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CableMeter> getCableMeterByMcu(String mcuUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("mcu.mcuUuid", mcuUuid));
		return c.list();
	}

	@Override
	public void deleteCaleMeterBySP(String spUUid) {
		List<CableMeter> list=getCableMeterBySP_mt(spUUid);
		Query query=getSession().createQuery("delete from CableMeter c where c.sp_MT.surveyPointUuid=:surveyPointUuid");
		query.setParameter("surveyPointUuid", spUUid);
		query.executeUpdate();
		
		for(CableMeter c:list){
			Query query1=getSession().createQuery("delete from Device d where d.deviceUuid=:deviceUuid");
			query1.setParameter("deviceUuid", c.getDevice().getDeviceUuid());
			query1.executeUpdate();
		}
	}

	@Override
	public CableMeter getCalbeMeterByDevice(String deviceUuid) {
		Criteria c=getCriteria();
		c.createAlias("c.device", "d");
		c.add(Restrictions.eq("d.deviceUuid", deviceUuid));
		return (CableMeter) c.list().get(0);
	}
	
	
}
