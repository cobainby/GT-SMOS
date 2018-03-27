	/**
	 * 获得权限数据
	 */
	var roleItemData;
	//获取权限信息
	$.ajax({
	    type: "post",
	    url: "/smosplat/getRoleItemsFromSession",
	    async: false,
	    success: function(data) {
	        roleItemData = JSON.parse(data);
	    }
	});
	function hasRoleItem(number) {
	    for (var i = 0; i < roleItemData.roleItems.length; i++) {
	        if (roleItemData.roleItems[i].number == number) {
	            return true;
	        }
	    }
	    return false;
	}
	function setView() {
	    //返回-1是超级管理员
	    if (roleItemData.result == -1) {
	    	//超级管理员所有界面元素都是可用的
	    	
	    } else {
	        if (!hasRoleItem(101)) {
	            $("#toOrganEdit").css("visibility", "hidden");
	        }
	    }
	}
	
	var organUuid;
$(function() {
	//根据权限控制界面元素
    setView();
	// 为按钮添加点击事件
	$("#toOrganEdit").bind('click', function() {
		window.location.href = "/smosplat/organEdit?organUuid="+organUuid;
	});
	$.post("getCurrentAccount",function(data,status){
		var account=JSON.parse(data).account;
		$.post("getRoleByAccount",{accountUuid:account.accountUuid},function(data,status){
			var role=JSON.parse(data).role;
			if(role.roleName=="普通账号"||role.roleName=="访客账号"){
				$("#toOrganEdit").hide();
			}
		})
	})
	// 从url里面获取organUuid参数
	organUuid = getUrlParam("organUuid");
	$.post("getOrgan", {organUuid : organUuid}, function(data, status) {
		var jsonData = JSON.parse(data);
		var organ = jsonData.organ;
		var worker = jsonData.worker;
		var organName = jsonData.organ.organName;
		$("#organName").text(organName);
		jsonData.worker.workerName != null ? $("#workerName").text(jsonData.worker.workerName) : $("#workerName").text("");
		jsonData.worker.phone != null ? $("#phone").text(jsonData.worker.phone) : $("#phone").text("");
		if(jsonData.supervisor != null){
			jsonData.supervisor.workerName != null ? $("#supervisorName").text(jsonData.supervisor.workerName) : $("#supervisorName").text("");
			jsonData.supervisor.phone != null ? $("#supervisorPhone").text(jsonData.supervisor.phone) : $("#supervisorPhone").text("");
		}
//		organ.telephone!=null?$("#telephone").text(organ.telephone):$("#telephone").text("");
		organ.establishedTime!=null?$("#establishedTime").text(organ.establishedTime):$("#establishedTime").text("");
		organ.adress!=null?$("#adress").text(organ.adress):$("#adress").text("");
//		organ.postcode!=null?$("#postcode").text(organ.postcode):$("#postcode").text("");
		organ.email!=null?$("#email").text(organ.email):$("#email").text("");
		organ.organCode!=null?$("#organCode").text(organ.organCode):$("#organCode").text("");
		organ.detactNumber!=null?$("#detactNumber").text(organ.detactNumber):$("#detactNumber").text("");
		organ.meteringNumber!=null?$("#meteringNumber1").text(organ.meteringNumber):$("#meteringNumber1").text("");
		organ.representative!=null?$("#representative").text(organ.representative):$("#representative").text("");
		organ.techDirector!=null?$("#techDirector").text(organ.techDirector):$("#techDirector").text("");
		if(organ.detactNumber == null){
			$("#viewDetact").hide();
		}
		$("#meteringNumber").text(organ.meteringNumber);
		if(organ.meteringNumber == null){
			$("#viewMetering").hide();
		}

		
	});
	//获取文件显示
	getOrganFileUrls();
	//查看大图
    $(".viewDoc").bind("click",function(){
    	var src = $(this).attr("value");
    	if(src != "" && src != undefined){
    		window.open(src,"","toolbar=no,scrollbars=no,menubar=no");
    	}
    });
});
function getOrganFileUrls(){
	$.post("/smosplat/getOrganFileUrls",{organUuid:organUuid},function(data,status){
    	//显示缩略图
    	var jsonObj=JSON.parse(data);
    	$("#viewDetact").attr("val","");
    	$("#viewMetering").attr("value","");
    	//有原始文件但是没缩略图，就显示pdf图片
    	if(jsonObj.zizhizhengshu!=undefined){
        	$("#viewDetact").attr("value",jsonObj.zizhizhengshu);
    	}
    	if(jsonObj.jiliangrenzhengzhengshu!=undefined){
        	$("#viewMetering").attr("value",jsonObj.jiliangrenzhengzhengshu);
    	}
    });
}
