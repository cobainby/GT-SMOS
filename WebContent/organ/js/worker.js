/**
 * 获得权限数据
 */
var roleItemData;
//机构id
var currentOrganUuid;
var map;
var scaleControl;
var navControl;
var overviewControl;
//gps查询时传入的工程id
var projectID;
//gps查询时的工程全称
var selTxt;
/**
 * 定义jqGrid的绑定列
 */
var wokerColNames = new Array();
var wokerColModel = new Array();
wokerColNames = ['人员ID', '账户ID', '姓名', '学历', '专业', '职称', '岗位', '证书编号', '账户登录名称', '权限等级', '操作', '创建人', '昵称'];
wokerColModel = [{
    name: 'workerUuid',
    hidden: true
}, {
    name: 'account.accountUuid',
    hidden: true
}, {
    name: 'workerName',
    align: 'center',
    width: 110
}, {
    name: 'education',
    align: 'center'
}, {
    name: 'titleMajor',
    align: 'center'
}, {
    name: 'jobTitle',
    align: 'center'
}, {
    name: 'workerType',
    align: 'center'
}, {
    name: 'paperID',
    align: 'center'
}, {
    name: 'account.loginName',
    align: 'center',
    hidden: true
}, {
    name: 'account.role.roleName',
    align: 'center'
}, {
    name: 'view',
    formatter: formatView,
    sortable: false,
    align: "left",
    width: 300
}, {
    name: 'account.creatorUuid',
    align: 'center',
    hidden: true
}, {
    name: 'account.accountName',
    align: 'center',
    hidden: true
}];
//gps留痕信息
var gpsColNames = new Array();
var gpsColModel = new Array();
gpsColNames = ['上传时间', '经度', '纬度'];
gpsColModel = [{
    name: 'time',
    align: 'center',
    cellattr: addCellAttr,
    formatter: formateTimestamp
}, {
    name: 'lon',
    hidden: true
}, {
    name: 'lat',
    hidden: true
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

function hasRoleItem(number) {
    for (var i = 0; i < roleItemData.roleItems.length; i++) {
        if (roleItemData.roleItems[i].number == number) {
            return true;
        }
    }
    return false;
}

function getColumnIndex(name) {
    for (var i = 0; i < wokerColNames.length; i++) {
        if (wokerColNames[i] == name) {
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
        if (!hasRoleItem(201)) {
            $("#addWorkerBtn").css("visibility", "hidden");
        }
        if (!hasRoleItem(202)) {
            $("#workerToolbar").css("visibility", "hidden");
        }
        if (!hasRoleItem(205)) {
            var index = getColumnIndex("权限等级");
            wokerColNames.splice(index, 1);
            wokerColModel.splice(index, 1);
        }
        if (!hasRoleItem(207)) {
            var index = getColumnIndex("修改基本信息");
            wokerColNames.splice(index, 1);
            wokerColModel.splice(index, 1);
        }
        if (!hasRoleItem(206)) {
            var index = getColumnIndex("修改账户信息");
            wokerColNames.splice(index, 1);
            wokerColModel.splice(index, 1);
        }
        if (!hasRoleItem(204)) {
            var index = getColumnIndex("删除");
            wokerColNames.splice(index, 1);
            wokerColModel.splice(index, 1);
        }
    }
}

$(function() {
    //
    var bodyHeight = $('#monitorBody').height();
    var bodyWidth = $('#monitorBody').width();
    //设置模态框弹出的表格的高度
    //普通账户不允许编辑
    $.post("getCurrentAccount", function(data, status) {
        var account = JSON.parse(data).account;
        $.post("getRoleByAccount", { accountUuid: account.accountUuid }, function(data, status) {
            var role = JSON.parse(data).role;
            if (role.roleName == "普通账号" || role.roleName == "访客账号") {
                $("#addWorkerBtn").hide();
            }
        })
    })
    $("#gps-modal").height(bodyHeight - 100);
    $("#gps-modal").width(bodyWidth - 150);
    $("#gpsMap").height($("#gps-modal").height() - 50);
    $("#gpsMap").width($("#gps-modal").width - 50);
    createMap(); //创建地图
    setMapEvent(); //设置地图事件
    addMapControl(); //向地图添加控件
    navControl.show();
    //根据权限控制界面元素
    setView();
    //加载数据
    //根据分页获得数据以初始化datagrid
    var pageSize = 10; //当前记录数
    var pageNumber = 1; //初始页码
    getWorkers({ pageNumber: pageNumber, pageSize: pageSize });
    //如果是超级管理员，则获取所有机构用于过滤列表
    if (roleItemData.result == -1) {
        getOrgansForCombobox();
    }
    $.post("/smosplat/getProjects", function(data, status) {
        //整理一下数据再放到datagrid中显示
        var allProject = JSON.parse(data).rows;
        if (allProject.length == 0) {
            return;
        }
        for (var i = 0; i < allProject.length; i++) {
            $(".dropdown-option").append("<li><a id=" + allProject[i].projectUuid + ">" + allProject[i].projectName + "</a></li>");
        }
    });
    //增加人员
    $("#addWorkerBtn").bind('click', function() {
        $('#addWorkerDialog').modal("show");
        $('#addWorkerForm').form('clear');
        $('#loginGroup').form('clear');
        $('#noAccount').prop('checked', true);

    });
    //取消人员增加
    $("#cancelupdateWorkerBtn").bind('click', function() {
        $('#addWorkerForm').data('validator').resetForm();
    });
    //指定账号单选按钮事件
    $("#noAccount").bind("click", function() {
        $('#loginGroup').css('display', "none");
    });
    $("#hasAccount").bind("click", function() {
        $('#loginGroup').css('display', "block");
    });
    //增加人员保存按钮点击
    $("#saveWorkerBtn").bind('click', function() {
        //先验证表单，如果表单验证不通过则不提交
        var val = $("#addWorkerForm").valid();
        if (!val) {
            return;
        }
        var checkedValue = $("input[name='Account']:checked").val();
        if (checkedValue == "hasAccount") {
            //验证附加表单
            var val = $("#loginGroup").valid();
            if (!val) {
                return;
            }
        }
        if ($("#password").val() != $("#password1").val()) {
            swal({ title: "密码输入前后不一致！", type: "warning" });
            return;
        }
        var params = {
            workerName: $("#workerName").val(),
            phone: $("#phone").val(),
            //            email: $("#email").val(),
            duty: $("#duty").val(),
            sex: $("#sex").val(),
            jobTitle: $("#jobTitle").val(),
            titleMajor: $("#titleMajor").val(),
            idNumber: $("#idNumber").val(),
            workYear: $("#workYear").val(),
            education: $("#education").val(),
            graduationInfo: $("#graduationInfo").val(),
            //$号分隔时间和工作经历，#号分隔行
            resume: $("#resumePeriod1").val() + "$" + $("#resume1").val() + "#" + $("#resumePeriod2").val() + "$" + $("#resume2").val() + "#" + $("#resumePeriod3").val() + "$" + $("#resume3").val(),
            registerdType: $("#registerdType").val(),
            characterId: $("#characterId").val(),
            registerdPaperId: $("#registerdPaperId").val(),
            workerType: $("input[name='workerType']:checked").val(),
            paperID: $("#paperID").val(),
            hasAccount: false
        };
        if (roleItemData.result == -1) {
            params.selectOrganUuid = $("#filterOrgans").combobox('getValue');
        }
        //加上账号的参数
        if (checkedValue == "hasAccount") {
            params.hasAccount = true;
            params.loginName = $("#loginName").val();
            params.accountName = $("#accountName").val();
            params.password = $("#password").val();
        }
        $.post("/smosplat/addWorker", params, function(data, status) {
            var jsonData = JSON.parse(data);
            if (jsonData.result == 0) {
                //加载新数据
                var gridNumbers = $("#workerDG").jqGrid('getRowData').length;
                $("#workerDG").jqGrid("addRowData", gridNumbers + 1, JSON.parse(data).entity, "last");
                swal({ title: "增加成功！", type: "success" });

                //提交相片
                //获取form数据
                var files = $("#portraitForm")[0].elements.file.files;
                //form选择了相片才要上传
                if (files.length > 0) {
                    var formData = new FormData($("#portraitForm")[0]);
                    formData.append("organUuid", currentOrganUuid);
                    formData.append("workerUuid", jsonData.entity.workerUuid);
                    formData.append("type", 0); //0是头像
                    $.ajax({
                        url: "uploadWorkerFiles",
                        type: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function(returnInfo) {
                            //                          swal({title:"操作成功！",type:"success"});
                        },
                        error: function(returnInfo) {
                            //上传失败时显示上传失败信息
                            swal({ title: "文件上传失败！", type: "error" });
                        }
                    });
                }
                var paper = $("#paperIDForm")[0].elements.file.files;
                //form选择了文件才要上传
                if (paper.length > 0) {
                    var formData = new FormData($("#paperIDForm")[0]);
                    formData.append("organUuid", currentOrganUuid);
                    formData.append("workerUuid", jsonData.entity.workerUuid);
                    formData.append("type", 1); //1是上岗证
                    $.ajax({
                        url: "uploadWorkerFiles",
                        type: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function(returnInfo) {
                            //                          swal({title:"操作成功！",type:"success"});
                        },
                        error: function(returnInfo) {
                            //上传失败时显示上传失败信息
                            swal({ title: "文件上传失败！", type: "error" });
                        }
                    });
                }

                $('#addWorkerDialog').modal('hide');
            } else {
                swal({ title: "增加失败！", text: JSON.parse(data).msg, type: "error" });
            }
        });
    });

    //修改人员保存按钮点击
    $("#updateWorkerBtn").bind('click', function() {
        //先验证表单，如果表单验证不通过则不提交
        var val = $("#updateWorkerForm").valid();
        if (!val) {
            return;
        }

        //提交相片
        var files = $("#changePortraitForm")[0].elements.file.files;
        //form选择了相片才要上传
        if (files.length > 0) {
            //获取form数据
            var formData = new FormData($("#changePortraitForm")[0]);
            formData.append("organUuid", currentOrganUuid);
            formData.append("workerUuid", $("#updateWorkerUuid").val());
            formData.append("type", 0); //0是头像
            $.ajax({
                url: "uploadWorkerFiles",
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function(returnInfo) {
                    //                      $('#addDeviceDialog').modal('hide');
                    swal({ title: "操作成功！", type: "success" });
                },
                error: function(returnInfo) {
                    //上传失败时显示上传失败信息
                    swal({ title: "文件上传失败！", type: "error" });
                }
            });
        }
        var paper = $("#changePaperIDForm")[0].elements.file.files;
        //form选择了文件才要上传
        if (paper.length > 0) {
            var formData = new FormData($("#changePaperIDForm")[0]);
            formData.append("organUuid", currentOrganUuid);
            formData.append("workerUuid", $("#updateWorkerUuid").val());
            formData.append("type", 1); //1是上岗证
            $.ajax({
                url: "uploadWorkerFiles",
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function(returnInfo) {
                    //                      swal({title:"操作成功！",type:"success"});
                },
                error: function(returnInfo) {
                    //上传失败时显示上传失败信息
                    swal({ title: "文件上传失败！", type: "error" });
                }
            });
        }

        var workerType = $("input[name='updateWorkerType']:checked").val();
        var params = {
            workerUuid: $("#updateWorkerUuid").val(),
            workerName: $("#updateWorkerName").val(),
            phone: $("#updatePhone").val(),
            //            email: $("#updateEmail").val(),
            paperID: $("#updatePaperID").val(),
            duty: $("#updateDuty").val(),
            sex: $("#updateSex").val(),
            jobTitle: $("#updateTitle").val(),
            birthday: $("#updateBirthday").val(),
            titleMajor: $("#updateTitleMajor").val(),
            jobTitle: $("#updateTitle").val(),
            idNumber: $("#updateIdNumber").val(),
            workYear: $("#updateWorkYear").val(),
            education: $("#updateEducation").val(),
            graduationInfo: $("#updateGraduationInfo").val(),
            //$号分隔时间和工作经历，#号分隔行
            resume: $("#updateResumePeriod1").val() + "$" + $("#updateResume1").val() + "#" + $("#updateResumePeriod2").val() + "$" + $("#updateResume2").val() + "#" + $("#updateResumePeriod3").val() + "$" + $("#updateResume3").val(),
            registerdType: $("#updateRegisterdType").val(),
            characterId: $("#updateCharacterId").val(),
            registerdPaperId: $("#updateRegisterdPaperId").val(),
            workerType: workerType
        };
        var workerJson = JSON.stringify(params);
        $.post("/smosplat/updateWorker", { workerJson: workerJson }, function(data, status) {
            var dataObj = JSON.parse(data);
            if (dataObj.result == 0) {
                $("#updateWorkerDialog").modal('hide');
                swal({ title: "修改成功！", type: "success" });
                //获取当前点击的行
                var selID = $("#workerDG").jqGrid("getGridParam", "selrow");
                //更新前端数据
                $("#workerDG").jqGrid('setCell', selID, 'workerName', params.workerName);
                $("#workerDG").jqGrid('setCell', selID, 'phone', params.phone);
                //                $("#workerDG").jqGrid('setCell', selID, 'email', params.email);
                $("#workerDG").jqGrid('setCell', selID, 'paperID', params.paperID);
                $('#updateWorkerForm').form('clear');
            } else {
                swal({ title: "修改失败！", text: dataObj.msg, type: "error" });
            }
        });
    });
    //修改人员取消
    $("#cancelupdateWorkerBtn").bind('click', function() {
        $('#addWorkerForm').form('clear');
        $('#loginGroup').form('clear');
    });
    //修改账号复选框事件
    $("#resetPassword").bind("change", function() {
        var isChecked = $("#resetPassword").is(':checked');
        if (isChecked) {
            $('#updateAccountForm1').css('display', "block");
        } else {
            $('#updateAccountForm1').css('display', "none");
        }
    });
    //修改账号保存按钮点击
    $("#updateAccountBtn").bind('click', function() {
        //先验证表单，如果表单验证不通过则不提交
        var val = $("#updateAccountForm").valid();
        if (!val) {
            return;
        }
        if ($("#updatePassword").val() != $("#updatePassword1").val()) {
            swal({ title: "密码输入前后不一致！", type: "warning" });
            return;
        }
        var isChecked = $("#resetPassword").is(':checked');
        if (isChecked == true) {
            //验证附加表单
            var val = $("#updateAccountForm1").valid();
            if (!val) {
                return;
            }
        }
        var params = {
            accountUuid: $("#updateAccountUuid").val(),
            accountName: $("#updateAccountName").val(),
            loginName: $("#updateLoginName").val(),
            resetPassword: false
        };
        //加上账号的参数
        if (isChecked == true) {
            params.resetPassword = true;
            params.password = $("#updatePassword").val();
        }
        $.post("/smosplat/updateAccount", params, function(data, status) {
            var dataObj = JSON.parse(data);
            if (dataObj.result == 0) {
                $("#updateAccountDialog").modal('hide');
                swal({ title: "修改成功！", type: "success" });
                //获取当前点击的行
                var selID = $("#workerDG").jqGrid("getGridParam", "selrow");
                //更新前端数据
                //更新前端数据
                $("#workerDG").jqGrid('setCell', selID, 'account.accountName', params.accountName);
                $("#workerDG").jqGrid('setCell', selID, 'account.loginName', params.loginName);
                $('#updateAccountForm').form('clear');
                $('#updateAccountForm1').form('clear');
            } else {
                swal({ title: "修改失败！", text: dataObj.msg, type: "error" });
            }
        });
    });
    //修改账号取消
    $("#cancelupdateAccountBtn").bind('click', function() {
        $('#updateAccountForm').form('clear');
        $('#updateAccountForm1').form('clear');
    });
    //点击查看大图
    $(".previewImage").bind("click", function(e) {
        if ($(this)[0].src.indexOf(".png") != -1 || $(this)[0].src.indexOf(".jpg") != -1 || $(this)[0].src.indexOf(".jpeg") != -1) {
            var src = $(this)[0].src;
            window.open(src, "", "toolbar=no,scrollbars=no,menubar=no");
        }
    });
    //上传照片刷新缩略图
    $('#portraitUpload').on('click', function(e) {
        $("#portrait_input").trigger('click');
    })
    $("#portrait_input").bind('change', function(e) {
        //上传前预览
        previewImage(this, "portraitDIV", "portrait");
    });

    $('#changePortraitUpload').on('click', function(e) {
        $("#changePortrait_input").trigger('click');
    })
    $("#changePortrait_input").bind('change', function(e) {
        //上传前预览
        previewImage(this, "updatePortraitDIV", "changePortrait");
        //        importFileClick("changePortrait_input", "changePortraitForm", "organUpload", "img", "", function(url) {
        //            $("#updatePhotoURL").val(url);
        //        });
    });
    $("#u_viewPaperID").bind("click", function(e) {
        var src = $(this).attr("value");
        if (src != "" && src != undefined) {
            window.open(src, "", "toolbar=no,scrollbars=no,menubar=no");
        }
    });
    $("#v_viewPaperID").bind("click", function(e) {
        var src = $(this).attr("value");
        if (src != "" && src != undefined) {
            window.open(src, "", "toolbar=no,scrollbars=no,menubar=no");
        }
    });


    //上传照片刷新缩略图(增加人员)
    $('#uploadPaperID').on('click', function(e) {
        $("#paperID_input").trigger('click');
    })
    //(修改人员)上传图片
    $('#uploadPaperID_update').on('click', function(e) {
        $("#changePaperID_input").trigger('click');
    })

    //确定游客可查看工程
    $("#checkProjectBtn").bind('click', function() {
        var rowId = $("#workerDG").jqGrid("getGridParam", "selrow");
        //得到当前选中的游客ID
        var accountUuid = $("#workerDG").jqGrid('getRowData', rowId)["account.accountUuid"];
        //得到选中的工程
        var selectedID = $("#projectDG").jqGrid('getGridParam', 'selarrrow');
        var selectedItems = new Array();
        //遍历id,得到选中的数据
        for (var i = 0; i < selectedID.length; i++) {
            //遍历得到的多选框
            var myrow = $('#projectDG').jqGrid('getRowData', selectedID[i]);
            selectedItems.push(myrow);
        }
        var projectUuids = "";
        for (var i = 0; i < selectedItems.length; i++) {
            if (i == selectedItems.length - 1) {
                projectUuids = projectUuids + selectedItems[i].projectUuid;
            } else {
                projectUuids = projectUuids + selectedItems[i].projectUuid + ",";
            }
        }
        $.post("updateProjectForAccount", { accountUuid: accountUuid, projectUuids: projectUuids }, function(data, status) {
            if (JSON.parse(data).result == 0) {
                swal({ title: "保存成功！", type: "success" });
            } else {
                swal("失败！", JSON.parse(data).msg, "error");
            }
        });
        $('#setProjectDialog').modal("hide");
    });
    //点击工程下拉列表
    $('.J_dropdown').each(function() {
        var _this = $(this);
        var $sel = _this.find('.dropdown-sel');
        var $id = _this.find('.getProjectUuid');
        var $option = _this.find('.dropdown-option');
        var $as = $option.children('li').children('a');
        var index = -1;

        $sel.click(function(e) {
            e.stopPropagation();
            $option.toggle();
        });
        $(document).click(function() {
            $option.hide();
        });
        $option.on('click', 'li', function() {
            selTxt = $(this).children('a').text();
            var selID = $(this).children('a').attr("id");
            $id.val(selID);
            //截取一部分字段并添加省略号
            if (selTxt.length > 6) {
                var proTxt = selTxt.substr(0, 6) + "……";
                $sel.val(proTxt);
            } else {
                $sel.val(selTxt);
            }
            $('.tableListTitle3').html(selTxt);
            //清空列表
            $("#gpsListDG").jqGrid("clearGridData");
        });

        $sel.keyup(function(e) {
            var keycode = e.keyCode;
            if (keycode === 38) {
                console.log(index);
                if (index === -1) {
                    index = $as.length - 1;
                    $($as[index]).addClass('hover').parent().siblings().children('a').removeClass('hover');
                } else if (index === -1 || index === 0) {
                    index = $as.length - 1;
                    $($as[index]).addClass('hover').parent().siblings().children('a').removeClass('hover');
                } else {
                    index--;
                    $($as[index]).addClass('hover').parent().siblings().children('a').removeClass('hover');
                }
            } else if (keycode === 40) {
                if (index === ($as.length - 1)) {
                    index = 0;
                    $($as[index]).addClass('hover').parent().siblings().children('a').removeClass('hover');
                } else {
                    index++;
                    $($as[index]).addClass('hover').parent().siblings().children('a').removeClass('hover');
                }
            } else if (keycode === 13) {
                $sel.val($($as[index]).text());
                $option.hide();
            }
        });

    });

    //搜索gps留痕情况
    $("#getGPSInfoBtn").bind("click", function(data, status) {
        if ($("#gpsBeginTime").val() == "" || $("#gpsEndTime").val() == "") {
            swal({ title: "请选择完整时间段！", type: "warning" });
            return;
        }
        if ($('.getProjectUuid').val() == "" || $('.getProjectUuid').val() == undefined) {
            swal({ title: "请选择工程！", type: "warning" });
            return;
        }
        var gpsBeginTime = $("#gpsBeginTime").val();
        var gpsEndTime = $("#gpsEndTime").val();
        if (gpsBeginTime > gpsEndTime) {
            swal({ title: "初始时间不能超过终止时间！", type: "warning" });
            return;
        }
        $.post("getCurrentAccount", function(data, status) {
            projectID = $('.getProjectUuid').val();
            account = JSON.parse(data).account;
            $.post("getGPSInfo", { accountUuid: account.accountUuid, startTime: gpsBeginTime, endTime: gpsEndTime, projectUuid: projectID }, function(data, status) {
                var gpsData = JSON.parse(data).gpsInfo;
                if (gpsData.length == 0) {
                    swal({ title: "该时间段上无人作业！", type: "warning" });
                    return;
                }
                //排序
                gpsData.sort(function(a, b) {
                    return a.time - b.time;
                });
                //初始显示出所有gps时间
                addMapOverlay(gpsData);
                //显示工程名字全称
                $('.tableListTitle3').show();
                $.jgrid.gridUnload("#gpsListDG");
                //显示位置数据
                $("#gpsListDG").jqGrid({
                    datatype: "local",
                    styleUI: 'Bootstrap',
                    data: gpsData,
                    colNames: gpsColNames,
                    colModel: gpsColModel,
                    autowidth: true,
                    gridview: true,
                    viewrecords: true,
                    rowNum: 1000000,
                    //隐藏掉表头
                    gridComplete: function() {
                        $(this).closest('.ui-jqgrid-view').find('div.ui-jqgrid-hdiv').hide()
                    },
                    onSelectRow: function(rowId) { //单击选择行
                        if (rowId == null) {
                            return;
                        } else {
                            var rowData = $('#gpsListDG').jqGrid('getRowData', rowId);
                            //点击时切换显示气泡
                            showInfo(rowData.time, rowData.lon, rowData.lat);
                        }
                    }
                });
                $("#gpsListDG").setGridHeight(bodyHeight - 300);
                $("#gpsListDG").setGridWidth($(".floatWinRight").width());
                $("#gpsListDG").jqGrid('setSelection', 1);
            });
        });
    });
    $("#canceGpsBtn").bind("click", function(data, status) {
        $.jgrid.gridUnload("#gpsListDG");
        //添加之前清空已添加的
        map.clearOverlays();
        $("#gpsBeginTime").val("");
        $("#gpsEndTime").val("");
        $(".tableListTitle3").hide();
    });
});
/**
 * 查看人员详细信息
 * @param index
 * @returns
 */
function viewWorker(index) {
    //照片置空
    $("#viewPortrait")[0].src = "";
    $("#workerDG").jqGrid('setSelection', index);
    var selectedData = $("#workerDG").jqGrid("getRowData", index);
    $('#workerInfoDialog').form('clear');
    //填充数据
    $("#viewWorkerName").val(selectedData.workerName);
    $("#viewPhone").val(selectedData.phone);
    //    $("#viewEmail").val(selectedData.email);
    $("#viewPaperID").val(selectedData.paperID);
    if (selectedData.paperID == "") {
        $("#v_viewPaperID").hide();
    }
    $("#viewLoginName").val(selectedData['account.loginName']);
    $("#viewAccountName").val(selectedData['account.accountName']);

    $.post("getWorkerByWorkerUuid", { workerUuid: selectedData.workerUuid }, function(data, status) {
        var jsonData = JSON.parse(data);
        if (jsonData.result == 0) {
            var worker = jsonData.worker;
            $("#viewDuty").val(worker.duty),
                $("#viewSex").val(worker.sex),
                $("#viewTitle").val(worker.jobTitle),
                $("#viewTitleMajor").val(worker.titleMajor),
                $("#viewIdNumber").val(worker.idNumber),
                $("#viewWorkYear").val(worker.workYear),
                $("#viewWorkerType").val(worker.workerType),
                $("#viewEducation").val(worker.education),
                $("#viewGraduationInfo").val(worker.graduationInfo),
                $("#viewRegisterdPaperId").val(worker.registerdPaperId),
                $("#viewRegisterdType").val(worker.registerdType),
                $("#viewCharacterId").val(worker.characterId),
                $("#viewRegisterdPaperId").val(worker.registerdPaperId)
            //$号分隔时间和工作经历，#号分隔行
            var resume = worker.resume;
            var rp = new Array();
            var rr = new Array();
            if (resume != undefined && resume != null) {
                var r = resume.split('#');
                for (var i = 0; i < r.length; i++) {
                    rp.push(r[i].split('$')[0]);
                    rr.push(r[i].split('$')[1]);
                }
                for (var j = 0; j < rp.length; j++) {
                    var rpj = "viewResumePeriod" + (j + 1);
                    var rrj = "viewResume" + (j + 1);
                    $("#" + rpj).val(rp[j]);
                    $("#" + rrj).val(rr[j]);
                }
            }

        }
    });
    //获取图片
    $.post("getWorkerFileUrls", { organUuid: currentOrganUuid, workerUuid: selectedData.workerUuid }, function(data, status) {
        //置空
        $("#v_viewPaperID").attr("value", "");
        var jsonPaths = JSON.parse(data);
        var workerPortrait = jsonPaths.workerPortrait;
        if (jsonPaths.paperID != undefined && jsonPaths.paperID != "") {
            $("#v_viewPaperID").show();
            $("#v_viewPaperID").attr("value", jsonPaths.paperID);
        }
        if (workerPortrait != null && workerPortrait != undefined) {
            $("#viewPortrait").attr("src", workerPortrait);
        }
        $('#workerInfoDialog').modal('show');
    });
    var role;
    //根据账号id获取角色
    $.post("getRoleByAccount", { accountUuid: selectedData['account.accountUuid'] }, function(data, status) {
        var role = JSON.parse(data).role;
        $("#viewRoleName").val(role.roleName);
        //如果是访客账户，获取创建人
        if (role.mark == 3) {
            $("#creatorDiv").css("display", "block");
            var creatorUuid = selectedData['account.creatorUuid'];
            if (creatorUuid == null || creatorUuid == "") {
                return;
            }
            $.post("getAccountById", { accountUuid: creatorUuid }, function(data, status) {
                var accountObj = JSON.parse(data).entity;
                $("#viewCreatorName").val(accountObj.worker.workerName);
            });
        } else {
            $("#creatorDiv").css("display", "none");
        }
    });
}
/**
 * 修改人员
 */
function updateWorker(index) {
    var paperIDUrl;
    $("#workerDG").jqGrid('setSelection', index);
    var selectedData = $("#workerDG").jqGrid("getRowData", index);
    //超级管理员机构不能修改
    if (selectedData['account.loginName'] == "superadmin") {
        swal({ title: "超级管理员不允许修改！", type: "warning" });
        return;
    }
    $('#updateWorkerDialog').modal('show');
    $('#updateWorkerForm').form('clear');
    //填充数据
    $("#updateWorkerUuid").val(selectedData.workerUuid);
    $("#updateWorkerName").val(selectedData.workerName);
    $("#updatePhone").val(selectedData.phone);
    $("#updateEmail").val(selectedData.email);
    $("#updatePaperID").val(selectedData.paperID);
    if (selectedData.paperID == "") {
        $("#u_viewPaperID").hide();
    }
    $("#changePortrait")[0].src = "";

    $.post("getWorkerByWorkerUuid", { workerUuid: selectedData.workerUuid }, function(data, status) {
        var jsonData = JSON.parse(data);
        if (jsonData.result == 0) {
            var worker = jsonData.worker;
            //让radio默认选中数据库存着的工作类型
            var rObj = $("input[name='workerType']");
            for (var i = 0; i < rObj.length; i++) {
                if (rObj[i].value == worker.workerType) {
                    //                    rObj[i].checked =  'checked';
                    //                  $("input[name='workerType'][value=" + workerType + "]").prop("checked", true); 
                }
            }
            var workerTypes = $("input[name='updateWorkerType']");
            for (var i = 0; i < workerTypes.length; i++) {
                var wt = workerTypes[i].defaultValue;
                if (wt == worker.workerType) {
                    $("#" + workerTypes[i].id).prop('checked', 'checked');
                    break;
                }
            }
            $("#updateDuty").val(worker.duty),
                $("#updateSex").val(worker.sex),
                $("#updateTitle").val(worker.jobTitle),
                $("#updateTitleMajor").val(worker.titleMajor),
                $("#updateIdNumber").val(worker.idNumber),
                $("#updateWorkYear").val(worker.workYear),
                $("#updateEducation").val(worker.education),
                $("#updateGraduationInfo").val(worker.graduationInfo),
                $("#updateRegisterdType").val(worker.registerdType),
                $("#updateCharacterId").val(worker.characterId),
                $("#updateRegisterdPaperId").val(worker.registerdPaperId)
            //            $("#updateWorkerType").val(worker.workerType);
            //$号分隔时间和工作经历，#号分隔行
            var resume = worker.resume;
            var rp = new Array();
            var rr = new Array();
            if (resume != undefined && resume != null) {
                var r = resume.split('#');
                for (var i = 0; i < r.length; i++) {
                    rp.push(r[i].split('$')[0]);
                    rr.push(r[i].split('$')[1]);
                }
                for (var j = 0; j < rp.length; j++) {
                    var rpj = "updateResumePeriod" + (j + 1);
                    var rrj = "updateResume" + (j + 1);
                    $("#" + rpj).val(rp[j]);
                    $("#" + rrj).val(rr[j]);
                }
            }

        }
        //获取图片
        $.post("getWorkerFileUrls", { organUuid: currentOrganUuid, workerUuid: selectedData.workerUuid }, function(data, status) {
            $("#u_viewPaperID").attr("value", "");
            var jsonPaths = JSON.parse(data);
            var workerPortrait = jsonPaths.workerPortrait;
            paperIDUrl = jsonPaths.paperID;
            if (paperIDUrl != undefined && paperIDUrl != "") {
                $("#u_viewPaperID").show();
                $("#u_viewPaperID").attr("value", paperIDUrl);
            }
            if (workerPortrait != null && workerPortrait != undefined) {
                $("#changePortrait").attr("src", workerPortrait);
            }
        });
    });


    //验证一下表单
    $("#updateWorkerForm").valid();
}
/**
 * 修改人员对应的账号
 */
function updateAccountForWorker(index) {
    $("#workerDG").jqGrid('setSelection', index);
    var selectedData = $("#workerDG").jqGrid("getRowData", index);
    //超级管理员机构不能修改
    if (selectedData['account.loginName'] == "superadmin") {
        swal({ title: "超级管理员不允许修改！", type: "warning" });
        return;
    }
    $('#updateAccountDialog').modal('show');
    $('#updateAccountForm').form('clear');
    //填充数据
    $("#updateAccountUuid").val(selectedData['account.accountUuid']);
    $("#updateAccountName").val(selectedData['account.accountName']);
    $("#updateLoginName").val(selectedData['account.loginName']);
    //验证一下表单
    $("#updateAccountForm").valid();
}
/**
 * 设置访客能查看的工程
 */
function setProject(index) {
    //获取机构下所有工程以及该账户能访问的所有工程信息
    $("#workerDG").jqGrid('setSelection', index);
    var selectedData = $("#workerDG").jqGrid("getRowData", index);
    var accountUuid = selectedData['account.accountUuid'];
    var projectColNames = ['工程ID', '工程名称', '工程地址'];
    var projectColModel = [{
        name: 'projectUuid',
        align: 'center',
        hidden: true
    }, {
        name: 'projectName',
        align: 'center'
    }, {
        name: 'address',
        align: 'center'
    }];
    $.post("getProjectsByAccount", { accountUuid: accountUuid }, function(data, status) {
        var dataObj = JSON.parse(data);
        var allProjects = dataObj.allProjects;
        var projectsCanView = dataObj.projectsCanView;
        var selectedUuids = [];
        //得到已经被选中的工程Uuid
        for (var i = 0; i < projectsCanView.length; i++) {
            selectedUuids.push(projectsCanView[i].projectUuid);
        }
        $.jgrid.gridUnload("projectDG");
        $("#projectDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: allProjects,
            colNames: projectColNames,
            colModel: projectColModel,
            autowidth: true,
            rowNum: 15,
            rowList: [15, 30, 45],
            pager: "#pager_projectDG",
            sortname: 'id',
            gridview: true,
            viewrecords: true,
            multiselect: true
        });
        $('#setProjectDialog').modal('show');
        $("#projectDG").setGridHeight(400);
        $("#projectDG").setGridWidth($("#project-modal").width() - 32);
        for (var j = 0; j < allProjects.length; j++) {
            if (isInArray(selectedUuids, allProjects[j].projectUuid) == true) {
                $("#projectDG").jqGrid('setSelection', j + 1);
            }
        }
    });
}
/**
 * 查看GPS信息
 * @returns
 */
function viewGPS(index) {
    $('#viewGPSDialog').modal('show');
}
/**
 * 删除人员
 */
function deleteWorker(index) {
    $("#workerDG").jqGrid('setSelection', index);
    var selectedData = $("#workerDG").jqGrid("getRowData", index);
    //超级管理员机构不能删除
    if (selectedData['account.loginName'] == "superadmin") {
        swal({ title: "超级管理员不允许删除！", type: "warning" });
        return;
    }
    //弹出提示框确认
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
            $.post("/smosplat/deleteWorker", { workerUuid: selectedData.workerUuid }, function(data, status) {
                if (JSON.parse(data).result == 0) {
                    //重新加载数据
                    $("#workerDG").jqGrid('delRowData', index);
                    //删除设备对应的图片
                    $.post("/smosplat/deleteWorkerFiles", { organUuid: currentOrganUuid, workerUuid: selectedData.workerUuid }, function(data, status) {
                        var jsonObj = JSON.parse(data);
                        if (jsonObj.result == 0) {
                            swal("删除成功！", "您已经永久删除了这条信息。", "success")
                        } else {
                            swal({ title: "删除失败！", text: jsonObj.msg, type: "error" });
                        }
                    });
                } else {
                    swal({ title: "失败！", text: JSON.parse(data).msg, type: "error" });
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    })
}

function formatView(cellvalue, options, rowObject) {
    if (rowObject.account == null) {
        return '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a data-toggle="modal" onclick="viewWorker(' + options.rowId + ')">查看基本信息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="updateWorker(' + options.rowId + ')">修改基本信息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteWorker(' + options.rowId + ')">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
    } else {
        if (rowObject.account.role.mark == 3) {
            return '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a data-toggle="modal" onclick="viewWorker(' + options.rowId + ')">查看基本信息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="setProject(' + options.rowId + ')">查看工程设置</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteWorker(' + options.rowId + ')">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '</br>' + '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="updateWorker(' + options.rowId + ')">修改基本信息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="updateAccountForWorker(' + options.rowId + ')">修改账户信息</a>';
        } else {
            return '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a data-toggle="modal" onclick="viewWorker(' + options.rowId + ')">查看基本信息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '<img src="/smosplat/common/image/view.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="viewGPS(' + options.rowId + ')">查看GPS轨迹</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteWorker(' + options.rowId + ')">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + '</br>' + '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="updateWorker(' + options.rowId + ')">修改基本信息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="updateAccountForWorker(' + options.rowId + ')">修改账户信息</a>';
        }
    }

}

/**
 * 查询数据用于在datagrid里面显示
 * @param params
 */
function getWorkers(params) {
    if (params == null) {
        params = {};
    }
    $.post("/smosplat/getCurrentOrgan", function(data, status) {
        var jsonData = JSON.parse(data);
        if (jsonData.result != 0) {
            return;
        }
        currentOrganUuid = jsonData.currentOrganUuid;
        if (params.organUuid == undefined) {
            params['organUuid'] = currentOrganUuid;
        }
        $.post("/smosplat/getWorkers", params,
            function(data, status) {
                //整理一下数据再放到datagrid中显示
                var jsonData = JSON.parse(data);
                $("#workerDG").jqGrid({
                    datatype: "local",
                    styleUI: 'Bootstrap',
                    data: jsonData.rows,
                    colNames: wokerColNames,
                    colModel: wokerColModel,
                    autowidth: true,
                    rowNum: 15,
                    rowList: [15, 30, 45],
                    pager: "#pager_workerDG",
                    sortname: 'id',
                    gridview: true,
                    viewrecords: true
                });
                $("#workerDG").jqGrid('navGrid', '#pager_workerDG', { edit: false, add: false, del: false });
                $("#workerDG").setGridHeight($("#jqGrid_worker").height() - 75);
                $("#workerDG").jqGrid('setSelection', '1');
            }
        );
    });
}
/**
 * 则获取所有机构列表用于根据机构进行过滤
 */
function getOrgansForCombobox() {
    $.get("/smosplat/getAllOrgans", function(data, status) {
        var organs = JSON.parse(data).organs;
        if (organs.length == 0) {
            return;
        }
        //把当前选中的机构标在下拉框上
        $.post("/smosplat/getCurrentOrgan", function(data, status) {
            var jsonData = JSON.parse(data);
            if (jsonData.result != 0) {
                return;
            }
            currentOrganUuid = jsonData.currentOrganUuid;
            $("#filterOrgans").combobox("loadData", organs);
            var comboboxData = $('#filterOrgans').combobox('getData');
            $("#filterOrgans").combobox("setValue", currentOrganUuid);
        });
        $("#filterByOrganBtn").bind("click", function() {
            var value = $("#filterOrgans").combobox("getValue");
            //销毁原来的datagrid重新加载，不然jqgrid无法刷新
            $.jgrid.gridUnload('workerDG');
            getWorkers({ pageNumber: 1, pageSize: 15, organUuid: value });
        });
    });
}
//根据出生年月计算年龄
function ages(str) {
    var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if (r == null)
        return false;
    var d = new Date(r[1], r[3] - 1, r[4]);
    if (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] &&
        d.getDate() == r[4]) {
        var Y = new Date().getFullYear();
        return ((Y - r[1]));
    }
    return ("输入的日期格式错误！");
}

function addCellAttr(rowId, val, rawObject, cm, rdata) {
    return "style='color:#1B87B8'";
}
//时间转换
function formateTimestamp(cellvalue, options, rowObject) {
    var timestamp = rowObject.time;
    if (timestamp == null) {
        return "";
    }
    if (rowObject.within == "0") {
        var img = '<img src="/smosplat/common/image/flag_mark_green.png" style="width:16px;height:16px;margin-bottom:2px;margin-left:8px;">';
    } else if (rowObject.within == "-1" || rowObject.within == null) {
        var img = '<img src="/smosplat/common/image/flag_mark_red.png" style="width:16px;height:16px;margin-bottom:2px;margin-left:8px;">';
    }
    var datetime = new Date();
    datetime.setTime(timestamp);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second + img;
};

function createMap() {
    map = new BMap.Map("gpsMap");
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
//表格点击时地图居中并且展示信息窗口的逻辑
function showInfo(time, lon, lat) {
    var point = new BMap.Point(lon, lat);
    opts = "<table id='tableHeader' style='margin-left:-10px;'>" + "<tr>" + "<th style='width:200px;text-align:center;'>" + time + "</th>" + "</tr>" + "</table>";
    var infoWindow = new BMap.InfoWindow(opts); // 创建信息窗口对象 
    map.openInfoWindow(infoWindow, point); //开启信息窗口
    map.centerAndZoom(new BMap.Point(lon, lat), 18);
}
//添加标注
function addMapOverlay(gpsData) {
    //添加之前清空已添加的
    map.clearOverlays();
    for (var i = 0; i < gpsData.length; i++) {
        if (gpsData[i].within == "0") {
            var marker = new BMap.Marker(new BMap.Point(gpsData[i].lon, gpsData[i].lat), {
                icon: new BMap.Icon("/smosplat/common/image/flag_mark_green.png", new BMap.Size(20, 32), {})
            });
        }
        if (gpsData[i].within == "-1" || gpsData[i].within == null) {
            var marker = new BMap.Marker(new BMap.Point(gpsData[i].lon, gpsData[i].lat), {
                icon: new BMap.Icon("/smosplat/common/image/flag_mark_red.png", new BMap.Size(20, 32), {})
            });
        }
        var id = i;
        map.addOverlay(marker);
        //跳动的动画
        //        marker.setAnimation(BMAP_ANIMATION_BOUNCE);
        addClickHandler(gpsData[i].time, marker, id);
    }
}
//点击marker事件
function addClickHandler(time, marker, id) {
    marker.addEventListener("click", function(e) {
        openInfo(time, id, e);
    });
}
//添加信息窗口，展示当前选中的是哪个项目
function openInfo(time, id, e) {
    var p = e.target;
    var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
    map.centerAndZoom(point, 18);
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    datetime = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
    opts = "<table id='tableHeader' style='margin-left:-10px;'>" + "<tr>" + "<th style='width:200px;text-align:center;'>" + datetime + "</th>" + "</tr>" + "</table>";
    var infoWindow = new BMap.InfoWindow(opts); // 创建信息窗口对象 
    map.openInfoWindow(infoWindow, point); //开启信息窗口
    //通过传入的当前marker选中顺序确定表格行，并选中当前行
    $("#gpsListDG").jqGrid('setSelection', id + 1, false);
}