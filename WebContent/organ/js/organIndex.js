var currentAccount;
var organUuid;
var account;
$(function() {
    //控制各大布局区块的宽度和高度
    //窗口高度
    var windowHeight = $(window).height();
    var rightHeight = $("#wrapper").height();
    //窗口宽度
    var windowWidth = $(window).width();
    //头部高度
    var headerHeight = $("#headerArea").height();
    //设置主题宽度和高度
    $("#content-main").height(windowHeight - headerHeight);
    //获得当前用户信息
    $.post("getCurrentAccount", function(data, status) {
        account = JSON.parse(data).account;
    	if(account.loginName=="superadmin"){
    		$("#toRoleBtn").hide();
    	}else{
    		$("#toRoleBtn").hide();
    	}
        currentAccount = account;
        organUuid = JSON.parse(data).organUuid;
        if (account != null) {
        	
        	$.post("getWorkerByAccount",{accountUuid:account.accountUuid}, function(data, status) {
        		var workerT=JSON.parse(data).worker;
        		 //显示用户名称
            	$("#workerInfo").text(workerT.workerName+"("+account.role.roleName+")");
                $("#userName").text(workerT.workerName+"("+account.role.roleName+")");
        	})
           
            //保存当前用户所属机构到session中并进行跳转
            $.post("/smosplat/setCurrentOrgan", { organUuid: organUuid }, function(data, status) {
                //默认选中第一行
            	$("#side-menu li").removeClass('selected');
                $("#side-menu li:eq(1)").addClass('selected');
                //加载第一个页面，如果是超级管理员，加载的页面不一样
                if (account.loginName == "superadmin") {
                    $(".J_iframe").attr("src", "organ");
                } else {
                    $(".J_iframe").attr("src", "organDetail?organUuid="+organUuid);
                }
                $("#organMgr").bind("click", function() {
                    //页面跳转时调用ifram里的方法进行跳转机构信息
                	$("#side-menu li").removeClass('selected');
                    $("#side-menu li:eq(1)").addClass('selected');
                    if (account.loginName == "superadmin") {
                        $(".J_iframe").attr("src", "organ");
                    } else {
                        $(".J_iframe").attr("src", "organDetail?organUuid="+organUuid);
                    }
                });
                $("#workerMgr").bind("click", function() {
                    $("#side-menu li").removeClass('selected');
                    $("#side-menu li:eq(2)").addClass('selected');
                    $("#J_iframe").attr("src", "worker");
                });
                $("#deviceMgr").bind("click", function() {
                    $("#side-menu li").removeClass('selected');
                    $("#side-menu li:eq(3)").addClass('selected');
                    $("#J_iframe").attr("src", "device");
                });
                $("#autoDeviceMgr").bind("click", function() {
                    $("#side-menu li").removeClass('selected');
                    $("#side-menu li:eq(4)").addClass('selected');
                    $("#J_iframe").attr("src", "autoDevice");
                });
            });

        }
    });
    //修改个人信息按钮
    $("#changeUser").bind("click", function() {
        $.post("/smosplat/getWorkerByAccount", { accountUuid: account.accountUuid }, function(data, status) {
            var jsonData = JSON.parse(data);
            var worker = jsonData.worker;
            $("#accountName").val(worker.account.accountName);
            $("#phone").val(worker.phone);
            $("#email").val(worker.email);
            $("#paperID").val(worker.paperID);
            $("#newPassword").val(worker.account.password);
            $("#confirmPassword").val(worker.account.password);
            $("#portrait")[0].src = "";
            //填充头像
            $.post("getWorkerFileUrls", {workerUuid: worker.workerUuid }, function(data, status) {
                var jsonPaths = JSON.parse(data);
                var workerPortrait = jsonPaths.workerPortrait;
                if (workerPortrait != null) {
                    //获取当前网址，如： http://localhost:8080/Tmall/index.jsp
                    var curWwwPath = window.document.location.href;
                    //获取主机地址之后的目录如：/Tmall/index.jsp
                    var pathName = window.document.location.pathname;
                    var pos = curWwwPath.indexOf(pathName);
                    //获取主机地址，如： http://localhost:8080
                    var localhostPath = curWwwPath.substring(0, pos);
                    //加载worker照片
//                    $("#portrait").attr('src', localhostPath + '/' + path);
                    $("#portrait").attr('src', localhostPath  + workerPortrait);
                } else {
                    return;
                }
            });
        });
    });
    //为按钮添加点击事件
    $("#indexMgr").bind("click", function() {
        //跳转回主页
        window.location.href = "/smosplat/projectIndex?type=list";
    });

    $('#portraitUpload').on('click', function(e) {
        $("#portrait_input").trigger('click');
    })
    $("#portrait_input").bind('change', function(e) {
        previewImage(this, "localImage", "portrait");
    });
    $(".previewImage").bind("click", function(data, status) {
        if ($(this)[0].src.indexOf(".png") != -1 || $(this)[0].src.indexOf(".jpg") != -1 || $(this)[0].src.indexOf(".jpeg") != -1) {
            var src = $(this)[0].src;
            window.open(src, "", "toolbar=no,scrollbars=no,menubar=no");
        }
    });

});
//退出
function back2Login() {
    //清空session
    $.post("logout", function(data, status) {
        var jsonData = JSON.parse(data);
        if (jsonData.result == 0) {
            //回到登录页
            window.location.href = "/smosplat/";
        }
    });

}
//修改个人信息
function modifyPw() {
    if (account.loginName == "superadmin") {
        swal({title:"超级管理员无法修改个人信息！",type:"warning"});
        return;
    }
        var jsonData = "";
        if ($("#newPassword").val() == $("#confirmPassword").val()) {
        	if($("#newPassword").val()!=undefined && $("#confirmPassword").val()!=undefined){
        		currentAccount.password = $("#newPassword").val();
        	}
            currentAccount.accountName = $("#accountName").val();
            var params = {
                accountUuid: currentAccount.accountUuid,
                accountName: $("#accountName").val(),
                loginName: currentAccount.loginName,
                resetPassword: true,
                password: $("#newPassword").val()
            };
            $.post("updateAccount", params, function(data, status) { //更新账号
                jsonData = JSON.parse(data);
                if (jsonData.result == 0) {
                    $("#userName")[0].innerHTML = $("#accountName").val();
                    $.post("getWorkerByAccount", { accountUuid: currentAccount.accountUuid }, function(data, status) { //根据账号获取人员
                        jsonData = JSON.parse(data);
                        var worker = jsonData.worker;
                        var params = new Array();
                        params.push(organUuid);
                        params.push(worker.workerUuid);
//                        if ($("#portrait_input")[0].value != null) {
                            //上传个人照片,用个人Uuid作文件夹
                        	//4代表worker
                            importFileClick("portrait_input", "portraitForm", 4, "img", params, function(_url) {
                            	var tempPhotoUrl;
                            	if(_url == undefined){
                            		_url = worker.photoURL;
                            	}else{
                            		tempPhotoUrl = worker.photoURL;
                            	}
                                worker['phone'] = $("#phone").val();
//                                worker['photoURL']=_url;
                                worker['email'] = $("#email").val();
                                worker['paperID'] = $("#paperID").val();
                                worker['account'] = currentAccount;
                                var workerJson = JSON.stringify(worker);
                                $.post("updateWorker", { workerJson: workerJson }, function(data, status) { //更新人员
                                    jsonData = JSON.parse(data);
                                    if (jsonData.result == 0) {
                                        $('#modifyPWDialog').modal('hide');
                                        if(tempPhotoUrl != undefined){
                                        //删除原来的照片
	                                        $.post("delete", { url: tempPhotoUrl }, function(data, status) {
	
	                                        });
                                        }
                                    } else {
                                        swal({title:"失败！",text:jsonData.msg,type:"error"});
                                    }
                                });
                            });
//                        }

                    });
                } else {
                    swal({title:"更新账号失败！",type:"warning"});
                    return;
                }
            });
        } else {
            swal({title:"两次输入密码不一，请重新输入！",type:"warning"});
            return;
        }

};


