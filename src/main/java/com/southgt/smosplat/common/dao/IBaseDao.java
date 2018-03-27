package com.southgt.smosplat.common.dao;

import java.util.List;

public interface IBaseDao<T> {
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.saveEntity
	 * @Description  保存实体到数据库 
	 * @date  2016年5月30日 上午10:44:12
	 * 
	 * @version v1.0
	 * @param @param t 实体对象
	 * @return void
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public void saveEntity(T t);
	
	/**
	 * 
	 * 合并不同session中的实例
	 * @date  2016年7月26日 下午3:35:09
	 * @return T
	 * @param t
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  admin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年7月26日     admin      v1.0          create</p>
	 *
	 */
	public T mergeEntity(T t);
	
	/**
	 * 
	 * 从session中清除持久化对象
	 * @date  2016年7月26日 下午4:57:31
	 * @return void
	 * @param t
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  admin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年7月26日     admin      v1.0          create</p>
	 *
	 */
	public void evictEntity(T t);
	
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.updateEntity
	 * @Description  更新实体到数据库 
	 * @date  2016年5月30日 上午10:44:26
	 * 
	 * @version v1.0
	 * @param @param t 实体对象
	 * @return void
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public void updateEntity(T t);
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.saveOrUpdateEntity
	 * @Description  保存或者更新实体到数据库
	 * @date  2016年5月30日 上午10:44:47
	 * 
	 * @version v1.0
	 * @param @param t 实体对象
	 * @return void
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public void saveOrUpdateEntity(T t);
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.deleteEntity
	 * @Description  从数据库中删除一个实体 
	 * @date  2016年5月30日 上午10:49:06
	 * 
	 * @version v1.0
	 * @param @param t 实体对象
	 * @return void
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public void deleteEntity(T t);
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.batchEntityByHQL
	 * @Description  根据hql批量操作数据库
	 * @date  2016年5月30日 上午10:49:41
	 * 
	 * @version v1.0
	 * @param @param hql hql语句
	 * @param @param objects hql语句可变参数
	 * @return void
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public void batchEntityByHQL(String hql,Object...objects);
	
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.loadEntity
	 * @Description  从数据库中加载一个实体 
	 * @date  2016年5月30日 上午10:53:43
	 * 
	 * @version v1.0
	 * @param @param id 实体id
	 * @param @return
	 * @return T
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public T loadEntity(Integer id);
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.getEntity
	 * @Description  从数据库中获取一个实体
	 * @date  2016年5月30日 上午10:58:10
	 * 
	 * @version v1.0
	 * @param @param id 实体id
	 * @param @return
	 * @return T
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public T getEntity(Integer id);
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.getEntity
	 * @Description  从数据库中获取一个实体
	 * @date  2016年6月15日 下午3:33:43
	 * 
	 * @version v1.0
	 * @param @param id 实体id
	 * @param @return
	 * @return T
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年6月15日     mohaolin      v1.0          create</p>
	 *
	 */
	public T getEntity(String id);
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.findEntityByHQL
	 * @Description  根据hql从数据库中获取实体集合
	 * @date  2016年5月30日 上午10:58:29
	 * 
	 * @version v1.0
	 * @param @param hql hql语句
	 * @param @param objects hql可变参数
	 * @param @return
	 * @return List<T>
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public List<T> findEntityByHQL(String hql,Object...objects);
	/**
	 * 
	 * @Function  com.southgt.smos.common.dao.IBaseDao.findAllEntity
	 * @Description  从数据库中获取所有实体 
	 * @date  2016年5月30日 上午10:59:07
	 * 
	 * @version v1.0
	 * @param @return
	 * @return List<T> T类型的所有实体的集合
	 * @throws null
	 * 
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年5月30日     mohaolin      v1.0          create</p>
	 *
	 */
	public List<T> findAllEntity();


}
