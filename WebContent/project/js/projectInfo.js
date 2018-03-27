var map;
var scaleControl;
var navControl;
var overviewControl;
//当前操作类型
var typeRequest;
//当前机构下人员
var workerObj;
//监测人员类型
var workerType;
//删除后的监测人员数据源
var lastWorkerItems=[];
var str = "";
var myValue;
$(function() {
    var url = location.search; //获取url中"?"符后的字串  
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        typeRequest = unescape(str.split("=")[1]);
    }

    createMap(); //创建地图
    setMapEvent(); //设置地图事件
    addMapControl(); //向地图添加控件
    //弹出框高度
    var bodyHeight = $('#monitorBody').height();
    var bodyWidth = $('#monitorBody').width();
    //设置模态框弹出的表格的高度
    $("#chooseMonitor-modal").height(bodyHeight - 100);
    $("#deleteMonitor-modal").height(bodyHeight - 100);
    $("#mapDialog").height(bodyHeight*0.85);
    $("#mapDialog").width(bodyWidth*0.6);
    //解决easyUI-dialog的头部由于自适应无法完全显示的bug
    $(".panel-header-noborder").width(bodyWidth*0.6);
    $("#mapDialog").dialog('close');
    $(".tangram-suggestion-main").height($("#mapDialog").height());
    $('#mapDialog').window('center');//使Dialog居中显示
    $("#projectType").combobox("loadData", [{ "label": '基坑', "value": 0 }]);
    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
    		{"input" : "suggestId"
    		,"location" : map
    });
    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
		var _value = e.fromitem.value;
		var value = "";
		if (e.fromitem.index > -1) {
			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		}    
		str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
		
		value = "";
		if (e.toitem.index > -1) {
			_value = e.toitem.value;
			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		}    
		str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		G("searchResultPanel").innerHTML = str;
	});
	ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
	var _value = e.item.value;
		myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
		
		setPlace();
	});
    $("#addBuildName").bind('click', function() {
        $("#addNameDialog").modal("show");
        $("#nameTitle").html("添加建筑单位联系人");
        $("#addType").val("Build");
    });
    $("#deleteBuildName").bind('click', function() {
        $("#deleteNameDialog").modal("show");
        $("#deleteTitle").html("删除建筑单位联系人");
        $("#deleteNameInput").val($("#buildContactName").html());
        $("#deleteType").val("Build");
    });
    $("#addDesignName").bind('click', function() {
        $("#addNameDialog").modal("show");
        $("#nameTitle").html("添加设计单位联系人");
        $("#addType").val("Design");
    });
    $("#deleteDesignName").bind('click', function() {
        $("#deleteNameDialog").modal("show");
        $("#deleteTitle").html("删除设计单位联系人");
        $("#deleteNameInput").val($("#designContactName").html());
        $("#deleteType").val("Design");
    });
    $("#addConstructName").bind('click', function() {
        $("#addNameDialog").modal("show");
        $("#nameTitle").html("添加施工单位联系人");
        $("#addType").val("Construct");
    });
    $("#deleteConstructName").bind('click', function() {
        $("#deleteNameDialog").modal("show");
        $("#deleteTitle").html("删除施工单位联系人");
        $("#deleteNameInput").val($("#constructContactName").html());
        $("#deleteType").val("Construct");
    });
    $("#addSupervisorName").bind('click', function() {
        $("#addNameDialog").modal("show");
        $("#nameTitle").html("添加监理单位联系人");
        $("#addType").val("Supervisor");
    });
    $("#deleteSupervisorName").bind('click', function() {
        $("#deleteNameDialog").modal("show");
        $("#deleteTitle").html("删除监理单位联系人");
        $("#deleteNameInput").val($("#supervisorContactName").html());
        $("#deleteType").val("Supervisor");
    });
    $("#addNameBtn").bind('click', function() {
        $("#addNameDialog").modal("hide");
        var type = $("#addType").val();
        var name;
        if (type == "Build") {
        	if($("#buildContactName").html()==""){
        		name = $("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}else{
        		name = $("#buildContactName").html() +'、'+$("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}
            $("#buildContactName").html(name);
        } else if (type == "Design") {
        	if($("#designContactName").html()==""){
        		name = $("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}else{
        		name = $("#designContactName").html() + '、'+ $("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}
            $("#designContactName").html(name);
        } else if (type == "Construct") {
        	if($("#constructContactName").html()==""){
        		name = $("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}else{
        		 name = $("#constructContactName").html()  + '、'+ $("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}
            $("#constructContactName").html(name);
        } else if (type == "Supervisor") {
        	if($("#supervisorContactName").html()==""){
        		name=$("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}else{
        		name = $("#supervisorContactName").html()  + '、'+ $("#addNameInput").val() + '(' + $("#addPhoneInput").val() + ')';
        	}
            $("#supervisorContactName").html(name);
        }
        $('#nameForm').form('clear');
    });
    $("#deleteNameBtn").bind('click', function() {
        $("#deleteNameDialog").modal("hide");
        var type = $("#deleteType").val();
        var name = $("#deleteNameInput").val();;
        if (type == "Build") {
            $("#buildContactName").html(name);
        } else if (type == "Design") {
            $("#designContactName").html(name);
        } else if (type == "Construct") {
            $("#constructContactName").html(name);
        } else if (type == "Supervisor") {
            $("#supervisorContactName").html(name);
        }else if(type=="leader"){
        	$("#monitorLeader").val(name);
        }else if(type=="worker"){
        	$("#monitorWorker").val(name);
        }
        $('#nameForm').form('clear');
    });
    $("#canceladdNameBtn").bind("click", function() {
        $('#nameForm').form('clear');
    });
    $("#cancelDeleteNameBtn").bind("click", function() {
        $('#deleteForm').form('clear');
    });

    //获取当前机构下所有worker
    
    $.post("getCurrentAccount", function(data, status) {
        var organUuid = JSON.parse(data).organUuid;
        $.post("/smosplat/getWorkersByOrgan", { organUuid: organUuid }, function(data, status) {
        	workerObj = JSON.parse(data).workers;
        });
    });
    //添加监测人员负责人
    $("#addMonitorLeader").bind("click", function() {
    	workerType="leader";
    	getMonitorWorker(workerType);
    });
    $("#addMonitorWorker").bind("click", function() {
    	workerType="worker";
    	getMonitorWorker(workerType);
    });
    $("#deleteMonitorLeader").bind("click",function(){
        $("#deleteNameInput").val($("#monitorLeader").val());
        workerType="leader";
        deleteMonitorWorker(workerType);
    });
    $("#deleteMonitorWorker").bind("click",function(){
        $("#deleteNameInput").val($("#monitorWorker").val());
        workerType="worker";
        deleteMonitorWorker(workerType);
    });
    //保存选择的监测人员
    $("#saveMonitorWorkerBtn").bind("click", function() {
    	var selectedIDs = $("#monitorWorkerDG").jqGrid('getGridParam', 'selarrrow');
    	//获得当前选择行的数据
    	var selectedItems = new Array();
        //遍历id,得到选中的数据
        if (selectedIDs.length!=0) {
        	//确定是监测负责人还是普通监测人
        	var workerInfo=(workerType == "leader") ? $("#monitorLeader").val():$("#monitorWorker").val();
            for (var i = 0; i < selectedIDs.length; i++) {
                //遍历得到的多选框
                var workerObject = $('#monitorWorkerDG').jqGrid('getRowData', selectedIDs[i]);
                //如果输入框初始为空，则不加顿号
                if(workerInfo==""){
                	workerInfo=workerObject.workerName+"("+workerObject.phone+")";
                }else{
                	workerInfo+="、"+workerObject.workerName+"("+workerObject.phone+")";
                }
            }
            if(workerType=="leader"){
            	$("#monitorLeader").val(workerInfo);
            }else if(workerType=="worker"){
            	$("#monitorWorker").val(workerInfo);
            }
        }
        $('#chooseNameDialog').modal('hide');
    });
    //删除监测人员后点击保存
    $("#saveDeleteWorkerBtn").bind("click",function(){
    	lastWorkerItems=$("#deleteWorkerDG").jqGrid('getRowData');
        var workerInfo="";
        for (var i = 0; i < lastWorkerItems.length; i++) {
                //遍历得到的多选框
                var workerObject = lastWorkerItems[i];
                if(workerInfo==""){
                	workerInfo=workerObject.workerName+"("+workerObject.phone+")";
                }else{
                	workerInfo+="、"+workerObject.workerName+"("+workerObject.phone+")";
                }
         }
         if(workerType=="leader"){
            	$("#monitorLeader").val(workerInfo);
         }else if(workerType=="worker"){
            	$("#monitorWorker").val(workerInfo);
         }
        $('#deleteWorkerDialog').modal('hide');
    });
    //当前如果是新增项目
    if (typeRequest == "add") {
        $("#tableName").text("新建工程");
        $("#formUrl").val("addProject");
        $("#projectType").combobox("setValue", 0);
    } else if (typeRequest == "change") {
        $("#tableName").text("修改工程");
        $.post("/smosplat/getCurrentProjectInfo", function(data, status) {
            var dataObj = JSON.parse(data);
            var projectUuid = dataObj.project.projectUuid;
            //父iframe的标题栏
            $('#projectName', parent.document).text(dataObj.project.projectName);
            $.post("getProjectByUuid", { projectUuid: projectUuid }, function(data, status) {
                var allDataObj = JSON.parse(data).project;
                $('#projectForm').form('clear');
                $("#formUrl").val("updateProject");
                //填充数据
                $("#projectUuid").val(allDataObj.projectUuid);
                $("#projectName").val(allDataObj.projectName);
                $("#lon").val(allDataObj.lon);
                $("#lat").val(allDataObj.lat);
                $("#code").val(allDataObj.code);
                $("#address").val(allDataObj.address);
                $("#projectType").combobox("setValue", 0);
                $("#structure").val(allDataObj.structure);
                $("#deep").val(allDataObj.deep);
                $("#perimeter").val(allDataObj.perimeter);
                $("#monitorLeader").val(allDataObj.monitorLeader);
                $("#monitorWorker").val(allDataObj.monitorWorker);
                $("#superviseCompany").val(allDataObj.superviseCompany);
                $("#superviseWorker").val(allDataObj.superviseWorker);
                $("#safeLevel").val("setValue", allDataObj.safeLevel);
                $("#superviseCode").val(allDataObj.superviseCode);
                $("#buildCompany").val(allDataObj.buildCompany);
                $("#buildContactName").html(allDataObj.buildContactName);
                $("#designCompany").val(allDataObj.designCompany);
                $("#designContactName").html(allDataObj.designContactName);
                $("#constructCompany").val(allDataObj.constructCompany);
                $("#constructContactName").html(allDataObj.constructContactName);
                $("#supervisorCompany").val(allDataObj.supervisorCompany);
                $("#supervisorContactName").html(allDataObj.supervisorContactName);
                //非手动输入会提示验证信息，手动验证一下去掉这个验证信息
                $("#projectForm").form('validate');
            });
        });
    }
    //保存按钮点击
    $("#saveBtn").bind('click', function() {
        //先验证表单，如果表单验证不通过则不提交
        var val = $("#projectForm").valid();
        if (!val) {
            return;
        }
        if($("#lon").val()==""||$("#lat").val()==""){
        	swal({ title: "失败！", text:"经纬度是必填项!",type: "error" });
        	return;
        }
        if($("#monitorLeader").val()==""){
        	swal({ title: "失败！", text:"监测负责人是必填项!",type: "error" });
        	return;
        }
        var params = {
            projectName: $("#projectName").val(),
            code: $("#code").val(),
            address: $("#address").val(),
            lon: $("#lon").val(),
            lat: $("#lat").val(),
            projectType: $("#projectType").combobox("getValue"),
            structure: $("#structure").val(),
            deep: $("#deep").val(),
            perimeter: $("#perimeter").val(),
            monitorLeader: $("#monitorLeader").val(),
            monitorWorker: $("#monitorWorker").val(),
            superviseCompany: $("#superviseCompany").val(),
            superviseWorker: $("#superviseWorker").val(),
            safeLevel: $("#safeLevel").combobox("getValue"),
            superviseCode: $("#superviseCode").val(),
            buildCompany: $("#buildCompany").val(),
            buildContactName: $("#buildContactName").html(),
            designCompany: $("#designCompany").val(),
            designContactName: $("#designContactName").html(),
            constructCompany: $("#constructCompany").val(),
            constructContactName: $("#constructContactName").html(),
            supervisorCompany: $("#supervisorCompany").val(),
            supervisorContactName: $("#supervisorContactName").html(),

        };
        var url = $("#formUrl").val();
        if (url == "updateProject") {
            params["projectUuid"] = $("#projectUuid").val();
        }
        $.post("/smosplat/" + url, params, function(data, status) {
            if (JSON.parse(data).result == 0) {
                if (url == "addProject") {
                    swal({ title: "添加成功！", type: "success" });
                    //改变父窗中theRequest的值，使另外几个设置项由不可用变为可用
                    parent.theRequest = "change";
                    var projectUuid = JSON.parse(data).project.projectUuid;
                    //把新增的项目存进session
                    $.post("/smosplat/setCurrentProject", { projectUuid: projectUuid }, function() {
                        $.get("/smosplat/getCurrentProjectInfo", function(data, status) {
                            var dataObj = JSON.parse(data);
                            $('#projectName', parent.document).text(dataObj.project.projectName);
                        });
                    });
                } else if (url == "updateProject") {
                    swal({ title: "修改成功！", type: "success" });
                }
            } else {
                swal({ title: "失败！", text: JSON.parse(data).msg, type: "error" });
            }
        });
    });
    //取消按钮点击
    $("#cancelBtn").bind('click', function() {
        $('#projectForm').form('clear');
        $('#projectForm').data('validator').resetForm();
    });
    //点击获取经纬度
    $("#lonlatSelect").bind('click', function(event) {
        /* Act on the event */
        map.clearOverlays();
        $("#mapDialog").dialog('open');
        $("#suggestId").val("");
    });
});

function getMonitorWorker(type){
	$("#chooseNameDialog").modal("show");
	//过滤掉工程中已有的监测人员
    var filterMonitorItems = new Array();
    //取得已经填写的数据
    if(type=="leader"){
    	var selectedMonitorWorkers = $("#monitorLeader").val();
    }else if(type=="worker"){
    	var selectedMonitorWorkers = $("#monitorWorker").val();
    }
    //过滤所有的监测项
    for (var i = 0; i < workerObj.length; i++) {
        //定义变量确定监测项是否存在
        var finded = false;
        if (selectedMonitorWorkers.indexOf(workerObj[i].phone)!==-1 ) {
               finded = true;
        }
        if (!finded) {
            filterMonitorItems.push(workerObj[i]);
        }
    }
	$.jgrid.gridUnload("monitorWorkerDG");
    $("#monitorWorkerDG").jqGrid({
        datatype: "local",
        styleUI: 'Bootstrap',
        data: filterMonitorItems,
        colNames: [ '人员名字', '电话号码'],
        colModel: [{
            name: 'workerName',
            align:'center'
        }, {
            name: 'phone',
            align:'center'
        }],
        autowidth: true,
        viewrecords: true,
        multiselect: true,
        gridview: true
    });
    $("#monitorWorkerDG").setGridHeight($("#chooseMonitor-modal").height() - 135);
    $("#monitorWorkerDG").setGridWidth($("#chooseMonitor-modal").width() - 32);
}
//删除监测负责人
function deleteMonitorWorker(type){
	$("#deleteWorkerDialog").modal("show");
	//过滤掉工程中已有的监测人员
    var deleteMonitorItems = new Array();
    //取得已经填写的数据
    if(type=="leader"){
    	var selectedMonitorWorkers = $("#monitorLeader").val();
    }else if(type=="worker"){
    	var selectedMonitorWorkers = $("#monitorWorker").val();
    }
    //过滤所有的监测项,将已经存在输入框的相关信息作为删除的数据源
    for (var i = 0; i < workerObj.length; i++) {
        if (selectedMonitorWorkers.indexOf(workerObj[i].phone)!==-1 ) {
            deleteMonitorItems.push(workerObj[i]);
        }
    }
	$.jgrid.gridUnload("deleteWorkerDG");
    $("#deleteWorkerDG").jqGrid({
        datatype: "local",
        styleUI: 'Bootstrap',
        data: deleteMonitorItems,
        colNames: [ '人员名字', '电话号码','处理'],
        colModel: [{
            name: 'workerName',
            align:'center'
        }, {
            name: 'phone',
            align:'center'
        },{
        	name: 'delete',
            align: "center",
            width:80,
            formatter: formatDeleteWorker
        }],
        autowidth: true,
        sortname: 'monitorItemUuid',
        viewrecords: true,
        gridview: true
    });
    $("#deleteWorkerDG").setGridHeight($("#deleteMonitor-modal").height() - 135);
    $("#deleteWorkerDG").setGridWidth($("#deleteMonitor-modal").width() - 32);
}
function formatDeleteWorker(cellvalue, options, rowObject) {
    return "<a href='javascript:void(0);'  onclick=\"deleteWorker('" + options.rowId + "')\">" + "&nbsp删除" + "</a>";
}
//删除监测负责人
function deleteWorker(rowId){
    $("#deleteWorkerDG").jqGrid('delRowData', rowId);  
}
function createMap() {
    map = new BMap.Map("projectMap");
    map.centerAndZoom(new BMap.Point(114.042765, 22.629061), 13);
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
    //地图点击获取工程当前经纬度
    map.addEventListener("click", function(e) {
        map.clearOverlays();
        $("#lon").val(e.point.lng);
        $("#lat").val(e.point.lat);
        //定义获取的坐标值来获取地点
        var gc = new BMap.Geocoder();
        gc.getLocation(e.point, function(rs) {
            var addComp = rs.addressComponents;
            $('#address').val(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
        });
        var projectMarker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat), {
            icon: new BMap.Icon("/smosplat/common/image/marker.png", new BMap.Size(20, 32), {
                imageOffset: new BMap.Size(0, 3)
            })
        });
        map.addOverlay(projectMarker);
    });
}


function addMapOverlay(projectData) {
    map.clearOverlays();
    for (var i = 0; i < projectData.length; i++) {
        var marker = new BMap.Marker(new BMap.Point(projectData[i].lon, projectData[i].lat), {
            icon: new BMap.Icon("/smosplat/common/image/marker.png", new BMap.Size(20, 32), {
                imageOffset: new BMap.Size(0, 3)
            })
        });
        //得到工程名字和当前工程顺序
        var content = projectData[i].projectName;
        var id = i;
        var projectUuid = projectData[i].projectUuid;
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
    opts = "<table id='tableHeader' style='margin-left:-10px;'>" + "<tr>" + "<th style='width:200px;'>" + content + "<a  class='btn btn-danger btn-xs' id='viewProecjt' onclick='viewProject(projectID);' style='float:right;margin:0 5px 0 10px;'>&nbsp查看</a>" + "</th>" + "</tr>" + "</table>";
    var infoWindow = new BMap.InfoWindow(opts); // 创建信息窗口对象 
}

// 百度地图API功能
function G(id) {
	return document.getElementById(id);
}

function setPlace(){
	map.clearOverlays();    //清除地图上所有覆盖物
	function myFun(){
		var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
		map.centerAndZoom(pp, 18);
		map.addOverlay(new BMap.Marker(pp));    //添加标注
	}
	var local = new BMap.LocalSearch(map, { //智能搜索
	  onSearchComplete: myFun
	});
	local.search(myValue);
}