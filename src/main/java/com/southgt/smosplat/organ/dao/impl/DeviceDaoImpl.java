package com.southgt.smosplat.organ.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IDeviceDao;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.Project;

@Repository("deviceDao")
public class DeviceDaoImpl extends BaseDaoImpl<Device> implements IDeviceDao {

	private Criteria getCriteria(){
		Criteria criteria = getSession().createCriteria(Device.class,"d").setCacheable(true);
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getDeviceByOrganUuid(String organUuid,int i) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.createAlias("d.devType", "dt");
		c.add(Restrictions.eq("dt.isAuto", (byte)i));
		return c.list();
	}

	@Override
	public long getDeviceBySN(String organUuid,String sn) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("sn", sn));
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return  number;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getUsingDeviceByOrgan(String organUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.add(Restrictions.eq("status", 0));
		return c.list();
	}

	@Override
	public long getDeviceBySNExceptSelf(String organUuid, String sn, String deviceUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("sn", sn));
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.add(Restrictions.not(Restrictions.eq("deviceUuid", deviceUuid)));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return  number;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getDevicesByProject(Project project) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("projectUuid", project.getProjectUuid()));
		return c.list();
	}

	@Override
	public long getDeviceNumber(String projectUuid, String sn) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("projectUuid", projectUuid));
		c.add(Restrictions.eq("sn", sn));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return  number;
	}

	@Override
	public long getDeviceNumberExceptSelf(String projectUuid, String sn, String deviceUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("sn", sn));
		c.add(Restrictions.eq("projectUuid", projectUuid));
		c.add(Restrictions.not(Restrictions.eq("deviceUuid", deviceUuid)));
		c.setProjection(Projections.rowCount());
		long number=(long) c.uniqueResult();
		return  number;
	}

	@Override
	public long getDeviceNumberByMcu(String mcuUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("mcu.mcuUuid", mcuUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getUsingMcuMudleAndPointNumByMcu(String mcuUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("mcu.mcuUuid", mcuUuid));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getUsingPointNumByMcuAndModuleNum(String mcuUuid, int moduleNum) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("mcu.mcuUuid", mcuUuid)).add(Restrictions.eq("moduleNum", moduleNum));
		return c.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getDevicesByMcu(String mcuUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("mcu.mcuUuid", mcuUuid));
		return c.list();
	}

	@Override
	public long getDeviceNumberByOrgan(String organUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Device> getAutoDevicesByCurrentProject(String projectUuid, int i) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("projectUuid", projectUuid));
		c.createAlias("d.devType", "dt");
		c.add(Restrictions.eq("dt.isAuto", (byte)i));
		return c.list();
	}
	
}
