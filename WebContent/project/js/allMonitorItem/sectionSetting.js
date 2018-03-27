//定义表格数组
var sectionColNames = ['断面id', '断面名称', '断面起始点', '断面终止点', '断面方向', '修改', '删除'];
var sectionColModel = [{
    name: 'sectionUuid',
    hidden: true
}, {
    name: 'sectionName',
    sorttype: "float",
    align: "center"
}, {
    name: 'startPointName',
    sorttype: "float",
    align: "center"
}, {
    name: 'endPointName',
    sorttype: "float",
    align: "center"
}, {
    name: 'direction',
    sorttype: "float",
    align: "center"
}, {
    name: 'edit',
    formatter: formatSectionEdit,
    align: "center"
}, {
    name: 'delete',
    formatter: formatSectionDel,
    align: "center"
}];
$(function() {
        var sectionDirections = [{ id: 0, text: "顺时针" }, { id: 1, text: "逆时针" }];
        $("#direction").combobox("loadData", sectionDirections);
        //增加断面
        $("#addSectionBtn").bind("click", function() {
            //清除表格原数据
            $("#sectionForm").form("clear");
            $("#addOrEditSection").text("add");
            //增加断面时不需要选择起始点和终止点
            $("#startPointNameTR").hide();
            $("#endPointNameTR").hide();
        });
        $("#cancelSectionBtn").bind("click",function(){
        	$('#sectionForm').data('validator').resetForm();
        })
        //保存断面
        $("#saveSectionBtn").bind("click", function() {
        	//先验证表单，如果表单验证不通过则不提交
            var val = $("#sectionForm").valid();
            if (!val) {
                return;
            }
            var params = {
                sectionName: $("#sectionName").val(),
                startPointName: $("#startPointName").combobox("getText"),
                endPointName: $("#endPointName").combobox("getText"),
                direction: $("#direction").combobox("getText")
            };
            var monitorRowId = $("#monitorItemDG").jqGrid('getGridParam', 'selrow');
            var monitorItemUuid = $("#monitorItemDG").jqGrid("getRowData", monitorRowId).monitorItemUuid;
            params["monitorItem.monitorItemUuid"] = monitorItemUuid;
            var type = $("#addOrEditSection").text();
            if (type == "add") {
                $.post("/smosplat/addSection", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                        $("#sectionInfoDialog").modal("hide");
                        //在现有数据中增加一条记录
                        var gridNumbers = $("#sectionDG").jqGrid('getRowData').length;
                        $("#sectionDG").jqGrid("addRowData", gridNumbers + 1, dataObj.entity, "last");
                        swal({title:"添加成功！",type:"success"});
                    } else {
                    	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
                    }
                });
            } else if (type == "edit") {
                var sectionRowId = $("#sectionDG").jqGrid('getGridParam', 'selrow');
                var sectionUuid = $("#sectionDG").jqGrid("getRowData", sectionRowId).sectionUuid;
                params["sectionUuid"] = sectionUuid;
                $.post("/smosplat/updateSection", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                        $("#sectionDG").jqGrid('setCell', sectionRowId, 'sectionUuid', dataObj.entity.sectionUuid);
                        $("#sectionDG").jqGrid('setCell', sectionRowId, 'sectionName', dataObj.entity.sectionName);
                        $("#sectionDG").jqGrid('setCell', sectionRowId, 'startPointName', dataObj.entity.startPointName);
                        $("#sectionDG").jqGrid('setCell', sectionRowId, 'endPointName', dataObj.entity.endPointName);
                        $("#sectionDG").jqGrid('setCell', sectionRowId, 'direction', dataObj.entity.direction);
                        $("#sectionInfoDialog").modal("hide");
                        swal({title:"修改成功！",type:"success"});
                    } else {
                    	swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                    }
                });
            }
        });
    })
    //=============================================================================
    //===========================断面设置相关==================================================
function formatSectionSetting(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    if (rowObject.hasSectionSetting == 0) {
        return '<img src="/smosplat/common/image/section.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="sectionSetting(' + rowId + ')" href="#">断面设置</a>';
    } else {
        return "";
    }
}

function sectionSetting(rowId) {
    $("#monitorItemDG").jqGrid("setSelection", rowId);
    var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', rowId).monitorItemUuid;
    //获得项目、监测项下的所有断面信息
    $.jgrid.gridUnload('sectionDG');
    $.post("/smosplat/getSections", { monitorItemUuid: monitorItemUuid }, function(data, status) {
        $("#sectionDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: JSON.parse(data).sections,
            colNames: sectionColNames,
            colModel: sectionColModel,
            autowidth: true,
            viewrecords: true,
            gridview: true
        });
        $("#sectionDG").setGridHeight($("#section-modal").height() - 135);
        $("#sectionDG").setGridWidth($("#section-modal").width() - 32);
        $("#sectionSettingDialog").modal("show");
    });
}

function formatSectionEdit(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="editSection(' + rowId + ')" href="#">修改</a>';
}

function formatSectionDel(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="delSection(' + rowId + ')" href="#">删除</a>';
}

function editSection(rowId) {
    $("#sectionDG").jqGrid('setSelection', rowId);
    //修改断面需要获取所有监测点并显示在下拉列表当中
    var monitorRowId=$("#monitorItemDG").jqGrid('getGridParam','selrow');
    var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).monitorItemUuid;
    //获得项目、监测项下的所有监测点
    $.post("/smosplat/getSurveyPoints", { monitorItemUuid: monitorItemUuid }, function(data, status) {
        $("#startPointName").combobox("loadData", JSON.parse(data).surveyPoints);
        $("#endPointName").combobox("loadData", JSON.parse(data).surveyPoints);
        var item = $("#sectionDG").jqGrid('getRowData', rowId);
        //修改断面
        //修改断面时需要选择起始点和终止点
        $("#startPointNameTR").show();
        $("#endPointNameTR").show();
        $("#addOrEditSection").text("edit");
        $("#sectionUuid").text(item.sectionUuid);
        $("#sectionName").val(item.sectionName);
        $("#startPointName").combobox("setText", item.startPointName);
        $("#endPointName").combobox("setText", item.endPointName);
        $("#direction").combobox("setText", item.direction);
        $("#sectionInfoDialog").modal("show");
    });
}

function delSection(rowId) {
    $("#sectionDG").jqGrid('setSelection', rowId);
    var sectionUuid = $("#sectionDG").jqGrid('getRowData', rowId).sectionUuid;
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
            $.post("/smosplat/deleteSection", { sectionUuid: sectionUuid },function(data, status) {
                if (JSON.parse(data).result == 0) {
                	//刷新本地数据
                	$("#sectionDG").jqGrid('delRowData', rowId);
                    swal("删除成功！", "您已经删除了该断面。", "success");
                } else {
                	swal("删除失败！",JSON.parse(data).msg,"error");
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    });
}
