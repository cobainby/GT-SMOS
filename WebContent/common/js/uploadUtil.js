//ajax上传
function importFileClick(ctrlName,formName,uploadType,fileType,params,callback){
    //获取上传文件控件内容
    var file=$("#"+ctrlName)[0].files[0];
    var url;
    //判断控件中是否存在文件内容，如果不存在，弹出提示信息，阻止进一步操作
//    if (file == null) { alert('错误，请选择文件'); return; }
    if (file != null) {
	    //获取文件名称
	    var fileName = file.name;
	    //获取文件类型名称
	    var file_typename = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
	    switch(fileType){
	    //图片类
	    case "img":
	    //这里限定上传文件文件类型必须为几种图片格式，如果文件类型不符，提示错误信息
	    if (file_typename == '.png'||file_typename=='.jpg'||file_typename=='.jpeg'||file_typename=='.pdf')
	    {
	    }
	    else {
	        alert("文件类型错误");
	        return;
	        //将错误信息显示在前端label文本中
	//        document.getElementById('fileName').innerHTML = "<span style='color:Red'>错误提示:上传文件应该是.xlsx后缀而不应该是" + file_typename + ",请重新选择文件</span>"
	    }
	    break;
	    //文档类
	    case "doc":
	    	if (file_typename == '.doc'||file_typename=='.docx'||file_typename=='.xls'||file_typename=='.xlsx'||file_typename=='.ppt'||file_typename=='.pptx'||file_typename=='.pdf')
	        {
	        }
	        else {
	            alert("文件类型错误");
	            return;
	            //将错误信息显示在前端label文本中
	//            document.getElementById('fileName').innerHTML = "<span style='color:Red'>错误提示:上传文件应该是.xlsx后缀而不应该是" + file_typename + ",请重新选择文件</span>"
	        }
	    	break;
	    	//所有文件类型
	    case "all":
	    	break;
	    }
	    //计算文件大小
	    var fileSize = 0;
	    //如果文件大小大于1024字节X1024字节，则显示文件大小单位为MB，否则为KB
	    if (file.size > 1024 * 1024) {
	
	　　fileSize = Math.round(file.size * 100 / (1024 * 1024)) / 100;
	
		　if (fileSize > 5) {
		            alert('错误，文件超过5MB，禁止上传！'); return;
		        }
		　fileSize = fileSize.toString() + 'MB';
	    }
	    else {
	        fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
	    }
	    //将文件名和文件大小显示在前端label文本中
	//    document.getElementById('fileName').innerHTML = "<span style='color:Blue'>文件名: " + file.name + ',大小: ' + fileSize + "</span>";
	    //获取form数据
	    var formData = new FormData($( "#"+formName )[0]);
	    formData.append("organUuid",params[0]);
	    if(uploadType == 4){
		    formData.append("workerUuid",params[1]);
		    url = "uploadWorkerFiles";
	    }else if(uploadType == 5){
	    	formData.append("deviceUuid",params[1]);
	    }else if(uploadType == 6){
	    	formData.append("projectUuid",params[1]);
	    	formData.append("type",params[2]);
	    	url = "uploadProjectFiles"
	    }
	    //调用上传方法的模块名
	    formData.append("type",uploadType);
	    //调用apicontroller后台action方法，将form数据传递给后台处理。contentType必须设置为false,否则chrome和firefox不兼容
	    
	    $.ajax({
	        url: url,
	        type: 'POST',
	        data:formData,
	        async: false,
	        cache: false,
	        contentType: false,
	        processData: false,
	        success: function (returnInfo) {
	            //上传成功后将控件内容清空，并显示上传成功信息
	        	var jsonStr=JSON.parse(returnInfo);
	        	var url=jsonStr.path;
//	        	$("#"+ctrlName)[0].value = null;
	        	callback(url);
	        },
	        error: function (returnInfo) {
	            //上传失败时显示上传失败信息
	//            document.getElementById('uploadInfo').innerHTML = "<span style='color:Red'>" + returnInfo + "</span>";
	        	alert(returnInfo.status);
	        }
	    });
    }else{
    	callback(null);
    }
}

	
	/*制作缩略图*/
	function resizeImage(obj, MaxW, MaxH){
	 var imageObject = obj;
	 var state = imageObject.readyState;
	 if(state!='complete') {
	  setTimeout("resizeImage("+imageObject+","+MaxW+","+MaxH+")",50);
	  return;
	 }
	 var oldImage = new Image();
	 oldImage.src = imageObject.src;
	 var dW = oldImage.width; 
	 var dH = oldImage.height;
	 if(dW>MaxW || dH>MaxH){
		  a = dW/MaxW; b = dH/MaxH;
		  if( b>a ) a = b;
		  dW = dW/a; dH = dH/a;
	 }
	 if(dW > 0 && dH > 0) {
	  imageObject.width = dW;
	  imageObject.height = dH;
	 }
}
	//获取当前网址，如： http://localhost:8080/Tmall/index.jsp
	var curWwwPath;
	//获取主机地址之后的目录如：/Tmall/index.jsp
	var pathName;
	var pos;
	//获取主机地址，如： http://localhost:8080
	var localhostPath;
	$(function(){
		//获取当前网址，如： http://localhost:8080/Tmall/index.jsp
		curWwwPath=window.document.location.href;
		//获取主机地址之后的目录如：/Tmall/index.jsp
		pathName=window.document.location.pathname;
		pos=curWwwPath.indexOf(pathName);
		//获取主机地址，如： http://localhost:8080
		localhostPath=curWwwPath.substring(0,pos);
	});
	  
	//图片上传预览    IE是用了滤镜。
	function previewImage(file,imgDIV,imgID)
	{
	  var MAXWIDTH  = 150; 
	  var MAXHEIGHT = 200;
	  //相片div
	  var div = $("#"+imgDIV)[0];
	  if (file.files && file.files[0])
	  {
	      div.innerHTML ='<img id='+"\""+imgID+"\""+' class="img-responsive img-rounded previewImage" alt="照片">';
	      //img控件
	      var img = $("#"+imgID)[0];
	      img.onload = function(){
	        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	        img.width  =  rect.width;
	        img.height =  rect.height;
	//         img.style.marginLeft = rect.left+'px';
	        img.style.marginTop = rect.top+'px';
	      }
	      var reader = new FileReader();
	      reader.onload = function(evt){img.src = evt.target.result;}
	      reader.readAsDataURL(file.files[0]);
	  }
	  else //兼容IE
	  {
	    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
	    file.select();
	    var src = document.selection.createRange().text;
	    div.innerHTML ='<img id='+"\""+imgID+"\""+' class="img-responsive img-rounded previewImage" alt="照片">';
	    var img = $("#"+imgID)[0];
	    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
	    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
	    div.innerHTML = "<div id="+"\""+imgDIV+"\""+" style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
	  }
	}
	function clacImgZoomParam( maxWidth, maxHeight, width, height ){
	    var param = {top:0, left:0, width:width, height:height};
	    if( width>maxWidth || height>maxHeight )
	    {
	        rateWidth = width / maxWidth;
	        rateHeight = height / maxHeight;
	         
	        if( rateWidth > rateHeight )
	        {
	            param.width =  maxWidth;
	            param.height = Math.round(height / rateWidth);
	        }else
	        {
	            param.width = Math.round(width / rateHeight);
	            param.height = maxHeight;
	        }
	    }
	     
	    param.left = Math.round((maxWidth - param.width) / 2);
	    param.top = Math.round((maxHeight - param.height) / 2);
	    return param;
	} 
