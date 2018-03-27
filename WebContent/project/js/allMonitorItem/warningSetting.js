//定义表格数组

var warningColModel = [{
    name: 'warningUuid',
    hidden: true
}, {
    name: 'warningName',
    sorttype: "float",
    align: "center"
}, {
    name: 'earlySingleRate',
    sorttype: "number",
    align: "center",
    hidden: true
}, {
    name: 'warnSingleRate',
    sorttype: "number",
    align: "center"
}, {
    name: 'controlSingleRate',
    sorttype: "number",
    align: "center",
    hidden: true
}, {
    name: 'earlyAccum',
    sorttype: "number",
    align: "center"
}, {
    name: 'warnAccum',
    sorttype: "number",
    align: "center"
}, {
    name: 'controlAccum',
    sorttype: "number",
    align: "center"
}, {
    name: 'edit',
    formatter: formatWarningEdit,
    align: "center"
}, {
    name: 'delete',
    formatter: formatWarningDel,
    align: "center"
}];

var warningColModel2 = [{
    name: 'warningUuid',
    hidden: true
}, {
    name: 'warningName',
    sorttype: "float",
    align: "center"
}, {
    name: 'earlySingleRate',
    sorttype: "number",
    align: "center",
    hidden: true
}, {
    name: 'warnSingleRate',
    sorttype: "number",
    align: "center"
}, {
    name: 'controlSingleRate',
    sorttype: "number",
    align: "center",
    hidden: true
},{
    name: 'edit',
    formatter: formatWarningEdit,
    align: "center"
}, {
    name: 'delete',
    formatter: formatWarningDel,
    align: "center"
}];
$(function() {
        //增加预警设置按钮点击
        $("#addWarningBtn").bind("click", function() {
        	var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
            var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
        	$("#warningForm").form("clear");
            $("#editOrDelWarning").text("add");
            if(number==6||number==15||number==18){
            	$("#perChangeL").text("(KN/d)");
            	$("#totalChangeL").text("(KN)");
            }else{
            	$("#perChangeL").text("(mm/d)");
            	$("#totalChangeL").text("(mm)");
            };
            $("#earlySingleRate").val("");
            $("#warnSingleRate").val("");
            $("#controlSingleRate").val("");
            $("#earlyAccum").val("");
            $("#warnAccum").val("");
            $("#controlAccum").val("");
            $("#warningName").val("");
        });
        $("#cancelSaveWarningBtn").bind("click",function(){
        	$('#warningForm').data('validator').resetForm();
        });
        //保存预警设置
        $("#saveWarningBtn").bind("click", function() {
        	//先验证表单，如果表单验证不通过则不提交
            var val = $("#warningForm").valid();
            if (!val) {
                return;
            }
            var params = {
                earlySingleRate: $("#earlySingleRate").val(),
                warnSingleRate: $("#warnSingleRate").val(),
                controlSingleRate: $("#controlSingleRate").val(),
                earlyAccum: $("#earlyAccum").val(),
                warnAccum: $("#warnAccum").val(),
                controlAccum: $("#controlAccum").val(),
                warningName: $("#warningName").val()
            };
            // 得到当前选中第几行，取得id
            var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
            var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).monitorItemUuid;
            params["monitorItem.monitorItemUuid"] = monitorItemUuid;
            // 取得type字段，判断是新增还是修改
            var type = $("#editOrDelWarning").text();
            if (type == "add") {
                $.post("/smosplat/addWarning", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                        $("#warningInfoDialog").modal("hide");
                        //在现有数据中增加一条记录
                        //获得当前表格行数
                        var gridNumbers = $("#warningDG").jqGrid('getRowData').length;
                        $("#warningDG").jqGrid("addRowData", gridNumbers + 1, dataObj.entity, "last");
                        swal({title:"添加成功！",type:"success"});
                    } else {
                    	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
                    }
                });
            } else if (type == "edit") {
                var warnRowId = $('#warningDG').jqGrid('getGridParam', 'selrow');
                var warningUuid = $("#warningDG").jqGrid('getRowData', warnRowId).warningUuid;
                params["warningUuid"] = warningUuid;
                $.post("/smosplat/updateWarning", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                        //更新前端数据
                        $("#warningDG").jqGrid('setCell', warnRowId, 'warningUuid', dataObj.entity.warningUuid);
                        $("#warningDG").jqGrid('setCell', warnRowId, 'earlySingleRate', dataObj.entity.earlySingleRate);
                        $("#warningDG").jqGrid('setCell', warnRowId, 'warnSingleRate', dataObj.entity.warnSingleRate);
                        $("#warningDG").jqGrid('setCell', warnRowId, 'controlSingleRate', dataObj.entity.controlSingleRate);
                        $("#warningDG").jqGrid('setCell', warnRowId, 'earlyAccum', dataObj.entity.earlyAccum);
                        $("#warningDG").jqGrid('setCell', warnRowId, 'warnAccum', dataObj.entity.warnAccum);
                        $("#warningDG").jqGrid('setCell', warnRowId, 'controlAccum', dataObj.entity.controlAccum);
                        $("#warningDG").jqGrid('setCell', warnRowId, 'warningName', dataObj.entity.warningName);
                        $("#warningInfoDialog").modal("hide");
                        swal({title:"修改成功！",type:"success"});
                    } else {
                    	swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                    }
                });
            }

        });
    })
    
    //===========================预警设置相关==================================================
function formatWarningSetting(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/warning.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="warningSetting(' + rowId + ')" href="#">报警设置</a>';
}

function warningSetting(rowId) {
    // 选中当前行
    $("#monitorItemDG").jqGrid('setSelection', rowId);
    // 得到监测项ID
    var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', rowId).monitorItemUuid;
    var warningColNames = ['报警id', '报警类型', '本次变化率预警值(mm/d)', '本次变化率报警值(mm/d)', '本次变化率控制值(mm/d)', '累计变化预警值(mm)', '累计变化报警值(mm)', '累计变化控制值(mm)', '修改', '删除'];
    var number = $("#monitorItemDG").jqGrid('getRowData', rowId).number;
    var colModel;
    if(number==6||number==15||number==18){
    	warningColNames = ['报警id', '报警类型', '本次变化率预警值(kN/d)', '本次变化率报警值(kN/d)', '本次变化率控制值(kN/d)','累计变化预警值(mm)', '累计变化报警值(mm)', '累计变化控制值(mm)', '修改', '删除'];
    	$('#accumPanel').show();
    	colModel=warningColModel;
    }else{
    	$('#accumPanel').show();
    	colModel=warningColModel;
    }
    //获得项目、监测项下的所有预警信息
    $.jgrid.gridUnload('warningDG');
    $.post("/smosplat/getWarnings", { monitorItemUuid: monitorItemUuid }, function(data, status) {
        $("#warningDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: JSON.parse(data).warnings,
            colNames: warningColNames,
            colModel: colModel,
            autowidth: true,
            viewrecords: true,
            gridview: true
        });
        $("#warningDG").setGridHeight($("#warning-modal").height() - 150);
        $("#warningDG").setGridWidth($("#warning-modal").width() - 32);
        $("#warningDG").jqGrid('setSelection', '1');
        $("#warningSettingDialog").modal("show");
    });
}
// 报警设置表格的处理函数
function formatWarningEdit(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="editWarning(' + rowId + ')" href="#">修改</a>';
}

function editWarning(rowId) {
    $("#warningDG").jqGrid('setSelection', rowId);
    var item = $("#warningDG").jqGrid('getRowData', rowId);
    var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
    var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
    if(number==6||number==15||number==18){
    	$("#perChangeL").text("(KN/d)");
    	$("#totalChangeL").text("(KN)");
    }else{
    	$("#perChangeL").text("(mm/d)");
    	$("#totalChangeL").text("(mm)");
    };
    //填充值
    $("#editOrDelWarning").text("edit");
    $("#earlySingleRate").val(item.earlySingleRate);
    $("#warnSingleRate").val(item.warnSingleRate);
    $("#controlSingleRate").val(item.controlSingleRate);
    $("#earlyAccum").val(item.earlyAccum);
    $("#warnAccum").val(item.warnAccum);
    $("#controlAccum").val(item.controlAccum);
    $("#warningName").val(item.warningName);
    $("#warningInfoDialog").modal("show");

}

function formatWarningDel(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="delWarning(' + rowId + ')" href="#">删除</a>';
}

function delWarning(rowId) {
    $("#warningDG").jqGrid('setSelection', rowId);
    var warningUuid = $("#warningDG").jqGrid('getRowData', rowId).warningUuid;
    //弹出提示框确认
    swal({
        title: "警告",
        text: "您确定要删除该报警设置吗?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/deleteWarning", { warningUuid: warningUuid },function(data, status) {
                if (JSON.parse(data).result == 0) {
                	//刷新本地数据
                	$("#warningDG").jqGrid('delRowData', rowId);
                    swal("删除成功！", "您已经删除了该报警设置。", "success");
                } else {
                	swal("删除失败！",JSON.parse(data).msg,"error");
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    });
}
