/**
 * 获得权限数据
 */
var roleItemData;
//安全状态
var warningInfo;
//定义三种状态
var IsControl;
var IsWarning;
var infoNames = ['监测项Id', '监测项目', '安全状态'];
var infoModel = [{
    name: 'code',
    align: "center",
    hidden: true
}, {
    name: 'MonitorItem',
    align: "center",
    cellattr: addCellAttr
}, {
    name: 'safe',
    align: "center",
    width: 100,
    formatter: formatSafeInfo,
}];
//地图自定义窗体
var opts;
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
    for (var i = 0; i < projectColNames.length; i++) {
        if (projectColNames[i] == name) {
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
        if (!hasRoleItem(502)) {
            $("#editProjectBtn").css("visibility", "hidden");
        }
        if (!hasRoleItem(503)) {
            $("#delProjectBtn").css("visibility", "hidden");
        }
        if (!hasRoleItem(505)) {
            var index = getColumnIndex("数据查看");
            projectColNames.splice(index, 1);
            projectColModel.splice(index, 1);
        }
    }
}
var map;
var scaleControl;
var navControl;
var overviewControl;
//获得当前选中的工程id
var projectID;
// 定义两个表格选中行数
var projectRowId;
//定义表格数组
var projectColNames = ['Uuid', '经度', '纬度', 'code', '工程地址', '支护形式', '基坑设计深度', '基坑周长', '监测负责人', '监测人员', '安全机构', '监督人员', '安全等级', '监督编号', '建设单位', '负责人', '电话', '联系人', '电话', '联系人', '电话', '设计单位', '负责人', '电话', '联系人', '电话', '联系人', '电话', '施工单位', '负责人', '电话', '联系人', '电话', '联系人', '电话', '监理单位', '负责人', '电话', '联系人', '电话', '联系人', '电话', '工程名称', '工程地址', '工程类别', '建设单位', '监督机构', '数据查看', '监测项'];
var projectColModel = [{
    name: 'projectUuid',
    hidden: true
}, {
    name: 'lon',
    hidden: true
}, {
    name: 'lat',
    hidden: true
}, {
    name: 'code',
    hidden: true
}, {
    name: 'address',
    hidden: true
}, {
    name: 'structure',
    hidden: true
}, {
    name: 'deep',
    hidden: true
}, {
    name: 'perimeter',
    hidden: true
}, {
    name: 'monitorLeader',
    hidden: true
}, {
    name: 'monitorWorker',
    hidden: true
}, {
    name: 'superviseCompany',
    hidden: true
}, {
    name: 'superviseWorker',
    hidden: true
}, {
    name: 'safeLevel',
    hidden: true
}, {
    name: 'superviseCode',
    hidden: true
}, {
    name: 'buildCompany',
    hidden: true
}, {
    name: 'buildLeaderName',
    hidden: true
}, {
    name: 'buildLeaderPhone',
    hidden: true
}, {
    name: 'buildContactName',
    hidden: true
}, {
    name: 'buildContactPhone',
    hidden: true
}, {
    name: 'buildContactName1',
    hidden: true
}, {
    name: 'buildContactPhone1',
    hidden: true
}, {
    name: 'designCompany',
    hidden: true
}, {
    name: 'designLeaderName',
    hidden: true
}, {
    name: 'designLeaderPhone',
    hidden: true
}, {
    name: 'designContactName',
    hidden: true
}, {
    name: 'designContactPhone',
    hidden: true
}, {
    name: 'designContactName1',
    hidden: true
}, {
    name: 'designContactPhone1',
    hidden: true
}, {
    name: 'constructCompany',
    hidden: true
}, {
    name: 'constructLeaderName',
    hidden: true
}, {
    name: 'constructLeaderPhone',
    hidden: true
}, {
    name: 'constructContactName',
    hidden: true
}, {
    name: 'constructContactPhone',
    hidden: true
}, {
    name: 'constructContactName1',
    hidden: true
}, {
    name: 'constructContactPhone1',
    hidden: true
}, {
    name: 'supervisorCompany',
    hidden: true
}, {
    name: 'supervisorLeaderName',
    hidden: true
}, {
    name: 'supervisorLeaderPhone',
    hidden: true
}, {
    name: 'supervisorContactName',
    hidden: true
}, {
    name: 'supervisorContactPhone',
    hidden: true
}, {
    name: 'supervisorContactName1',
    hidden: true
}, {
    name: 'supervisorContactPhone1',
    hidden: true
}, {
    name: 'projectName',
    align: "center",
    width: 180
}, {
    name: 'address',
    align: "center"
}, {
    name: 'projectType',
    formatter: formatType,
    align: "center",
    width: 70
},{
    name: 'buildCompany',
    align: "center"
}, {
    name: 'superviseCompany',
    align: "center"
}, {
    name: 'view',
    formatter: formatView,
    align: "center",
    width: 100
}, {
    name: 'projectMonitorItems',
    hidden: true
}];
$(function() {
    //根据权限控制界面元素
    setView();
    //初始化地图
    //设置初始地图显示位置
    var left = $("#mainDiv").width();
    var top = $("#eastTopDiv").height();
    $("#mapDialog").dialog("move", {
        left: left,
        top: top
    });
    
    $("#add-modal").height($("#mainDiv").height());
    $("#add-modal").width($(window).width());
    $("#view-modal").height($("#mainDiv").height());
    $("#view-modal").width($(window).width());
    createMap(); //创建地图
    setMapEvent(); //设置地图事件
    addMapControl(); //向地图添加控件
    //加载数据
    //初始化datagrid
    getProjects();
    //查看工程
    $("#viewOrganBtn").bind('click', function() {
    	window.top.location.href = "organIndex";
    })
    //修改工程
    $("#editProjectBtn").bind('click', function() {
    	var rowId = $('#projectDG').jqGrid('getGridParam', 'selrow');
        var selectedRow = $("#projectDG").jqGrid('getRowData', rowId);
        if (selectedRow == null) {
            swal({ title: "没有选中记录！", type: "warning" });
            return;
        };
    	$.post("/smosplat/setCurrentProject", { projectUuid: selectedRow.projectUuid }, function(data, status) {
            if (JSON.parse(data).result == 0) {
            	window.location.href = "projectSetting"+"?type=change";
            }
        });
    });
    //删除工程
    $("#delProjectBtn").bind('click', function() {
        var rowId = $('#projectDG').jqGrid('getGridParam', 'selrow');
        var selectedRow = $("#projectDG").jqGrid('getRowData', rowId);
        if (selectedRow == null) {
            swal({ title: "没有选中记录！", type: "warning" });
            return;
        };
        //弹出提示框确认
        swal({
            title: "警告",
            text: "您确定要删除该项目吗?",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "是的，我要删除！",
            cancelButtonText: "让我再考虑一下…",
            closeOnConfirm: false,
            closeOnCancel: false
        }, function(isConfirm) {
            if (isConfirm) {
                $.post("/smosplat/deleteProject", { projectUuid: selectedRow.projectUuid }, function(data, status) {
                    if (JSON.parse(data).result == 0) {
                        swal("删除成功！", "您已经删除了该项目。", "success");
                        getProjects();
                    } else {
                        swal("删除失败！", JSON.parse(data).msg, "error");
                    }
                });
            } else {
                swal("已取消", "您取消了删除操作！", "error")
            }
        });
    });
});

function createMap() {
    map = new BMap.Map("map");
    map.centerAndZoom(new BMap.Point(113.33, 23.33), 14);
}
//地图加载完成调用的事件,得到选中的行数，然后移除地图加载的监听事件
function getRow() {
    $("#listDG").jqGrid('setSelection', projectRowId);
    $("#projectDG").jqGrid('setSelection', projectRowId);
    map.removeEventListener("tilesloaded", getRow);
}

function setMapEvent() {
    map.enableScrollWheelZoom();
    map.disableKeyboard();
    map.enableDragging();
    map.disableDoubleClickZoom();
}

//向地图添加控件
function addMapControl() {
    //左下角控件
    scaleControl = new BMap.ScaleControl({
        anchor: BMAP_ANCHOR_BOTTOM_LEFT
    });
    scaleControl.setUnit(BMAP_UNIT_IMPERIAL);
    map.addControl(scaleControl);
    navControl = new BMap.NavigationControl({
        anchor: BMAP_ANCHOR_TOP_LEFT,
        type: 0
    });
    map.addControl(navControl);
    navControl.hide();
    overviewControl = new BMap.OverviewMapControl({
        anchor: BMAP_ANCHOR_BOTTOM_RIGHT,
        isOpen: false
    });
    map.addControl(overviewControl);
}


function addMapOverlay(projectData) {
    //添加之前清空已添加的
    map.clearOverlays();
    for (var i = 0; i < projectData.length; i++) {
        if (projectData[i].warningStatus == "0"||projectData[i].warningStatus == ""||projectData[i].warningStatus == null) {
            var marker = new BMap.Marker(new BMap.Point(projectData[i].lon, projectData[i].lat), {
                icon: new BMap.Icon("/smosplat/common/image/flag_mark_green.png", new BMap.Size(20, 32), {
//                    imageOffset: new BMap.Size(0, 3)
                })
            });
        }if (projectData[i].warningStatus == "1") {
            var marker = new BMap.Marker(new BMap.Point(projectData[i].lon, projectData[i].lat), {
                icon: new BMap.Icon("/smosplat/common/image/flag_mark_yellow.png", new BMap.Size(20, 32), {
                })
            });
        }if (projectData[i].warningStatus == "2") {
            var marker = new BMap.Marker(new BMap.Point(projectData[i].lon, projectData[i].lat), {
                icon: new BMap.Icon("/smosplat/common/image/flag_mark_red.png", new BMap.Size(20, 32), {
                })
            });
        }
        //得到工程名字和当前工程顺序
        var content = projectData[i].projectName;
        var id = i;
        var projectUuid = projectData[i].projectUuid;
        map.addOverlay(marker);
        addClickHandler(content, marker, id, projectUuid);
    }
    // 当小表格没选中时，外表格默认选中第一行
    $("#projectDG").jqGrid('setSelection', '1');

}
// 添加点击图标事件
function addClickHandler(content, marker, id, projectUuid) {
    marker.addEventListener("click", function(e) {
        //取得当前选中的工程id
        projectID = projectUuid;
        openInfo(content, id, e);
    });
}
//添加信息窗口，展示当前选中的是哪个项目
function openInfo(content, id, e) {
    var p = e.target;
    var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
    opts="<table id='tableHeader' style='margin-left:-10px;'>" + "<tr>" + "<th style='width:200px;'>" + content + "<a  class='btn btn-danger btn-xs' id='viewProecjt' onclick='viewProject(projectID);' style='float:right;margin:0 5px 0 10px;'>&nbsp查看</a>" + "</th>" + "</tr>" + "</table>";
    var infoWindow = new BMap.InfoWindow(opts); // 创建信息窗口对象 
    map.openInfoWindow(infoWindow, point); //开启信息窗口
    //通过传入的当前marker选中顺序确定表格行，并选中当前行
    $("#projectDG").jqGrid('setSelection', id + 1, false);
    $("#listDG").jqGrid('setSelection', id + 1, false);

}
/**
 * 查询工程的数据用于在datagrid里面显示
 * @param params
 */
function getProjects(params) {
    if (params == null) {
        params = {};
    }
    $.post("/smosplat/getProjects", params,
        function(data, status) {
            //整理一下数据再放到datagrid中显示
            var allData = JSON.parse(data);
            if (allData.length == 0) {
                return;
            }
            //每次加载时清空一次表格
            $.jgrid.gridUnload("projectDG");
            $("#projectDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                data: allData.rows,
                colNames: projectColNames,
                colModel: projectColModel,
                autowidth: true,
                rowNum: 100000,
                pager: "#pager_projectDG",
                sortname: 'id',
                viewrecords: true,
                gridview: true,
                onSelectRow: function(rowId) { //单击选择行
                    if (rowId == null) {
                        return;
                    } else {
                        var rowData = $('#projectDG').jqGrid('getRowData', rowId);
                        projectRowId = rowId;
                        projectID = rowData.projectUuid;
                        //点击时切换显示气泡
                        showInfo(rowData.projectName, rowData.lon, rowData.lat);
                        //显示对应的监测项
                        var projectUuid = rowData.projectUuid;
                        for (var i = 0; i < allData.rows.length; i++) {
                            if (projectUuid == allData.rows[i].projectUuid) {
                                var tempData = allData.rows[i];
                                var projectMonitorItems = tempData.projectMonitorItems;
                                //获得所有监测项的安全状态
                                $.post("getWarningData", { projectUuid: projectUuid }, function(data, status) {
                                    warningInfo = new Object;
                                    warningInfo = JSON.parse(data);
                                    //通过当前选中的项目获取监测项信息
                                    $.post("getLimitAccumOffsetAndBiggestChangeRate", { projectUuid: projectUuid }, function(data, status) {
                                        var projectInfo = JSON.parse(data);
                                        var allMonitorItems = projectMonitorItems;
                                        var mainInfo = new Array();
                                        for (var i = 0; i < allMonitorItems.length; i++) {
                                            //根据编号拿到不同编号的数据
                                            var projectDatas = projectInfo[allMonitorItems[i].monitorItem.code];
                                            var lastData = projectDatas;
                                            if (lastData == undefined) {
                                                lastData = new Object;
                                            }
                                            lastData["code"] = allMonitorItems[i].monitorItem.code;
                                            lastData["MonitorItem"] = allMonitorItems[i].monitorItem.monitorItemName;
                                            mainInfo.push(lastData);
                                        }
                                        $.jgrid.gridUnload('monitorItemDG');
                                        $("#monitorItemDG").jqGrid({
                                            datatype: "local",
                                            styleUI: 'Bootstrap',
                                            data: mainInfo,
                                            colNames: infoNames,
                                            colModel: infoModel,
                                            autowidth: true,
                                            sortname: 'id',
                                            viewrecords: true,
                                            gridview: true
                                        });
                                        $("#monitorItemDG").setGridHeight($("#jqGrid_monitor").height() - 70);
                                        $("#projectNameLabel").text(tempData.projectName);
                                        $("#monitorItemDG").jqGrid('setSelection', "1");
                                    });
                                });

                            }
                        }
                    }
                }
            });
            $("#projectDG").jqGrid('navGrid', '#pager_projectDG', { edit: false, add: false, del: false });
            $("#projectDG").setGridHeight($("#jqGrid_project").height() - 45);
            //外表格初始状态默认加载第一行
            addMapOverlay(allData.rows);
        }
    );
}

//表格点击时地图居中并且展示信息窗口的逻辑
function showInfo(name, lon, lat) {
    var point = new BMap.Point(lon, lat);
    opts="<table id='tableHeader' style='margin-left:-10px;'>" + "<tr>" + "<th style='width:200px;'>" + name + "<a  class='btn btn-danger btn-xs' id='viewProecjt' onclick='viewProject(projectID);' style='float:right;margin:0 5px 0 10px;'>&nbsp查看</a>" + "</th>" + "</tr>" + "</table>";
    var infoWindow = new BMap.InfoWindow(opts); // 创建信息窗口对象 
    map.openInfoWindow(infoWindow, point); //开启信息窗口
    map.centerAndZoom(new BMap.Point(lon, lat), 14);
}

function formatView(cellvalue, options, rowObject) {
    var projectUuid = rowObject.projectUuid;
    return '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="viewProject(\'' + projectUuid + '\')" href="#">数据查看</a>';
}

function formatMonitorItemStatus(cellvalue, options, rowObject) {
    //return '<img src="/smosplat/common/image/red.gif" style="width:12px;height:12px;margin-bottom:2px;">';
    return "未处理！";
}

function formatType(cellvalue, options, rowObject) {
    return cellvalue == 0 ? "基坑" : cellvalue;
}

function viewProject(projectUuid) {
    $.post("/smosplat/setCurrentProject", { projectUuid: projectUuid }, function(data, status) {
        if (JSON.parse(data).result == 0) {
            window.top.location.href = "dataIndex";
        }
    });
}

function addCellAttr(rowId, val, rawObject, cm, rdata) {
    return "style='color:#1B87B8;'";
}

function formatSafeInfo(cellvalue, options, rowObject) {
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
            } if (warningObject.accumWarningPoints.length ==0&& warningObject.gapChangeRateWarningPoints.length == 0) {
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
            } else if ( warningObject.accumControlPoints.length == 0) {
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
            return '<img src="/smosplat/common/image/green.png"  style="width:12px;height:12px;margin-bottom:2px;">';
        } else if (IsControl == false && IsWarning == true ) {
            return "<img src='/smosplat/common/image/yellow.gif' style='width:12px;height:12px;margin-bottom:2px;'>";
        } else if (IsControl == true && IsWarning == false) {
            return "<img src='/smosplat/common/image/red.gif' style='width:12px;height:12px;margin-bottom:2px;'>";
        } else if (IsControl == true && IsWarning == true) {
            return "<img src='/smosplat/common/image/yellow.gif' style='width:12px;height:12px;margin-bottom:2px;margin-right:5px;'>" + "<img src='/smosplat/common/image/red.gif' style='width:12px;height:12px;margin-bottom:2px;'>";
        }
    } else {
        return "/";
    }
}
//判断值是否为空
function formatIsNull(cellvalue, options, rowObject) {
    if (cellvalue == "" || cellvalue == undefined) {
        return "/";
    } else {
        return cellvalue;
    }
}
