//机构名称
var currentOrganUuid;
var isSuperAdmin = false;
//人员列表
var deviceList;
/**
 * 定义jqGrid的绑定列
 */
function setView() {
    $.post("getCurrentAccount", function(data, status) {
        var jsonData = $.parseJSON(data);
        if (jsonData.account.loginName == "superadmin") {
            isSuperAdmin = true;
            $.post("getAllOrgans",
                function(data, status) {
                    var dtData = $.parseJSON(data).organs;
                    $("#organCB").combobox({
                        data: dtData,
                        valueField: 'organUuid',
                        textField: 'organName',
                        panelHeight: 'auto',
                        editable: false
                    });
                    //设置当前机构
                    for (var j = 0; j < dtData.length; j++) {
                        if (dtData[j].organName == "超级管理员所属机构") {
                            currentOrganUuid = dtData[j].organUuid;
                        }
                    }
                    $('#organCB').combobox('setValue', currentOrganUuid);
                    //获取所有数据到表格
                    getDevices(currentOrganUuid);
                });
            $("#autoDeviceToolbar").css("display", "block");
        } else {
        	//非超级管理员不提供过滤功能
            getDevices();
            $("#autoDeviceToolbar").remove();
        }
    });
}
$(function() {
    //根据权限控制界面元素
    setView();
    //点击过滤
    $("#filterByOrganBtn").bind("click", function() {
        var value = $("#organCB").combobox("getValue");
        getDevices(value);
    });

});

/**
 * 查询数据用于在datagrid里面显示
 * @param params
 */
function getDevices(OrganUuid) {
    $.post("/smosplat/getAutoDevicesByOrganUuid", { organUuid: OrganUuid },
        function(data, status) {
            var deviceData = JSON.parse(data);
            $.jgrid.gridUnload('autoDeviceDG');
            //整理一下数据再放到datagrid中显示
            var deviceColNames = ['设备ID', '设备类型', '设备类型ID', '设备编号', '设备型号', '设备型号ID', '部门ID', '设备所属部门'];
            var deviceColModel = [
                { name: 'deviceUuid', hidden: true },
                { name: 'devTypeName', align: 'center' },
                { name: 'devTypeUuid', hidden: true },
                { name: 'sn', align: 'center' },
                { name: 'devModelName', align: 'center' },
                { name: 'devModelUuid', hidden: true },
                { name: 'organUuid', hidden: true },
                { name: 'organName', align: 'center' }
            ];
            //设备列表
            for (var i = 0; i < deviceData.rows.length; i++) {
                deviceData.rows[i]["devTypeName"] = deviceData.rows[i].devType != null ? deviceData.rows[i].devType.devTypeName : "";
                deviceData.rows[i]["devTypeUuid"] = deviceData.rows[i].devType != null ? deviceData.rows[i].devType.devTypeUuid : "";
                deviceData.rows[i]["devModelName"] = deviceData.rows[i].deviceModel != null ? deviceData.rows[i].deviceModel.devModelName : "";
                deviceData.rows[i]["devModelUuid"] = deviceData.rows[i].deviceModel != null ? deviceData.rows[i].deviceModel.devModelUuid : "";
                deviceData.rows[i]["organUuid"] = deviceData.rows[i].organ != null ? deviceData.rows[i].organ.organUuid : "";
                deviceData.rows[i]["organName"] = deviceData.rows[i].organ != null ? deviceData.rows[i].organ.organName : "";
            };
            $("#autoDeviceDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                data: deviceData.rows,
                colNames: deviceColNames,
                colModel: deviceColModel,
                rowNum: 15,
                rowList: [15, 30, 45],
                pager: $("#pager_autoDeviceDG"),
                autowidth: true,
                sortname: 'id',
                viewrecords: true
            });
            $("#autoDeviceDG").jqGrid('navGrid', '#pager_autoDeviceDG', { edit: false, add: false, del: false });
            $("#autoDeviceDG").setGridHeight($("#jqGrid_AutoDevice").height() - 75);
            $("#autoDeviceDG").jqGrid('setSelection', '1');

        }
    );
}
