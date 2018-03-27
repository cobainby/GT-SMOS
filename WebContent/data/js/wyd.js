//数据接口
var url;
//累计位移曲线
var myLineChartAccum;
//单次位移曲线
var myLineChartGap;
var wydCodes;
var wydNames = new Array();
var wydModel = new Array();
//累积变化量的最小值
var minAccum;
//累积变化量的最大值
var maxAccum;
//单次变化量的最小值
var minGap;
//单次变化量的最大值
var maxGap;
//项目id
var projectUuid;
wydNames = ['数据ID', '编号', '单次变化量', '累计变化量', '测量时间', '高程'];
wydModel = [{
    name: 'surveyPointUuid',
    hidden: true
}, {
    name: 'code',
    align: "center"
}, {
    name: 'gapHOffset',
    align: "center"
}, {
    name: 'accumHOffset',
    align: "center"
}, {
    name: 'surveyTime',
    formatter: formateTimestamp,
    align: "center",
    width: 240
}, {
    name: 'levelH',
    align: "center"
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
        var monitorItemCode = "WYD";
        $.post("getSourceFileUrlsByDateTime", { projectUuid: projectUuid, monitorItem: monitorItemCode, sDate: sDate, eDate: eDate }, function(data, status) {
            var downLoadList = JSON.parse(data).sourceFileUrls;
            if(downLoadList.length==0){
            	swal({ title: "无原始数据！", type: "error" });
            	return;
            }
            for (var i = 0; i < downLoadList.length; i++) {
            	//文件名
                var fileName = downLoadList[i].slice(-22);
                var year=fileName.substring(0, 4);
                var mouth=fileName.substring(4,6);
                var date=fileName.substring(6,8);
                var time=fileName.substring(8,10)+":"+fileName.substring(10,12)+":"+fileName.substring(12,14);
                var type=fileName.substring(14,18);
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
            var projectAddress = JSON.parse(data).project.address;
            $("#projectName").val(projectName);
            $("#projectAddress").val(projectAddress);
        });
        $.post("getProjectReportByProject", function(data, status) {
            reportDataObj = JSON.parse(data).entity;
            if (reportDataObj != null) {
                var deviceName=reportDataObj.p1p80;
                var specification=reportDataObj.p1p81;
                $("#deviceName").val(deviceName);
                $("#specification").val(specification);
            }
        });
        $("#dataExportDialog").modal("show");
    });
    $("#saveExportBtn").bind("click", function() {
        var pjName = $("#projectName").val();
        var pjAddress = $("#projectAddress").val();
        var deviceName = $("#deviceName").val();
        var bDate = $("#beginDate").datetimebox('getValue');
        var eDate = $("#endDate").datetimebox('getValue');
        var specification = $("#specification").val();
        $("#dataExportDialog").modal("hide");
        //导出报表
        document.location.href = "/smosplat/wydExport?pjName=" + pjName + "&pjAddress=" + pjAddress + "&deviceName=" + deviceName + "&specification=" + specification + "&bDateStr=" + bDate + "&eDateStr=" + eDate;
    });
    $("#wydLineDivAccum").width($("#echarts").width());
    $("#wydLineDivAccum").height($("#echarts").height());
    $("#wydLineDivGap").width($("#echarts").width());
    $("#wydLineDivGap").height($("#echarts").height());
});

function downUrl(fileName,projectUuid,monitorItemCode) {
	window.location.href="/smosplat/downloadSourceFile?fileName=" + fileName + "&projectUuid=" + projectUuid+"&monitorItemCode="+monitorItemCode;
}

function init() {
    //加载数据
    $.MyCommon.PageLoading({ loadingTips: '加载中,请稍候...' });
    $.post("/smosplat/getLatestWYDLevelDatasByProject", function(data, status) {
        //先取出最新10条数据进行显示
        var dataObj = JSON.parse(data);
        projectUuid = dataObj.projectUuid;
        var surveyPoints = dataObj.surveyPoints;
        var wydDatasObj = dataObj.levelData;
        if (dataObj.result == 0) {
            $('#loadingPage').remove();
            //保存所有时间值
            var times = new Array();
            //取出最新一条数据，显示到datagrid中
            //所有监测点的最新一条数据
            var lastwydDatas = new Array();
            //累积变化量（用来找出东坐标的最大最小值，确定y轴范围）
            var allAccumHOffsetData = new Array();
            //单次变化量(用来找出单次变化量的最大最小值，确定y轴范围)
            var allGapHOffsetData = new Array();
            for (var i = 0; i < surveyPoints.length; i++) {
                //根据编号拿到不同编号的数据
                var wydDatas = wydDatasObj[surveyPoints[i].code];
                var lastwydData = wydDatas == undefined ? {} : wydDatas[wydDatas.length - 1];
                if (lastwydData == undefined) {
                    lastwydData = new Object;
                }
                lastwydData["code"] = surveyPoints[i].code;
                lastwydDatas.push(lastwydData);
                //存累计变化量、不同编号点的时间
                if (wydDatas != undefined) {
                    //保存时间点
                    for (var k = 0; k < wydDatas.length; k++) {
                        if (isInArray(times, wydDatas[k].surveyTime) == false) {
                            times.push(wydDatas[k].surveyTime);
                        }
                    }
                }
            }
            $("#wydDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                data: lastwydDatas,
                colNames: wydNames,
                colModel: wydModel,
                autowidth: true,
                rowNum: 1000,
                sortname: 'id',
                viewrecords: true,
                gridview: true,
                multiselect: true,
                beforeSelectRow: function (rowid, e) {  
             	   var $myGrid = $(this),  
             	       i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),  
             	       cm = $myGrid.jqGrid('getGridParam', 'colModel');  
             	   return (cm[i].name === 'cb');  
                },
                onSelectRow: function(rowId) {
                    //获取当前选择的行的ID
                    var selectedIds = $("#wydDG").jqGrid("getGridParam", "selarrrow");
                    var rowDatas = [];
                    //获取当前选择行的数据
                    for (var l = 0; l < selectedIds.length; l++) {
                        rowDatas.push($("#wydDG").jqGrid("getRowData", selectedIds[l]));
                    }
                    //选中的点的时间集合
                    var selectTimes=[];
                    for(var i=0;i<rowDatas.length;i++){
                    	//根据编号拿到不同编号的数据
                        var wydDatas = wydDatasObj[rowDatas[i].code];
                        if (wydDatas != undefined) {
                            //保存时间点
                            for (var k = 0; k < wydDatas.length; k++) {
                                if (isInArray(selectTimes, wydDatas[k].surveyTime) == false) {
                                	selectTimes.push(wydDatas[k].surveyTime);
                                }
                            }
                        }
                    }
                    getSelectDatas(selectTimes,rowDatas,wydDatasObj);
                }
            });
            $("#wydDG").setGridHeight($("#wydGrid").height() - 80);
            $("#cb_wydDG").click();
            $("#cb_wydDG").bind("click", function() {
                if ($("#cb_wydDG").is(':checked')) {
                	getSelectDatas(times,surveyPoints,wydDatasObj);
                } else {
                    myLineChartGap.clear();
                    myLineChartAccum.clear();
                }
            });
            getSelectDatas(times,surveyPoints,wydDatasObj);
            //初始化柱状图
            initSocket(projectUuid);
        } else if (dataObj.result == -1) {
            swal({ title: "失败！", text: dataObj.msg, type: "error" });
            $('#loadingPage').remove();
        }
    });
}

function getSelectDatas(times,surveyPoints,wydDatasObj){
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
    //累计变化曲线图数据
    var seriesDataForAccumLine = new Array();
    //单次变化量曲线图数据
    var seriesDataForGapLine = new Array();
    for (var i = 0; i < surveyPoints.length; i++) {
        //每一个监测点的最新的10条数据
        var wydDataList = wydDatasObj[surveyPoints[i].code];
        //单独取出每一条线的时间，用来和所有的时间点比较，得到当前测点哪个时间无返回数据
        var singleTime = new Array();
        var newWydData=[];
        for (var o = 0; o < wydDataList.length; o++) {
        	if(isInArray(singleTime,wydDataList[o].surveyTime)==false){
        		singleTime.push(wydDataList[o].surveyTime);
        		newWydData.push(wydDataList[o]);
        	}
            
        }
        //无返回数据的时间给他赋空值
        for (var q = 0; q < times.length; q++) {
            if (isInArray(singleTime, times[q]) == false) {
                newWydData.push({
                    'surveyTime': times[q],
                    'accumHOffset': '/',
                    'gapHOffset': '/'
                });
            }
        }
        //重新排序
        newWydData.sort(function(a, b) {
            return a.surveyTime - b.surveyTime;
        });
        //取最新的10条
        if (newWydData.length > 10) {
            newWydData = newWydData.slice(-10);
        }
        var serieDataForAccum = new Array();
        var serieDataForGap = new Array();
        if (newWydData != undefined) {
            for (var j = 0; j < newWydData.length; j++) {
                //判断当前测点是否有这个时间点的数据,没有则默认为空
                var value = newWydData[j].accumHOffset;
                serieDataForAccum.push([newWydData[j].surveyTime + "", value]);
                value = newWydData[j].gapHOffset;
                serieDataForGap.push([newWydData[j].surveyTime + "", value]);
            }
        }
        seriesDataForAccumLine.push({
            name: surveyPoints[i].code,
            type: 'line',
            showSymbol: true,
            showAllSymbol: true,
            smooth: false,
            hoverAnimation: false,
            data: serieDataForAccum
        });
        seriesDataForGapLine.push({
            name: surveyPoints[i].code,
            type: 'line',
            showSymbol: true,
            smooth: false,
            showAllSymbol: true,
            hoverAnimation: false,
            data: serieDataForGap
        });
    }
    wydCodes = new Array();
    for (var i = 0; i < surveyPoints.length; i++) {
        wydCodes.push(surveyPoints[i].code);
    }
    getOption(times, seriesDataForGapLine, seriesDataForAccumLine);
}

function initSocket(projectUuid) {
    //初始化websocket并订阅来自服务器的消息推送
    var socket = new SockJS('/smosplat/collect');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        var subscribeUrl = "/collect/projectWYDCollect/" + projectUuid;
        stompClient.subscribe(subscribeUrl, function(message) {
            var resultObj = JSON.parse(message.body);
            //得到测站下的所有监测点的数据列表（最新推送过来的）
            var dataList = JSON.parse(resultObj);
            //保存所有时间值
            var times = new Array();
            //选项
            var chartTotalOption = myLineChartAccum.getOption();
            //各个点的数据（折线图累计变化量
            var seriesDataForAccumLine = chartTotalOption.series;
            //参数
            var chartOption = chartTotalOption.chartOption;
            //折线图单次变化量数据
            var seriesDataForGapLine = myLineChartGap.getOption().series;
            //得到datagrid的数据源
            var dgData = $("#wydDG").jqGrid("getRowData");
            for (var d = 0; d < dgData.length; d++) {
                //避免时间为""时前台表格出现"1970-01-01 08:00:00"的错误
                if (dgData[d].surveyTime == "") {
                    dgData[d].surveyTime = null;
                }
            }
            //根据监测点的id替换掉对应的数据记录
            for (var i = 0; i < dataList.length; i++) {
                var spName = dataList[i].surveyPoint.code;
                var spUuid = dataList[i].surveyPoint.surveyPointUuid;
                for (var j = 0; j < dgData.length; j++) {
                    if (dgData[j]["code"] == spName) {
                        dgData[j].surveyPointUuid = spUuid;
                        dgData[j].gapHOffset = dataList[i].gapHOffset;
                        dgData[j].accumHOffset = dataList[i].accumHOffset;
                        dgData[j].levelH = dataList[i].levelH;
                        dgData[j].surveyTime = dataList[i].surveyTime;
                    }
                }
                //循环测点的个数
                for (var k = 0; k < seriesDataForAccumLine.length; k++) {
                    if (seriesDataForAccumLine[k].name == spName) {
                        var dataLength = seriesDataForAccumLine[k].data.length;
                        for (var l = 0; l < dataLength; l++) {
                            //10条数据，去掉第一条，其他的往前挪一条
                            if (l < dataLength - 1) {
                                seriesDataForAccumLine[k].data[l] = seriesDataForAccumLine[k].data[l + 1];
                                seriesDataForGapLine[k].data[l] = seriesDataForGapLine[k].data[l + 1];
                            } else {
                                // 最后一条放新推送过来的数据
                                seriesDataForAccumLine[k].data[l] = [dataList[i].surveyTime + "", dataList[i].accumHOffset];
                                seriesDataForGapLine[k].data[l] = [dataList[i].surveyTime + "", dataList[i].gapHOffset];
                            }
                        }
                    }
                }
                //得出新的数据所有的时间点
                for (var ii = 0; ii < seriesDataForAccumLine.length; ii++) {
                    var spDataList = seriesDataForAccumLine[ii].data;
                    //保存时间点
                    for (var k = 0; k < spDataList.length; k++) {
                        if (isInArray(times, spDataList[k][0]) == false) {
                            times.push(spDataList[k][0]);
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
            $("#wydDG").jqGrid('setGridParam', { // 重新加载数据
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
            getOption(times, seriesDataForGapLine, seriesDataForAccumLine);
        });
    });
}

function getOption(times, seriesDataForGapLine, seriesDataForAccumLine) {
    var chartOption = {
        legend: {
            data: wydCodes,
            itemHeight: 8,
            itemWidth: 18,
            itemGap: 8,
            textStyle: {
                fontSize: 10
            },
            right: 20
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
            splitLine: {
                show: true
            }
        },
        grid: {
            left: '60',
            right: '60',
            top: '32',
            bottom: '38'
        },
        toolbox: {
            show: true,
            feature: {
                saveAsImage: {}
            }
        },
        tooltip: {
            trigger: 'axis',
            formatter: function(params) {
                var tipStr = "";
                var date = new Date(parseFloat(params[0].value[0]));
                tipStr = date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate() + " " +
                    date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                for (var i = 0; i < params.length; i++) {
                    if (params[i].value != undefined) {
                        var dataStr = params[i].seriesName + ":" + params[i].value[1];
                        if (i % 4 == 0) {
                            tipStr = tipStr + "<br>" + dataStr;
                        } else {
                            tipStr = tipStr + ',' + ' &nbsp;' + dataStr;
                        }
                    }
                }
                return tipStr;
            },
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            },
            textStyle: {
                fontSize: 10
            }
        },
    };
    //初始化折线图
    myLineChartAccum = echarts.init(document.getElementById('wydLineDivAccum'), 'macarons');
    myLineChartAccum.setOption(chartOption);
    myLineChartAccum.setOption({
        series: seriesDataForAccumLine,
        yAxis: {
            min: minAccum,
            max: maxAccum
        }
    });
    myLineChartGap = echarts.init(document.getElementById('wydLineDivGap'), 'macarons');
    myLineChartGap.setOption(chartOption);
    myLineChartGap.setOption({
        series: seriesDataForGapLine,
        yAxis: {
        }
    });
}

function formateTimestamp(cellvalue, options, rowObject) {
    return timestamp2String(rowObject.surveyTime);
};