var reportDataObj;
$(function(){
	$.post("getProjectReportByProject", function(data, status) {
        reportDataObj = JSON.parse(data).entity;
        if(reportDataObj!=null){
        	reportUuid = reportDataObj.reportUuid;
        }
    });
    $.post("/smosplat/getCurrentProjectInfo", function(data, status) {
        var dataObj = JSON.parse(data);
        var projectUuid = dataObj.project.projectUuid;
        $.post("getProjectByUuid", { projectUuid: projectUuid }, function(data, status) {
            allDataObj = JSON.parse(data).project;
            if (reportDataObj != null) {
                $("#p1p1").html(reportDataObj.p1p2);
                $("#p1p2").html(reportDataObj.p1p3);
                $("#p1p3").html(reportDataObj.p1p4);
                $("#p1p4").html(reportDataObj.p4p7);
                $("#p1p5").html(reportDataObj.p4p6);
                $("#p1p78").val(reportDataObj.p1p78);
                $("#p1p79").val(reportDataObj.p1p79);
                $("#p1p80").val(reportDataObj.p1p80);
                $("#p1p81").val(reportDataObj.p1p81);
                $("#p1p82").val(reportDataObj.p1p82);
                $("#p1p83").val(reportDataObj.p1p83);
                $("#p1p84").val(reportDataObj.p1p84);
                $("#p1p85").val(reportDataObj.p1p85);
                $("#p1p86").val(reportDataObj.p1p86);
                $("#p1p87").val(reportDataObj.p1p87);
                $("#p1p88").val(reportDataObj.p1p88);
                $("#p1p89").val(reportDataObj.p1p89);
                $("#p1p90").val(reportDataObj.p1p90);
                $("#p1p91").val(reportDataObj.p1p91);
                $("#p1p92").val(reportDataObj.p1p92);
                $("#p1p93").val(reportDataObj.p1p93);
                $("#p1p94").val(reportDataObj.p1p94);
                $("#p1p95").val(reportDataObj.p1p95);
            }else{
            	$("#p1p1").html(allDataObj.projectName);
                $("#p1p2").html(allDataObj.address);
                $("#p1p3").html(allDataObj.buildCompany);
                $("#p1p4").html(allDataObj.supervisorCompany);
                $("#p1p5").html(allDataObj.constructCompany);
            }
        });
    });
    //获取监测项
    $.post("getMonitorItemsByProject", function(data, status) {
        var allMonitorItems = JSON.parse(data).monitorItems;
        for(var i=0;i<allMonitorItems.length;i++){
        	if(allMonitorItems[i].code=="WYS"){
        		//表格填充
        		var trWYS="<tr>"+"<th colspan='2'>围护墙(边坡)顶部水平位移</th><th class='monitor' colspan='1'><label id='p1p6' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p7' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p8' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p9' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p10' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p11' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p12' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p13' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData1").append(trWYS);
        		 //附件导出
        		var trWYS3="<tr style='height:70px;'>"+"<th colspan='4'>围护墙(边坡)顶部水平位移</th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p78' class='input' required></textarea></th>"+"<th class='monitor' colspan='8' ><textarea rows='2' cols='20' id='p1p79' class='input' style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trWYS3);
        	}if(allMonitorItems[i].code=="WYD"){
        		var trWYD="<tr>"+"<th colspan='2'>围护墙(边坡)顶部竖向位移</th><th class='monitor' colspan='1'><label id='p1p14' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p15' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p16' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p17' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p18' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p19' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p20' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p21' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData1").append(trWYD);
        		//附件导出
        		var trWYD3="<tr style='height:70px;'>"+"<th colspan='4'>围护墙(边坡)顶部竖向位移</th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p80' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p81' class='input' style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trWYD3);
        	}if(allMonitorItems[i].code=="MT"){
        		var trMT="<tr>"+"<th colspan='2'>锚杆内力</th><th class='monitor' colspan='1'><label id='p1p22' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p23' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p24' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p25' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p26' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p27' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p29' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData").append(trMT);
        		//附件导出
        		var trMT3="<tr style='height:70px;'>"+"<th colspan='4'>锚杆内力</th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p82' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p83'  style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trMT3);
        	}if(allMonitorItems[i].code=="SM"){
        		var trSM="<tr>"+"<th colspan='2'>周边建筑物竖向位移</th><th class='monitor' colspan='1'><label id='p1p30' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p31' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p32' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p33' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p34' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p35' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p36' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p37' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData1").append(trSM);
        		//附件导出
        		var trSM3="<tr style='height:70px;'>"+"<th colspan='4'>周边建筑物竖向位移</th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p84' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p85' class='input' style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trSM3);
        	}if(allMonitorItems[i].code=="ZGD"){
        		var trZGD="<tr>"+"<th colspan='2'>周边管线竖向位移</th><th class='monitor' colspan='1'><label id='p1p38' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p39' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p40' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p41' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p42' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p43' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p44' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p45' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData1").append(trZGD);
        		//附件导出
        		var trZGD3="<tr style='height:70px;'>"+"<th colspan='4'>周边管线竖向位移</th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p86' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p87' class='input' style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trZGD3);
        	}if(allMonitorItems[i].code=="SW"){
        		var trSW="<tr>"+"<th colspan='2'>地下水位</th><th class='monitor' colspan='1'><label id='p1p46' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p47' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p48' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p49' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p50' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p51' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p52' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p53' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData1").append(trSW);
        		//附件导出
        		var trSW3="<tr style='height:70px;'>"+"<th colspan='4'>地下水位</th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p88' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p89' class='input' style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trSW3);
        	}if(allMonitorItems[i].code=="CX"){
        		var trCX="<tr>"+"<th colspan='2'>支护结构深层水平位移</th><th class='monitor' colspan='1'><label id='p1p54' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p55' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p56' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p57' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p58' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p59' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p60' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p61' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData1").append(trCX);
        		//附件导出
        		var trCX3="<tr style='height:70px;'>"+"<th colspan='4'>支护结构深层水平位移</th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p90' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p91' style='height:80%;width:90%;' class='input' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trCX3);
        	}if(allMonitorItems[i].code=="LZ"){
        		var trLZ="<tr>"+"<th colspan='2'>立柱竖向位移 </th><th class='monitor' colspan='1'><label id='p1p62' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p63' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p64' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p65' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p66' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p67' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p68' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p69' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData1").append(trLZ);
        		//附件导出
        		var trLZ3="<tr style='height:70px;'>"+"<th colspan='4'>立柱竖向位移 </th><th class='monitor' colspan='4'><textarea rows='2' cols='20' id='p1p92' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p93' class='input' style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trLZ3);
        	}if(allMonitorItems[i].code=="ZC"){
        		var trZC="<tr>"+"<th colspan='2'>支撑内力</th><th class='monitor' colspan='1'><label id='p1p70' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p71' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p72' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p73' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><label id='p1p74' style='width:80%;'></label></th>"+
        		"<th class='monitor' colspan='1'><label id='p1p75' style='width:80%;'></label></th>"+"<th class='monitor' colspan='1'><textarea rows='2' cols='20' id='p1p77' class='input' required></textarea></th>"+"</tr>";
        		$("#dateData").append(trZC);
        		//附件导出
        		var trZC3="<tr style='height:70px;' >"+"<th colspan='4'>支撑内力 </th><th class='monitor' colspan='2'><textarea rows='3' cols='20' id='p1p94' class='input' required></textarea></th>"+"<th class='monitor' colspan='8'><textarea rows='2' cols='20' id='p1p95' class='input' style='height:80%;width:90%;' required></textarea></th>"+"</tr>";
        		$("#addtionTable").append(trZC3);
        	}
        }
    });
    var datePick;
    //设置模态框弹出的表格的高度
    $('#datePick').datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: 1,
        todayHighlight: 1,
        todayBtn: 1,
        startView: 2,
        minView: 2,
        endDate: new Date(),
        forceParse: 0
    }).on('hide', function(ev){
    	datePick = $("#datePick").val() + " " + "23:59:59";
    	//先找到表格填充数据并清空
    	var tbl_content = document.getElementById("dateData"); 
    	var labels = tbl_content.getElementsByTagName("label"); 
    	for(var k=0;k<labels.length;k++) 
    	{ 
    		labels[k].innerHTML = " ";
    	} 
    	//获取表格数据
        $.post("getLimitAccumOffsetAndBiggestChangeRateByDay",{dateStr:datePick},function(data,status){
        	var allData=JSON.parse(data);
        	if(allData["WYS"]!=undefined){
        		$("#p1p6").html(allData["WYS"].maxAccumOffsetCode);
                $("#p1p7").html(allData["WYS"].maxAccumOffset);
                $("#p1p8").html(allData["WYS"].maxChangeRateCode);
                $("#p1p9").html(allData["WYS"].maxChangeRate);
                $("#p1p10").html(allData["WYS"].warnAccum);
                $("#p1p11").html(allData["WYS"].controlAccum);
                $("#p1p12").html(allData["WYS"].warnSingleRate);
        	}if(allData["WYD"]!=undefined){
        		$("#p1p14").html(allData["WYD"].maxAccumOffsetCode);
                $("#p1p15").html(allData["WYD"].maxAccumOffset);
                $("#p1p16").html(allData["WYD"].maxChangeRateCode);
                $("#p1p17").html(allData["WYD"].maxChangeRate);
                $("#p1p18").html(allData["WYD"].warnAccum);
                $("#p1p19").html(allData["WYD"].controlAccum);
                $("#p1p20").html(allData["WYD"].warnSingleRate);
        	}if(allData["MT"]!=undefined){
        		$("#p1p22").html(allData["MT"].maxCalValCode);
                $("#p1p23").html(allData["MT"].maxCalVal);
                $("#p1p24").html(allData["MT"].maxChangeRateCode);
                $("#p1p25").html(allData["MT"].maxChangeRate);
                $("#p1p26").html(allData["MT"].warnAccum);
                $("#p1p27").html(allData["MT"].controlAccum);
        	}if(allData["SM"]!=undefined){
        		$("#p1p30").html(allData["SM"].maxAccumOffsetCode);
                $("#p1p31").html(allData["SM"].maxAccumOffset);
                $("#p1p32").html(allData["SM"].maxChangeRateCode);
                $("#p1p33").html(allData["SM"].maxChangeRate);
                $("#p1p34").html(allData["SM"].warnAccum);
                $("#p1p35").html(allData["SM"].controlAccum);
                $("#p1p36").html(allData["SM"].warnSingleRate);
        	}if(allData["ZGD"]!=undefined){
        		 $("#p1p38").html(allData["ZGD"].maxAccumOffsetCode);
                 $("#p1p39").html(allData["ZGD"].maxAccumOffset);
                 $("#p1p40").html(allData["ZGD"].maxChangeRateCode);
                 $("#p1p41").html(allData["ZGD"].maxChangeRate);
                 $("#p1p42").html(allData["ZGD"].warnAccum);
                 $("#p1p43").html(allData["ZGD"].controlAccum);
                 $("#p1p44").html(allData["ZGD"].warnSingleRate);
        	}if(allData["SW"]!=undefined){
        		$("#p1p46").html(allData["SW"].maxAccumOffsetCode);
                $("#p1p47").html(allData["SW"].maxAccumOffset);
                $("#p1p48").html(allData["SW"].maxChangeRateCode);
                $("#p1p49").html(allData["SW"].maxChangeRate);
                $("#p1p50").html(allData["SW"].warnAccum);
                $("#p1p51").html(allData["SW"].controlAccum);
                $("#p1p52").html(allData["SW"].warnSingleRate);
        	}if(allData["CX"]!=undefined){
        		 $("#p1p54").html(allData["CX"].maxAccumOffsetCode);
                 $("#p1p55").html(allData["CX"].maxAccumOffset);
                 $("#p1p56").html(allData["CX"].maxChangeRateCode);
                 $("#p1p57").html(allData["CX"].maxChangeRate);
                 $("#p1p58").html(allData["CX"].warnAccum);
                 $("#p1p59").html(allData["CX"].controlAccum);
                 $("#p1p60").html(allData["CX"].warnSingleRate);
        	}if(allData["LZ"]!=undefined){
        		$("#p1p62").html(allData["LZ"].maxAccumOffsetCode);
                $("#p1p63").html(allData["LZ"].maxAccumOffset);
                $("#p1p64").html(allData["LZ"].maxChangeRateCode);
                $("#p1p65").html(allData["LZ"].maxChangeRate);
                $("#p1p66").html(allData["LZ"].warnAccum);
                $("#p1p67").html(allData["LZ"].controlAccum);
                $("#p1p68").html(allData["LZ"].warnSingleRate);
        	}if(allData["ZC"]!=undefined){
        		$("#p1p70").html(allData["ZC"].maxCalValCode);
                $("#p1p71").html(allData["ZC"].maxCalVal);
                $("#p1p72").html(allData["ZC"].maxChangeRateCode);
                $("#p1p73").html(allData["ZC"].maxChangeRate);
                $("#p1p74").html(allData["ZC"].warnAccum);
                $("#p1p75").html(allData["ZC"].controlAccum);
        	}
        	if(reportDataObj!=null){
        		$("#p1p13").html(reportDataObj.p7p8);
        		$("#p1p21").html(reportDataObj.p7p16);
        		$("#p1p29").html(reportDataObj.p7p24);
        		$("#p1p37").html(reportDataObj.p7p32);
        		$("#p1p45").html(reportDataObj.p7p40);
        		$("#p1p53").html(reportDataObj.p7p48);
        		$("#p1p61").html(reportDataObj.p7p56);
        	    $("#p1p69").html(reportDataObj.p7p64);
        	    $("#p1p77").val(reportDataObj.p7p72);
        	}
        });
    });
    //日报导出
    $("#btnDaily").bind('click', function() {
        if ($("#datePick").val() == "") {
            swal({ title: "导出失败！", text: "请选择日期", type: "error" });
            return;
        }
        var p1p13 = $("#p1p13").val();
        var p1p21 = $("#p1p21").val();
        var p1p29 = $("#p1p29").val();
        var p1p37 = $("#p1p37").val();
        var p1p45 = $("#p1p45").val();
        var p1p53 = $("#p1p53").val();
        var p1p61 = $("#p1p61").val();
        var p1p69 = $("#p1p69").val();
        var p1p77 = $("#p1p77").val();
        //预加载界面 shuang pi gu 
        $.MyCommon.PageLoading({ loadingTips: '日报导出中,请稍候...' });
        $.post("/smosplat/dailyReport", {dateStr: datePick,p7p8:p1p13,p7p16:p1p21,p7p24:p1p29,p7p32:p1p37,p7p40:p1p45,p7p48:p1p53,p7p56:p1p61,p7p64:p1p69,p7p72:p1p77}, function(data, status) {
        	//p1p13:围护墙(边坡)顶部水平位移观测仪器备注 p1p21:围护墙(边坡)顶部竖向位移观测仪器备注p1p29:锚杆内力备注p1p37:周边建筑物竖向位移备注p1p45:周边管线竖向位移备注
        	//p1p53:地下水位备注p1p61:支护结构深层水平位移备注p1p69:立柱竖向位移备注p1p77:支撑内力备注
            if (JSON.parse(data).result == 0) {
                if (JSON.parse(data).fileUrl != "" && JSON.parse(data).fileUrl != undefined) {
                    window.location.href = JSON.parse(data).fileUrl;
                    $('#loadingPage').remove();
                } else {
                    swal({ title: "导出失败！", text: "返回地址为空！", type: "error" });
                    $('#loadingPage').remove();
                }
            }
        });
    });
    $("#otherInfo").bind('click', function() {
    	if ($("#datePick").val() == "") {
            swal({ title: "导出失败！", text: "请选择日期", type: "error" });
            return;
        }
    	$("#addtionDialog").modal("show");
    });
    $("#saveAddTionBtn").bind("click",function(){
    	var pjName = $("#p1p1").html();
        var pjAddress = $("#p1p2").html();
        var p1p78 = $("#p1p78").val()== undefined  ? "":$("#p1p78").val();
        var p1p79 = $("#p1p79").val()== undefined  ? "":$("#p1p79").val();
        var p1p80 = $("#p1p80").val()== undefined  ? "":$("#p1p80").val();
        var p1p81 = $("#p1p81").val()== undefined  ? "":$("#p1p81").val();
        var p1p82 = $("#p1p82").val()== undefined  ? "":$("#p1p82").val();
        var p1p83 = $("#p1p83").val()== undefined  ? "":$("#p1p83").val();
        var p1p84 = $("#p1p84").val()== undefined  ? "":$("#p1p84").val();
        var p1p85 = $("#p1p85").val()== undefined  ? "":$("#p1p85").val();
        var p1p86 = $("#p1p86").val()== undefined  ? "":$("#p1p86").val();
        var p1p87 = $("#p1p87").val()== undefined  ? "":$("#p1p87").val();
        var p1p88 = $("#p1p88").val()== undefined  ? "":$("#p1p88").val();
        var p1p89 = $("#p1p89").val()== undefined  ? "":$("#p1p89").val();
        var p1p90 = $("#p1p90").val()== undefined  ? "":$("#p1p90").val();
        var p1p91 = $("#p1p91").val()== undefined  ? "":$("#p1p91").val();
        var p1p92 = $("#p1p92").val()== undefined  ? "":$("#p1p92").val();
        var p1p93 = $("#p1p93").val()== undefined  ? "":$("#p1p93").val();
        var p1p94 = $("#p1p94").val()== undefined  ? "":$("#p1p94").val();
        var p1p95 = $("#p1p95").val()== undefined  ? "":$("#p1p95").val();
        var bDateStr = $("#datePick").val() + " " + "00:00:00";
        var eDateStr = $("#datePick").val() + " " + "23:59:59";
        $("#addtionDialog").modal("hide");
        //p1p78:围护墙(边坡)顶部水平位移观测仪器  p1p79:围护墙(边坡)顶部水平位移依据规范  p1p80:围护墙(边坡)顶部竖向位移观测仪器 p1p81:围护墙(边坡)顶部竖向位移依据规范 p1p82:锚杆内力观测仪器p1p83:锚杆内力依据规范
        //p1p84:周边建筑物竖向位移观测仪器 p1p85:周边建筑物竖向位移依据规范  p1p86:周边管线竖向位移观测仪器 p1p87:周边管线竖向位移依据规范 p1p88:地下水位观测仪器 p1p89:地下水位依据规范 p1p90:支护结构深层水平位移观测仪器 p1p91:支护结构深层水平位移依据规范 
        //p1p92:立柱竖向位移观测仪器 p1p93:立柱竖向位移依据规范 p1p94:支撑内力观测仪器 p1p95:支撑内力依据规范
        $.MyCommon.PageLoading({ loadingTips: '附件导出中,请稍候...' });
        $.post("dailyExport", {
        	pjName: pjName,
        	pjAddress:pjAddress,
        	bDateStr: bDateStr,
        	eDateStr: eDateStr,
        	p1p78: p1p78,
        	p1p79: p1p79,
        	p1p80: p1p80,
        	p1p81: p1p81,
        	p1p82: p1p82,
        	p1p83: p1p83,
        	p1p84: p1p84,
        	p1p85: p1p85,
        	p1p86: p1p86,
        	p1p87: p1p87,
        	p1p88: p1p88,
        	p1p89: p1p89,
        	p1p90: p1p90,
        	p1p91: p1p91,
        	p1p92: p1p92,
        	p1p93: p1p93,
        	p1p94: p1p94,
        	p1p95: p1p95
        },function(data, status){
        	if (JSON.parse(data).result == 0) {
                if (JSON.parse(data).fileUrl != "" && JSON.parse(data).fileUrl != undefined) {
                    window.location.href = JSON.parse(data).fileUrl;
                    $('#loadingPage').remove();
                } else {
                    swal({ title: "导出失败！", text: "返回地址为空！", type: "error" });
                    $('#loadingPage').remove();
                }
            }
        });
//		window.open("/smosplat/spDataExport?pjName=" + pjName
//						+ "&pjAddress=" + pjAddress + "&bDateStr=" + bDateStr
//						+ "&eDateStr=" + eDateStr + "&p1p78=" + p1p78
//						+ "&p1p79=" + p1p79 + "&p1p80=" + p1p80 + "&p1p81="
//						+ p1p81 + "&p1p82=" + p1p82 + "&p1p83=" + p1p83
//						+ "&p1p84=" + p1p84 + "&p1p85=" + p1p85 + "&p1p86="
//						+ p1p86 + "&p1p87=" + p1p87 + "&p1p88=" + p1p88
//						+ "&p1p89=" + p1p89 + "&p1p90=" + p1p90 + "&p1p91="
//						+ p1p91 + "&p1p92=" + p1p92 + "&p1p93=" + p1p93
//						+ "&p1p94=" + p1p94 + "&p1p95=" + p1p95);
    });
})