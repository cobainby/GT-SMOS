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
function getColumnIndex(name) {
    for (var i = 0; i < deviceColNames.length; i++) {
        if (deviceColNames[i] == name) {
            return i;
        }
    }
    return -1;
}
function setView() {
    //返回-1是超级管理员
    if (roleItemData.result == -1) {
    	//超级管理员所有界面元素都是可用的
    	
    } else {
        if (!hasRoleItem(301)) {
            $("#addDeviceBtn").css("visibility", "hidden");
        }
        if (!hasRoleItem(302)) {
        	$("#deviceToolbar").css("visibility", "hidden");
        }
        if (!hasRoleItem(303)) {
        	var index=getColumnIndex("修改基本信息");
        	deviceColNames.splice(index, 1);
        	deviceColModel.splice(index, 1);
        }
        if (!hasRoleItem(304)) {
        	var index=getColumnIndex("删除");
        	deviceColNames.splice(index, 1);
        	deviceColModel.splice(index, 1);
        }
    }
}
//机构id
var currentOrganUuid;
var isSuperAdmin=false;
//设备列表的列
var deviceColNames = ['设备ID', '设备名称','设备类型ID','设备编号', '设备型号','设备型号ID','部门ID','设备所属部门', '生产厂家',
	'检定/校准机构','检定/校准有效期','仪器设备台帐及计量检定证书', '设备保管员','校准证书','状态', '状态','详细信息'];
var deviceColModel = [
	{ name: 'deviceUuid',hidden: true},
    { name:'devTypeName',align:'center',width:100},
    { name:'devTypeUuid',hidden: true},
    { name: 'sn',align:'center',width:100},
    { name: 'devModelName',align:'center',width:100},
    { name: 'devModelUuid',hidden: true},
    { name: 'organUuid',hidden: true},
    { name: 'organName',align:'center',hidden: true},
    { name: 'manufactor',align:'center',width:100},
    { name: 'calibratedOrg',align:'center'}, 
    { name: 'expDate',align:'center'},
    { name: 'qualityCert',hidden:true},
    { name: 'keeper',align:'center'},
    { name: 'calibratedNum',align:'center'},
    {name: 'status',hidden:true},
    { name: 'status2',formatter:deviceStatus,sortable: false,align: "center",width:110},
    { name: 'view',formatter: deviceView,sortable: false,align: "center",width:300}
];
/**
 * 定义jqGrid的绑定列
 */
function isSuperadmin() {
	$.post("getCurrentAccount",
			function(data,status){
			var jsonData=$.parseJSON(data);
			if(jsonData.account.loginName=="superadmin"){
				isSuperAdmin=true;
				$.post("getAllOrgans",
						function(data,status){
							var dtData=$.parseJSON(data).organs;
							$("#organCB").combobox({
								data:dtData,
								valueField:'organUuid',
								textField:'organName',
								panelHeight:'auto',
								editable:false
							});
							
							//设置当前机构
							for(var j=0;j<dtData.length;j++){
								if(dtData[j].organName=="超级管理员所属机构"){
									currentOrganUuid=dtData[j].organUuid;
								}
							}
							$('#organCB').combobox({
								onSelect:function(param){
									currentOrganUuid=$('#organCB').combobox('getValue');
								}
							});
							$('#organCB').combobox('setValue',currentOrganUuid);
							//获取所有数据到表格
							getDevices(currentOrganUuid);
						});
				$("#deviceToolbar").css("display", "block");
			}else{
				currentOrganUuid=jsonData.organUuid;
				getDevices(currentOrganUuid);
				 $("#deviceToolbar").remove();
			}
	});
}
var organName;
$(function() {
	//普通账户不允许编辑
	$.post("getCurrentAccount",function(data,status){
		var account=JSON.parse(data).account;
		$.post("getRoleByAccount",{accountUuid:account.accountUuid},function(data,status){
			var role=JSON.parse(data).role;
			if(role.roleName=="普通账号"||role.roleName=="访客账号"){
				$("#addDeviceBtn").hide();
			}
		})
	})
	//根据权限控制界面元素
    setView();
	isSuperadmin();
    //初始化文件上传==============================
    $("#deviceLicense_input").fileinput({  
        language: 'zh', // 设置语言
        showUpload: false, // 是否显示上传按钮
        showRemove:false,  
        dropZoneEnabled: false,  
        showCaption: false,// 是否显示标题
        allowedFileExtensions:  ['jpg','png','jpeg','pdf'],  
        maxFileSize : 2000,  
        maxFileCount: 1
    });
    $("#deviceLicense_input").on("filebatchselected", function(event, files) {
    	//选中文件之后生成临时缩略图
    	var file=$("#deviceLicense_input")[0].files[0];
		//获取文件名称
	    var fileName = file.name;
	    //获取文件类型名称
	    var file_typename = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
	    if(!(file_typename=='.png'||file_typename=='.jpg'||file_typename=='.jpeg'||file_typename=='.pdf')){
	    	swal({title:"不支持的文件类型！",type:"warning"});
	    	//清除文件
	    	$("#deviceLicense_input").fileinput('clear');
	    	return;
	    }
	    // 计算文件大小
	    var fileSize = 0;
	    // 如果文件大小大于1024字节X1024字节，则显示文件大小单位为MB，否则为KB
	    if (file.size > 1024 * 1024) {
	　　		fileSize = Math.round(file.size * 100 / (1024 * 1024)) / 100;
		　	if (fileSize > 2) {
	            swal({title:"文件大小不能超过2MB！",type:"warning"});
	            //清除文件
		    	$("#deviceLicense_input").fileinput('clear');
	            return;
	        }
		　	fileSize = fileSize.toString() + 'MB';
	    }
	    else {
	        fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
	    }
	    //生成缩略图并显示
	    if(file_typename=='.pdf'){
	    	$("#deviceLicense").attr("src","/smosplat/common/image/pdf.png");
	    }else{
	    	 previewImage(this,"localImage","deviceLicense");
	    }
	    //选择了文件之后，不能点击查看原始文件（因为看到的是之前的）
	    $("#deviceLicense").attr("src","");
    	$("#deviceLicense1").val("");
    });
    $("#deviceLicense").bind("click",function(){
    	var src1=$("#deviceLicense1").val();
    	if(src1==undefined||src1==""){
    		return;
    	}
    	window.open(src1,"","toolbar=no,scrollbars=no,menubar=no");
    });
    $("#viewPortrait").bind("click",function(){
    	var src1=$("#viewPortrait1").val();
    	if(src1==undefined||src1==""){
    		return;
    	}
    	window.open(src1,"","toolbar=no,scrollbars=no,menubar=no");
    });
    //初始化文件上传===================end===========
    //设备类型下拉列表数据获取
	$.post("getAllDevTypes",
		function(data,status){
			var dtData=$.parseJSON(data).rows;
			var control=$("#devType");
	        control.empty();//清空下拉框
	        $.each(dtData, function (i, item) {
	        	//只能添加手动采集的设备
	        	if(item.isAuto==-1){
	        		control.append("<option value='" + item.devTypeUuid + "'>" + item.devTypeName + "</option>");
	        	}
	        });
	        //获取设备型号
	        $.post("getAllDeviceModel", function(data, status) {
	            var dtData1 = $.parseJSON(data).rows;
	            var control1 = $("#devMoldel");
	            //选择一种设备类型之后更改对应的设备型号下拉框
	            $("#devType").on("change",function(){
	            	var selDevType=$("#devType").val();
	            	var selModels=new Array();
	            	for (var i = 0; i < dtData1.length; i++) {
						if(dtData1[i].deviceType.devTypeUuid==selDevType){
							selModels.push(dtData1[i]);
						}
					}
	            	control1.empty();// 清空下拉框
		            $.each(selModels, function(i, item) {
		                control1.append("<option value='" + item.devModelUuid + "'>"
		                        + item.devModelName + "</option>");
		            });
	            });
	            //选中第0个
			    $('#devType').val(dtData[0].devTypeUuid);
	        });
	});
    //增加设备
    $("#addDeviceBtn").bind('click', function() {
        $('#addDeviceForm').form('clear');
        $("#formUrl").text("增加设备");
    	$("#formUrl").val("addDevice");
    	$("#deviceLicense").attr("src","");
    	$("#deviceLicense1").val("");
    	$("#viewPortrait").attr("src","");
        $("#viewPortrait1").val("");
    	$('#addDeviceDialog').modal('show');
    });
    //增加设备保存按钮点击
    $("#saveDeviceBtn").bind('click', function() {
        //先验证表单，如果表单验证不通过则不提交
        var val = $("#addDeviceForm").valid();
        if (!val) {
            return;
        }
		var url=$("#formUrl").val();
		var params={
				sn:$("#devSn").val(),
				manufactor:$("#devManufactor").val(),
				calibratedOrg:$("#devOrg").val(),
				keeper:$("#keeper").val(),
				calibratedNum:$("#calibratedNum").val(),
				expDate:$("#devValidateDate").datetimebox('getValue'),
				status:$("#devStatusCB option:selected").val(),
				remark:$("#remarkL").val()
		};
		params["devType.devTypeUuid"]=$("#devType option:selected").val();
		params["deviceModel.devModelUuid"]=$("#devMoldel option:selected").val();
		if(isSuperAdmin){
			//若是超级管理员则添加设备到当前浏览部门下面
			params["organ.organUuid"]=$('#organCB').combobox('getValue');
		}else{
			params["organ.organUuid"]=currentOrganUuid;    //设备所属部门拾取
		}
		//如果是修改操作，增加uuid参数值
		if(url=="updateDevice"){
            var selIndex = $("#deviceDG").jqGrid("getGridParam", "selrow");
            var selData=$("#deviceDG").jqGrid("getRowData", selIndex);
			params["deviceUuid"]=selData.deviceUuid;//选中行Uuid
			params["organ.organUuid"]=selData.organUuid;
		}
		$.post("/smosplat/"+url,params,function(data,status){
			var jObj=JSON.parse(data);
			if(jObj.result==0){
				if(url=="addDevice"){
					getDevices(currentOrganUuid);
				}else if(url=="updateDevice"){
					getDevices(currentOrganUuid);
				}
				//增加或修改设备成功后上传文件
				//选择了文件才上传，不选择就显示已上传的文件
				var fileCount=$("#deviceLicense_input").fileinput("getFilesCount");
	            var deviceUuid=jObj.entity.deviceUuid;
				if(fileCount>0){
					//获取form数据
				    var formData = new FormData($("#deviceFileForm")[0]);
				    formData.append("organUuid", currentOrganUuid);
				    formData.append("deviceUuid",deviceUuid);
				    $.ajax({
				        url: "uploadDeviceFiles",
				        type: 'POST',
				        data:formData,
				        async: false,
				        cache: false,
				        contentType: false,
				        processData: false,
				        success: function (returnInfo) {
				        	$('#addDeviceDialog').modal('hide');
				        	swal({title:"操作成功！",type:"success"});
				        },
				        error: function (returnInfo) {
				            //上传失败时显示上传失败信息
				        	swal({title:"文件上传失败！",type:"error"});
				        }
				    });
				}else{
					//关闭窗口
					$('#addDeviceDialog').modal('hide');
				}
			}else{
				swal({title:"失败！",text:JSON.parse(data).msg,type:"error"});
			}
		});
    });
    //取消填写表单，清除验证信息
    $("#cancelDeviceBtn").bind("click",function(){
    	$('#addDeviceForm').data('validator').resetForm();
    });
    //点击过滤
    $("#filterByOrganBtn").bind("click",function(){
    	var value=$("#organCB").combobox("getValue");
        getDevices(value);
    });
    
});
function deviceStatus(cellvalue,options,rowObject){
	var nowDate = new Date().getTime();
	if(cellvalue>nowDate){
		//判断检定时间是否剩余1个月
		if(cellvalue-nowDate>2592000000){
			return '<img src="/smosplat/common/image/green.png" style="width:12px;height:12px;margin-bottom:2px;">'+'(正常)';
		}else{
			return '<img src="/smosplat/common/image/yellow.gif" style="width:12px;height:12px;margin-bottom:2px;">'+'(即将过期)';
		}
	}else if (cellvalue<nowDate){
		return '<img src="/smosplat/common/image/red.gif" style="width:12px;height:12px;margin-bottom:2px;">'+'(过期)';
	}
}
function deviceView(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a data-toggle="modal" onclick="getDeviceView(' + options.rowId + ')">查看</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+'<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a data-toggle="modal" onclick="getDeviceEdit(' + options.rowId + ')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+'<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a data-toggle="modal" onclick="getDel(' + options.rowId + ')">删除</a>';
}


/**
 * 查看设备的详细信息窗口
 * @param index
 * @returns
 */
function getDeviceView(index) {
	$("#deviceDG").jqGrid('setSelection', index);
    var selectedData = $("#deviceDG").jqGrid("getRowData", index);
    $('#viewForm').form('clear');
    //填充数据
    $("#devTypeL").val(selectedData.devTypeName);
    $("#devSnL").val(selectedData.sn);
    $("#devManufactorL").val(selectedData.manufactor);
    $("#devDateL").val(selectedData.verifyDate);
    $("#devStatusCBL").val(selectedData.status != 0 ? "停用": "启用");
    $("#devMoldelL").val(selectedData.devModelName);
    $("#devOrgL").val(selectedData.calibratedOrg);
    $("#calibratedNumL").val(selectedData.calibratedNum);
    $("#devValidateDateL").val(selectedData.expDate);
    $("#remarkL").val(selectedData.remark);
    $("#viewPortrait").attr("src","");
    $("#viewPortrait1").val("");
    $.post("/smosplat/getDeviceFileUrls",{organUuid:currentOrganUuid,deviceUuid:selectedData.deviceUuid},function(data,status){
		var jsonObj=JSON.parse(data);
		if(jsonObj.deviceUrl==undefined){
    		$("#viewPortrait").attr("src","/smosplat/common/image/pdf.png");
    	}else{
    		$("#viewPortrait").attr("src",jsonObj.deviceUrl1[0]);
    	}
		$("#viewPortrait1").val(jsonObj.deviceUrl[0]);
		$('#viewDialog').modal('show');
	});
}

/**
 * 修改设备窗口
 */
function getDeviceEdit(index) {
	$('#addDeviceForm').form('clear');
	//图片控件重新生成一下
    $("#deviceDG").jqGrid('setSelection', index);
    var selectedData = $("#deviceDG").jqGrid("getRowData", index);
    //超级管理员机构不能修改
    $("#formUrl").text("修改设备");
    $("#devType").val(selectedData.devTypeUuid);
    $("#devSn").val(selectedData.sn);
    //设置devType的值的时候不能加载设备型号到下拉列表，这里根据设备重新加载一下
    $.post("getDeviceModelByDevType",{devTypeName:selectedData.devTypeName},
		function(data,status){
			var dmData=$.parseJSON(data).rows;
			var control=$("#devMoldel");
	        control.empty();//清空下拉框
	        $.each(dmData, function (i, item) {
	        	//只能添加手动采集的设备
	        	if(item.deviceType.isAuto==-1){
	        		control.append("<option value='" + item.devModelUuid + "'>"
	                        + item.devModelName + "</option>");
	            }
	        });
	        $("#devMoldel").val(selectedData.devModelUuid);
	});
    $("#devOrg").val(selectedData.calibratedOrg);
    $("#calibratedNum").val(selectedData.calibratedNum);
    $("#devManufactor").val(selectedData.manufactor);
    $("#devDate").datetimebox('setValue',selectedData.verifyDate);
	$("#devValidateDate").datetimebox('setValue',selectedData.expDate);
	$("#devStatusCB").val(selectedData.status);
    $("#formUrl").val("updateDevice");
    $.post("/smosplat/getDeviceFileUrls",{organUuid:currentOrganUuid,deviceUuid:selectedData.deviceUuid},function(data,status){
		var jsonObj=JSON.parse(data);
		if(jsonObj.deviceUrl==undefined){
    		$("#deviceLicense").attr("src","/smosplat/common/image/pdf.png");
    	}else{
    		$("#deviceLicense").attr("src",jsonObj.deviceUrl1[0]);
    	}
		$("#deviceLicense1").val(jsonObj.deviceUrl[0]);
	});
    $('#addDeviceDialog').modal('show');
}

/**
 * 删除设备
 */
function getDel(index) {
    $("#deviceDG").jqGrid('setSelection', index);
    var selectedData = $("#deviceDG").jqGrid("getRowData", index);
    //超级管理员机构不能删除
    //弹出提示框确认
    swal({
        title: "您确定要该设备信息吗",
        text: "删除后将无法恢复，请谨慎操作！",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/deleteDevice", {deviceUuid: selectedData.deviceUuid }, function(data, status) {
                if (JSON.parse(data).result == 0) {
                    //重新加载数据
                    $("#deviceDG").jqGrid('delRowData', index);
                    //删除设备对应的图片
                    $.post("/smosplat/deleteDeviceFiles",{organUuid:currentOrganUuid, deviceUuid: selectedData.deviceUuid},function(data,status){
            			var jsonObj=JSON.parse(data);
            			if(jsonObj.result==0){
            				$("#comLicense").attr("src","");
            				$("#comLicense1").val("");
            				swal("删除成功！", "您已经永久删除了这条信息。", "success")
            			}else{
            				swal({title:"删除失败！",text:jsonObj.msg,type:"error"});
            			}
            		});
                } else {
                    swal({title:"失败！",text:JSON.parse(data).msg,type:"error"});
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    })
}
/**
 * 查询数据用于在datagrid里面显示
 * @param params
 */
function getDevices(OrganUuid) {
    $.post("/smosplat/getDeviceByOrgan", {organUuid:OrganUuid},
        function(data, status) {
    	 	var deviceData = JSON.parse(data);
    	    $.jgrid.gridUnload('deviceDG');
            //设备列表
            for (var i = 0; i < deviceData.rows.length; i++) {
                deviceData.rows[i]["devTypeName"] = deviceData.rows[i].devType != null ? deviceData.rows[i].devType.devTypeName : "";
                deviceData.rows[i]["devTypeUuid"] = deviceData.rows[i].devType != null ? deviceData.rows[i].devType.devTypeUuid : "";
                deviceData.rows[i]["devModelName"] = deviceData.rows[i].deviceModel != null ? deviceData.rows[i].deviceModel.devModelName : "";
                deviceData.rows[i]["devModelUuid"] = deviceData.rows[i].deviceModel != null ? deviceData.rows[i].deviceModel.devModelUuid : "";
                deviceData.rows[i]["organUuid"] = deviceData.rows[i].organ != null ? deviceData.rows[i].organ.organUuid : "";
                deviceData.rows[i]["organName"] = deviceData.rows[i].organ != null ? deviceData.rows[i].organ.organName : "";
                deviceData.rows[i]["verifyDate"] =deviceData.rows[i].verifyDate!=null ?timestamp2String(deviceData.rows[i].verifyDate) : "";
                deviceData.rows[i]["calibratedNum"] =deviceData.rows[i].calibratedNum!=null ?deviceData.rows[i].calibratedNum : "";
                deviceData.rows[i]["status2"] = deviceData.rows[i].expDate!=null ?deviceData.rows[i].expDate: "";
                deviceData.rows[i]["expDate"] = deviceData.rows[i].expDate!=null ?timestamp2String(deviceData.rows[i].expDate) : "";
                
            };
            $("#deviceDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                data: deviceData.rows,
                colNames: deviceColNames,
                colModel: deviceColModel,
                rowNum: 15,
                rowList: [15, 30, 45],
                pager: $("#pager_deviceDG"),
                autowidth: true,
                sortname: 'id',
                viewrecords: true
            });
            $("#deviceDG").jqGrid('navGrid', '#pager_deviceDG', { edit: false, add: false, del: false });
            $("#deviceDG").setGridHeight($("#jqGrid_Device").height()-75);
            $("#deviceDG").jqGrid('setSelection', '1');
            
        }
    );
}
// 图片上传预览 IE是用了滤镜。
function previewImage(file,imgDIV,imgID)
{
  var MAXWIDTH  = 120; 
  var MAXHEIGHT = 120;
  // 相片div
  var div = $("#"+imgDIV)[0];
  if (file.files && file.files[0])
  {
      //div.innerHTML ='<img id='+"\""+imgID+"\""+' class="img-responsive img-rounded previewImage" alt="照片">';
      // img控件
      var img = $("#"+imgID)[0];
      img.onload = function(){
        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
        img.width  =  rect.width;
        img.height =  rect.height;
      }
      var reader = new FileReader();
      reader.onload = function(evt){img.src = evt.target.result;}
      reader.readAsDataURL(file.files[0]);
  }
  else // 兼容IE
  {
    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
    file.select();
    var src = document.selection.createRange().text;
    //div.innerHTML ='<img id='+"\""+imgID+"\""+' class="img-responsive img-rounded previewImage" alt="照片">';
    var img = $("#"+imgID)[0];
    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
    //div.innerHTML = "<div id="+"\""+imgDIV+"\""+" style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
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