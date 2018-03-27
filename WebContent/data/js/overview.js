/**
 * 获得权限数据
 */
var roleItemData;
var infoNames1 = new Array();
var infoModel1 = new Array();
var infoNames2 = new Array();
var infoModel2 = new Array();
var lastmtDatas = new Array();

var sitePicNames = new Array();
var sitePicModel = new Array();
//所有的现场图片路径
var allSitePicUrls;
//项目ID
var projectUuid;
//安全状态
var warningInfo;
//定义三种状态
var IsControl;
var IsEarly;
var IsWarning;
infoNames1 = ['监测项Id', '监测项目', '点号', '最大值(mm)', '点号', '最大值(mm/d)', '报警值', '允许值(mm)', '变化速率(mm/d)', '报警情况'];
infoModel1 = [{
    name: 'code',
    align: "center",
    hidden: true
}, {
    name: 'MonitorItem',
    align: "center",
    cellattr: addCellAttr
}, {
    name: 'maxAccumOffsetCode',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'maxAccumOffset',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'maxChangeRateCode',
    align: 'center',
    width:60,
    formatter: formatIsNull
}, {
    name: 'maxChangeRate',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'warnAccum',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'controlAccum',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'warnSingleRate',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'safe',
    align: "center",
    width:60,
    formatter: formatWarningInfo
}];
infoNames2 = ['监测项Id', '监测项目', '点号', '最大值(kN)', '点号', '变化速率(kN/d)', '报警值(kN)', '设计值(kN)','报警情况'];
infoModel2 = [{
    name: 'code',
    align: "center",
    hidden: true
}, {
    name: 'MonitorItem',
    align: "center",
    cellattr: addCellAttr
}, {
    name: 'maxCalValCode',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'maxCalVal',
    align: "center",
    width:60,
    formatter:formatIsNull
}, {
    name: 'maxChangeRateCode',
    align: 'center',
    width:60,
    formatter: formatIsNull
}, {
    name: 'maxChangeRate',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'warnAccum',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'controlAccum',
    align: "center",
    width:60,
    formatter: formatIsNull
}, {
    name: 'safe',
    align: "center",
    width:60,
    formatter: formatWarningInfo
}];
var accumColNames = ['监测项code', '测点ID', '测点', '累计变化值', '累计变化报警值', '累计变化允许值', '变化速率', '变化速率报警值', '处理'];
var accumColModel = [{
    name: 'code',
    hidden: true
}, {
    name: 'PointsUuid',
    hidden: true
}, {
    name: 'accumPoints',
    align: "center",
    width:100
}, {
    name: 'accumChangeOffset',
    align: "center",
    formatter: formatViewAccumInfo
}, {
    name: 'accumWarningOffset',
    align: "center",
}, {
    name: 'accumControlOffset',
    align: "center",
}, {
    name: 'changeRateOffset',
    align: "center",
    formatter: formatViewChangeRateInfo
}, {
    name: 'warningChangeRate',
    align: "center",
}, {
    name: 'dealInfo',
    align: "center",
    formatter: formatDealAccumInfo,
    width:100
}];
//获取权限信息
$.ajax({
    type: "post",
    url: "/smosplat/getRoleItemsFromSession",
    async: false,
    success: function(data) {
        roleItemData = JSON.parse(data);
    }
});
//判断是否具有相应权限
function hasRoleItem(number) {
    for (var i = 0; i < roleItemData.roleItems.length; i++) {
        if (roleItemData.roleItems[i].number == number) {
            return true;
        }
    }
    return false;
}
//判断累计数组
function getAccumIndex(name) {
    for (var i = 0; i < accumColNames.length; i++) {
        if (accumColNames[i] == name) {
            return i;
        }
    }
    return -1;
}
//判断单次变化率数组
//function getGapIndex(name) {
//    for (var i = 0; i < gapColNames.length; i++) {
//        if (gapColNames[i] == name) {
//            return i;
//        }
//    }
//    return -1;
//}

function setView() {
    //返回-1是超级管理员
    if (roleItemData.result == -1) {
        //超级管理员所有界面元素都是可用的
    } else {
        //判断是否能处理累计变化值
        if (!hasRoleItem(601)) {
            var index = getAccumIndex("处理");
            accumColNames.splice(index, 1);
            accumColModel.splice(index, 1);
        }
        //判断是否能处理单次变化率
        //        if (!hasRoleItem(602)) {
        //            var index = getGapIndex("处理");
        //            gapColNames.splice(index, 1);
        //            gapColModel.splice(index, 1);
        //        }
    }
}
$(function() {
    //根据权限控制界面元素
    setView();
    $.MyCommon.PageLoading({ loadingTips: '加载中,请稍候...' });
    //数据导出
    $("#dataExportButton").bind("click", function() {
        $.post("/smosplat/getCurrentProjectInfo", function(data, status) {
            var projectName = JSON.parse(data).project.projectName;
            var projectAddress = JSON.parse(data).project.address;
            $("#projectName").val(projectName);
            $("#projectAddress").val(projectAddress);
        });
        $("#dataExportDialog").modal("show");
    });
    $("#saveExportBtn").bind("click", function() {
        var pjName = $("#projectName").val();
        var pjAddress = $("#projectAddress").val();
        var deviceName=$("#deviceName").val();
        var bDate = $("#beginDate").datetimebox('getValue');
        var eDate = $("#endDate").datetimebox('getValue');
        var specification = $("#specification").val();
        $("#dataExportDialog").modal("hide");
        //导出报表
        window.open("/smosplat/spDataExport?pjName=" + pjName + "&pjAddress=" + pjAddress +"&deviceName="+deviceName+ "&specification=" + specification + "&bDateStr=" + bDate + "&eDateStr=" + eDate);
    });
    $.post("/smosplat/getCurrentProjectInfo", function(data, status) {
        projectUuid = JSON.parse(data).project.projectUuid;
        //获得所有监测项的安全状态
        $.post("getWarningData", { projectUuid: projectUuid }, function(data, status) {
            warningInfo = JSON.parse(data);
            //获得所有监测项的基本数据
            $.post("getLimitAccumOffsetAndBiggestChangeRate", { projectUuid: projectUuid }, function(data, status) {
                var projectInfo = JSON.parse(data);
                $('#loadingPage').remove();
                $.post("getMonitorItemsByProject", function(data, status) {
                    var allMonitorItems = JSON.parse(data).monitorItems;
                    var mainInfo1 = new Array();
                    var mainInfo2 = new Array();
                    for (var i = 0; i < allMonitorItems.length; i++) {
                        //根据编号拿到不同编号的数据
                    	if(allMonitorItems[i].code!="MT"&&allMonitorItems[i].code!="ZC"){
                    		var projectDatas1 = projectInfo[allMonitorItems[i].code];
                            var lastData1 = projectDatas1;
                            if (lastData1 == undefined) {
                                lastData1 = new Object;
                            }
                            lastData1["code"] = allMonitorItems[i].code;
                            lastData1["MonitorItem"] = allMonitorItems[i].monitorItemName;
                            mainInfo1.push(lastData1);
                    	}else{ 
                    		var projectDatas2 = projectInfo[allMonitorItems[i].code];
                            var lastData2 = projectDatas2;
                            if (lastData2 == undefined) {
                                lastData2 = new Object;
                            }
                            lastData2["code"] = allMonitorItems[i].code;
                            lastData2["MonitorItem"] = allMonitorItems[i].monitorItemName;
                            mainInfo2.push(lastData2);
                    	}
                    }
                    $("#mainInfo1").jqGrid({
                        datatype: "local",
                        styleUI: 'Bootstrap',
                        data: mainInfo1,
                        colNames: infoNames1,
                        colModel: infoModel1,
                        autowidth: true,
                        sortname: 'id',
                        viewrecords: true,
                        gridview: true
                    });
                    jQuery("#mainInfo1").jqGrid('setGroupHeaders', {
                        useColSpanStyle: false,
                        groupHeaders: [
                            { startColumnName: 'maxAccumOffsetCode', numberOfColumns: 2, titleText: '累积变化' },
                            { startColumnName: 'maxChangeRateCode', numberOfColumns: 2, titleText: '变化速率' },
                            { startColumnName: 'warnAccum', numberOfColumns: 3, titleText: '报警指标' }
                        ]
                    });
                    $("#mainInfo1").setGridHeight($("#infoGrid1").height() -110);
                    $("#mainInfo1").jqGrid('setSelection', "1");
                    $("#mainInfo2").jqGrid({
                        datatype: "local",
                        styleUI: 'Bootstrap',
                        data: mainInfo2,
                        colNames: infoNames2,
                        colModel: infoModel2,
                        autowidth: true,
                        sortname: 'id',
                        viewrecords: true,
                        gridview: true
                    });
                    jQuery("#mainInfo2").jqGrid('setGroupHeaders', {
                        useColSpanStyle: false,
                        groupHeaders: [
                            { startColumnName: 'maxCalValCode', numberOfColumns: 2, titleText: '本次内力值' },
                            { startColumnName: 'maxChangeRateCode', numberOfColumns: 2, titleText: '变化速率' },
                            { startColumnName: 'warnAccum', numberOfColumns: 3, titleText: '报警指标' }
                        ]
                    });
                    $("#mainInfo2").setGridHeight($("#infoGrid2").height() - 100);
                    $("#mainInfo2").jqGrid('setSelection', "1");
                });
            });
        });
        $.post("getProjectPicUrls",{ projectUuid: projectUuid },function(data,status){
        	var jsonData = JSON.parse(data);
        	allSitePicUrls = jsonData.sitePic;
//        	if(allSitePicUrls == undefined ||　allSitePicUrls.length == 0){
//        		return;
//        	}
        	$.post("getAllSitePicByProjectUuid",function(data,status){
        		var json = JSON.parse(data);
        		var allSitePics = json.sitePics;
            	var sitePicInfo = new Array();
                for (var i = 0; i < allSitePics.length; i++) {
                	var lastData = new Object();
                    lastData["sitePicUuid"] = allSitePics[i].sitePicUuid;
                    lastData["sitePicName"] = allSitePics[i].picName;
                    lastData["time"] = allSitePics[i].picName.substring(0, allSitePics[i].picName.lastIndexOf('.')); 
                    lastData["description"] = allSitePics[i].description;
                    sitePicInfo.push(lastData);
                }
                //进来先显示第一张图片
                $("#sitePicContent").attr("src",allSitePicUrls[0]);
                $("#sitePicInfo").jqGrid({
                    datatype: "local",
                    styleUI: 'Bootstrap',
                    data: sitePicInfo,
                    colNames: sitePicNames,
                    colModel: sitePicModel,
                    autowidth: true,
                    sortname: 'id',
                    viewrecords: true,
                    gridview: true
                });
                $("#sitePicInfo").setGridHeight(200);
                $("#sitePicInfo").setGridWidth($("#infoGrid1").width());
                $("#sitePicInfo").jqGrid('setSelection', "1");
        	});

        });
        
    });

})

function addCellAttr(rowId, val, rawObject, cm, rdata) {
    return "style='color:#1B87B8'";
}

function formatWarningInfo(cellvalue, options, rowObject) {
	warningObject = warningInfo[rowObject.code];
    var nullValue=Math.pow(10,10);
    //当报警值有值时，显示红色
    var code = rowObject.code;
    if (warningObject != undefined) {
    	if(rowObject.code!="MT"&&rowObject.code!="ZC"){
    		if (warningObject.accumControlWarningPoints.length != 0) {
                IsControl = true;
             //判断如果控制值数组长度为0.或者数组里全都是nullValue，则为false
            } else if (warningObject.accumControlWarningPoints.length == 0) {
                IsControl = false;
            }  if (warningObject.accumWarningPoints.length ==0&& warningObject.gapChangeRateWarningPoints.length == 0) {
                IsWarning = false;
            }//如果控制值数组等于报警值数组。报警不显示，只显示控制值
            else if(warningObject.accumWarningPoints.length==warningObject.accumControlWarningPoints.length && warningObject.gapChangeRateWarningPoints.length == 0){
            	IsWarning = false;
            }else if (warningObject.accumWarningPoints.length != 0||warningObject.gapChangeRateWarningPoints.length != 0 ) {
                IsWarning = true;
            }
    	}else {
    		if (warningObject.accumControlPoints.length != 0) {
                IsControl = true;
             //判断如果控制值数组长度为0
            } else if (warningObject.accumControlPoints.length == 0) {
                IsControl = false;
            } if (warningObject.accumWarningPoints.length ==0 && warningObject.calValWarningPoints.length == 0) {
                IsWarning = false;
            }//如果控制值数组等于报警值数组。报警不显示，只显示控制值
            else if(warningObject.accumControlPoints.length==warningObject.accumWarningPoints.length && warningObject.calValWarningPoints.length == 0){
            	IsWarning = false;
            }else if (warningObject.accumWarningPoints.length != 0||warningObject.calValWarningPoints.length != 0  ) {
                IsWarning = true;
            }
    	}
    	if (IsControl == false && IsWarning == false) {
            return '<img src="/smosplat/common/image/green.png"  style="width:12px;height:12px;margin-bottom:2px;margin-right:5px;">'+"<a href='javascript:void(0);'  onclick=\"getSafeInfo('" + code + "')\"><img src='/smosplat/common/image/view.png' style='width:16px;height:16px;margin-bottom:2px;'>" + "&nbsp详情" + "</a>";
        } else if (IsControl == false && IsWarning == true ) {
            return "<img src='/smosplat/common/image/yellow.gif' style='width:12px;height:12px;margin-bottom:2px;margin-right:5px;'>"+"<a href='javascript:void(0);'  onclick=\"getSafeInfo('" + code + "')\"><img src='/smosplat/common/image/view.png' style='width:16px;height:16px;margin-bottom:2px;'>" + "&nbsp详情" + "</a>";
        } else if (IsControl == true && IsWarning == false) {
            return "<img src='/smosplat/common/image/red.gif' style='width:12px;height:12px;margin-bottom:2px;margin-right:5px;'>"+"<a href='javascript:void(0);'  onclick=\"getSafeInfo('" + code + "')\"><img src='/smosplat/common/image/view.png' style='width:16px;height:16px;margin-bottom:2px;'>" + "&nbsp详情" + "</a>";
        } else if (IsControl == true && IsWarning == true) {
            return "<img src='/smosplat/common/image/yellow.gif' style='width:12px;height:12px;margin-bottom:2px;margin-right:5px;'>" + "<img src='/smosplat/common/image/red.gif' style='width:12px;height:12px;margin-bottom:2px;margin-right:5px;'>"+"<a href='javascript:void(0);'  onclick=\"getSafeInfo('" + code + "')\"><img src='/smosplat/common/image/view.png' style='width:16px;height:16px;margin-bottom:2px;'>" + "&nbsp详情" + "</a>";
        }
    } else {
        return "/";
    }
}
function formatView(cellvalue, options, rowObject) {
	var name = rowObject.sitePicName;
    return "<a href='javascript:void(0);'  onclick=\"viewPic('" + name + "')\"><img src='/smosplat/common/image/view.png' style='width:16px;height:16px;margin-bottom:2px;'>" + "&nbsp查看" + "</a>";
}

function viewPic(name){
	for(var i = 0; i< allSitePicUrls.length; i++){
		if(allSitePicUrls[i].indexOf(name)!=-1){
			$("#sitePicContent").attr("src",allSitePicUrls[i]);
		}
	}
}

//弹出所选监测项
function getSafeInfo(code) {
    //根据number值判断是预警报警还是控制值
    $.post("getWarningData", { projectUuid: projectUuid }, function(data, status) {
        var allData = JSON.parse(data);
        //根据监测项取不同数据
        var warningInfo = allData[code];
        if (warningInfo == undefined) {
            return;
        }
        //定义累计变化数据源
        var accumDataArray = [];
        for (var i = 0; i < warningInfo.code.length; i++) {
            accumDataArray.push({
                'code': code,
                'PointsUuid': warningInfo.pointsExpect2Process[warningInfo.code[i]],
                'accumPoints': warningInfo.code[i],
                'accumChangeOffset': warningInfo.accumWarningOffset[i],
                'accumWarningOffset': warningInfo.accumWarningVal[i],
                'accumControlOffset': warningInfo.accumControlWarningVal[i],
                'changeRateOffset': warningInfo.gapChangeRateWarningOffset[i],
                'warningChangeRate': warningInfo.gapChangeRateWarningVal[i]
            });
        }
        //清空数据
        $.jgrid.gridUnload('safeInfoDG');
        $("#safeInfoDG").jqGrid({  
            datatype: "local",
            styleUI: 'Bootstrap',
            data: accumDataArray,
            colNames: accumColNames,
            colModel: accumColModel,
            sortname: 'id',
            autowidth: true,
            viewrecords: true,
            gridview: true
        });
        $("#safeInfoDG").setGridHeight($("#safeInfo-modal").height() - 120);
        $("#safeInfoDG").setGridWidth(680);
        $("#safeInfoDG").jqGrid('setSelection', "1");
        $("#safeInfoDialog").modal("show");
    });
}

function formatViewAccumInfo(cellvalue, options, rowObject) {
    var nullValue=Math.pow(10,10);
    if(cellvalue==nullValue||cellvalue == undefined||cellvalue==''){
		return "/";
	}
    if (cellvalue > rowObject.accumControlOffset) {
        return cellvalue + '<img src="/smosplat/common/image/red.gif"  style="width:12px;height:12px;margin-bottom:2px;margin-left:5px;">';
    } else if (cellvalue > rowObject.accumWarningOffset) {
        return cellvalue + '<img src="/smosplat/common/image/yellow.gif"  style="width:12px;height:12px;margin-bottom:2px;margin-left:5px;">';
    } else {
        return cellvalue + '<img src="/smosplat/common/image/green.png"  style="width:12px;height:12px;margin-bottom:2px;margin-left:5px;">';
    }
}
function formatViewChangeRateInfo(cellvalue,options,rowObject){
    var nullValue=Math.pow(10,10);
    if(cellvalue==nullValue||cellvalue == undefined){
		return "/";
	}
    if (cellvalue > rowObject.warningChangeRate) {
        return cellvalue + '<img src="/smosplat/common/image/yellow.gif"  style="width:12px;height:12px;margin-bottom:2px;margin-left:5px;">';
    }else {
        return cellvalue + '<img src="/smosplat/common/image/green.png"  style="width:12px;height:12px;margin-bottom:2px;margin-left:5px;">';
    }
}
//删除超限值
function formatDealAccumInfo(cellvalue, options, rowObject) {
    var point = rowObject.PointsUuid;
    var code = rowObject.code;
    return "<a href='javascript:void(0);'  onclick=\"deleteInfo('" + point + "','" + code + "','" + options.rowId + "')\">" + "&nbsp处理" + "</a>";
}
//时间转换
function formateTimestamp(cellvalue, options, rowObject) {
    return timestamp2String(rowObject.time);
};

function deleteInfo(point, code, rowId) {
    $.post("processPoints", { point: point, flag: code }, function(data, status) {
        if (JSON.parse(data).result == 0) {
            //刷新本地数据
            $("#safeInfoDG").jqGrid('delRowData', rowId);
            swal("处理成功！", "您已经处理了该监测点。", "success");
        } else {
            swal("处理失败！", JSON.parse(data).msg, "error");
        }
    });
}

//删除现场图片
function formatDelSitePic(cellvalue, options, rowObject) {
    var name = rowObject.sitePicName;
    var sitePicUuid = rowObject.sitePicUuid;
    return "<a href='javascript:void(0);'  onclick=\"deleteSitePic('" + name + "','" + sitePicUuid + "','" + options.rowId +  "')\"><img src='/smosplat/common/image/delete.png' style='width:16px;height:16px;margin-bottom:2px;'>" + "&nbsp删除" + "</a>";
}

//删除现场图片
function deleteSitePic(name, sitePicUuid, index) {
	var src;
	for(var i = 0; i< allSitePicUrls.length; i++){
		if(allSitePicUrls[i].indexOf(name)!=-1){
			src = allSitePicUrls[i];
			break;
		}
	}
	$.post("delSitePicById", {sitePicUuid:sitePicUuid},function(data,status){
		var jsonR = JSON.parse(data);
		if(jsonR.result == 0){
		    $.post("deleteProjectOtherFiles", { projectUuid: projectUuid, src: src }, function(data, status) {
		        if (JSON.parse(data).result == 0) {
		            //刷新本地数据
		        	$("#sitePicContent").attr("src","");
		        	$("#sitePicInfo").jqGrid('delRowData', index);
		            swal("处理成功！", "您已经删除了该图片。", "success");
		        } else {
		            swal("处理失败！", JSON.parse(data).msg, "error");
		        }
		    });
		}else{
			swal("处理失败！", jsonR.msg, "error");
		}
	});

}

function formatIsNull(cellvalue, options, rowObject) {
    if (cellvalue == undefined||cellvalue=="") {
        return "/";
    } else {
        return cellvalue;
    }
}
sitePicNames = ['现场图片ID', '文件名', '上传时间','描述','查看','删除'];
sitePicModel = [{
    name: 'sitePicUuid',
    align: "center",
    hidden: true
}, {
    name: 'sitePicName',
    align: "center",
    cellattr: addCellAttr
},{
    name: 'time',
    align: "center",
    formatter: formateTimestamp
},{
    name: 'description',
    align: "center",
},{
    name: 'view',
    align: "center",
    formatter: formatView
},{
    name: 'delete',
    align: "center",
    formatter: formatDelSitePic
}];