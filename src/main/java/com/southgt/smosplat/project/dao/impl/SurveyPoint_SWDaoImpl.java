package com.southgt.smosplat.project.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.dao.ISurveyPoint_SWDao;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;

@Repository("sp_SWDao")
public class SurveyPoint_SWDaoImpl extends BaseDaoImpl<SurveyPoint_SW> implements ISurveyPoint_SWDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(SurveyPoint_SW.class,"sw").setCacheable(true);
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_SW> getExistedSurveyPoint_SWsByCode(String projectUuid, String monitorItemUuid,
			String codeChar) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		c.add(Restrictions.eq("codeChar", codeChar));
		return c.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_SW> getSurveyPoint_SWs(String projectUuid, String monitorItemUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid));
		return c.list();
	}

	@Override
	public long getSP_SWByCode(String projectUuid, String monitorItemUuid, String code) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		.add(Restrictions.eq("code", code));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
		
	}

	@Override
	public long getSP_SWByCodeExceptSelf(String projectUuid, String monitorItemUuid, String code, String spUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid)).add(Restrictions.eq("monitorItem.monitorItemUuid", monitorItemUuid))
		 .add(Restrictions.eq("code", code)).add(Restrictions.not(Restrictions.eq("surveyPointUuid", spUuid)));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getDeviceSnByItemName(String devTypeName,String monitorItem) {
		Query query=getSession().createQuery("select d from Device d,DeviceType dt where d.devType.devTypeUuid=dt.devTypeUuid and dt.devTypeName=:devTypeName and d not in (select sp.device from "+monitorItem+" sp)");
		query.setString("devTypeName", devTypeName);
		return query.list();
	}

	@Override
	public SurveyPoint_SW getSPSWByDevice(String deviceUuid) {
		Criteria c=getCriteria();
		c.createAlias("sw.device", "d");
		c.add(Restrictions.eq("d.deviceUuid", deviceUuid));
		return (SurveyPoint_SW) c.list().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyPoint_SW> getSP_SWsByProject(String projectUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("project.projectUuid", projectUuid));
		return c.list();
	}

}
