//机构id
var organUuid;
$(function(){
	//为按钮添加点击事件
	$("#toOrganDetail").bind('click',function(){
		$.post("getCurrentAccount",function(data,status){
			var account=JSON.parse(data).account;
			if(account.loginName=="superadmin"){
				window.location.href="/smosplat/organ";
			}else{
				window.location.href="/smosplat/organDetail?organUuid="+organUuid;
			}
		})
	});
	//从url里面获取organUuid参数
	organUuid=getUrlParam("organUuid");
	//获取信息
	$.post("getOrgan",{organUuid:organUuid},function(data,status){
		var jsonData=JSON.parse(data);
		var organ=jsonData.organ;
		var worker=jsonData.worker;
		var organName=jsonData.organ.organName;
		$("#organName").val(organName);
		$("#establishedTime").val(jsonData.organ.establishedTime);
		$("#adress").val(jsonData.organ.adress);
//		$("#telephone").val(jsonData.organ.telephone);
//		$("#postcode").val(jsonData.organ.postcode);
		$("#organCode").val(jsonData.organ.organCode);
		$("#detactNumber").val(jsonData.organ.detactNumber);
		//没有检测资质证书
		if(jsonData.organ.detactNumber == null){
			$("#viewDetactNumber").hide();
			$("#delDetactNumber").hide();
		}
		$("#meteringNumber").val(jsonData.organ.meteringNumber);
		if(jsonData.organ.meteringNumber == null){
			$("#viewMeteringNumber").hide();
			$("#delMeteringNumber").hide();
		}
		$("#representative").val(jsonData.organ.representative);
		$("#techDirector").val(jsonData.organ.techDirector);
//		$("#email").val(jsonData.organ.email);
		$("#loginName").val(jsonData.loginName);
		$("#accountName").val(jsonData.accountName);
		$("#workerName").val(jsonData.worker.workerName);
		$("#phone").val(jsonData.worker.phone);
//		$("#supervisorName").val(jsonData.supervisor.workerName);
//		$("#supervisorPhone").val(jsonData.supervisor.phone);
	});
	//点击保存
	$("#saveOrgan").bind("click",function(data,status){
		var organName = $("#organName").val();
		var loginName = $("#loginName").val();
		var accountName = $("#accountName").val();
		var workerName = $("#workerName").val();
		var phone = $("#phone").val();
//		var email = $("#email").val();
//		var supervisorName=$("#supervisorName").val();
//		var supervisorPhone=$("#supervisorPhone").val();
		var establishedTime = $("#establishedTime").val();
		var adress = $("#adress").val();
//		var telephone = $("#telephone").val();
//		var postcode = $("#postcode").val();
		var organCode = $("#organCode").val();
		var detactNumber = $("#detactNumber").val();
		var meteringNumber = $("#meteringNumber").val();
		var representative = $("#representative").val();
		var techDirector = $("#techDirector").val();
		if(loginName == undefined || loginName == ""){
			alert("登录名不可为空！");
			return;
		}
		if(accountName == undefined || accountName == ""){
			alert("昵称(账号名)不可为空！");
			return;
		}
		var params = {
			organUuid:organUuid,
			organName:organName,
            workerName: workerName,
            phone: phone,
//            email: email,
            loginName:loginName,
            accountName: accountName,
//            supervisorName:supervisorName,
//            supervisorPhone:supervisorPhone,
            establishedTime:establishedTime,
            adress:adress,
//            telephone:telephone,
//            postcode:postcode,
            organCode:organCode,
            detactNumber:detactNumber,
            meteringNumber:meteringNumber,
            representative:representative,
            techDirector:techDirector
        };
		$.post("/smosplat/updateOrgan",params,function(data,status){
			var result = JSON.parse(data);
			if(result.result == 0){
				swal({title:"保存成功！",type:"success"});
			}else{
				swal({title:"修改失败！",text:result.msg,type:"error"});
			}
		});
	});
	
	
	$("#viewMeteringNumber").bind("click",function(e){
		var src = $(this).attr("value");
		if(src!=""&&src!=undefined){
    		window.open(src, "", "toolbar=no,scrollbars=no,menubar=no");
    	}
	});
	$("#viewDetactNumber").bind("click",function(e){
		var src = $(this).attr("value");
    	if(src!=""&&src!=undefined){
    		window.open(src, "", "toolbar=no,scrollbars=no,menubar=no");
    	}
	});
	
	//初始化检测资质证书上传
	initFileInput("detactNumber_input");
	$("#detactNumber_input").fileinput('refresh', {
		uploadUrl: "uploadOrganFiles",
        uploadExtraData: {organUuid:organUuid,type:1}//1是资质证书。这里是检测资质证书。
    });
    $("#detactNumber_input").on("filebatchselected", function(event, files) {  
        $(this).fileinput("upload");
    });
    $("#detactNumber_input").on("fileuploaded", function(event, data) {
    	var path=data.response.path;
    	if(path!=undefined||path!=""){
    		$("#viewDetactNumber").attr("value",path);
			$("#viewDetactNumber").show();
			$("#delDetactNumber").show();
    	}
    });
    //初始化计量资格证书文件上传
	initFileInput("meteringNumber_input");
	$("#meteringNumber_input").fileinput('refresh', {
		uploadUrl: "uploadOrganFiles",
        uploadExtraData: {organUuid:organUuid,type:4}
    });
    $("#meteringNumber_input").on("filebatchselected", function(event, files) {  
        $(this).fileinput("upload");
    });
    $("#meteringNumber_input").on("fileuploaded", function(event, data) {
    	var path=data.response.path;
    	if(path!=undefined||path!=""){
    		$("#viewMeteringNumber").attr("value",path);
			$("#viewMeteringNumber").show();
			$("#delMeteringNumber").show();
    	}
    });
    //获取文件显示
    getOrganFileUrls();
	$("#delDetactNumber").bind("click",function(data,status){
		var src=$("#viewDetactNumber").attr("value");
		if(src==""){
			swal({title:"没有文件！",type:"warning"});
			return;
		}
		$.post("/smosplat/deleteOrganFiles",{organUuid:organUuid,type:"zizhizhengshu"},function(data,status){
			var jsonObj=JSON.parse(data);
			if(jsonObj.result==0){
				$("#viewDetactNumber").attr("val","");
				$("#viewDetactNumber").hide();
				$("#delDetactNumber").hide();
				swal({title:"删除成功！",type:"success"});
			}else{
				swal({title:"删除失败！",text:jsonObj.msg,type:"error"});
			}
		});
	});
	$("#delMeteringNumber").bind("click",function(data,status){
		var src=$("#viewMeteringNumber").attr("value");
		if(src==""){
			swal({title:"没有文件！",type:"warning"});
			return;
		}
		$.post("/smosplat/deleteOrganFiles",{organUuid:organUuid,type:"jiliangrenzhengzhengshu"},function(data,status){
			var jsonObj=JSON.parse(data);
			if(jsonObj.result==0){
				$("#viewMeteringNumber").attr("src","");
				$("#viewMeteringNumber").hide();
				$("#delMeteringNumber").hide();
				swal({title:"删除成功！",type:"success"});
			}else{
				swal({title:"删除失败！",text:jsonObj.msg,type:"error"});
			}
		});
	});
});
//bootstrap-fileinput组件初始化
function initFileInput(ctrlName) {      
    var control = $('#' + ctrlName);   
    control.fileinput({  
        language: 'zh', // 设置语言
        showUpload: false, // 是否显示上传按钮
        showRemove:false,  
        dropZoneEnabled: false,  
        showCaption: false,// 是否显示标题
        allowedFileExtensions:  ['jpg','png','jpeg','pdf'],  
        maxFileSize : 2000,  
        maxFileCount: 1
    });
}
function getOrganFileUrls(){
	$.post("/smosplat/getOrganFileUrls",{organUuid:organUuid},function(data,status){
    	//显示缩略图
    	var jsonObj=JSON.parse(data);
    	$("#viewDetactNumber").attr("val","");
    	$("#viewMeteringNumber").attr("value","");
    	//有原始文件但是没缩略图，就显示pdf图片
    	if(jsonObj.zizhizhengshu!=undefined){
        	$("#viewDetactNumber").attr("value",jsonObj.zizhizhengshu);
    	}
    	if(jsonObj.jiliangrenzhengzhengshu!=undefined){
    		$("#viewMeteringNumber").attr("value",jsonObj.jiliangrenzhengzhengshu);
    	}
    });
}


