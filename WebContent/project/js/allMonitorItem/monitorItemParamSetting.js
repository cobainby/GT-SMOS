$(function() {
    //增加或者保存监测项参数
    $("#saveMonitorItemSettingBtn").bind("click", function() {
        var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
        var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
      //先验证表单，如果表单验证不通过则不提交
        var val = $("#monitorItemSettingForm").valid();
        if (!val) {
            return;
        }
        var params = {};
        if (number) {
            params = {
                frequency: $("#frequency").val()
            };
        }
        var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).monitorItemUuid;
        params["monitorItem.monitorItemUuid"] = monitorItemUuid;
        var type = $("#addOrEditMonitorItemParam").text();
        if (type == "edit") {
            params["monitorItemParamUuid"] = $("#monitorItemParamUuid").text();
        }
        $.post("/smosplat/saveOrUpdateMonitorItemParam", params, function(data, status) {
            var dataObj = JSON.parse(data);
            if (dataObj.result == 0) {
                var type = $("#addOrEditMonitorItemParam").text();
                if (type == "add") {
                    swal({title:"添加成功！",type:"success"});
                } else if (type == "edit") {
                	swal({title:"修改成功！",type:"success"});
                }
                $('#monitorItemSettingDialog').modal("hide");
            }
        });
    });
});

function formatMonitorItemSetting(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/MonitorItemSetting.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="monitorItemSetting(' + rowId + ')" href="#">监测项设置</a>';
}

function monitorItemSetting(rowId) {
    $("#monitorItemDG").jqGrid('setSelection', rowId);
    var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', rowId).monitorItemUuid;
    var number = $("#monitorItemDG").jqGrid('getRowData', rowId).number;
    //清空设置
    $("#updateMonitorItemSettingContentDiv").empty();
    if (number) {
        var content = '<form id="monitorItemSettingForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测项设置</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<tr><td><label id="monitorItemParamUuid" style="display:none;"></label></td><td><label id="addOrEditMonitorItemParam" style="display:none;"></label></td></tr>' +
            '<tr><th colspan="1">监测频率：</th><td colspan="1"><input id="frequency" name="frequency" type="text" maxlength="100" class="input" style="width: 70%;" required>&nbsp&nbsp天</td></tr>' +
            '</table>'+'</form>';
    }
    //添加新的子内容
    $("#updateMonitorItemSettingContentDiv").append(content);
    //需要重新渲染dom元素
    $.parser.parse($("#updateMonitorItemSettingContentDiv"));
    //根据监测项设置获得监测项设置实体
    $.post("/smosplat/getMonitorItemParam", { monitorItemUuid: monitorItemUuid }, function(data, status) {
        var dataObj = JSON.parse(data);
        if (dataObj.entity == null) {
            //还没有设置过，进行添加
            $("#monitorItemParamUuid").text("");
            $("#addOrEditMonitorItemParam").text("add");
        } else {
            var monitorItemParam = dataObj.entity;
            //将设置参数的值设置到界面
            var number = $("#monitorItemDG").jqGrid('getRowData', rowId).number;
            if (number) {
                $("#monitorItemParamUuid").text(monitorItemParam.monitorItemParamUuid);
                $("#addOrEditMonitorItemParam").text("edit");
                $("#frequency").val(monitorItemParam.frequency);
            }
        }
        $("#monitorItemSettingDialog").modal("show");
    });
}
