package com.southgt.smosplat.organ.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.ScaleImage;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.entity.Project;

/**
 * 
 * 设备管理控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月13日     姚家俊       v1.0.0        create</p>
 *
 */
/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 吴达
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月6日     吴达       v1.0.0        create</p>
 *
 */
@Controller
public class DeviceController {
	@Resource
	private IDeviceService deviceService;
	
	/**
	 * 
	 * 加载设备管理页面
	 * @date  2017年3月13日 上午10:38:19
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月13日     姚家俊      v1.0          create</p>
	 *
	 */
	@RequestMapping("/device")
	public String toDevice(){
		return "organ/view/device";
	}
	
	/**
	 * 根据组织机构获取设备信息
	 * 
	 * @date  2017年3月30日 上午9:16:00
	 * 
	 * @param @param organUuid
	 * @param @return
	 * @return String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  杨杰
	 * <p>Modification History:</p>
	 * <p>Date         Author     Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月30日      杨杰     	   v1.0          create</p>
	 *
	 */
	@RequestMapping(value="/getDeviceByOrgan")
	@ResponseBody
	public String getDeviceByOrgan(String organUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Account account=(Account) session.getAttribute("account");
		boolean isSuperAdmin=account.getLoginName().equals("superadmin")?true:false;
		List<Device> devices=new ArrayList<Device>();
		//超级管理员查询所有,有机构筛选查询机构下的设备
		if(isSuperAdmin){
			devices=deviceService.getDeviceByOrganUuid(organUuid,-1);
		}else{
			String currentOrganUuid=(String) session.getAttribute("currentOrganUuid");
			devices=deviceService.getDeviceByOrganUuid(currentOrganUuid,-1);
		}
		for (Device device : devices) {
			device.setMcu(null);
		}
		map.put("rows", devices);
		map.put("total", devices.size());
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/getDevicesByCurrentOrgan",method=RequestMethod.POST)
	@ResponseBody
	public String getDevicesByCurrentOrgan(HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		//获得当前机构id
		String curOrganUuid=(String) session.getAttribute("currentOrganUuid");
		List<Device> devices=deviceService.getDeviceByOrganUuid(curOrganUuid,0);
		map.put("rows", devices);
		map.put("total", devices.size());
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/getNoAutoDeviceMapOrderByDevTypeOfOrgan",method=RequestMethod.POST)
	@ResponseBody
	public String getNoAutoDeviceMapOrderByDevTypeOfOrgan(HttpSession session,String organUuid){
		Map<String,List<Device>> devicesMap=deviceService.getDeviceMapOrderByDevTypeOfOrgan(organUuid,-1);
		String s=JsonUtil.beanToJson(devicesMap);
		return s;
	}
	
	
	@RequestMapping(value="/getNoAutoDeviceByOrganUuid")
	@ResponseBody
	public String getNoAutoDeviceByOrganUuid(String organUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Device> devices=new ArrayList<Device>();
		devices=deviceService.getDeviceByOrganUuid(organUuid,-1);
		for (Device device : devices) {
			device.setMcu(null);
		}
		map.put("rows", devices);
		map.put("total", devices.size());
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/getAutoDevicesByOrganUuid")
	@ResponseBody
	public String getAutoDevicesByOrganUuid(String organUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		Account account=(Account) session.getAttribute("account");
		boolean isSuperAdmin=account.getLoginName().equals("superadmin")?true:false;
		List<Device> devices=new ArrayList<Device>();
		//超级管理员查询所有,有机构筛选查询机构下的设备
		if(isSuperAdmin){
			devices=deviceService.getDeviceByOrganUuid(organUuid,0);
		}else{
			String currentOrganUuid=(String) session.getAttribute("currentOrganUuid");
			devices=deviceService.getDeviceByOrganUuid(currentOrganUuid,0);
		}
		
		for (Device device : devices) {
			device.setMcu(null);
		}
		map.put("rows", devices);
		map.put("total", devices.size());
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/getAutoDevicesByOrganUuidForAndriod")
	@ResponseBody
	public String getAutoDevicesByOrganUuidForAndriod(String organUuid,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Device> devices=new ArrayList<Device>();
		devices=deviceService.getDeviceByOrganUuid(organUuid,0);
		for (Device device : devices) {
			device.setMcu(null);
		}
		map.put("rows", devices);
		map.put("total", devices.size());
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/getAutoDevicesByCurrentProject",method=RequestMethod.POST)
	@ResponseBody
	public String getAutoDevicesByCurrentProject(HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		//获得当前工程id
		Project project=(Project) session.getAttribute("currentProject");
		List<Device> devices=deviceService.getAutoDevicesByCurrentProject(project.getProjectUuid(),0);
		map.put("rows", devices);
		map.put("total", devices.size());
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping(value="/addDevice",method=RequestMethod.POST)
	@ResponseBody
	public String addDevice(Device device){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			deviceService.addDevice(device);
			map.put("result", 0);
			map.put("entity", device);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/updateDevice",method=RequestMethod.POST)
	@ResponseBody
	public String updateDevice(Device device){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			deviceService.updateDevice(device);
			map.put("result", 0);
			map.put("entity", device);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/updateDeviceForMcu",method=RequestMethod.POST)
	@ResponseBody
	public String updateDeviceForMcu(String deviceUuid,String mcuUuid,String moduleNum,String pointNum){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			deviceService.updateDeviceForMcu(deviceUuid,mcuUuid,moduleNum,pointNum);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/deleteDevice",method=RequestMethod.POST)
	@ResponseBody
	public String deleteDevice(String deviceUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			Device device=new Device();
			device.setDeviceUuid(deviceUuid);
			deviceService.deleteEntity(device);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/deleteDeviceForMcu",method=RequestMethod.POST)
	@ResponseBody
	public String deleteDeviceForMcu(String deviceUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			deviceService.deleteDeviceForMcu(deviceUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	@RequestMapping(value="/getUsingDeviceByOrgan")
	@ResponseBody
	public String getUsingDeviceByOrgan(String organUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			List<Device> devices=deviceService.getUsingDeviceByOrganUuid(organUuid);
			map.put("result", 0);
			map.put("devices", devices);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
	
	@RequestMapping("/importImage")
	public void importImage(
			String currentOrgan,
			MultipartFile file,//上传的图片内容
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException{
		response.setCharacterEncoding("UTF-8");
//		String uploadImgPath="";
		String uploadImagePath = "C:/Program Files/Apache Software Foundation/qualifiedFileImage/"+currentOrgan+"/";
		String fileNameAndType=file.getOriginalFilename();   
		String fileName=fileNameAndType.substring(0,fileNameAndType.lastIndexOf("."));     //原始图片名
			fileName=fileName+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());	//图片名+时分秒
		String imageTypeName=fileNameAndType.substring(fileNameAndType.lastIndexOf(".")+1);//图片后缀名
		//如果目录不存在，则创建该目录
		File imgDir = new File(uploadImagePath);
		if (!imgDir.exists()){
			imgDir.mkdirs();
		}
		//上传的图片格式不对，返回-1
		if(!(imageTypeName.equals("jpg")||imageTypeName.equals("jpeg")||imageTypeName.equals("png")||
				imageTypeName.equals("JPG")||imageTypeName.equals("JPEG")||imageTypeName.equals("PNG"))){
			response.getWriter().write("-1");
			return;
		}
		//得到最后图片保存的完整路径
		//原始上传图片路径
		String uploadImagePath_original=imgDir.getAbsolutePath()+"\\"+fileName+"."+imageTypeName;
//		System.out.println(uploadImagePath_original);
		//保存大图片
		FileOutputStream fos = new FileOutputStream(uploadImagePath_original);
		FileInputStream fis = (FileInputStream) file.getInputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fis.read(buffer)) > 0)
		{
			fos.write(buffer, 0, len);
		}
		fos.close();
		fis.close();
		//缩略图
		String minPath="C:/Program Files/Apache Software Foundation/qualifiedFileImage/"+currentOrgan+"/";
		File imgDir_min = new File(minPath);
		if (!imgDir_min.exists())
		{
			imgDir_min.mkdirs();
		}
		String uploadImagePath_min=imgDir_min.getAbsolutePath()+"\\min"+fileName+"."+imageTypeName;
//		System.out.println(imgDir.getAbsolutePath());
		//读取原始图片
		BufferedImage srcBufferImage = ImageIO.read(file.getInputStream());
		//原始图片的宽度和高度
		int yw = srcBufferImage.getWidth();
		int yh = srcBufferImage.getHeight();
		//缩放之后的图片
		BufferedImage scaledImage;
		ScaleImage scaleImage = ScaleImage.getInstance();
		//缩略图的尺寸，保持比例
		int w = 200, h = 200;
		//如果上传的图片的尺寸小于缩略图尺寸，则不缩放图片
		if (yw<w && yh < h)
		{
			//保存缩略图
			fos=new FileOutputStream(uploadImagePath_min);
			fis=(FileInputStream) file.getInputStream();
			buffer = new byte[1024];
			len = 0;
			while ((len = fis.read(buffer)) > 0)
			{
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();
		}
		else
		{
			scaledImage = scaleImage.imageZoomOut(srcBufferImage, w, h);
			FileOutputStream out = new FileOutputStream(uploadImagePath_min);
			ImageIO.write(scaledImage, imageTypeName, out);
			out.close();
		}

		String address2=InetAddress.getLocalHost().toString();
		String[] strarray=address2.split("/");
		//原图路径
//		address3=http://172.16.20.201:8080/uploadImage/10/projectImage.png
		String address3="http://"+strarray[1].toString()+":"+request.getServerPort()+"/qualifiedFileImage/"+currentOrgan+"/"+fileName+"."+imageTypeName;
		//缩略图的路径
		String address4="http://"+strarray[1].toString()+":"+request.getServerPort()+"/qualifiedFileImage/"+currentOrgan+"/min"+fileName+"."+imageTypeName;
		String adress5=address3+"#"+address4;
//		System.out.println(address4);
//		String[] adress=new String[]{address3,address4};
		response.getWriter().write(adress5);
	}
	
	/**
	 * 自动化设备管理
	 * @date  2017年5月6日 下午2:44:28
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月6日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/autoDevice")
	public String toautoDevice(){
		return "organ/view/autoDevice";
	}
	
	@RequestMapping("/getUsingMcuMudleAndPointNumByMcu")
	@ResponseBody
	public String getUsingMcuMudleAndPointNumByMcu(String mcuUuid){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Device> list = deviceService.getUsingMcuMudleAndPointNumByMcu(mcuUuid);
		map.put("rows", list);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getUsingPointNumByMcuAndModuleNum")
	@ResponseBody
	public String getUsingPointNumByMcuAndModuleNum(String mcuUuid,int moduleNum){
		Map<String, Object> map=new HashMap<String,Object>();
		List<Device> list = deviceService.getUsingPointNumByMcuAndModuleNum(mcuUuid,moduleNum);
		map.put("rows", list);
		return JsonUtil.beanToJson(map);
	}
	
}
















