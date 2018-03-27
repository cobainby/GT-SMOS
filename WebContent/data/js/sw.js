var waterNames = new Array();
var waterModel = new Array();
var myChart1;
var myChart2;
var minAccum;
var maxAccum;
var minGap;
var maxGap;
//测点列表
var codes;
//工程id
var projectUuid;
waterNames = ['测点', '水位高(mm)', '采集模数','单次变化(mm)', '累计变化(mm)','变化速率(mm/d)','测量时间'];
waterModel = [{
    name: 'code',
    sorttype: "string",
    align: "center"
}, {
    name: 'calValue',
    sorttype: "int",
    align: "center"
}, {
    name: 'moduleData',
    sorttype: "float",
    align: "center"
}, {
    name: 'gapOffset',
    sorttype: "float",
    align: "center"
}, {
    name: 'accumOffset',
    sorttype: "float",
    align: "center"
}, {
    name: 'gapChangeRate',
    sorttype: "float",
    align: "center"
}, {
    name: 'collectTime',
    formatter: formateTimestamp,
    sorttype: "float",
    align: "center",
    width: 240
}];
$(function() {
	 $('#startTime').datetimepicker({
	        language: 'zh-CN',
	        format: 'yyyy-mm-dd',
	        weekStart: 1,
	        autoclose: 1,
	        todayHighlight: 1,
	        todayBtn: 1,
	        startView: 2,
	        minView: 2,
	        endDate: new Date(),
	        forceParse: 0,
	        pickerPosition: "bottom-right"
	    });
	    $('#endTime').datetimepicker({
	        language: 'zh-CN',
	        format: 'yyyy-mm-dd',
	        weekStart: 1,
	        autoclose: 1,
	        todayHighlight: 1,
	        todayBtn: 1,
	        startView: 2,
	        minView: 2,
	        endDate: new Date(),
	        forceParse: 0,
	        pickerPosition: "bottom-right"
	    });
	//加载数据
    init();
  //原始数据下载
    $("#originalDataButton").bind("click", function() {
        $("#originalDataDialog").modal("show");
    });
    $("#downLoadBtn").bind("click", function() {
        var sDate = $("#startTime").val()
        var eDate = $("#endTime").val();
        //去掉时间戳的-,方便后台处理
        sDate = sDate.replace(/-/g, "");
        eDate = eDate.replace(/-/g, "");
        $("#textList").empty();
        if (sDate == "" || eDate == "") {
            swal({ title: "请选择时间！", type: "error" });
            return;
        }
        var monitorItemCode = "SW";
        $.post("getSourceFileUrlsByDateTime", { projectUuid: projectUuid, monitorItem: monitorItemCode, sDate: sDate, eDate: eDate }, function(data, status) {
            var downLoadList = JSON.parse(data).sourceFileUrls;
            if(downLoadList.length==0){
            	swal({ title: "无原始数据！", type: "error" });
            	return;
            }
            for (var i = 0; i < downLoadList.length; i++) {
            	//文件名
                var fileName = downLoadList[i].slice(-21);
                var year=fileName.substring(0, 4);
                var mouth=fileName.substring(4,6);
                var date=fileName.substring(6,8);
                var time=fileName.substring(8,10)+":"+fileName.substring(10,12)+":"+fileName.substring(12,14);
                var type=fileName.substring(14,17);
                //标签文字
                var textName=year+"-"+mouth+"-"+date+" "+time+type+".txt";
                $("#textList").append("<a style='display:block' href='javascript:void(0);'  onclick=\"downUrl('" + fileName + "','" + projectUuid + "','" + monitorItemCode + "')\">" + textName + "&nbsp</a>");
            }
        });
    });
  //数据导出
    $("#dataExportButton").bind("click", function() {
		$.post("/smosplat/getCurrentProjectInfo", function(data, status) {
	        var projectName = JSON.parse(data).project.projectName;
	        var projectAddress=JSON.parse(data).project.address;
	        $("#projectName").val(projectName);
	        $("#projectAddress").val(projectAddress);
	    });
		$.post("getProjectReportByProject", function(data, status) {
            reportDataObj = JSON.parse(data).entity;
            if (reportDataObj != null) {
                var deviceName=reportDataObj.p1p88;
                var specification=reportDataObj.p1p89;
                $("#deviceName").val(deviceName);
                $("#specification").val(specification);
            }
        });
		$("#dataExportDialog").modal("show");
	});
	$("#saveExportBtn").bind("click",function(){
		var pjName=$("#projectName").val();
		var pjAddress=$("#projectAddress").val();
		var deviceName=$("#deviceName").val();
		var bDate=$("#beginDate").datetimebox('getValue');
		var eDate=$("#endDate").datetimebox('getValue');
		var specification=$("#specification").val();
		$("#dataExportDialog").modal("hide");
		//导出报表
		window.open("/smosplat/swExport?pjName="+pjName+"&pjAddress="+pjAddress+"&deviceName="+deviceName+"&specification="+specification+"&bDateStr="+bDate+"&eDateStr="+eDate);
	});
	$("#waterLineGap").width($("#echart").width());
    $("#waterLineGap").height($("#echart").height());
    $("#waterLineAccum").width($("#echart").width());
    $("#waterLineAccum").height($("#echart").height());
});

function downUrl(fileName,projectUuid,monitorItemCode) {
	window.location.href="/smosplat/downloadSourceFile?fileName=" + fileName + "&projectUuid=" + projectUuid+"&monitorItemCode="+monitorItemCode;
}
function init(){
    $.MyCommon.PageLoading({loadingTips:'加载中,请稍候...'});
    $.post("/smosplat/getLatestSWDatasByProject", function(data, status) {
        //先取出最新10条数据进行显示
        var dataObj = JSON.parse(data);
        if (dataObj.result == 0) {
            $('#loadingPage').remove();
            projectUuid = dataObj.projectUuid;
            codes = new Array;
            var surveyPoints = dataObj.surveyPoints;
            for (var i = 0; i < surveyPoints.length; i++) {
                codes.push(surveyPoints[i].code);
            }
            //以点名为标记，所有点最新10条数据
            var spDatasObj = dataObj.dataList;
            //所有监测点的最新一条数据
            var lastSpDatas = new Array();
            //保存所有时间值
            var times = new Array();
            //所有的累积差（用来找出水位计算值的最大最小值，确定y轴范围）
            var allAccumOffsetData = new Array();
          //单次变化量(用来找出单次变化量的最大最小值，确定y轴范围)
            var allGapOffsetData=new Array();
            for (var i = 0; i < surveyPoints.length; i++) {
                var spDatas = spDatasObj[surveyPoints[i].code];
                var lastSPData = spDatas == undefined ? {} : spDatas[spDatas.length - 1];
                if (lastSPData == undefined) {
                    lastSPData = new Object;
                }
                lastSPData["code"] = surveyPoints[i].code;
                lastSpDatas.push(lastSPData);
                if (spDatas != undefined) {
                    //保存时间点
                    for (var k = 0; k < spDatas.length; k++) {
                        if (isInArray(times, spDatas[k].collectTime) == false) {
                            times.push(spDatas[k].collectTime);
                        }
                    }
                }
            }

            //加载数据
            $("#swDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                colNames: waterNames,
                colModel: waterModel,
                autowidth: true,
                data:lastSpDatas,
                rowNum: 1000,
                sortname: 'id',
                gridview: true,
                viewrecords: true,
                multiselect: true,
                beforeSelectRow: function (rowid, e) {  
             	   var $myGrid = $(this),  
             	       i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),  
             	       cm = $myGrid.jqGrid('getGridParam', 'colModel');  
             	   return (cm[i].name === 'cb');  
                },
                onSelectRow: function(rowId) {
                    //获取当前选择的行的ID
                    var selectedIds = $("#swDG").jqGrid("getGridParam", "selarrrow");
                    var rowDatas = [];
                    //获取当前选择行的数据
                    for (var l = 0; l < selectedIds.length; l++) {
                        rowDatas.push($("#swDG").jqGrid("getRowData", selectedIds[l]));
                    }
                    //选中的点的时间集合
                    var selectTimes=[];
                    for(var i=0;i<rowDatas.length;i++){
                    	 var spDatas = spDatasObj[rowDatas[i].code];
                    	 if (spDatas != undefined) {
                             //保存时间点
                             for (var k = 0; k < spDatas.length; k++) {
                                 if (isInArray(selectTimes, spDatas[k].collectTime) == false) {
                                	 selectTimes.push(spDatas[k].collectTime);
                                 }
                             }
                         }
                    }
                    getSelectDatas(selectTimes,rowDatas,spDatasObj);
                }

            });
            $("#swDG").setGridHeight($("#waterGrid").height() - 80);
            $("#cb_swDG").click();
            $("#cb_swDG").bind("click", function() {
                if ($("#cb_swDG").is(':checked')) {
                	getSelectDatas(times,surveyPoints,spDatasObj);
                } else {
                	myChart1.clear();
                	myChart2.clear();
                }
            });
          //累积变化量的最小值
            minAccum = Math.min.apply(null, allAccumOffsetData);
            //累积变化量的最大值
            maxAccum = Math.max.apply(null, allAccumOffsetData);
            //单次变化量的最小值
            minGap=Math.min.apply(null, allGapOffsetData);
            //单次变化量的最大值
            maxGap=Math.max.apply(null, allGapOffsetData);
            
//            //让数据显示占中间的三分之一空间
//            var differAccum = maxAccum - minAccum;
//            minAccum = minAccum - differAccum;
//            maxAccum = maxAccum + differAccum;
//            minAccum = minAccum.toFixed(4);
//            maxAccum = maxAccum.toFixed(4);
//            var differGap = maxGap - minGap;
//            minGap = minGap - differGap;
//            maxGap = maxGap + differGap;
//            minGap = minGap.toFixed(4);
//            maxGap = maxGap.toFixed(4);
            
            //建立推送连接
            getSelectDatas(times,surveyPoints,spDatasObj);
            initSocket(projectUuid);
        } else if (dataObj.result == -1) {
            swal({title:"失败！",text:dataObj.msg,type:"error"});
            $('#loadingPage').remove();
        }
    });
}

function getSelectDatas(times,surveyPoints,spDatasObj){
	//将所有时间点的值按照时间进行排序，然后取出最新10个值作为x轴的数据源
    times.sort(function(a, b) {
        return a - b;
    });
    if (times.length > 10) {
        times = times.slice(-10);
    }
    //x轴为类目时必须为字符串，不能是数字
    for (var i = 0; i < times.length; i++) {
        times[i] = times[i] + "";
    }
    //单次差曲线图数据
    var seriesDataForGapLine = new Array();
    //累积差曲线图数据
    var seriesDataForAccumLine = new Array();
    for (var i = 0; i < surveyPoints.length; i++) {
        //每一个监测点的最新的10条数据
        var spDataList = spDatasObj[surveyPoints[i].code];
        //单独取出每一条线的时间，用来和所有的时间点比较，得到当前测点哪个时间无返回数据
        var singleTime = new Array();
        var newSpData=[];
        for (var o = 0; o < spDataList.length; o++) {
        	//去掉原数组中的重复上传的数据
        	if(isInArray(singleTime,spDataList[o].collectTime)==false){
        		singleTime.push(spDataList[o].collectTime);
        		newSpData.push(spDataList[o]);
        	}
        }
        //无返回数据的时间给他赋空值
        for (var q = 0; q < times.length; q++) {
            if (isInArray(singleTime, times[q]) == false) {
                newSpData.push({
                    'collectTime': times[q],
                    'accumOffset': '/',
                    'gapOffset': '/'
                });
            }
        }
        //重新排序
        newSpData.sort(function(a, b) {
            return a.collectTime - b.collectTime;
        });
        //取最新的10条
        if (newSpData.length > 10) {
            newSpData = newSpData.slice(-10);
        }
        var serieDataForAccum = new Array();
        var serieDataForGap = new Array();
        if (newSpData != undefined) {
            for (var j = 0; j < newSpData.length; j++) {
                //判断当前测点是否有这个时间点的数据,没有则默认为0
                var value = newSpData[j].accumOffset;
                serieDataForAccum.push([newSpData[j].collectTime + "", value]);
                value = newSpData[j].gapOffset;
                serieDataForGap.push([newSpData[j].collectTime + "", value]);
            }
        }
        seriesDataForGapLine.push({
            name: surveyPoints[i].code,
            type: 'line',
            showSymbol: true,
            showAllSymbol: true,
            hoverAnimation: false,
            data: serieDataForGap
        });
        seriesDataForAccumLine.push({
            name: surveyPoints[i].code,
            type: 'line',
            showSymbol: true,
            showAllSymbol: true,
            hoverAnimation: false,
            data: serieDataForAccum
        });
    }
    getOption(times,seriesDataForGapLine,seriesDataForAccumLine);
}
function initSocket(projectUuid) {
    //初始化websocket并订阅来自服务器的消息推送
    //建立与服务器的长连接
    var socket = new SockJS('/smosplat/collect');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        //根据测站进行订阅
        var subscribeUrl = "/collect/mcuSWStartCollect/" + projectUuid;
        stompClient.subscribe(subscribeUrl, function(message) {
            //水位数据
            var resultObj = JSON.parse(message.body);
            //mcu采集数据
            if (resultObj != undefined) {
                //得到测站下的所有监测点的最新一条数据（最新推送过来的）
                var dataList = JSON.parse(resultObj);
                //保存所有时间值
                var times = new Array();
                //单次变化量现状图的选项
                var chartTotalOption = myChart1.getOption();
                //获取折线图各个点的数据
                var seriesDataForGap = chartTotalOption.series;
                //现状图累计变化量数据
                var seriesDataForAccum = myChart2.getOption().series;
                //得到datagrid的数据源
                var dgData = $("#swDG").jqGrid("getRowData");
                for (var d = 0; d < dgData.length; d++) {
                    //避免时间为""时前台表格出现"1970-01-01 08:00:00"的错误
                    if (dgData[d].collectTime == "") {
                        dgData[d].collectTime = null;
                    }
                }
                //根据监测点的id替换掉对应的数据记录
                for (var i = 0; i < dataList.length; i++) {
                    var spName = dataList[i].surveyPoint.code;
                    for (var j = 0; j < dgData.length; j++) {
                        if (dgData[j]["code"] == spName) {
                            dgData[j].gapOffset = dataList[i].gapOffset;
                            dgData[j].moduleData = dataList[i].moduleData;
                            dgData[j].gapChangeRate = dataList[i].gapChangeRate;
                            dgData[j].accumOffset = dataList[i].accumOffset;
                            dgData[j].calValue = dataList[i].calValue;
                            dgData[j].collectTime = dataList[i].collectTime;
                        } else {
                            //把时间转为毫秒，前台展示才正常
                            var t = dgData[j].collectTime;
                            if (t != null && "string" == typeof t && t.indexOf('-') != -1) {
                                //必须改成2017/09/08 08:09:09的格式
                                t = t.replace(new RegExp("-", "gm"), "/");
                                dgData[j].collectTime = (new Date(t)).getTime();
                            }
                        }
                    }
                    var wlName = dataList[i].surveyPoint.code;
                    //循环测点的个数
                    for (var k = 0; k < seriesDataForGap.length; k++) {
                        if (seriesDataForGap[k].name == wlName) {
                            var dataLength = seriesDataForGap[k].data.length;
                            for (var l = 0; l < dataLength; l++) {
                                //10条数据，去掉第一条，其他的往前挪一条
                                if (l < dataLength - 1) {
                                    seriesDataForGap[k].data[l] = seriesDataForGap[k].data[l + 1];
                                    seriesDataForAccum[k].data[l] = seriesDataForAccum[k].data[l + 1];
                                } else {
                                    //最后一条放新推送过来的数据
                                    seriesDataForGap[k].data[l] = [dataList[i].collectTime + "", dataList[i].gapOffset];
                                    seriesDataForAccum[k].data[l] = [dataList[i].collectTime + "", dataList[i].accumOffset];
                                }
                            }
                        }
                    }
                    //得出新的数据所有的时间点
                    for (var k = 0; k < seriesDataForGap.length; k++) {
                        var wlDataList = seriesDataForGap[k].data;
                        //保存时间点
                        for (var kk = 0; kk < wlDataList.length; kk++) {
                            if (isInArray(times, wlDataList[kk][0]) == false) {
                                times.push(wlDataList[kk][0]);
                            }
                        }
                    }
                    //将所有时间点的值按照时间进行排序，然后取出最新10个值作为x轴的数据源
                    times.sort(function(a, b) {
                        return a - b;
                    });
                    if (times.length > 10) {
                        times = times.slice(-10);
                    }
                }
                //dqgrid重新加载数据
                $("#swDG").jqGrid('setGridParam', { // 重新加载数据
                    datatype: "local",
                    styleUI: 'Bootstrap',
                    data: dgData,
                    autowidth: true,
                    //全部显示。如果不设置，默认20行
                    rowNum: 1000,
                    sortname: 'id',
                    viewrecords: true,
                    gridview: true
                }).trigger("reloadGrid");
                getOption(times,seriesDataForGap,seriesDataForAccum);
            }
        });
    });
}
function getOption(times,seriesDataForGapLine,seriesDataForAccumLine) {
	// 指定图表的配置项和数据
    var option1 = {
        tooltip: {
            trigger: 'axis',
            formatter: function(params) {
                var tipStr = "";
                var date = new Date(parseFloat(params[0].value[0]));
                tipStr= date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate() + " " +
                date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                for (var i = 0; i < params.length; i++) {
                    if (params[i].value != undefined) {
                        var dataStr= params[i].seriesName + ":" + params[i].value[1];
                        if(i % 4==0){
                            tipStr = tipStr +"<br>"+ dataStr;
                        }else{
                            tipStr = tipStr +','+'&nbsp;'+dataStr;
                        }
                    }
                }
                return tipStr;
            },
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle:{
                fontSize:10
            }
        },
        toolbox: {
            show: true,
            feature: {
                saveAsImage: {}
            }
        },
        legend: {
            data: codes,
            itemHeight: 8,
            itemWidth: 18,
            itemGap: 8,
            textStyle: {
                fontSize: 10
            },
            right: 20
        },
        grid: {
            left: '60',
            right: '60',
            top: '32',
            bottom: '38'
        },
        xAxis: {
            type: 'category',
            splitLine: {
                show: true,
                interval: 0
            },
            axisTick: {
                show: true,
                interval: 0
            },
            axisLabel: {
                formatter: function(value, index) {
                    var date = new Date(parseFloat(value));
                    var dataStr = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate() + " " +
                        date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                    return dataStr;
                }
            },
            data: times
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: true
            }
        },
    };
    var option2 = {
        tooltip: {
            trigger: 'axis',
            formatter: function(params) {
                var tipStr = "";
                var date = new Date(parseFloat(params[0].value[0]));
                tipStr= date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate() + " " +
                date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                for (var i = 0; i < params.length; i++) {
                    if (params[i].value != undefined) {
                        var dataStr= params[i].seriesName + ":" + params[i].value[1];
                        if(i % 4==0){
                            tipStr = tipStr +"<br>"+ dataStr;
                        }else{
                            tipStr = tipStr +','+'&nbsp;'+dataStr;
                        }
                    }
                }
                return tipStr;
            },
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle:{
                fontSize:10
            }
        },
        legend: {
            data: codes,
            itemHeight: 8,
            itemWidth: 18,
            itemGap: 8,
            textStyle: {
                fontSize: 10
            },
            right: 20
        },
        grid: {
            left: '60',
            right: '60',
            top: '32',
            bottom: '38'
        },
        xAxis: {
            type: 'category',
            axisTick: {
                show: true,
                interval: 0
            },
            splitLine: {
                show: true,
                interval: 0
            },
            axisLabel: {
                formatter: function(value, index) {
                    var date = new Date(parseFloat(value));
                    var dataStr = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate() + " " +
                        date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                    return dataStr;
                }
            },
            data: times
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: true
            }
        },
        toolbox: {
            show: true,
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '60',
            right: '60',
            top: '40',
            bottom: '40'
        }
    };
    //线状图单次变化量
    myChart1 = echarts.init(document.getElementById('waterLineGap'), 'shine');
    //现状图累计变化量
    myChart2 = echarts.init(document.getElementById('waterLineAccum'), 'shine');

    // 使用刚指定的配置项和数据显示图表。
    myChart1.setOption(option1);
    myChart1.setOption({
        series: seriesDataForGapLine
    });
    myChart2.setOption(option2);
    myChart2.setOption({
        series: seriesDataForAccumLine
    });
}
function formateTimestamp(cellvalue, options, rowObject) {
    return timestamp2String(rowObject.collectTime);
};
