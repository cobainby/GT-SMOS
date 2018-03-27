//单次位移曲线
var myLineChartGap;
var zcNames = new Array();
var zcModel = new Array();
//单次变化量的最小值
var minGap;
//单次变化量的最大值
var maxGap;
var zcCodes;
//工程id
var projectUuid;
zcNames = ['数据ID', '编号', '单次变化量', '变化速率', '测量时间', '计算值'];
zcModel = [{
    name: 'surveyPointUuid',
    hidden: true
}, {
    name: 'code',
    align: "center"
}, {
    name: 'gapOffset',
    align: "center"
}, {
    name: 'gapChangeRate',
    sorttype: "float",
    align: "center"
}, {
    name: 'collectTime',
    formatter: formateTimestamp,
    align: "center",
    width: 240
}, {
    name: 'calValue',
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
        var monitorItemCode = "ZC";
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
            var projectAddress = JSON.parse(data).project.address;
            $("#projectName").val(projectName);
            $("#projectAddress").val(projectAddress);
        });
        $.post("getProjectReportByProject", function(data, status) {
            reportDataObj = JSON.parse(data).entity;
            if (reportDataObj != null) {
                var deviceName=reportDataObj.p1p94;
                var specification=reportDataObj.p1p95;
                $("#deviceName").val(deviceName);
                $("#specification").val(specification);
            }
        });
        $("#dataExportDialog").modal("show");
    });
    $("#saveExportBtn").bind("click", function() {
        var pjName = $("#projectName").val();
        var pjAddress = $("#projectAddress").val();
        var deviceName=$("#deviceName").val();
        var bDate = $("#beginDate").datetimebox('getValue');
        var eDate = $("#endDate").datetimebox('getValue');
        var specification = $("#specification").val();
        $("#dataExportDialog").modal("hide");
        //导出报表
        window.open("/smosplat/zcExport?pjName=" + pjName + "&pjAddress=" + pjAddress +"&deviceName="+deviceName+ "&specification=" + specification + "&bDateStr=" + bDate + "&eDateStr=" + eDate);
    });
});
function downUrl(fileName,projectUuid,monitorItemCode) {
	window.location.href="/smosplat/downloadSourceFile?fileName=" + fileName + "&projectUuid=" + projectUuid+"&monitorItemCode="+monitorItemCode;
}
function init() {
    //加载数据
    $.MyCommon.PageLoading({ loadingTips: '加载中,请稍候...' });
    $.post("/smosplat/getLatestZCDatasByProject", function(data, status) {
        //先取出最新10条数据进行显示
        var dataObj = JSON.parse(data);
        var projectUuid = dataObj.projectUuid;
        var surveyPoints = dataObj.surveyPoints;
        var zcDatasObj = dataObj.dataList;
        if (dataObj.result == 0) {
            $('#loadingPage').remove();
            //保存所有时间值
            var times = new Array();
            //取出最新一条数据，显示到datagrid中
            //所有监测点的最新一条数据
            var lastZcDatas = new Array();
            //单次变化量(用来找出单次变化量的最大最小值，确定y轴范围)
            var allGapOffsetData = new Array();
            for (var i = 0; i < surveyPoints.length; i++) {
                //根据编号拿到不同编号的数据
                var zcDatas = zcDatasObj[surveyPoints[i].code];
                var lastZcData = zcDatas == undefined ? {} : zcDatas[zcDatas.length - 1];
                if (lastZcData == undefined) {
                    lastZcData = new Object;
                }
                lastZcData["code"] = surveyPoints[i].code;
                lastZcDatas.push(lastZcData);
                //存累计变化量、不同编号点的时间
                if (zcDatas != undefined) {
                    //保存时间点
                    for (var k = 0; k < zcDatas.length; k++) {
                        if (isInArray(times, zcDatas[k].collectTime) == false) {
                            times.push(zcDatas[k].collectTime);
                        }
                    }
                }
            }
            $("#zcDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                data: lastZcDatas,
                colNames: zcNames,
                colModel: zcModel,
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
                    var selectedIds = $("#zcDG").jqGrid("getGridParam", "selarrrow");
                    var rowDatas = [];
                    //获取当前选择行的数据
                    for (var l = 0; l < selectedIds.length; l++) {
                        rowDatas.push($("#zcDG").jqGrid("getRowData", selectedIds[l]));
                    }
                  //选中的点的时间集合
                    var selectTimes=[];
                    for(var i=0;i<rowDatas.length;i++){
                    	//拿到选中点的数据
                    	var zcDatas = zcDatasObj[rowDatas[i].code];
                    	if (zcDatas != undefined) {
                            //保存时间点
                            for (var k = 0; k < zcDatas.length; k++) {
                                if (isInArray(selectTimes, zcDatas[k].collectTime) == false) {
                                	selectTimes.push(zcDatas[k].collectTime);
                                }
                            }
                        }
                    }
                    getSelectDatas(selectTimes,rowDatas,spDatasObj);
                }
            });
            $("#zcDG").setGridHeight($("#zcGrid").height() - 80);
            $("#cb_zcDG").click();
            $("#cb_zcDG").bind("click", function() {
                if ($("#cb_zcDG").is(':checked')) {
                	getSelectDatas(times,surveyPoints,zcDatasObj);
                } else {
                    myLineChartGap.clear();
                }
            });
            getSelectDatas(times,surveyPoints,zcDatasObj);
            initSocket(projectUuid);
        } else if (dataObj.result == -1) {
            swal({ title: "失败！", text: dataObj.msg, type: "error" });
            $('#loadingPage').remove();
        }
    });
}

function getSelectDatas(times,surveyPoints,zcDatasObj){
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
    //单次变化量曲线图数据
    var seriesDataForGapLine = new Array();
    for (var i = 0; i < surveyPoints.length; i++) {
        //每一个监测点的最新的10条数据
        var zcDataList = zcDatasObj[surveyPoints[i].code];
        //单独取出每一条线的时间，用来和所有的时间点比较，得到当前测点哪个时间无返回数据
        var singleTime = new Array();
        var newZcData=[];
        for (var o = 0; o < zcDataList.length; o++) {
        	if(isInArray(singleTime,zcDataList[o].collectTime)==false){
        		singleTime.push(zcDataList[o].collectTime);
        		newZcData.push(zcDataList[o]);
        	}
            
        }
        //无返回数据的时间给他赋空值
        for (var q = 0; q < times.length; q++) {
            if (isInArray(singleTime, times[q]) == false) {
                newZcData.push({
                    'collectTime': times[q],
                    'gapOffset': '/'
                });
            }
        }
        //重新排序
        newZcData.sort(function(a, b) {
            return a.collectTime - b.collectTime;
        });
        //取最新的10条
        if (newZcData.length > 10) {
            newZcData = newZcData.slice(-10);
        }
        var serieDataForGap = new Array();
        if (newZcData != undefined) {
            for (var j = 0; j < newZcData.length; j++) {
                //判断当前测点是否有这个时间点的数据,没有则默认为空
                var value = newZcData[j].gapOffset;
                serieDataForGap.push([newZcData[j].collectTime + "", value]);
            }
        }
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
    zcCodes = new Array();
    for (var i = 0; i < surveyPoints.length; i++) {
        zcCodes.push(surveyPoints[i].code);
    }
    getOption(times, seriesDataForGapLine);
}

function initSocket(projectUuid) {
    //初始化websocket并订阅来自服务器的消息推送
    //建立与服务器的长连接
    var socket = new SockJS('/smosplat/collect');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        //根据测站进行订阅
        var subscribeUrl = "/collect/mcuZCStartCollect/" + projectUuid;
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
                var chartTotalOption = myLineChartGap.getOption();
                //获取折线图各个点的数据
                var seriesDataForGap = chartTotalOption.series;
                //得到datagrid的数据源
                var dgData = $("#zcDG").jqGrid("getRowData");
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
                            dgData[j].surveyPointUuid = dataList[i].surveyPoint.surveyPointUuid;
                            dgData[j].calValue = dataList[i].calValue;
                            dgData[j].collectTime = dataList[i].collectTime;
                        } else {
                            //把时间转为毫秒，前台展示才正常
                            var t = dgData[j].collectTime;
                            //必须改成2017/09/08 08:09:09的格式
                            t = t.replace(new RegExp("-", "gm"), "/");
                            dgData[j].collectTime = (new Date(t)).getTime();
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
                                } else {
                                    //最后一条放新推送过来的数据
                                    seriesDataForGap[k].data[l] = [dataList[i].collectTime + "", dataList[i].gapOffset];
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
                $("#zcDG").jqGrid('setGridParam', { // 重新加载数据
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
                // 指定图表的配置项和数据
                getOption(times, seriesDataForGap);
            }
        });
    });
}

function getOption(times, seriesDataForGapLine) {
    var chartOption = {
        legend: {
            data: zcCodes,
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
            boundaryGap: [0, '100%'],
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
    myLineChartGap = echarts.init(document.getElementById('zcLineDivGap'), 'macarons');
    myLineChartGap.setOption(chartOption);
    myLineChartGap.setOption({
        series: seriesDataForGapLine,
        yAxis: {
        }
    });
}

function formateTimestamp(cellvalue, options, rowObject) {
    return timestamp2String(rowObject.collectTime);
};