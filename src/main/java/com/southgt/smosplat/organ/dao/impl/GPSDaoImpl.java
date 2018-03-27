package com.southgt.smosplat.organ.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.southgt.smosplat.common.dao.impl.BaseDaoImpl;
import com.southgt.smosplat.organ.dao.IGPSDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.GPSInfo;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年10月13日     mohaolin       v1.0.0        create</p>
 *
 */
@Repository("gpsDao")
public class GPSDaoImpl extends BaseDaoImpl<GPSInfo> implements IGPSDao {

	private Criteria getCriteria(){
		Criteria c=getSession().createCriteria(GPSInfo.class).setCacheable(true);
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GPSInfo> getGPSInfosByAccount(Account account, Date startDate, Date endDate, String projectUuid) {
		Criteria c = getCriteria();
		c.add(Restrictions.eq("account.accountUuid", account.getAccountUuid()));
		c.add(Restrictions.between("time", startDate, endDate));
		c.add(Restrictions.eq("projectUuid", projectUuid));
		return c.list();
	}

}
