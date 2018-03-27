package com.southgt.smosplat.organ.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IWorkerDao;
import com.southgt.smosplat.organ.entity.Worker;

@Repository("workerDao")
public class WorkerDaoImpl extends BaseDaoImpl<Worker> implements IWorkerDao {

	private Criteria getCriteria(){
		Criteria criteria = getSession().createCriteria(Worker.class).setCacheable(true);
		return criteria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> getWorkers(int first, int max) {
		Criteria c=getCriteria();
		List<Worker> dataList=c.setFirstResult(first).setMaxResults(max).list();
		return dataList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> getWorkers(int first, int max, String organUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		List<Worker> dataList=c.setFirstResult(first).setMaxResults(max).list();
		return dataList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> getWorkersByOrgan(String organUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		return c.list();
	}
	
	@Override
	public Worker getWorkerByAccount(String accountUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("account.accountUuid", accountUuid));
		return (Worker)c.uniqueResult();
	}

	@Override
	public long getWorkersNumberByOrgan(String organUuid) {
		Criteria c=getCriteria();
		c.add(Restrictions.eq("organ.organUuid", organUuid));
		c.setProjection(Projections.rowCount());
		return (long) c.uniqueResult();
	}
}
