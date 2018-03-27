var map;
var scaleControl;
var navControl;
var overviewControl;
//存取所有工程项
var allProject;
//自定义窗口
var opts;

$(function() {
	createMap(); //创建地图
    setMapEvent(); //设置地图事件
    addMapControl(); //向地图添加控件
    navControl.show();
    $.post("/smosplat/getProjects", function(data, status) {
        //整理一下数据再放到datagrid中显示
        allProject = JSON.parse(data).rows;
        if (allProject.length == 0) {
            return;
        }
        addMapOverlay(allProject);
        //表格获取所有数据
        getProject(allProject);
    });
    //点击机构管理按钮
    $('#organBtn').bind('click', function() {
    	window.top.location.href = "organIndex";
    });
    //点击工程列表
    $('#projectBtn').bind('click', function() {
        window.location.href = "/smosplat/projectList";
    });
    $(".floatWinRight").height($("#monitorBody").height() - 100);
    //点击增加工程
    $('#addProjectBtn').bind('click', function() {
        window.location.href = "projectSetting" + "?type=add";
    });
    //点击搜索按钮
    $('#filterProjectBtn').bind('click', function() {
        var proVal = $("#filterProjectInput").val();
        var selectData = [];
        for (var i = 0; i < allProject.length; i++) {
            if (allProject[i].projectName.indexOf(proVal) != -1) {
                selectData.push({
                    'projectUuid': allProject[i].projectUuid,
                    'lon': allProject[i].lon,
                    'lat': allProject[i].lat,
                    'projectName': allProject[i].projectName,
                    'address': allProject[i].address
                });
            }
        }
        getProject(selectData);
    });
    $("body").keyup(function(e) {
        if (e.keyCode == 13) {
            $("#filterProjectBtn").click();
        }
    });
});

function getProject(selectData) {
    addMapOverlay(selectData);
    $.jgrid.gridUnload("projectListDG");
    //地图上方显示项目列表浮动层
    $("#projectListDG").jqGrid({
        datatype: "local",
        styleUI: 'Bootstrap',
        data: selectData,
        colNames: ['Uuid', '经度', '纬度', '工程名称', '工程地址'],
        colModel: [{
            name: 'projectUuid',
            hidden: true
        }, {
            name: 'lon',
            hidden: true
        }, {
            name: 'lat',
            hidden: true
        }, {
            name: 'projectName',
            align: 'center'
        }, {
            name: 'address',
            align: 'center'
        }],
        rowNum: 100000,
        sortname: 'id',
        autowidth: true,
        viewrecords: true,
        gridview: true,
        onSelectRow: function(rowId) { //单击选择行
            if (rowId == null) {
                return;
            } else {
                var rowData = $('#projectListDG').jqGrid('getRowData', rowId);
                projectRowId = rowId;
                projectID = rowData.projectUuid;
                //点击时切换显示气泡
                showInfo(rowData.projectName, rowData.lon, rowData.lat);
            }
        }
    });
    $("#projectListDG").setGridHeight($(".floatWinRight").height() - 78);
    $("#projectListDG").setGridWidth($(".floatWinRight").width());
    $("#projectListDG").jqGrid('setSelection', 1);

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
//                    imageOffset: new BMap.Size(0, 3)
                })
            });
        }if (projectData[i].warningStatus == "2") {
            var marker = new BMap.Marker(new BMap.Point(projectData[i].lon, projectData[i].lat), {
                icon: new BMap.Icon("/smosplat/common/image/flag_mark_red.png", new BMap.Size(20, 32), {
//                    imageOffset: new BMap.Size(0, 3)
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
}
//点击marker事件
function addClickHandler(content, marker, id, projectUuid) {
    marker.addEventListener("click", function(e) {
        //取得当前选中的工程id
        projectID = projectUuid;
        openInfo(content, id, e);
    });
}
//点击查看
function viewProject(projectUuid) {
    $.post("/smosplat/setCurrentProject", { projectUuid: projectUuid }, function(data, status) {
        if (JSON.parse(data).result == 0) {
            window.top.location.href = "dataIndex";
        }
    });
}
//添加信息窗口，展示当前选中的是哪个项目
function openInfo(content, id, e) {
    var p = e.target;
    var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
    map.centerAndZoom(point, 12);
    opts = "<table id='tableHeader' style='margin-left:-10px;'>" + "<tr>" + "<th style='width:200px;'>" + content + "<a  class='btn btn-danger btn-xs' id='viewProecjt' onclick='viewProject(projectID);' style='float:right;margin:0 5px 0 10px;'>&nbsp查看</a>" + "</th>" + "</tr>" + "</table>";
    var infoWindow = new BMap.InfoWindow(opts); // 创建信息窗口对象 
    map.openInfoWindow(infoWindow, point); //开启信息窗口
    //通过传入的当前marker选中顺序确定表格行，并选中当前行
    $("#projectListDG").jqGrid('setSelection', id + 1, false);
}
//表格点击时地图居中并且展示信息窗口的逻辑
function showInfo(name, lon, lat) {
    var point = new BMap.Point(lon, lat);
    opts = "<table id='tableHeader' style='margin-left:-10px;'>" + "<tr>" + "<th style='width:200px;'>" + name + "<a  class='btn btn-danger btn-xs' id='viewProecjt' onclick='viewProject(projectID);' style='float:right;margin:0 5px 0 10px;'>&nbsp查看</a>" + "</th>" + "</tr>" + "</table>";
    var infoWindow = new BMap.InfoWindow(opts); // 创建信息窗口对象 
    map.openInfoWindow(infoWindow, point); //开启信息窗口
    map.centerAndZoom(new BMap.Point(lon, lat), 12);
}

function createMap() {
    map = new BMap.Map("map");
    map.centerAndZoom(new BMap.Point(114.042765, 22.629061), 12);
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
}