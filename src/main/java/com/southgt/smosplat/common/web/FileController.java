package com.southgt.smosplat.common.web;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.ScaleImage;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.organ.service.IOrganService;
import com.southgt.smosplat.organ.service.IWorkerService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SitePic;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISitePicService;

/**
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年2月22日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class FileController {
	
	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;
	
	@Resource
	IOrganService organService;
	
	@Resource
	IWorkerService workerService;
	
	@Resource
	IDeviceService deviceService;
	
	@Resource
	IProjectService projectService;
	
	@Resource
	ISitePicService sitePicService;
	
	@RequestMapping("/uploadOrganFiles")
	@ResponseBody
	public String uploadOrganFiles(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		//得到机构id
		String organUuid=request.getParameter("organUuid");
		//得到类型（0：营业执照，1：资质证书，2：生产许可，3：其他文件，4：计量认证证书）
		String type=request.getParameter("type");
		if(type.equals("0")){
			type="yingyezhizhao";
		}else if(type.equals("1")){
			type="zizhizhengshu";
		}else if(type.equals("2")){
			type="shengchanxuke";
		}else if(type.equals("3")){
			type="other";
		}else if(type.equals("4")){
			type="jiliangrenzhengzhengshu";
		}
		//解析器解析request的上下文
		try {
			CommonsMultipartResolver MultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
			//判断request中是否包含multipart的数据
			if(MultipartResolver.isMultipart(request)){
				//将request转换成可以处理multipart类型的request
				MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
				Iterator<String> it=multipartRequest.getFileNames();
				while (it.hasNext()) {
					String paramName = (String) it.next();
					MultipartFile file=multipartRequest.getFile(paramName);
					if(file!=null){
						String fileName=file.getOriginalFilename();
						//如果没有路径，建立路径
						File directory=new File(uploadFileSrc+"/"+organUuid+"/organ/"+type);
						if(!(directory.exists())){
							directory.mkdirs();
						}
						File directory1=new File(uploadFileSrc+"/"+organUuid+"/organ/"+type+"/thumbnail");
						if(!(directory1.exists())){
							directory1.mkdirs();
						}
						String path=directory+"/"+fileName;
	        	    	String path1=directory1+"/"+fileName;//缩略图路径
						//其他文件有很多个，直接放进去即可
						if(type.equals("other")){
							//如果已经有同名文件，删除掉
							if(new File(path).exists()){
								new File(path).delete();
							}
							if(new File(path1).exists()){
								new File(path1).delete();
							}
						}else{//将原来的删除
							File[] files=directory.listFiles();
							for (File tempFile : files) {
								if(!tempFile.isDirectory()){
									tempFile.delete();
								}
							}
							File[] files1=directory1.listFiles();
							for (File tempFile1 : files1) {
								tempFile1.delete();
							}
						}
						File localFile=new File(path);
						//写入到本地
						file.transferTo(localFile);
						/*生成缩略图*/
						//其他文件不生成缩略图
						if(fileName.contains(".png")||fileName.contains(".jpg")||fileName.contains(".jpeg")){
	        	            try {
	        	            	FileInputStream srcFis=new FileInputStream(localFile);
	        	            	BufferedImage srcBufferImage = ImageIO.read(srcFis);  
	        	            	srcFis.close();
	        	                BufferedImage scaledImage;  
	        	                ScaleImage scaleImage = ScaleImage.getInstance();  
	        	                int yw = srcBufferImage.getWidth();  
	        	                int yh = srcBufferImage.getHeight();  
	        	                int w = 120, h = 120;  
	        	                // 如果上传图片 宽高 比 压缩的要小 则不压缩  直接生成
	        	                if (w > yw && h > yh)  
	        	                {  
	        	                    FileOutputStream fos = new FileOutputStream(path1);  
	        	                    FileInputStream fis = new FileInputStream(localFile);  
	        	                    byte[] buffer = new byte[1024];  
	        	                    int len = 0;  
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
	        	                    FileOutputStream out = new FileOutputStream(path1);  
	        	                    ImageIO.write(scaledImage, path1.substring(path1.lastIndexOf(".")+1,path1.length()), out);
	        	                    out.close();
	        	                }
	        	                //缩略图路径（pdf没有缩略图路径）
	        	        		map.put("path1", "/smosplatUploadFiles"+path1.split("smosplatUploadFiles")[1]);
	        	            } catch (IOException e) {  
	        	            	map.put("result", -1);
	        	    			map.put("msg", "后台上传出错！请联系管理员！");
	        	            } 
	                    }
	                    map.put("result", 0);
    	                //原始文件路径
    	                map.put("path", "/smosplatUploadFiles"+path.split("smosplatUploadFiles")[1]);
					}
				}
			}
		} catch (IllegalStateException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		} catch (IOException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getOrganFileUrls")
	@ResponseBody
	public String getOrganFileUrls(String organUuid,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/yingyezhizhao");
		File directory11=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/yingyezhizhao"+"/thumbnail");
		//原始图片路径
		if(directory1.exists()){
			File[] files1=directory1.listFiles();
			for (File file : files1) {
				if(!file.isDirectory()){
					map.put("yingyezhizhao", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		//缩略图路径
		if(directory11.exists()){
			File[] files11=directory11.listFiles();
			for (File file : files11) {
				if(!file.isDirectory()){
					map.put("yingyezhizhao1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		File directory2=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/zizhizhengshu");
		File directory22=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/zizhizhengshu"+"/thumbnail");
		if(directory2.exists()){
			File[] files2=directory2.listFiles();
			for (File file : files2) {
				if(!file.isDirectory()){
					map.put("zizhizhengshu", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		if(directory22.exists()){
			File[] files22=directory22.listFiles();
			for (File file : files22) {
				if(!file.isDirectory()){
					map.put("zizhizhengshu1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		File directory3=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/shengchanxuke");
		File directory33=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/shengchanxuke"+"/thumbnail");
		if(directory3.exists()){
			File[] files3=directory3.listFiles();
			for (File file : files3) {
				if(!file.isDirectory()){
					map.put("shengchanxuke", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			} 
		}
		if(directory33.exists()){
			File[] files33=directory33.listFiles();
			for (File file : files33) {
				if(!file.isDirectory()){
					map.put("shengchanxuke1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		//计量资格证书
		File directory4=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/jiliangrenzhengzhengshu");
		File directory44=new File(uploadFileSrc+"/"+organUuid+"/organ"+"/jiliangrenzhengzhengshu"+"/thumbnail");
		if(directory4.exists()){
			File[] files4=directory4.listFiles();
			for (File file : files4) {
				if(!file.isDirectory()){
					map.put("jiliangrenzhengzhengshu", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			} 
		}
		if(directory44.exists()){
			File[] files44=directory44.listFiles();
			for (File file : files44) {
				if(!file.isDirectory()){
					map.put("jiliangrenzhengzhengshu1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		return JsonUtil.beanToJson(map);
	}
	
	
	@RequestMapping("/uploadProjectFiles")
	@ResponseBody
	public String uploadProjectFiles(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		//得到机构id
//		String organUuid=request.getParameter("organUuid");
		//得到项目id
		String projectUuid=request.getParameter("projectUuid");
		Project project = projectService.getEntity(projectUuid);
		String organUuid = project.getOrgan().getOrganUuid();
		//得到类型（0：工程底图，1：监测方案审批表，2：基坑监测方案，3：其他文件,4:现场图片）
		String type=request.getParameter("type");
		if(type.equals("0")){
			type="projectPic";
		}else if(type.equals("1")){
			type="monitorPlanForm";
		}else if(type.equals("2")){
			type="monitoringPlan";
		}else if(type.equals("3")){
			type="other";
		}else if(type.equals("4")){
			type="sitePic";
		}
		//解析器解析request的上下文
		try {
			CommonsMultipartResolver MultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
			//判断request中是否包含multipart的数据
			if(MultipartResolver.isMultipart(request)){
				//将request转换成可以处理multipart类型的request
				MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
				Iterator<String> it=multipartRequest.getFileNames();
				while (it.hasNext()) {
					String paramName = (String) it.next();
					MultipartFile file=multipartRequest.getFile(paramName);
					if(file!=null){
						String fileName=file.getOriginalFilename();
						//如果没有路径，建立路径
						File directory=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/"+type);
						if(!(directory.exists())){
							directory.mkdirs();
						}
						File directory1=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/"+type+"/thumbnail");
						if(!(directory1.exists())){
							directory1.mkdirs();
						}
						String path=directory+"/"+fileName;
	        	    	String path1=directory1+"/"+fileName;//缩略图路径
	        	    	//其他文件/工程平面图有很多个，直接放进去即可
						if(type.equals("other") || type.equals("projectPic") || type.equals("sitePic")){
							//如果已经有同名文件，删除掉
							if(new File(path).exists()){
								new File(path).delete();
							}
							if(new File(path1).exists()){
								new File(path1).delete();
							}
						}else{
							//删除原来的文件
							File[] files=directory.listFiles();
							for (File tempFile : files) {
								if(!tempFile.isDirectory()){
									tempFile.delete();
								}
							}
							File[] files1=directory1.listFiles();
							for (File tempFile1 : files1) {
								tempFile1.delete();
							}
						}
						File localFile=new File(path);
						//写入到本地
						file.transferTo(localFile);
						/*生成缩略图*/
						//其他文件不生成缩略图
						if(fileName.contains(".png")||fileName.contains(".jpg")||fileName.contains(".jpeg")){
	        	            try {
	        	            	FileInputStream srcFis=new FileInputStream(localFile);
	        	            	BufferedImage srcBufferImage = ImageIO.read(srcFis);  
	        	            	srcFis.close();
	        	                BufferedImage scaledImage;  
	        	                ScaleImage scaleImage = ScaleImage.getInstance();  
	        	                int yw = srcBufferImage.getWidth();  
	        	                int yh = srcBufferImage.getHeight();  
	        	                int w = 120, h = 120;  
	        	                // 如果上传图片 宽高 比 压缩的要小 则不压缩  直接生成
	        	                if (w > yw && h > yh)  
	        	                {  
	        	                    FileOutputStream fos = new FileOutputStream(path1);  
	        	                    FileInputStream fis = new FileInputStream(localFile);  
	        	                    byte[] buffer = new byte[1024];  
	        	                    int len = 0;  
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
	        	                    FileOutputStream out = new FileOutputStream(path1);  
	        	                    ImageIO.write(scaledImage, path1.substring(path1.lastIndexOf(".")+1,path1.length()), out);
	        	                    out.close();
	        	                }
	        	                //缩略图路径（pdf没有缩略图路径）
	        	                path1 = path1.replace('\\', '/');
	        	        		map.put("path1", "/smosplatUploadFiles"+path1.split("smosplatUploadFiles")[1]);
	        	            } catch (IOException e) {  
	        	            	map.put("result", -1);
	        	    			map.put("msg", "后台上传出错！请联系管理员！");
	        	    			return JsonUtil.beanToJson(map);
	        	            } 
	                    }
						
						if(type.equals("sitePic")){
							String picName = request.getParameter("picName");
//							String name = picName.substring(0, picName.lastIndexOf('.'));
							String description = request.getParameter("description");
							SitePic sitePic = new SitePic();
							sitePic.setPicName(picName);
							sitePic.setProject(project);
							sitePic.setDescription(description);
							sitePicService.saveEntity(sitePic);
						}
						
	                    map.put("result", 0);
	                    path = path.replace('\\', '/');
    	                //原始文件路径
    	                map.put("path", "/smosplatUploadFiles"+path.split("smosplatUploadFiles")[1]);
					}
				}
			}
		} catch (IllegalStateException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		} catch (IOException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		}
		return JsonUtil.beanToJson(map);
	}
	
	
	
	
	@RequestMapping("/deletProjectFiles")
	@ResponseBody
	public String deletProjectFiles(String projectUuid,String type,HttpServletRequest request) {
		String organUuid  = projectService.getEntity(projectUuid).getOrgan().getOrganUuid();
		File directory=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/"+type);
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/"+type+"/thumbnail");
		//删除缩略图文件
		if(directory1.exists()){
			File[] files1=directory1.listFiles();
			for (File file : files1) {
				file.delete();
			}
		}
		if(directory.exists()){
			//删除文件
			File[] files=directory.listFiles();
			for (File file : files) {
				file.delete();
			}
			directory.delete();
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getProjectFileUrls")
	@ResponseBody
	public String getProjectFileUrls(String projectUuid,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		String organUuid  = projectService.getEntity(projectUuid).getOrgan().getOrganUuid();
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/projectPic");
		File directory11=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/projectPic"+"/thumbnail");
		//原始图片路径
		if(directory1.exists()){
			File[] files1=directory1.listFiles();
			for (File file : files1) {
				if(!file.isDirectory()){
					map.put("projectPic", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		//缩略图路径
		if(directory11.exists()){
			File[] files11=directory11.listFiles();
			for (File file : files11) {
				if(!file.isDirectory()){
					map.put("projectPic1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		File directory2=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/monitorPlanForm");
		File directory22=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/monitorPlanForm"+"/thumbnail");
		//原始图片路径
		if(directory2.exists()){
			File[] files2=directory2.listFiles();
			for (File file : files2) {
				if(!file.isDirectory()){
					map.put("monitorPlanForm", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		//缩略图路径
		if(directory22.exists()){
			File[] files22=directory22.listFiles();
			for (File file : files22) {
				if(!file.isDirectory()){
					map.put("monitorPlanForm1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		File directory3=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/monitoringPlan");
		File directory33=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/monitoringPlan"+"/thumbnail");
		//原始图片路径
		if(directory3.exists()){
			File[] files3=directory3.listFiles();
			for (File file : files3) {
				if(!file.isDirectory()){
					map.put("monitoringPlan", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		//缩略图路径
		if(directory33.exists()){
			File[] files33=directory33.listFiles();
			for (File file : files33) {
				if(!file.isDirectory()){
					map.put("monitoringPlan1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getProjectPicUrls")
	@ResponseBody
	public String getProjectPicUrls(String projectUuid,HttpServletRequest request,HttpServletResponse response){
		String organUuid  = projectService.getEntity(projectUuid).getOrgan().getOrganUuid();
		Map<String, Object> map=new HashMap<String, Object>();
		String projectPicPath=uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/projectPic";
		String projectPicPath1=uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/projectPic"+"/thumbnail";
		File directory1=new File(projectPicPath);
		File directory11=new File(projectPicPath1);
		List<String> projectPicFileUrls=new ArrayList<String>();
		List<String> projectPicFileUrls1=new ArrayList<String>();
		File[] files1=null;
		File[] files11=null;
		if(!directory1.exists()){
			new File(projectPicPath).mkdirs();
			directory1=new File(projectPicPath);
		}
		if(!directory11.exists()){
			new File(projectPicPath1).mkdirs();
			directory11=new File(projectPicPath1);
		}
		files1=directory1.listFiles();
		files11=directory11.listFiles();
		if(files1!=null){
			for (File file : files1) {
				if(file.isDirectory()){
					continue;
				}
				String name=file.getName();
				boolean finded=false;
				if(files11!=null){
					for (File file1 : files11) {
						if(file1.isDirectory()){
							continue;
						}
						String name1=file1.getName();
						if(name.equals(name1)){
							finded=true;
							projectPicFileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
							projectPicFileUrls1.add("/smosplatUploadFiles"+file1.getAbsolutePath().split("smosplatUploadFiles")[1]);
						}
					}
					if(!finded){
						projectPicFileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
						projectPicFileUrls1.add("noThumbnail");
					}
				}
			}
		}
		
		String sitePicPath=uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/sitePic";
		String sitePicPath1=uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/sitePic"+"/thumbnail";
		File directory2=new File(sitePicPath);
		File directory22=new File(sitePicPath1);
		List<String> sitePicFileUrls=new ArrayList<String>();
		List<String> sitePicFileUrls1=new ArrayList<String>();
		File[] files2=null;
		File[] files22=null;
		if(!directory2.exists()){
			new File(sitePicPath).mkdirs();
			directory2=new File(sitePicPath);
		}
		if(!directory22.exists()){
			new File(sitePicPath1).mkdirs();
			directory22=new File(sitePicPath1);
		}
		files2=directory2.listFiles();
		files22=directory22.listFiles();
		if(files2!=null){
			for (File file : files2) {
				if(file.isDirectory()){
					continue;
				}
				String name=file.getName();
				boolean finded=false;
				if(files2!=null){
					for (File file22 : files22) {
						if(file22.isDirectory()){
							continue;
						}
						String name1=file22.getName();
						if(name.equals(name1)){
							finded=true;
							sitePicFileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
							sitePicFileUrls1.add("/smosplatUploadFiles"+file22.getAbsolutePath().split("smosplatUploadFiles")[1]);
						}
					}
					if(!finded){
						sitePicFileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
						sitePicFileUrls1.add("noThumbnail");
					}
				}
			}
		}
		for(int i = 0; i < projectPicFileUrls.size(); i++){
			String t = projectPicFileUrls.get(i).replace('\\', '/');
			projectPicFileUrls.set(i, t);
			String tt = projectPicFileUrls1.get(i).replace('\\', '/');
			projectPicFileUrls1.set(i, tt);
		}
		
		for(int i = 0; i < sitePicFileUrls.size(); i++){
			String t = sitePicFileUrls.get(i).replace('\\', '/');
			sitePicFileUrls.set(i, t);
			String tt = sitePicFileUrls1.get(i).replace('\\', '/');
			sitePicFileUrls1.set(i, tt);
		}
		//排序
		sitePicFileUrls.sort(new Comparator<String>() {
			@Override
			public int compare(String sp1, String sp2) {
				if (sp1.compareTo(sp2) < 0) {
					return -1;
				} else if (sp1.equals(sp2)) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		map.put("projectPic", projectPicFileUrls);
		map.put("projectPic1", projectPicFileUrls1);
		map.put("sitePic", sitePicFileUrls);
		map.put("sitePic1", sitePicFileUrls1);
		return JsonUtil.beanToJson(map);
	}
	
	
	@RequestMapping("/getProjectOtherFileUrls")
	@ResponseBody
	public String getProjectOtherFileUrls(String projectUuid,HttpServletRequest request,HttpServletResponse response){
		String organUuid  = projectService.getEntity(projectUuid).getOrgan().getOrganUuid();
		Map<String, Object> map=new HashMap<String, Object>();
		String path=uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/other";
		String path1=uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/other"+"/thumbnail";
		File directory=new File(path);
		File directory1=new File(path1);
		List<String> fileUrls=new ArrayList<String>();
		List<String> fileUrls1=new ArrayList<String>();
		File[] files=null;
		File[] files1=null;
		if(!directory.exists()){
			new File(path).mkdirs();
			directory=new File(path);
		}
		if(!directory1.exists()){
			new File(path1).mkdirs();
			directory1=new File(path1);
		}
		files=directory.listFiles();
		files1=directory1.listFiles();
		if(files!=null){
			for (File file : files) {
				if(file.isDirectory()){
					continue;
				}
				String name=file.getName();
				boolean finded=false;
				if(files1!=null){
					for (File file1 : files1) {
						if(file1.isDirectory()){
							continue;
						}
						String name1=file1.getName();
						if(name.equals(name1)){
							finded=true;
							fileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
							fileUrls1.add("/smosplatUploadFiles"+file1.getAbsolutePath().split("smosplatUploadFiles")[1]);
						}
					}
					if(!finded){
						fileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
						fileUrls1.add("noThumbnail");
					}
				}
			}
		}
		map.put("other", fileUrls);
		map.put("other1", fileUrls1);
		return JsonUtil.beanToJson(map);
	}
	   
	@RequestMapping("/deleteProjectOtherFiles")
	@ResponseBody
	public String deleteProjectOtherFiles(String projectUuid,String src,HttpServletRequest request) {
		String organUuid  = projectService.getEntity(projectUuid).getOrgan().getOrganUuid();
		if(src.indexOf(organUuid)!=-1){
			File file=new File(uploadFileSrc+"/"+organUuid+src.split(organUuid)[1]);
			//删除文件
			if(!file.isDirectory()){
				file.delete();
			}
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getOrganOtherFileUrls")
	@ResponseBody
	public String getOrganOtherFileUrls(String organUuid,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		String path=uploadFileSrc+"/"+organUuid+"/organ"+"/other";
		String path1=uploadFileSrc+"/"+organUuid+"/organ"+"/other"+"/thumbnail";
		File directory=new File(path);
		File directory1=new File(path1);
		List<String> fileUrls=new ArrayList<String>();
		List<String> fileUrls1=new ArrayList<String>();
		File[] files=null;
		File[] files1=null;
		if(!directory.exists()){
			new File(path).mkdirs();
			directory=new File(path);
		}
		if(!directory1.exists()){
			new File(path1).mkdirs();
			directory1=new File(path1);
		}
		files=directory.listFiles();
		files1=directory1.listFiles();
		for (File file : files) {
			if(file.isDirectory()){
				continue;
			}
			String name=file.getName();
			boolean finded=false;
			for (File file1 : files1) {
				if(file1.isDirectory()){
					continue;
				}
				String name1=file1.getName();
				if(name.equals(name1)){
					finded=true;
					fileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
					fileUrls1.add("/smosplatUploadFiles"+file1.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
			if(!finded){
				fileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				fileUrls1.add("noThumbnail");
			}
		}
		map.put("other", fileUrls);
		map.put("other1", fileUrls1);
		return JsonUtil.beanToJson(map);
	}
	    
	@RequestMapping("/deleteOrganFiles")
	@ResponseBody
	public String deleteOrganFiles(String organUuid,String type,HttpServletRequest request) {
		File directory=new File(uploadFileSrc+"/"+organUuid+"/organ/"+type);
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/organ/"+type+"/thumbnail");
		//删除缩略图文件
		if(directory1.exists()){
			File[] files1=directory1.listFiles();
			for (File file : files1) {
				file.delete();
			}
		}
		if(directory.exists()){
			//删除文件
			File[] files=directory.listFiles();
			for (File file : files) {
				file.delete();
			}
			directory.delete();
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/deleteOrganOtherFiles")
	@ResponseBody
	public String deleteOrganOtherFiles(String organUuid,String src,String src1,HttpServletRequest request) {
		if(src.indexOf(organUuid)!=-1){
			File file=new File(uploadFileSrc+"/"+organUuid+src.split(organUuid)[1]);
			//删除文件
			if(!file.isDirectory()){
				file.delete();
			}
		}
		//删除缩略图
		if(src1.indexOf(organUuid)!=-1){
			File file1=new File(uploadFileSrc+"/"+organUuid+src1.split(organUuid)[1]);
			if(!file1.isDirectory()){
				file1.delete();
			}
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/uploadWorkerFiles")
	@ResponseBody
	public String uploadWorkerFiles(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		//得到机构id
		String organUuid=request.getParameter("organUuid");
		//得到人员id
		String workerUuid=request.getParameter("workerUuid");
		//得到类型（0：头像，1：上岗证书）
		String type=request.getParameter("type");
		if(type.equals("0")){
			type="/workerPortrait";
		}else if(type.equals("1")){
			type="/qulification";
		}
		//解析器解析request的上下文
		try {
			CommonsMultipartResolver MultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
			//判断request中是否包含multipart的数据
			if(MultipartResolver.isMultipart(request)){
				//将request转换成可以处理multipart类型的request
				MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
				Iterator<String> it=multipartRequest.getFileNames();
				while (it.hasNext()) {
					String paramName = (String) it.next();
					MultipartFile file=multipartRequest.getFile(paramName);
					if(file!=null){
						String fileName=file.getOriginalFilename();
						//如果没有路径，建立路径
						File directory=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+type);
						if(!(directory.exists())){
							directory.mkdirs();
						}
						File directory1=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+type+"/thumbnail");
						if(!(directory1.exists())){
							directory1.mkdirs();
						}
						String path=directory+"/"+fileName;
	        	    	String path1=directory1+"/"+fileName;//缩略图路径
						//删除原来的文件
						File[] files=directory.listFiles();
						for (File tempFile : files) {
							if(!tempFile.isDirectory()){
								tempFile.delete();
							}
						}
						File[] files1=directory1.listFiles();
						for (File tempFile1 : files1) {
							tempFile1.delete();
						}
						File localFile=new File(path);
						//写入到本地
						file.transferTo(localFile);
						/*生成缩略图*/
						//其他文件不生成缩略图
						if(fileName.contains(".png")||fileName.contains(".jpg")||fileName.contains(".jpeg")||fileName.contains(".pdf")){
	        	            try {
	        	            	FileInputStream srcFis=new FileInputStream(localFile);
	        	            	BufferedImage srcBufferImage = ImageIO.read(srcFis);  
	        	            	srcFis.close();
	        	                BufferedImage scaledImage;  
	        	                ScaleImage scaleImage = ScaleImage.getInstance();  
	        	                int yw = srcBufferImage.getWidth();  
	        	                int yh = srcBufferImage.getHeight();  
	        	                int w = 120, h = 120;  
	        	                // 如果上传图片 宽高 比 压缩的要小 则不压缩  直接生成
	        	                if (w > yw && h > yh)  
	        	                {  
	        	                    FileOutputStream fos = new FileOutputStream(path1);  
	        	                    FileInputStream fis = new FileInputStream(localFile);  
	        	                    byte[] buffer = new byte[1024];  
	        	                    int len = 0;  
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
	        	                    FileOutputStream out = new FileOutputStream(path1);  
	        	                    ImageIO.write(scaledImage, path1.substring(path1.lastIndexOf(".")+1,path1.length()), out);
	        	                    out.close();
	        	                }
	        	                //缩略图路径（pdf没有缩略图路径）
	        	        		map.put("path1", "/smosplatUploadFiles"+path1.split("smosplatUploadFiles")[1]);
	        	            } catch (IOException e) {  
	        	            	map.put("result", -1);
	        	    			map.put("msg", "后台上传出错！请联系管理员！");
	        	            } 
	                    }
	                    map.put("result", 0);
    	                //原始文件路径
    	                map.put("path", "/smosplatUploadFiles"+path.split("smosplatUploadFiles")[1]);
					}
				}
			}
		} catch (IllegalStateException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		} catch (IOException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getWorkerFileUrls")
	@ResponseBody
	public String getWorkerFileUrls(String workerUuid,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		String organUuid = workerService.getEntity(workerUuid).getOrgan().getOrganUuid();
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+"/workerPortrait");
		File directory11=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+"/workerPortrait"+"/thumbnail");
		//原始图片路径
		if(directory1.exists()){
			File[] files1=directory1.listFiles();
			for (File file : files1) {
				if(!file.isDirectory()){
					map.put("workerPortrait", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		//上岗证书
		File directory2=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+"/qulification");
		File directory22=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+"/qulification"+"/thumbnail");
		//原始图片路径
		if(directory2.exists()){
			File[] files2=directory2.listFiles();
			for (File file : files2) {
				if(!file.isDirectory()){
					map.put("paperID", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		//缩略图路径
		if(directory11.exists()){
			File[] files11=directory11.listFiles();
			for (File file : files11) {
				if(!file.isDirectory()){
					map.put("workerPortrait1", "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
		}
		return JsonUtil.beanToJson(map);
	}
	@RequestMapping("/deleteWorkerFiles")
	@ResponseBody
	public String deleteWorkerFiles(String organUuid,String workerUuid,HttpServletRequest request) {
		File directory=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+"/workerPortrait");
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/worker/"+workerUuid+"/workerPortrait"+"/thumbnail");
		//删除缩略图文件
		if(directory1.exists()){
			File[] files1=directory1.listFiles();
			for (File file : files1) {
				file.delete();
			}
		}
		if(directory.exists()){
			//删除文件
			File[] files=directory.listFiles();
			for (File file : files) {
				file.delete();
			}
			directory.delete();
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/uploadDeviceFiles")
	@ResponseBody
	public String uploadDeviceFiles(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		//得到机构id
		String organUuid=request.getParameter("organUuid");
		//得到设备id
		String deviceUuid=request.getParameter("deviceUuid");
		//解析器解析request的上下文
		try {
			CommonsMultipartResolver MultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
			//判断request中是否包含multipart的数据
			if(MultipartResolver.isMultipart(request)){
				//将request转换成可以处理multipart类型的request
				MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
				Iterator<String> it=multipartRequest.getFileNames();
				while (it.hasNext()) {
					String paramName = (String) it.next();
					MultipartFile file=multipartRequest.getFile(paramName);
					if(file!=null){
						String fileName=file.getOriginalFilename();
						//如果没有路径，建立路径
						File directory=new File(uploadFileSrc+"/"+organUuid+"/device/"+deviceUuid);
						if(!(directory.exists())){
							directory.mkdirs();
						}
						File directory1=new File(uploadFileSrc+"/"+organUuid+"/device/"+deviceUuid+"/thumbnail");
						if(!(directory1.exists())){
							directory1.mkdirs();
						}
						String path=directory+"/"+fileName;
	        	    	String path1=directory1+"/"+fileName;//缩略图路径
						//删除原来的文件
						File[] files=directory.listFiles();
						for (File tempFile : files) {
							if(!tempFile.isDirectory()){
								tempFile.delete();
							}
						}
						File[] files1=directory1.listFiles();
						for (File tempFile1 : files1) {
							tempFile1.delete();
						}
						File localFile=new File(path);
						//写入到本地
						file.transferTo(localFile);
						/*生成缩略图*/
						//其他文件不生成缩略图
						if(fileName.contains(".png")||fileName.contains(".jpg")||fileName.contains(".jpeg")){
	        	            try {
	        	            	FileInputStream srcFis=new FileInputStream(localFile);
	        	            	BufferedImage srcBufferImage = ImageIO.read(srcFis);  
	        	            	srcFis.close();
	        	                BufferedImage scaledImage;  
	        	                ScaleImage scaleImage = ScaleImage.getInstance();  
	        	                int yw = srcBufferImage.getWidth();  
	        	                int yh = srcBufferImage.getHeight();  
	        	                int w = 120, h = 120;  
	        	                // 如果上传图片 宽高 比 压缩的要小 则不压缩  直接生成
	        	                if (w > yw && h > yh)  
	        	                {  
	        	                    FileOutputStream fos = new FileOutputStream(path1);  
	        	                    FileInputStream fis = new FileInputStream(localFile);  
	        	                    byte[] buffer = new byte[1024];  
	        	                    int len = 0;  
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
	        	                    FileOutputStream out = new FileOutputStream(path1);  
	        	                    ImageIO.write(scaledImage, path1.substring(path1.lastIndexOf(".")+1,path1.length()), out);
	        	                    out.close();
	        	                }
	        	                //缩略图路径（pdf没有缩略图路径）
	        	        		map.put("path1", "/smosplatUploadFiles"+path1.split("smosplatUploadFiles")[1]);
	        	            } catch (IOException e) {  
	        	            	map.put("result", -1);
	        	    			map.put("msg", "后台上传出错！请联系管理员！");
	        	            } 
	                    }
	                    map.put("result", 0);
    	                //原始文件路径
    	                map.put("path", "/smosplatUploadFiles"+path.split("smosplatUploadFiles")[1]);
					}
				}
			}
		} catch (IllegalStateException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		} catch (IOException e) {
			map.put("resule", -1);
			map.put("msg", "后台上传出错！请联系管理员！");
		}
		return JsonUtil.beanToJson(map);
	}
	@RequestMapping("/getDeviceFileUrls")
	@ResponseBody
	public String getDeviceFileUrls(String organUuid,String deviceUuid,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map=new HashMap<String, Object>();
		File directory=new File(uploadFileSrc+"/"+organUuid+"/device/"+deviceUuid);
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/device/"+deviceUuid+"/thumbnail");
		List<String> fileUrls=new ArrayList<String>();
		List<String> fileUrls1=new ArrayList<String>();
		File[] files=null;
		File[] files1=null;
		if(directory.exists()){
			files=directory.listFiles();
		}else{
			//制定目录不存在
			map.put("deviceUrl", fileUrls);
			map.put("deviceUrl1", fileUrls1);
			return JsonUtil.beanToJson(map);
		}
		if(directory1.exists()){
			files1=directory1.listFiles();
		}
		for (File file : files) {
			if(file.isDirectory()){
				continue;
			}
			String name=file.getName();
			boolean finded=false;
			for (File file1 : files1) {
				if(file1.isDirectory()){
					continue;
				}
				String name1=file1.getName();
				if(name.equals(name1)){
					finded=true;
					fileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
					fileUrls1.add("/smosplatUploadFiles"+file1.getAbsolutePath().split("smosplatUploadFiles")[1]);
				}
			}
			if(!finded){
				fileUrls.add("/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1]);
				fileUrls1.add("noThumbnail");
			}
		}
		map.put("deviceUrl", fileUrls);
		map.put("deviceUrl1", fileUrls1);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/deleteDeviceFiles")
	@ResponseBody
	public String deleteDeviceFiles(String organUuid,String deviceUuid,HttpServletRequest request) {
		File directory=new File(uploadFileSrc+"/"+organUuid+"/device/"+deviceUuid);
		File directory1=new File(uploadFileSrc+"/"+organUuid+"/device/"+deviceUuid+"/thumbnail");
		//删除缩略图文件
		if(directory1.exists()){
			File[] files1=directory1.listFiles();
			for (File file : files1) {
				file.delete();
			}
		}
		if(directory.exists()){
			//删除文件
			File[] files=directory.listFiles();
			for (File file : files) {
				file.delete();
			}
			directory.delete();
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
	
	
	@RequestMapping("/getSourceFileUrlsByDateTime")
	@ResponseBody
	public String getSourceFileUrlsByDateTime(String projectUuid,String monitorItem,String sDate, String eDate,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, Object> map=new HashMap<String, Object>();
		Project project = projectService.getEntity(projectUuid);
		String organUuid = project.getOrgan().getOrganUuid();
		File directory=new File(uploadFileSrc+"/"+organUuid+"/project/"+projectUuid+"/SourceFile/"+monitorItem);
		List<String> fileUrls=new ArrayList<String>();
		File[] files=null;
		if(directory.exists()){
			files=directory.listFiles();
		}else{
			//指定目录不存在
			map.put("sourceFileUrls", fileUrls);
			return JsonUtil.beanToJson(map);
		}
		for (File file : files) {
			if(file.isDirectory()){
				continue;
			}
			//时间精确到天
			String fileTime = file.getName().substring(0,8);
			//比较时间，若这个文件在所请求的时间段之内，就返回路径。
			if(fileTime.compareTo(sDate) >= 0 && fileTime.compareTo(eDate) <= 0){
				String filePath = "/smosplatUploadFiles"+file.getAbsolutePath().split("smosplatUploadFiles")[1];
				fileUrls.add(filePath);
			}
		}
		for(int i = 0; i < fileUrls.size(); i++){
			String t = fileUrls.get(i).replace('\\','/');
			fileUrls.set(i, t);
		}
		//排序
		fileUrls.sort(new Comparator<String>() {
		@Override
		public int compare(String sp1, String sp2) {
			if (sp1.compareTo(sp2) < 0) {
				return -1;
			} else if (sp1.equals(sp2)) {
				return 0;
			} else {
				return 1;
			}
		}
	});
		map.put("sourceFileUrls", fileUrls);
		return JsonUtil.beanToJson(map);
	}
	
	

	@RequestMapping("/downloadSourceFile")
	public void download(String fileName,  String projectUuid,String monitorItemCode, HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("GBK");
		String organUuid  = projectService.getEntity(projectUuid).getOrgan().getOrganUuid();
		File directory = new File(uploadFileSrc + "/" + organUuid + "/project/" + projectUuid + "/"+"SourceFile"+"/"+monitorItemCode);
		response.setContentType("application/octet-stream;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName+"\"");
		try {
			File filename = new File(directory+"\\"+fileName); // 要读取以上路径的input。txt文件
		      InputStreamReader isr = new InputStreamReader(new FileInputStream(filename), "GBK");
		      BufferedReader br = new BufferedReader(isr);
		      String lineTxt = "";
		      String t ="" ;
			  while ((t = br.readLine()) != null) {
				  lineTxt += t; // 一次读入一行数据
			  }
		      br.close();
            byte[] dataByte = lineTxt.getBytes();
            OutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(dataByte);
            out.flush();
            out.close();
			// 这里主要关闭。
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
