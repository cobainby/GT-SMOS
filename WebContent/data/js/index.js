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
    //获得当前工程项目名字
    $.post("/smosplat/getCurrentProjectInfo", function(data, status) {
        var projectName = JSON.parse(data).project.projectName;
        $("#projectName").text("工程名称：" + projectName);
    });
    $.get("getMonitorItemsByProject", function(data, status) {
        var MonitorItems = JSON.parse(data).monitorItems;
        if (MonitorItems != null) {
        	//添加监测概况
        	MonitorItems.push({
        		'number':'0',
        	    'monitorItemName':'监测概况'
        	});
            //排序
            MonitorItems.sort(function(a, b) {
                if (a.number < b.number) {
                    return -1;
                } else {
                    return 1;
                }
            });
            //显示用户名称
            for (var i = 0; i < MonitorItems.length; i++) {
                $("#projectList").append('<li onclick="getMonitor(' + MonitorItems[i].number +','+i+ ')">' + '<a href="#" id=' + MonitorItems[i].code + '>' + '<span class="nav-label">' + MonitorItems[i].monitorItemName + '</span>' + '</a>' + '</li>');
            }
            //默认选中第一个li，添加一个选中状态
            $("#J_iframe").attr("src", "overview");
            $("#side-menu li").removeClass('selected');
            $("#projectList li:eq(0)").addClass('selected');
            $("#projectList").addClass('open');
        }
    });
    //为按钮添加点击事件
    $("#indexMgr").bind("click", function() {
        //跳转回主页  
    	window.location.href = "/smosplat/projectIndex?type=list";
    });
    $("#projectMgr").bind("click",function(){
    	$("#side-menu li").removeClass('selected');
    	$("#side-menu li:eq(1)").addClass('selected');
    	$("#J_iframe").attr("src","basicInfo");
    	$("#projectList").removeClass('open');
    });
    $("#programMgr").bind("click",function(){
    	$("#side-menu li").removeClass('selected');
    	$("#side-menu li:eq(2)").addClass('selected');
    	$("#J_iframe").attr("src", "proPlan");
    	$("#projectList").removeClass('open');
    });
    $("#picMgr").bind("click",function(){
    	$("#side-menu li").removeClass('selected');
    	$("#side-menu li:eq(3)").addClass('selected');
    	$("#J_iframe").attr("src", "proPic");
    	$("#projectList").removeClass('open');
    });
    $("#monitorMgr").bind("click",function(){
    	$("#side-menu li").removeClass('selected');
    	$("#side-menu li:eq(4)").addClass('selected');
    	$("#J_iframe").attr("src", "proStatus");
    	$("#projectList").removeClass('open');
    });
    $("#viewData").bind("click",function(){
    	$("#side-menu li").removeClass('selected');
    	$("#side-menu li:eq(5)").addClass('selected');
    	$("#projectList").removeClass('open');
    });
    $("#dateExport").bind("click",function(){
    	$("#side-menu li").removeClass('selected');
    	$("#side-menu li:eq(-2)").addClass('selected');
    	$("#J_iframe").attr("src", "dateExport");
    	$("#projectList").removeClass('open');
    });
    $("#exportData").bind("click",function(){
    	$("#side-menu li").removeClass('selected');
    	$("#side-menu li:eq(-1)").addClass('selected');
    	$("#J_iframe").attr("src", "exPortData");
    	$("#projectList").removeClass('open');
    });
});
function getMonitor(number,i) {
    //去掉第一行默认的选中状态
    $("#side-menu li").removeClass('selected');
    $("#projectList li:eq("+i+")").addClass('selected');
    if(number=="0"){
    	//监测概况
    	$("#J_iframe").attr("src", "overview");
    }else if (number == "1") {
        //      围护墙(边坡)顶部水平位移
        $("#J_iframe").attr("src", "dataWYS");
    } else if (number == "4") {
        //      支护结构深层水平位移
        $("#J_iframe").attr("src", "dataCX");
    } else if (number == "5") {
        //      围护墙(边坡)顶部竖向位移
        $("#J_iframe").attr("src", "dataWYD");
    } else if (number == "6") {
        //      立柱竖向位移
        $("#J_iframe").attr("src", "dataLZ");
    } else if (number == "8") {
        //      周边建筑物竖向位移
        $("#J_iframe").attr("src", "dataSM");
    } else if (number == "10") {
        //      周边管线竖向位移
        $("#J_iframe").attr("src", "dataZGD");
    } else if (number == "12") {
        //      地下水位
        $("#J_iframe").attr("src", "dataSW");
    } else if (number == "15") {
        //      支撑内力
        $("#J_iframe").attr("src", "dataZC");
    } else if (number == "18") {
        //      锚杆内力
        $("#J_iframe").attr("src", "dataMT");
    }
}
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
