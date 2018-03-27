//$(function(){
//	$.post("getCurrentProjectInfo", function(data, status) {
//        var project = JSON.parse(data).project;
//        //获取图片
//        $.post("/smosplat/getProjectFileUrls", { projectUuid: project.projectUuid }, function(data, status) {
//            var dataObj = JSON.parse(data);
//            var projectPic = dataObj.projectPic;
//            if (projectPic != null) {
//                $("#pmtImage").attr("src", localhostPath + projectPic);
//            } else {
//                $("#pmtImage").attr("src", "/smosplat/common/image/thumbnail.png");
//            }
//        });
//    });
//    //原始数据导出
//    $("#btnOriginal").bind('click',function(){
//    	swal({ title: "暂无模板！", type: "error" });
//    })
//    //日报导出
//    $("#btnDaily").bind('click', function() {
//        if ($("#datePick").val() == "") {
//            swal({ title: "导出失败！", text: "请选择日期", type: "error" });
//            return;
//        }
//        var datePick = $("#datePick").val() + " " + "23:59:59";
//        //预加载界面
//        $.MyCommon.PageLoading({ loadingTips: '日报导出中,请稍候...' });
//        $.post("/smosplat/dailyReport", { dateStr: datePick }, function(data, status) {
//            if (JSON.parse(data).result == 0) {
//                if (JSON.parse(data).fileUrl != "" && JSON.parse(data).fileUrl != undefined) {
//                    window.location.href = JSON.parse(data).fileUrl;
//                    $('#loadingPage').remove();
//                } else {
//                    swal({ title: "导出失败！", text: "返回地址为空！", type: "error" });
//                    $('#loadingPage').remove();
//                }
//            }
//        });
//    });
//    //周报导出
//    $("#btnWeekly").bind('click', function() {
//        if ($("#startDate").val() == "") {
//            swal({ title: "导出失败！", text: "请选择初始日期", type: "error" });
//            return;
//        }
//        var startDate = $("#startDate").val() + " " + "00:00:00";
//        var endDate = $("#endDate").val() + " " + "23:59:59";
//        $.MyCommon.PageLoading({ loadingTips: '周报导出中,请稍候...' });
//        $.post("/smosplat/weeklyReport", { bDateStr: startDate, eDateStr: endDate }, function(data, status) {
//            if (JSON.parse(data).result == 0) {
//                if (JSON.parse(data).fileUrl != "" && JSON.parse(data).fileUrl != undefined) {
//                    window.location.href = JSON.parse(data).fileUrl;
//                    $('#loadingPage').remove();
//                } else {
//                    swal({ title: "导出失败！", text: "返回地址为空！", type: "error" });
//                    $('#loadingPage').remove();
//                }
//            }
//        });
//
//    })
//    $("#getWholePic").bind('click', function() {
//        var src = $("#pmtImage")[0].src;
//        window.open(src, "", "toolbar=no,scrollbars=no,menubar=no");
//    });
//})