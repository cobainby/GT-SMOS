var allMonitorItems;
//定义表格数组
var moitorItemsColNames = ['监测项id', '监测项名称', '报警设置', '断面设置', '自动采集/手动上传', '测点设置', '监测项设置', '删除', 'code', '自动采集参数', '断面设置参数', 'number'];
var moitorItemsColModel = [{
    name: 'monitorItemUuid',
    hidden: true
}, {
    name: 'monitorItemName',
    sorttype: "float",
    align: "center"
}, {
    name: 'warningSetting',
    formatter: formatWarningSetting,
    align: "center"
}, {
    name: 'sectionSetting',
    formatter: formatSectionSetting,
    align: "center"
}, {
    name: 'autoSetting',
    formatter: formatAutoSetting,
    align: "center"
}, {
    name: 'suveyPointSetting',
    formatter: formatSurveyPointSetting,
    align: "center"
}, {
    name: 'monitorItemSetting',
    formatter: formatMonitorItemSetting,
    align: "center"
}, {
    name: 'delMonitorItem',
    formatter: formatDelMonitorItem,
    align: "center"
}, {
    name: 'code',
    hidden: true
}, {
    name: 'hasAutoSetting',
    hidden: true
}, {
    name: 'hasSectionSetting',
    hidden: true
}, {
    name: 'number',
    hidden: true
}];
$(function() {
    //获得所有监测项信息并保存到客户端，用于增加监测项时进行过滤
    $.get("/smosplat/getAllMonitorItems", function(data, status) {
        var dataObj = JSON.parse(data);
        allMonitorItems = dataObj.monitorItems;
    });
    //获得跟工程相关的所有监测项
    $.get("/smosplat/getMonitorItemsByProject", function(data, status) {
        var dataObj = JSON.parse(data);
        var monitorItems = dataObj.monitorItems;
        $.jgrid.gridUnload("monitorItemDG");
        $("#monitorItemDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: monitorItems,
            colNames: moitorItemsColNames,
            colModel: moitorItemsColModel,
            autowidth: true,
            sortname: 'id',
            viewrecords: true,
            gridview: true
        });
        $("#monitorItemDG").setGridHeight($("#jqGrid_monitorItemDG").height() - 40);
        $("#monitorItemDG").jqGrid('setSelection', '1');
    });
    $("#addMonitorItemBtn").bind("click", function() {
        //过滤掉工程中已有的监测项
        var filterMonitorItems = new Array();
        //取得表格所有数据
        var selectedMonitorItems = $("#monitorItemDG").jqGrid("getRowData");
        //过滤所有的监测项
        for (var i = 0; i < allMonitorItems.length; i++) {
            //定义变量确定监测项是否存在
            var finded = false;
            for (var j = 0; j < selectedMonitorItems.length; j++) {
                if (selectedMonitorItems[j].monitorItemUuid == allMonitorItems[i].monitorItemUuid) {
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                filterMonitorItems.push(allMonitorItems[i]);
            }
        }
        $.jgrid.gridUnload("selMonitorItemDG");
        $("#selMonitorItemDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: filterMonitorItems,
            colNames: ['选择操作', '监测项名称', '编码格式', 'code', '自动采集参数', '断面设置参数', 'number'],
            colModel: [{
                name: 'monitorItemUuid',
                hidden: true
            }, {
                name: 'monitorItemName',
                sorttype: "float",
                align:'center'
            }, {
                name: 'code',
                sorttype: "float",
                align:'center'
            }, {
                name: 'code',
                hidden: true
            }, {
                name: 'hasAutoSetting',
                hidden: true
            }, {
                name: 'hasSectionSetting',
                hidden: true
            }, {
                name: 'number',
                hidden: true
            }],
            autowidth: true,
            sortname: 'monitorItemUuid',
            viewrecords: true,
            multiselect: true,
            gridview: true
        });
        $("#selMonitorItemDG").setGridHeight($("#addMonitor-modal").height() - 150);
        $("#selMonitorItemDG").setGridWidth($("#addMonitor-modal").width() - 32);
    });
    $("#testBtn").bind("click", function() {
    	document.location.href="/smosplat/mtExport?bDateStr=2017-06-24 00:00:00&eDateStr=2017-07-20 23:59:59"
    	//测试
//        $.get("/smosplat/mtExport",{bDateStr:"2017-06-24 00:00:00",eDateStr:"2017-07-20 23:59:59"},function(data,status){
//        	if(JSON.parse(data).result == 0){
//        		if(JSON.parse(data).fileUrl!=""||JSON.parse(data).fileUrl!=undefined){
//        			window.location.href=JSON.parse(data).fileUrl;
//        		}
//        	}
//        
//        });
    });
    //增加监测项保存按钮点击
    $("#saveMonitorItemBtn").bind("click", function() {
        //得到选中的id
        var selectedID = $("#selMonitorItemDG").jqGrid('getGridParam', 'selarrrow');
        var selectedItems = new Array();
        //遍历id,得到选中的数据
        if (selectedID.length) {
            for (var i = 0; i < selectedID.length; i++) {
                //遍历得到的多选框
                var myrow = $('#selMonitorItemDG').jqGrid('getRowData', selectedID[i]);
                selectedItems.push(myrow);
            }
        }
        var monitorItemUuids = "";
        for (var i = 0; i < selectedItems.length; i++) {
            if (i == selectedItems.length - 1) {
                monitorItemUuids = monitorItemUuids + selectedItems[i].monitorItemUuid;
            } else {
                monitorItemUuids = monitorItemUuids + selectedItems[i].monitorItemUuid + ",";
            }
        }
        //保存关联关系
        $.post("/smosplat/addMonitorItemsForProject", { monitorItemUuids: monitorItemUuids }, function(data, status) {
            if (JSON.parse(data).result == 0) {
                //更新客户端的数据
                for (var i = 0; i < selectedItems.length; i++) {
                    var gridNumbers = $("#monitorItemDG").jqGrid('getRowData').length;
                    $("#monitorItemDG").jqGrid('addRowData', gridNumbers + i + 1, selectedItems[i]);
                }
                $("#selMonitorItemDG").jqGrid('setSelection', '1');
                $('#addMonitorItemDialog').modal('hide');
                swal({title:"保存成功！",type:"success"});
            } else {
            	swal("失败！",JSON.parse(data).msg,"error");
            }
        });
    });
    //水平位移上传
    var optionsWYS={
            url:"/smosplat/uploadWYSDatas",   
            type:"post",
            dataType:null, 
            success:function(mes){
                var dataObj=JSON.parse(mes);
                if(dataObj.result==0){
                	if(dataObj.msg != undefined){
                		swal({title: "上传成功！",text:"<p style='color:#F82A2A;word-wrap:break-word;'>"+dataObj.msg+"<p>",html: true,type:"success"});
                	}else{
                		swal("恭喜","上传成功！","success");
                	}
                }else{
                	swal("上传失败！",dataObj.msg,"error");
                }
            }
        };
	$("#uploadWYSForm").submit(function() {
        $(this).ajaxSubmit(optionsWYS);
        //延时5s，避免频繁点击上传
        var startTimeout=setTimeout(function(){
       	 	$("#uploadWYSDataBtn").removeAttr("disabled");
    	}, 1000*5);
        return false;
    });
    $("#uploadWYSDataBtn").bind("click",function(){
    	$("#uploadWYSForm").submit();
    	 $("#uploadWYSDataBtn").attr({"disabled":"disabled"});
    });
    
    //竖向位移上传
    var optionsWYD={
            url:"/smosplat/uploadWYDDatas",   
            type:"post",
            dataType:null, 
            success:function(mes){
                var dataObj=JSON.parse(mes);
                if(dataObj.result==0){
                	if(dataObj.msg != undefined){
                		swal({title: "上传成功！",text:"<p style='color:#F82A2A;word-wrap:break-word;'>"+dataObj.msg+"<p>",html: true,type:"success"});
                	}else{
                		swal("恭喜","上传成功！","success");
                	}
                }else{
                	swal("上传失败！",dataObj.msg,"error");
                }
            }
        };
	$("#uploadWYDLevelHForm").submit(function() {
        $(this).ajaxSubmit(optionsWYD);
        //延时5s，避免频繁点击上传
        var startTimeout=setTimeout(function(){
       	 	$("#uploadWYDLevelHDataBtn").removeAttr("disabled");
    	}, 1000*5);
        return false;
    });
    $("#uploadWYDLevelHDataBtn").bind("click",function(){
    	$("#uploadWYDLevelHForm").submit();
    	 $("#uploadWYDLevelHDataBtn").attr({"disabled":"disabled"});
    });
    
});
//删除监测项设置
function formatDelMonitorItem(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteMonitorItem(' + rowId + ')" href="#">删除</a>';
}

function deleteMonitorItem(rowId) {
    $("#monitorItemDG").jqGrid('setSelection', rowId);
    var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', rowId).monitorItemUuid;
    //弹出提示框确认
    swal({
        title: "警告",
        text: "删除监测项将删除相关的设置以及测量到的数据！您确定要删除吗?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/deleteMonitorItemForProject", { monitorItemUuid: monitorItemUuid }, function(data, status) {
                if (JSON.parse(data).result == 0) {
                    //重新加载数据
                	$("#monitorItemDG").jqGrid('delRowData', rowId);
                    swal("删除成功！", "您已经删除了该监测项。", "success");
                } else {
                	swal("删除失败！",JSON.parse(data).msg,"error");
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    })
}
//=============================================================================
//===========================自动化采集设置相关==================================================
function formatAutoSetting(cellvalue, options, rowObject) {
    if (rowObject.hasAutoSetting == 0) {
        return '自动采集';
    } else {
        return '手动上传';
    }
}

//=============================================================================
