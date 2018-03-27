package com.southgt.smosplat.project.service.autoCollect.mcu;

import java.util.Calendar;
import java.util.List;

import com.southgt.smosplat.data.vo.CabelMeterMeasureData;
import com.southgt.smosplat.data.vo.CxMessureData;
import com.southgt.smosplat.data.vo.StressMeasureData;
import com.southgt.smosplat.data.vo.WaterLevelMeasureData;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.project.entity.Mcu;

public interface IAbstract_McuCollectLogic {
	
	/**
	 * 设置mcu
	 */
	void setMcus(List<Mcu> mcus);
	/**
	 * 获取mcu
	 */
	public List<Mcu> getMcus();
	/**
	 * 首次采集对仪器进行设置是否完毕
	 */
	boolean isSetOver();
	/**
	 * 
	 * 是否测试mcu完成
	 * @date  2017年7月10日 下午2:14:23
	 * 
	 * @return
	 * boolean
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月10日     姚家俊      v1.0          create</p>
	 *
	 */
	public boolean isTestOver();
	
	/**
	 * 是否获取数据完毕 
	 */
	boolean isGetDataOver();
	
	/**
	 * 获得所有轴力计测到的数据
	 * @date  2017年4月28日 上午10:15:04
	 * @return List<StressMeasureData>
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月28日     mohaolin      v1.0          create</p>
	 *
	 */
	List<StressMeasureData> getStressDataList();
	
	/**
	 * 设置轴力列表 
	 * @date  2017年4月28日 上午11:46:33
	 * @return void
	 * @param stressDataList
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月28日     mohaolin      v1.0          create</p>
	 *
	 */
	void setStressDataList(List<StressMeasureData> stressDataList);
	
	/**
	 * 
	 * 获取水位数据列表
	 * @date  2017年5月2日 下午2:57:58
	 * 
	 * @return
	 * List<WaterLevelMeasureData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月2日     姚家俊      v1.0          create</p>
	 *
	 */
	List<WaterLevelMeasureData> getWaterLevelDataList();
	
	/**
	 * 
	 * 设置水位数据列表
	 * @date  2017年5月2日 下午2:58:28
	 * 
	 * @param waterLevelDataList
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月2日     姚家俊      v1.0          create</p>
	 *
	 */
	void setWaterLevelDataList(List<WaterLevelMeasureData> waterLevelDataList);
	/**
	 * 获取设备列表
	 * @date  2017年5月8日 下午2:15:00
	 * @return List<Device>
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月8日     mohaolin      v1.0          create</p>
	 *
	 */
	List<Device> getDeviceList();
	/**
	 * 设置设备列表
	 * @date  2017年5月8日 下午2:15:11
	 * @return void
	 * @param deviceList
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月8日     mohaolin      v1.0          create</p>
	 *
	 */
	void setDeviceList(List<Device> deviceList);
	/**
	 * 
	 * 获取锚索监测数据
	 * @date  2017年5月24日 上午11:43:49
	 * 
	 * @return
	 * List<CabelMeterMeasureData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月24日     姚家俊      v1.0          create</p>
	 *
	 */
	List<CabelMeterMeasureData> getCableDataList();
	
	/**
	 * 
	 * 获取测斜数据列表
	 * @date  2018年1月15日 上午9:18:29
	 * 
	 * @return
	 * List<CxMessureData>
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月15日     姚家俊      v1.0          create</p>
	 *
	 */
	public List<CxMessureData> getCxDataList();
	
	/**
	 * 
	 * 保存mcu报文源文件的路径
	 * @date  2018年1月31日 下午2:45:26
	 * 
	 * @param uploadFileSrc
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月31日     姚家俊      v1.0          create</p>
	 *
	 */
	public void setUploadFileSrc(String uploadFileSrc);
	
	/**
	 * 
	 * 设置采集周期
	 * @date  2018年1月31日 下午2:45:52
	 * 
	 * @param interval
	 * void
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月31日     姚家俊      v1.0          create</p>
	 *
	 */
	public void setInterval(int interval);
	
	/**
	 * 
	 * 获取mcu第一次采集时的时间
	 * @date  2018年1月31日 下午2:46:05
	 * 
	 * @return
	 * Calendar
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月31日     姚家俊      v1.0          create</p>
	 *
	 */
	public Calendar getFirstCollectTime();
	
	/**
	 * 
	 * 获取下个mcu采集时间
	 * @date  2018年1月30日 下午7:17:03
	 * 
	 * @return
	 * Calendar
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2018年1月30日     姚家俊      v1.0          create</p>
	 *
	 */
	public Calendar getNextCollectTime();
}
