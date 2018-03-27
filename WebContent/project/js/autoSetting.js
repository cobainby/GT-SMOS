var stompClient;
var startTimeout;
var stopTimeout;
var projectUuid;
$(function() {
    $("#type").combobox("loadData", [{ id: 0, text: "服务端" }, { id: 1, text: "客户端" }]);
    $("#type").combobox("setValue", 0);
    getNetWork();
    //增加网络连接
    $("#addNetworkBtn").bind("click", function() {
        $("#sectionForm").form("clear");
        $("#addOrEditNetwork").text("add");
        $("#networkInfoDialog").modal("show");
    });
    //增加或修改网络连接保存
    $("#saveNetworkBtn").bind("click", function() {
        var params = {
            networkName: $("#networkName").val(),
            type: $("#type").combobox("getValue"),
            ip: $("#ip").val(),
            port: $("#port").val()
        };
        var type = $("#addOrEditNetwork").text();
        if (type == "add") {
            $.post("/smosplat/addNetwork", params, function(data, status) {
                var dataObj = JSON.parse(data);
                if (dataObj.result == 0) {
                    //重新加载数据
                    getNetWork();
                    $("#networkInfoDialog").modal("hide");
                    swal({title:"添加成功！",type:"success"});
                } else {
                	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
                }
            });
        } else if (type == "edit") {
            var netRowId = $('#networkDG').jqGrid('getGridParam', 'selrow');
            var networkUuid = $("#networkDG").jqGrid('getRowData', netRowId).networkUuid;
            params["networkUuid"] = networkUuid;
            $.post("/smosplat/updateNetwork", params, function(data, status) {
                var dataObj = JSON.parse(data);
                if (dataObj.result == 0) {
                    getNetWork();
                    $("#networkInfoDialog").modal("hide");
                    swal({title:"修改成功！",type:"success"});
                } else {
                	swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                }
            });
        }
    });
    getMcu();
    //增加mcu
    $("#addMcuBtn").bind("click", function() {
        //获得所有的mcu型号
        $("#mcuForm").form("clear");
        $.post("/smosplat/getAllDeviceModel", {}, function(data, status) {
            var allDeviceModels = JSON.parse(data).rows;
            //过滤出mcu设备
            var mcuModels = new Array();
            for (var i = 0; i < allDeviceModels.length; i++) {
                if (allDeviceModels[i].deviceType.devTypeName == "mcu") {
                    mcuModels.push(allDeviceModels[i]);
                }
            }
            $("#deviceSN").val("");
            $("#sn").val("");
            $("#deviceModel").combobox("clear");
            $("#deviceModel").combobox("loadData", mcuModels);
            $("#addOrEditMcu").text("add");
            var networks = $("#networkDG").jqGrid('getRowData');
            $("#network").combobox("loadData", networks);
            if (networks.length == 0) {
                $("#network").combobox("clear");
            } else {
                $("#network").combobox("setValue", networks[0].networkUuid);
            }
            $("#mcuInfoDialog").modal("show");
        });
    });
    //增加或修改mcu保存
    $("#saveMcuBtn").bind("click", function() {
        var params = {
            sn: $("#sn").val()
        };
        params["network.networkUuid"] = $("#network").combobox("getValue");
        params["network.networkName"] = $("#network").combobox("getText");
        params["deviceSN"] = $("#deviceSN").val();
        params["devModelUuid"] = $("#deviceModel").combobox("getValue");
        var type = $("#addOrEditMcu").text();
        if (type == "add") {
            $.post("/smosplat/addMcu", params, function(data, status) {
                var dataObj = JSON.parse(data);
                if (dataObj.result == 0) {
                	getMcu();
                    $("#mcuInfoDialog").modal("hide");
                    swal({title:"添加成功！",type:"success"});
                } else {
                	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
                }
            });
        } else if (type == "edit") {
            var mcuRowId = $('#mcuDG').jqGrid('getGridParam', 'selrow');
            var mcuUuid = $("#mcuDG").jqGrid('getRowData', mcuRowId).mcuUuid;
            var deviceId=$("#mcuDG").jqGrid('getRowData', mcuRowId)['device.deviceUuid'];
            params["mcuUuid"] = mcuUuid;
            params["deviceUuid"] = deviceId;
            $.post("/smosplat/updateMcu", params, function(data, status) {
                var dataObj = JSON.parse(data);
                if (dataObj.result == 0) {
                    getMcu();
                    $("#mcuInfoDialog").modal("hide");
                    swal({title:"修改成功！",type:"success"});
                } else {
                	swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                }
            });
        }
    });
    $("#closeDeviceInfoBtn").bind("click", function() {
        //关闭窗口
        $("#deviceInfoDialog").dialog("close");
    });
    //获得项目采集状态
    $.get("/smosplat/getProjectCollectStatus",function(data,status){
    	var jsonObj=JSON.parse(data);
    	if(jsonObj.result==0){
    		var interval = jsonObj.interval;
    		projectUuid = jsonObj.projectUuid;
    		$("#period").val(interval);
    		if(jsonObj.collectStatus==0){
    			$("#startBtn").text("工程采集中！点击停止采集！");
    			$("#callCollectBtn").text("工程采集中！");
    			$("#callCollectBtn").attr({"disabled":"disabled"});
    		}else if(jsonObj.collectStatus==-1){
    			$("#startBtn").text("工程采集已停止！点击开始采集！");
    			$("#callCollectBtn").text("点击开始召测采集(采集一次)！");
    			
    			
    			
    		}
    	}else{
    		swal({title:"失败！",text:jsonObj.msg,type:"error"});
    	}
    });
    $("#callCollectBtn").bind("click", function() {
    	var warningStr;
    	var text=$("#callCollectBtn").text();
    	if(text.indexOf("召测")!=-1){
    		warningStr = "确定开始采集吗？";
    	}
    	$.messager.confirm('警告', warningStr, function(r) {
            if (r) {
            	if(text.indexOf("召测")!=-1){
            		 var connectMcuTime = $("#connectMcuTime").val()*60;
                 	if(connectMcuTime<180){
                 		swal({title:"召测每个mcu通讯时间不可低于3分钟！",type:"warning"});
                 		$("#callCollectBtn").text("点击开始召测采集(采集一次)！");
                 		$("#callCollectBtn").removeAttr("disabled");
                 		return;
                 	}else if(connectMcuTime == undefined){
                 		swal({title:"请输入mcu配置时间！",type:"warning"});
                 		return;
                 	}
            		var mcus = $("#mcuDG").jqGrid('getRowData');
            		//获得一个dtu连接的mcu的最大数量
                	var networks = $("#networkDG").jqGrid('getRowData');
                	var networkUuids = new Array();
                    for (var i = 0; i < networks.length; i++) {
                        networkUuids.push(networks[i].networkUuid);
                    }
            		$("#callCollectBtn").text("采集开始中，成功采集需要"+networks.length*connectMcuTime+"秒！");
            		$("#callCollectBtn").attr({"disabled":"disabled"});
            		$("#startBtn").attr({"disabled":"disabled"});
                    stopTimeout=setTimeout(function(){
                    	$("#startBtn").removeAttr("disabled");
                    	$("#callCollectBtn").removeAttr("disabled");
                    	$("#callCollectBtn").text("点击开始召测采集(采集一次)！");
                    }, 1000*(networks.length*connectMcuTime));
                    var mcus = $("#mcuDG").jqGrid('getRowData');
                    if (networkUuids.length == 0) {
                        swal({title:"没有网络连接！",type:"warning"});
                        return;
                    }
            		$.post("/smosplat/callCollect", { networkUuids: networkUuids.join(","),connectMcuTime:connectMcuTime}, function(data, status) {});
            	}
            }
    	});
    });
    
    //开始采集
    $("#startBtn").bind("click", function() {
    	var warningStr;
    	var text=$("#startBtn").text();
    	if(text.indexOf("工程采集已停止")!=-1){
    		warningStr = "确定开始采集吗？";
    	}else if(text.indexOf("工程采集中")!=-1){
    		warningStr = "确定停止采集吗？";
    	}
        //自动采集开始
        $.messager.confirm('警告', warningStr, function(r) {
            if (r) {
            	//获得一个dtu连接的mcu的最大数量
            	var networks = $("#networkDG").jqGrid('getRowData');
            	var networkUuids = new Array();
                for (var i = 0; i < networks.length; i++) {
                    networkUuids.push(networks[i].networkUuid);
                }
                var mcus = $("#mcuDG").jqGrid('getRowData');
                var maxMcuNum=0;
                for (var i = 0; i < networkUuids.length; i++) {
                	var mcuNum=0;
                	for (var j = 0; j < mcus.length; j++) {
        				if(mcus[j]['network.networkUuid']==networkUuids[i]){
        					mcuNum++;
        				}
        			}
        			if(mcuNum>maxMcuNum){
        				maxMcuNum=mcuNum;
        			}
        		}
                if (networkUuids.length == 0) {
                    swal({title:"没有网络连接！",type:"warning"});
                    return;
                }
                //点击开始采集按钮或者停止采集按钮之后需要等待一定时间才能点击停止，避免频繁操作
                var interval = $("#period").val();
                var connectMcuTime;
                if(text.indexOf("工程采集已停止")!=-1){
                	//时间为一个mcu的连接时间
                	connectMcuTime = $("#connectMcuTime").val()*60;
                	if(connectMcuTime<180){
                		swal({title:"每个mcu通讯时间不可低于3分钟！",type:"warning"});
                		return;
                	}else if(connectMcuTime == undefined){
                		swal({title:"请输入mcu配置时间！",type:"warning"});
                		return;
                	}
                	//设定采集间隔必须超过最大mcu数量*每个mcu的连接时间
//                	if(interval<connectMcuTime/60*maxMcuNum){
//                		swal({title:"采集时间间隔过短！",type:"warning"});
//                		return;
//                	}
                	$("#startBtn").text("采集开始中，成功采集需要"+connectMcuTime*mcus.length+"秒！");
                    $("#startBtn").attr({"disabled":"disabled"});
                    $("#callCollectBtn").text("工程采集中！");
                    $("#callCollectBtn").attr({"disabled":"disabled"});
                    $.post("/smosplat/mcuFilter",{ networkUuids : networkUuids.join(",")},function(data,status){
                    	var jsonObj=JSON.parse(data);
                    	if(jsonObj.result==0){
                    		var networks1 = new Array();
                    		var networks2 = new Array();
                    		for(var i = 0; i< jsonObj.mcus1.length; i++) {
                    			//去掉重复的网络
                    			if(networks1.indexOf(jsonObj.mcus1[i].network.networkUuid) == -1){
                    				networks1.push(jsonObj.mcus1[i].network.networkUuid);
                    			}
                    		}
                    		$.post("/smosplat/projectStartCollect_NEW", { networkUuids: networks1.join(","), interval: interval ,connectMcuTime:connectMcuTime}, function(data, status) {});
                    		for(var j = 0; j< jsonObj.mcus2.length; j++) {
                    			//去掉重复的网络
                    			if(networks2.indexOf(jsonObj.mcus2[j].network.networkUuid) == -1){
                    				networks2.push(jsonObj.mcus2[j].network.networkUuid);
                    			}
                    		}
                    		 $.post("/smosplat/projectStartCollect_LRK", { networkUuids: networks2.join(","), interval: interval ,connectMcuTime:connectMcuTime}, function(data, status) {});
		                    //采集状态
		                    $.post("/smosplat/updateProjectCollectStatus",{status:0},function(data,status){});
		                    
		                   
                    	}
                    });
                    //等所有mcu设置成功后才允许点击按钮，避免频繁点击操作
                    clearTimeout(startTimeout);
                	clearTimeout(stopTimeout);
                    startTimeout=setTimeout(function(){
                    	//更新采集间隔
                    	$.post("/smosplat/updateProjectInterval",{projectUuid:projectUuid, interval:interval},function(data,status){});
                   	 	$("#startBtn").text("工程采集中！点击停止采集！");
                   	 	$("#startBtn").removeAttr("disabled");
                	}, 1000*connectMcuTime*mcus.length);
                }else if(text.indexOf("工程采集中")!=-1){
                	$("#startBtn").text("采集正在停止，请等待"+connectMcuTime*mcus.length+"秒再重新开始！");
                    $("#startBtn").attr({"disabled":"disabled"});
                    //采集状态
                    $.post("/smosplat/updateProjectCollectStatus",{status:-1},function(data,status){});
                    $.post("/smosplat/projectStopCollect", { networkUuids: networkUuids.join(",")}, function(data, status) {});
                    //停止采集只是停止接受数据的线程，并不用进行通讯，10秒后才允许点击按钮
                    clearTimeout(startTimeout);
                	clearTimeout(stopTimeout);
                    stopTimeout=setTimeout(function(){
                   	 	$("#startBtn").text("工程采集已停止！点击开始采集！");
                   	 	$("#startBtn").removeAttr("disabled");
                   	 	$("#callCollectBtn").text("点击开始召测采集(采集一次)！");
                   	 	$("#callCollectBtn").removeAttr("disabled");
                	}, 1000*connectMcuTime*mcus.length);
                }
            }
        });
    });
    //socket相关==========================================================================================================
    //建立与服务器的长连接
    var socket = new SockJS('/smosplat/collect');
    stompClient = Stomp.over(socket);
    //建立连接
    stompClient.connect({}, function(frame) {
        $("#onlineInfo").val($("#onlineInfo").val() + "\r\n" + "浏览器与服务器通讯成功！");
        //获得当前项目id
        $.get("/smosplat/getCurrentProjectInfo", function(data, status) {
            var projectUuid = JSON.parse(data).project.projectUuid;
            //订阅消息
            stompClient.subscribe('/collect/mcuStartCollect/' + projectUuid, function(data) {
                var dataInfo = data.body;
                //出现异常，停止自动采集
                if(dataInfo.indexOf("【自动采集启动失败！】")!=-1){
                	$.post("/smosplat/updateProjectCollectStatus",{status:-1},function(data,status){});
                	clearTimeout(startTimeout);
                	clearTimeout(stopTimeout);
                    $("#startBtn").text("工程采集已停止！点击开始采集！");
               	 	$("#startBtn").removeAttr("disabled");
                }
                $("#onlineInfo").val($("#onlineInfo").val() + "\r\n" + dataInfo);
            });
        });
    });

    //===================================================================================================================
});
//获得网络连接数据
function getNetWork() {
    //获得所有网络连接
    $.post("/smosplat/getNetworks", function(data, status) {
        var dataObj = JSON.parse(data);
        var networks = dataObj.networks;
        $.jgrid.gridUnload('networkDG');
        $("#networkDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: networks,
            colNames: ['网络id', '网络连接名', '接入方式', 'IP', '端口', '编辑', '删除', '测试连接'],
            colModel: [{
                name: 'networkUuid',
                hidden: true
            }, {
                name: 'networkName',
                sorttype: "float",
                align: "center"
            }, {
                name: 'type',
                formatter: formateNetworkType,
                align: "center"
            }, {
                name: 'ip',
                sorttype: "float",
                align: "center"

            }, {
                name: 'port',
                sorttype: "float",
                align: "center"

            }, {
                name: 'editNetwork',
                formatter: formatEditNetwork,
                align: "center"

            }, {
                name: 'delNetwork',
                formatter: formatDelNetwork,
                align: "center"

            }, {
                name: 'testConnnection',
                formatter: formatNetworkTest,
                align: "center"
            }],
            autowidth: true,
            viewrecords: true,
            gridview: true
        });
        $("#networkDG").setGridHeight($("#north_rg").height() - 105);
        $("#networkDG").jqGrid('setSelection', '1');
    });
}

function formateNetworkType(cellvalue, options, rowObject) {
    if (rowObject.type == 0) {
        return "服务端";
    } else if (rowObject.type == 1) {
        return "客户端";
    }
}

function formatEditNetwork(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="editNetwork(' + options.rowId + ')" href="#">编辑</a>';
}

function formatDelNetwork(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteNetwork(' + options.rowId + ')" href="#">删除</a>';
}

function formatNetworkTest(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/connect.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="testNetwork(' + options.rowId + ')" href="#">测试</a>';
}
//编辑网络连接
function editNetwork(rowId) {
    $("#sectionForm").form("clear");
    $("#networkDG").jqGrid('setSelection', rowId);
    var item = $("#networkDG").jqGrid('getRowData', rowId);
    //填充值
    $("#networkName").val(item.networkName);
    var type=item.type=="服务端"?0:1;
    $("#type").combobox("setValue", type);
    $("#ip").val(item.ip);
    $("#port").val(item.port);
    $("#addOrEditNetwork").text("edit");
    $("#networkInfoDialog").modal("show");
}
//删除网络连接
function deleteNetwork(rowId) {
    $("#networkDG").jqGrid('setSelection', rowId);
    var networkUuid = $("#networkDG").jqGrid('getRowData', rowId).networkUuid;
    //弹出提示框确认
    swal({
        title: "警告",
        text: "您确定要删除吗?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/deleteNetwork", { networkUuid: networkUuid },function(data, status) {
                if (JSON.parse(data).result == 0) {
                	//刷新本地数据
                	$("#networkDG").jqGrid('delRowData', rowId);
                    swal("删除成功！", "您已经删除了该网络连接。", "success");
                } else {
                	swal("删除失败！",JSON.parse(data).msg,"error");
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    });
}



//获得所有mcu
function getMcu() {
    //获得所有mcu
    $.post("/smosplat/getMcus", function(data, status) {
        var dataObj = JSON.parse(data);
        var mcus = dataObj.mcus;
        var devices=dataObj.devices;
        for (var i = 0; i < devices.length; i++) {
        	mcus[i]["device.deviceUuid"]=devices[i].deviceUuid;
        	mcus[i]["device.deviceModel.devModelUuid"]=devices[i].deviceModel.devModelUuid;
        	mcus[i]["device.deviceModel.devModelName"]=devices[i].deviceModel.devModelName;
        	mcus[i]["device.sn"]=devices[i].sn;
		}
        $.jgrid.gridUnload('mcuDG');
        $("#mcuDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: mcus,
            colNames: ['mcuId', '设备Id','型号Id', '网络Id', 'mcu编号', 'mcu型号', 'mcu箱号', '网络连接', '设备状态', '编辑', '删除', '查看设备', '测试连接'],
            colModel: [{
                name: 'mcuUuid',
                hidden: true
            }, {
                name: 'device.deviceUuid',
                hidden: true
            },{
                name: 'device.deviceModel.devModelUuid',
                hidden: true
            }, {
                name: 'network.networkUuid',
                hidden: true
            }, {
                name: 'device.sn',
                align: "center"
            }, {
                name: 'device.deviceModel.devModelName',
                align: "center"
            }, {
                name: 'sn',
                align: "center"
            }, {
                name: 'network.networkName',
                align: "center"
            }, {
                name: 'status',
                formatter: formatMcuStatus,
                align: "center"
            }, {
                name: 'editMcu',
                formatter: formatEditMcu,
                align: "center"
            }, {
                name: 'delMcu',
                formatter: formatDelMcu,
                align: "center"
            }, {
                name: 'editMcuDevice',
                formatter: formatViewMcuDevice,
                align: "center"
            }, {
                name: 'testMcu',
                formatter: formatTestMcu,
                align: "center"
            }],
            autowidth: true,
            viewrecords: true,
            gridview: true
        });
        $("#mcuDG").setGridHeight($("#center_rg").height() - 105);
        $("#mcuDG").jqGrid('setSelection', '1');
    });
}
//测试网络连接
function testNetwork(rowId) {
    $("#networkDG").jqGrid('setSelection', rowId);
    var networkUuid = $("#networkDG").jqGrid('getRowData', rowId).networkUuid;
    //获得项目采集状态
    $.get("/smosplat/getProjectCollectStatus",function(data,status){
    	var jsonObj=JSON.parse(data);
    	if(jsonObj.result==0){
    		if(jsonObj.collectStatus==0){
    			swal({title:"测试网络必须先停止采集！",type:"warning"});
    		}else if(jsonObj.collectStatus==-1){
    			$.post("/smosplat/testConnect", { networkUuid: networkUuid },function(data, status) {});
    		}
    	}else{
    		swal({title:"获取项目采集状态失败！",text:jsonObj.msg,type:"error"});
    	}
    });
}

function formatMcuStatus(cellvalue, options, rowObject) {
    return "程序未处理";
}

function formatEditMcu(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="editMcu(' + options.rowId + ')" href="#">编辑</a>';
}

function formatViewMcuDevice(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="viewMcuDevice(' + options.rowId + ')" href="#">查看设备</a>';
}

function formatDelMcu(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteMcu(' + options.rowId + ')" href="#">删除</a>';
}

function formatTestMcu(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/connect.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="testMcu(' + options.rowId + ')" href="#">测试</a>';
}
//编辑mcu
function editMcu(rowId) {
    $("#mcuForm").form("clear");
    $("#mcuDG").jqGrid('setSelection', rowId);
    var item = $("#mcuDG").jqGrid('getRowData', rowId);
    $("#deviceSN").val(item['device.sn']);
    $("#deviceModel").combobox("setValue", item['device.deviceModel.devModelUuid']);
    $("#deviceModel").combobox("setText", item['device.deviceModel.devModelName']);
    $("#sn").val(item.sn);
    $("#network").combobox("setValue", item['network.networkUuid']);
    $("#network").combobox("setText", item['network.networkName']);
    $("#addOrEditMcu").text("edit");
    $("#mcuInfoDialog").modal("show");
}
//查看mcu连接的设备
function viewMcuDevice(rowId) {
    $("#mcuDG").jqGrid('setSelection', rowId);
    var item = $("#mcuDG").jqGrid('getRowData', rowId);
    //往mcu的设备连接信息表格里面填充数据
    var mcuUuid = item.mcuUuid;
    var dgData = [{ moduleNum: 1, pointNum: 1 }, { moduleNum: 1, pointNum: 2 }, { moduleNum: 1, pointNum: 3 }, { moduleNum: 1, pointNum: 4 }, { moduleNum: 1, pointNum: 5 }, { moduleNum: 1, pointNum: 6 }, { moduleNum: 1, pointNum: 7 }, { moduleNum: 1, pointNum: 8 }, { moduleNum: 2, pointNum: 1 }, { moduleNum: 2, pointNum: 2 }, { moduleNum: 2, pointNum: 3 }, { moduleNum: 2, pointNum: 4 }, { moduleNum: 2, pointNum: 5 }, { moduleNum: 2, pointNum: 6 }, { moduleNum: 2, pointNum: 7 }, { moduleNum: 2, pointNum: 8 }, { moduleNum: 3, pointNum: 1 }, { moduleNum: 3, pointNum: 2 }, { moduleNum: 3, pointNum: 3 }, { moduleNum: 3, pointNum: 4 }, { moduleNum: 3, pointNum: 5 }, { moduleNum: 3, pointNum: 6 }, { moduleNum: 3, pointNum: 7 }, { moduleNum: 3, pointNum: 8 }, { moduleNum: 4, pointNum: 1 }, { moduleNum: 4, pointNum: 2 }, { moduleNum: 4, pointNum: 3 }, { moduleNum: 4, pointNum: 4 }, { moduleNum: 4, pointNum: 5 }, { moduleNum: 4, pointNum: 6 }, { moduleNum: 4, pointNum: 7 }, { moduleNum: 4, pointNum: 8 }];
    //获得工程下所有需要自动采集的设备
    $.post("/smosplat/getAutoDevicesByCurrentProject", {}, function(data, status) {
        var allDevices = JSON.parse(data).rows;
        for (var i = 0; i < allDevices.length; i++) {
            var moduleNum = allDevices[i].moduleNum;
            var pointNum = allDevices[i].pointNum;
            //如果没指定mcu，则略过
            if ((allDevices[i].mcu == undefined) || (allDevices[i].mcu == null)) {
                continue;
            }
            var tempMcuUuid = allDevices[i].mcu.mcuUuid;
            for (var j = 0; j < dgData.length; j++) {
                if ((dgData[j].moduleNum == moduleNum) && (dgData[j].pointNum == pointNum) && (mcuUuid == tempMcuUuid)) {
                    dgData[j]["deviceUuid"] = allDevices[i].deviceUuid;
                    dgData[j]["deviceSN"] = allDevices[i].sn;
                    dgData[j]["deviceModel"] = allDevices[i].deviceModel.devModelName;
                    dgData[j]["deviceType"] = allDevices[i].devType.devTypeName;
                }
            }
        }
        $.jgrid.gridUnload('deviceInfoDG');
        $("#deviceInfoDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: dgData,
            colNames: ['模块号', '通道号', '设备uuid', '设备名称（编号）', '设备类型', '设备型号'],
            colModel: [{
                name: 'moduleNum',
                align: "center"
            }, {
                name: 'pointNum',
                sorttype: "float",
                align: "center"
            }, {
                name: 'deviceUuid',
                hidden: true
            }, {
                name: 'deviceSN',
                align: "center"

            }, {
                name: 'deviceType',
                align: "center"

            }, {
                name: 'deviceModel',
                align: "center"

            }],
            autowidth: true,
            viewrecords: true,
            gridview: true
        });
        $("#deviceInfoDG").setGridHeight($("#deviceInfo-modal").height() - 135);
        $("#deviceInfoDG").setGridWidth($("#deviceInfo-modal").width() - 32);
        $("#deviceInfoDG").jqGrid('setSelection', '1');
        $("#deviceInfoDialog").modal("show");
    });
}
//删除mcu
function deleteMcu(rowId) {
    $("#mcuDG").jqGrid('setSelection', rowId);
    var mcuUuid = $("#mcuDG").jqGrid('getRowData', rowId).mcuUuid;
    //弹出提示框确认
    swal({
        title: "警告",
        text: "您确定要删除吗?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/deleteMcu", { mcuUuid: mcuUuid },function(data, status) {
                if (JSON.parse(data).result == 0) {
                	//刷新本地数据
                	$("#mcuDG").jqGrid('delRowData', rowId);
                    swal("删除成功！", "您已经删除了该mcu。", "success");
                } else {
                	swal("删除失败！",JSON.parse(data).msg,"error");
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    });
}
//测试mcu
function testMcu(rowId) {
	var mcuUuid = $("#mcuDG").jqGrid('getRowData', rowId).mcuUuid;
    //获得项目采集状态
    $.get("/smosplat/getProjectCollectStatus",function(data,status){
    	var jsonObj=JSON.parse(data);
    	if(jsonObj.result==0){
    		if(jsonObj.collectStatus==0){
    			swal({title:"测试mcu必须先停止采集！",type:"warning"});
    		}else if(jsonObj.collectStatus==-1){
    			$.post("/smosplat/testMcu", { mcuUuid: mcuUuid },function(data, status) {});	
        		$("#callCollectBtn").attr({"disabled":"disabled"});
        		$("#startBtn").attr({"disabled":"disabled"});
                stopTimeout=setTimeout(function(){
                	$("#startBtn").removeAttr("disabled");
                	$("#callCollectBtn").removeAttr("disabled");
                	$("#callCollectBtn").text("点击开始召测采集(采集一次)！");
                }, 1000*90);
    		}
    	}else{
    		swal({title:"获取项目采集状态失败！",text:jsonObj.msg,type:"error"});
    	}
    });
}
