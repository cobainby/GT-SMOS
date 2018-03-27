package com.southgt.smosplat.common.service;

import java.util.List;

public interface IBaseService<T> {
	/**
	 * 
	 * @Function  com.southgt.smos.common.service.IBaseService.saveEntity
	 * @Description  保存实体到数据库中
	 * @date  2016年5月30日 上午9:49:39
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
	 * @Function  com.southgt.smos.common.service.IBaseService.updateEntity
	 * @Description  更新实体到数据库
	 * @date  2016年5月30日 上午9:50:35
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
	 * @Function  com.southgt.smos.common.service.IBaseService.saveOrUpdateEntity
	 * @Description  添加或者更新实体到数据库 
	 * @date  2016年5月30日 上午9:51:09
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
	 * @Function  com.southgt.smos.common.service.IBaseService.deleteEntity
	 * @Description  删除一个实体，同时从数据库中删除一条记录 
	 * @date  2016年5月30日 上午9:51:34
	 * 
	 * @version v1.0
	 * @param @param t 实体对象，必须要有id值，否则删除出错
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
	 * @Function  com.southgt.smos.common.service.IBaseService.batchEntityByHQL
	 * @Description  根据hql语句批量对数据库实体进行处理
	 * @date  2016年5月30日 上午9:52:01
	 * 
	 * @version v1.0
	 * @param @param hql hql语句
	 * @param @param objects hql语句的参数
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
	 * @Function  com.southgt.smos.common.service.IBaseService.loadEntity
	 * @Description  从数据库中加载实体
	 * @date  2016年5月30日 上午9:53:18
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
	 * @Function  com.southgt.smos.common.service.IBaseService.getEntity
	 * @Description  从数据库总获取实体 
	 * @date  2016年5月30日 上午10:23:34
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
	 * @Function  com.southgt.smos.common.service.IBaseService.getEntity
	 * @Description  从数据库总获取实体 
	 * @date  2016年5月30日 上午10:23:34
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
	public T getEntity(String id);
	/**
	 * 
	 * @Function  com.southgt.smos.common.service.IBaseService.findEntityByHQL
	 * @Description  根据hql语句获取实体集合 
	 * @date  2016年5月30日 上午10:24:31
	 * 
	 * @version v1.0
	 * @param @param hql hql查询语句
	 * @param @param objects hql查询语句的参数
	 * @param @return
	 * @return List<T> T类型实体集合
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
	 * 从数据库获取所有实体
	 * @date  2017年3月10日 下午4:41:15
	 * @return List<T>
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月10日     mohaolin      v1.0          create</p>
	 *
	 */
	public List<T> findAllEntity();
}
