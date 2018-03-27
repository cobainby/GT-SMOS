var id;
var organUuid;

var organColNames = new Array();
var organColModel = new Array();
organColNames = ['机构ID', '机构名称', '登录名', '昵称', '联系人', '联系人电话', '联系人邮箱', '监督员','监督员电话','详细信息', '修改', '删除'];
organColModel = [{
    name: 'organUuid',
    hidden: true
}, {
    name: 'organName',
    align: 'center'
}, {
    name: 'loginName',
    align: 'center'
}, {
    name: 'accountName',
    align: 'center'
}, {
    name: 'workerName',
    align: 'center'
}, {
    name: 'phone',
    align: 'center'
}, {
    name: 'email',
    align: 'center'
}, {
    name: 'supervisorName',
    align: 'center'
}, {
    name: 'supervisorPhone',
    align: 'center'
}, {
    name: 'view',
    formatter: formatView,
    align: "center"
}, {
    name: 'edit',
    formatter: formatEdit,
    align: "center"
}, {
    name: 'delete',
    formatter: formatDelete,
    align: 'center'
}];
$(function() {
    //加载数据
    //根据分页获得数据以初始化datagrid
    var pageSize = 10; //当前记录数
    var pageNumber = 1; //初始页码
    getOrgans({ pageNumber: pageNumber, pageSize: pageSize });
    //增加机构
    $("#addOrganBtn").bind('click', function() {
        $('#addOrganForm')[0].reset();
    });
    $("#cancelSaveOrganBtn").bind('click', function() {
    	$('#addOrganForm').data('validator').resetForm();
    });
    //增加机构保存按钮点击
    $("#saveOrganBtn").bind('click', function() {
        //先验证表单，如果表单验证不通过则不提交
        var val = $("#addOrganForm").valid();
        if (!val) {
            return;
        }
        var password=$("#password").val();
        var confirmPassword=$("#confirmPassword").val();
        if(password!=confirmPassword){
        	alert("机构联系人密码和确认密码不匹配！");
        	return;
        }
        var supervisorPassword=$("#password").val();
        var supervisorConfirmPassword=$("#confirmPassword").val();
        if(supervisorPassword!=supervisorConfirmPassword){
        	alert("监督员密码和确认密码不匹配！");
        	return;
        }
        var params = {
            organName: $("#organName").val(),
            loginName: $("#loginName").val(),
            accountName: $("#accountName").val(),
            password: password,
            workerName: $("#workerName").val(),
            phone: $("#phone").val(),
            email: $("#email").val()
//            supervisorName:$("#supervisorName").val(),
//            supervisorPhone:$("#supervisorPhone").val(),
//            supervisorEmail:$("#supervisorEmail").val(),
//            supervisorLoginName:$("#supervisorLoginName").val(),
//            supervisorAccountName:$("#supervisorAccountName").val(),
//            supervisorPassword:$("#supervisorPassword").val()
        };
        $.post("/smosplat/addOrgan", params, function(data, status) {
            if (JSON.parse(data).result == 0) {
                var organUuid = JSON.parse(data).entity.organUuid;
                $('#addOrganDialog').modal('hide');
                swal({title:"增加成功！",type:"success"});
                //加载新数据
                params.organUuid = organUuid;
                var gridNumbers = $("#organDG").jqGrid('getRowData').length;
                $("#organDG").jqGrid("addRowData", gridNumbers + 1, params, "last");
            } else {
                swal({title:"增加失败！",text:JSON.parse(data).msg,type:"error"});
            }
        });
    });
});

/**
 * 删除机构
 */
function deleteOrgan(index) {
    $("#organDG").jqGrid('setSelection', index);
    var selectedData = $("#organDG").jqGrid("getRowData", index);
    //超级管理员机构不能删除
    if (selectedData.loginName == "superadmin") {
        swal({title:"超级管理员机构不允许删除！",type:"warning"});
        return;
    }
    swal({
        title: "您确定要删除机构相关的人员、账号、仪器等信息吗",
        text: "删除后将无法恢复，请谨慎操作！",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/deleteOrgan", { organUuid: selectedData.organUuid }, function(data, status) {
                if (JSON.parse(data).result == 0) {
                    swal("删除成功！", "您已经永久删除了这条信息。", "success")
                        //重新加载数据
                    $("#organDG").jqGrid('delRowData', index);
                } else {
                	swal({title:"删除失败！",text:JSON.parse(data).msg,type:"error"});
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    })
}
//cellvalue - 当前cell的值  
//options - 该cell的options设置，包括{rowId, colModel,pos,gid}  
//rowObject - 当前cell所在row的值，如{ id=1, name="name1", price=123.1, ...}
function formatView(cellvalue, options, rowObject) {
    var organUuid = rowObject.organUuid;
    return '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="organDetail(\'' + organUuid + '\')">查看</a>';
}

function formatEdit(cellvalue, options, rowObject) {
    var loginName = rowObject.loginName;
    var organUuid = rowObject.organUuid;
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="organEdit(\'' + organUuid + "','" + loginName + '\')">编辑</a>';
}

function formatDelete(cellvalue, options, rowObject) {
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteOrgan(' + options.rowId + ')" >删除</a>';
}

/**
 * 查询机构的数据用于在datagrid里面显示
 * @param params
 */
function getOrgans(params) {
    if (params == null) {
        params = {};
    }
    $.post("/smosplat/getOrgans", params,
        function(data, status) {
            //整理一下数据再放到datagrid中显示
            var jsonData = JSON.parse(data);
            for (var i = 0; i < jsonData.rows.length; i++) {
                jsonData.rows[i]["loginName"] = jsonData.workers[i].account.loginName;
                jsonData.rows[i]["accountName"] = jsonData.workers[i].account.accountName;
                jsonData.rows[i]["password"] = jsonData.workers[i].account.password;
                jsonData.rows[i]["workerName"] = jsonData.workers[i].workerName;
                jsonData.rows[i]["phone"] = jsonData.workers[i].phone;
                jsonData.rows[i]["email"] = jsonData.workers[i].email;
//                jsonData.rows[i]["supervisorName"] = jsonData.supervisors[i].workerName;
//                jsonData.rows[i]["supervisorPhone"] = jsonData.supervisors[i].phone;
            }
            $("#organDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                data: jsonData.rows,
                colNames: organColNames,
                colModel: organColModel,
                autowidth: true,
                rowNum: 15,
                rowList: [15, 30, 45],
                pager: $("#pager_organDG"),
                sortname: 'id',
                gridview: true,
                viewrecords: true
            });
            $("#organDG").jqGrid('navGrid', '#pager_organDG', { edit: false, add: false, del: false });
            $("#organDG").setGridHeight($("#jqGrid_organ").height() - 75);
            $("#organDG").jqGrid('setSelection', '1');

        }
    );
}

function organDetail(organUuid) {
	//加载机构信息查看界面
    window.location.href = "/smosplat/organDetail?organUuid="+organUuid;
}

function organEdit(organUuid, loginName) {
    if (loginName == "superadmin") {
        swal({title:"超级管理员机构不允许修改！",type:"warning"});
        return;
    }
    //加载机构信息编辑界面
    window.location.href = "/smosplat/organEdit?organUuid="+organUuid;
}
