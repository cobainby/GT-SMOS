var myLineChartAccum;
var chartOption;
//控制值-
var controlMI;
//控制值+
var controlPlus;
//报警值-
var warningMI;
//报警值+
var warningPlus;
//选中的点号
var coderValue;
//选中的次数
var NumberValue;
//测量次数
//测点
var surveyPoints;
//工程ID
var projectUuid;
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
    initData();
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
        var monitorItemCode = "CX";
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
                var deviceName=reportDataObj.p1p90;
                var specification=reportDataObj.p1p91;
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
		window.open("/smosplat/cxExport?pjName="+pjName+"&pjAddress="+pjAddress+"&deviceName="+deviceName+"&specification="+specification+"&bDateStr="+bDate+"&eDateStr="+eDate);
	});
});
function downUrl(fileName,projectUuid,monitorItemCode) {
	window.location.href="/smosplat/downloadSourceFile?fileName=" + fileName + "&projectUuid=" + projectUuid+"&monitorItemCode="+monitorItemCode;
}
function initData() {
    //先拿到所有次数,加载所有的次数
	$.MyCommon.PageLoading({loadingTips:'加载中,请稍候...'});
    $.post('getmaxSurveyOrderByProject', function(data, status) {
    	$('#loadingPage').remove();
        var dataObj = JSON.parse(data);
        projectUuid = dataObj.projectUuid;
        surveyPoints = dataObj.surveyPoints;
        //控制值-
        controlMI = -surveyPoints[0].warning.controlAccum;
        //控制值+
        controlPlus = surveyPoints[0].warning.controlAccum;
        //报警值-
        warningMI = -surveyPoints[0].warning.warnAccum;
        //报警值+
        warningPlus = surveyPoints[0].warning.warnAccum;
        //测量次数
        var cxNumbers = new Array();
        //点号选择后再选择次数
        $("#cxCoderCB").combobox({
            data: surveyPoints,
            valueField: 'surveyPointUuid',
            textField: 'code',
            panelHeight: 'auto',
            editable: false,
            onSelect: function(record) {
                var totalNumbers = record.totalSurveyOrders;
                //定义不同点号的次数数组
                cxNumbers = new Array();
                for (var i = totalNumbers; i > 0; i--) {
                    var number = {
                        "numberValue": i,
                        "numberText": '第' + i + '次'
                    }
                    cxNumbers.push(number);
                }
                if (cxNumbers.length != 0) {
                    $("#cxNumberCB").combobox({
                        data: cxNumbers,
                        valueField: 'numberValue',
                        textField: 'numberText',
                        editable: false,
                        onSelect: function(record) {
                            getCombobox();
                        }
                    });
                    //选中点号时，默认选中监测最近的次数
                    var data = $('#cxNumberCB').combobox('getData');
                    $('#cxNumberCB').combobox('select', data[0].numberValue);
                } else {
                    //次数为0时,说明尚未监测,清空监测次数
                    $("#cxNumberCB").combobox('loadData', {});
                    $("#cxNumberCB").combobox('setValue', '尚无数据');
                    getCombobox();
                }
            }
        });
        //默认选中第一个点号和该最新一次监测次数
        var coderData = $('#cxCoderCB').combobox('getData');
        $('#cxCoderCB').combobox('select', coderData[0].surveyPointUuid);
        getCombobox();
        initSocket(projectUuid);
    });
};

function getCombobox() {
    coderValue = $("#cxCoderCB").combobox("getValue");
    NumberValue = $("#cxNumberCB").combobox("getValue");
    getCxData(coderValue, NumberValue);
}

function getCxData(coderValue, NumberValue) {
    if (NumberValue == "尚无数据") {
        $.jgrid.gridUnload('cxDG');
        if (myLineChartAccum != undefined) {
            myLineChartAccum.clear();
        }
        $("#cxTime").text("暂无数据");
        return;
    }
    $.post('getCxDataBySurveyOrderAndSurveyPoint', {
        "surveyPointUuid": coderValue,
        "surveyOrder": NumberValue
    }, function(data) {　　
        //先取出最新数据进行显示
        var dataObj = JSON.parse(data);
        var cxData = dataObj.cxData;
        if (cxData.length == 0) {
            //取不到数据，清空表格
            $.jgrid.gridUnload('cxDG');
            if (myLineChartAccum != undefined) {
                myLineChartAccum.clear();
            }
            $("#cxTime").text("暂无数据");
            return;
        }
        //取出当前采集时间,因为不管埋深深度多少，采集时间一致，所以取出任意时间即可
        var collectTime = cxData[0].collectTime;
        //格式化时间
        var cxTime = timestamp2String(collectTime);
        $("#cxTime").text(cxTime);
        var lastCxDatas = new Array();
        for (var i = 0; i < cxData.length; i++) {
            var lastCxData = cxData[i];
            //累计位移减去本次位移，得到上次累计位移
            lastCxData["lastAccumOffset"] = parseFloat(cxData[i].accumOffset - cxData[i].gapOffset).toFixed(3);
            lastCxDatas.push(lastCxData);
        }
        lastCxDatas.sort(function(a, b) {
            return a.depth - b.depth
        });
        $.jgrid.gridUnload('cxDG');
        $("#cxDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            colNames: ['埋深', '上次累计位移', '本次位移增量', '累计位移', '变化速率'],
            colModel: [{
                name: 'depth',
                align: 'center'
            }, {
                name: 'lastAccumOffset',
                formatter: "number",
                align: "center",
                formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
            }, {
                name: 'gapOffset',
                formatter: "number",
                align: "center",
                formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
            }, {
                name: 'accumOffset',
                formatter: "number",
                align: "center",
                formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
            }, {
                name: 'changeRate',
                formatter: "number",
                align: "center",
                formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
            }],
            data: lastCxDatas,
            autowidth: true,
            rowNum: 20,
            rowList: [20, 40, 60],
            pager: $("#pager_cxDG"),
            sortname: 'id',
            gridview: true,
            viewrecords: true
        });
        $("#cxDG").jqGrid('navGrid', '#pager_cxDG', { edit: false, add: false, del: false });
        $("#cxDG").setGridHeight($("#cxGrid").height() - 145);
        $("#cxDG").jqGrid('setSelection', '1');
        //保存累积变化值、报警值
        var allAccumOffsetData = new Array();
        //控制值-
        var controlMIData = new Array();;
        //控制值+
        var controlPlusData = new Array();;
        //报警值-
        var warningMIData = new Array();;
        //报警值+
        var warningPlusData = new Array();;
        var depthData = new Array();
        //根据选择的点号拿到当前点号的累计变化量,注意深度值取负值
        for (var k = lastCxDatas.length - 1; k >= 0; k--) {
            depthData.push(-lastCxDatas[k].depth);
            controlMIData.push(controlMI);
            controlPlusData.push(controlPlus);
            warningMIData.push(warningMI);
            warningPlusData.push(warningPlus);
            allAccumOffsetData.push(lastCxDatas[k].accumOffset);
        }
        // 指定图表的配置项和数据
        var symbolSize = 10;
        chartOption = {
            legend: {
                data: ['本次累计位移', '报警值(+)', '报警值(-)', '控制值(+)', '控制值(-)'],
                itemHeight:8,
                itemWidth:18,
                itemGap:8,
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
            tooltip: {
                trigger: 'item',
                formatter: "深度：{b}m,累计位移{c}mm"
            },
            grid: {
                left: '6%',
                right: '6%',
                containLabel: true
            },
            xAxis: {
                name: '累计位移',
                nameLocation: 'middle',
                nameGap: '30',
                position: 'top',
                type: 'value',
                splitLine: {
                    lineStyle: {
                        // 使用深浅的间隔色
                        color: ['#aaa']
                    }
                },
                axisLabel: {
                    formatter: '{value} mm'
                }
            },
            yAxis: {
                name: '埋深',
                nameLocation: 'middle',
                nameGap: '50',
                type: 'category',
                axisLine: {
                    onZero: false
                },
                axisLabel: {
                    formatter: '{value} m'
                },
                boundaryGap: false,
                data: depthData
            },
            series: [{
                name: '本次累计位移',
                type: 'line',
                smooth: false,
                symbolSize: symbolSize,
                symbol: 'diamond',
                lineStyle: {
                    normal: {
                        width: 2.5
                    }
                },
                data: allAccumOffsetData
            }, {
                name: '报警值(+)',
                type: 'line',
                smooth: false,
                symbolSize: 5,
                symbol: 'Rect',
                itemStyle: {
                    normal: {
                        color: '#F3FC02'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 1,
                        color: '#F3FC02'
                    }
                },
                data: warningPlusData
            }, {
                name: '报警值(-)',
                type: 'line',
                smooth: false,
                symbolSize: 5,
                symbol: 'circle',
                itemStyle: {
                    normal: {
                        color: '#F3FC02'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 1,
                        color: '#F3FC02'
                    }
                },
                data: warningMIData
            }, {
                name: '控制值(-)',
                type: 'line',
                smooth: false,
                symbolSize: 5,
                symbol: 'roundRect',
                itemStyle: {
                    normal: {
                        color: '#FB0606'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 1,
                        color: '#FB0606'
                    }
                },
                data: controlMIData
            }, {
                name: '控制值(+)',
                type: 'line',
                smooth: false,
                symbolSize: 5,
                symbol: 'triangle',
                itemStyle: {
                    normal: {
                        color: '#FB0606'
                    }
                },
                lineStyle: {
                    normal: {
                        width: 1,
                        color: '#FB0606'
                    }
                },
                data: controlPlusData
            }]

        };
        myLineChartAccum = echarts.init(document.getElementById('cxLine'), 'macarons');
        myLineChartAccum.setOption(chartOption);
    });
}

function initSocket(projectUuid) {
    //初始化websocket并订阅来自服务器的消息推送
    var socket = new SockJS('/smosplat/collect');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        var subscribeUrl = "/collect/projectCXCollect/" + projectUuid;
        stompClient.subscribe(subscribeUrl, function(message) {
            var resultObj = JSON.parse(message.body);
            //得到测站下的所有监测点的数据列表（最新推送过来的）
            var dataList = JSON.parse(resultObj);
            //上次最大次数
            var timesData = $('#cxNumberCB').combobox('getData');
            //当前选中的点号
            var spUuid = $('#cxCoderCB').combobox('getValue');
            //推送过来的数据点号跟 
            if (dataList[0][0].surveyPoint.surveyPointUuid != spUuid) {
                return;
            }
            //新一次数据推过来，最大次数+1；
            var totalNumbers;
            if (timesData.length > 0) {
                totalNumbers = timesData[0].numberValue + dataList.length;
            } else {
                totalNumbers = dataList.length;
            }
            //测量次数
            var cxNumbers = new Array();
            for (var i = totalNumbers; i > 0; i--) {
                var number = {
                    "numberValue": i,
                    "numberText": '第' + i + '次'
                }
                cxNumbers.push(number);
            }

            //先取出最新数据进行显示
            var cxData = dataList[dataList.length - 1];
            //取出当前采集时间,因为不管埋深深度多少，采集时间一致，所以取出任意时间即可
            var collectTime = cxData[0].collectTime;
            //格式化时间
            var cxTime = timestamp2String(collectTime);
            $("#cxTime").text(cxTime);
            var lastCxDatas = new Array();
            for (var i = 0; i < cxData.length; i++) {
                var lastCxData = cxData[i];
                //累计位移减去本次位移，得到上次累计位移
                lastCxData["lastAccumOffset"] = parseFloat(cxData[i].accumOffset - cxData[i].gapOffset).toFixed(3);
                lastCxDatas.push(lastCxData);
            }
            lastCxDatas.sort(function(a, b) {
                return a.depth - b.depth
            });
            $.jgrid.gridUnload('cxDG');
            $("#cxDG").jqGrid({
                datatype: "local",
                styleUI: 'Bootstrap',
                colNames: ['埋深', '上次累计位移', '本次位移增量', '累计位移', '变化速率'],
                colModel: [{
                    name: 'depth',
                    align: 'center'
                }, {
                    name: 'lastAccumOffset',
                    formatter: "number",
                    align: "center",
                    formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
                }, {
                    name: 'gapOffset',
                    formatter: "number",
                    align: "center",
                    formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
                }, {
                    name: 'accumOffset',
                    formatter: "number",
                    align: "center",
                    formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
                }, {
                    name: 'changeRate',
                    formatter: "number",
                    align: "center",
                    formatoptions: { thousandsSeparator: ",", defaulValue: "", decimalPlaces: 2 }
                }],
                data: lastCxDatas,
                autowidth: true,
                rowNum: 20,
                rowList: [20, 40, 60],
                pager: $("#pager_cxDG"),
                sortname: 'id',
                gridview: true,
                viewrecords: true
            });
            $("#cxDG").jqGrid('navGrid', '#pager_cxDG', { edit: false, add: false, del: false });
            $("#cxDG").setGridHeight($("#cxGrid").height() - 145);
            $("#cxDG").jqGrid('setSelection', '1');
            //保存累积变化值、报警值
            var allAccumOffsetData = new Array();
            //控制值-
            var controlMIData = new Array();;
            //控制值+
            var controlPlusData = new Array();;
            //报警值-
            var warningMIData = new Array();;
            //报警值+
            var warningPlusData = new Array();;
            var depthData = new Array();
            //根据选择的点号拿到当前点号的累计变化量,注意深度值取负值
            for (var k = lastCxDatas.length - 1; k >= 0; k--) {
                depthData.push(-lastCxDatas[k].depth);
                controlMIData.push(controlMI);
                controlPlusData.push(controlPlus);
                warningMIData.push(warningMI);
                warningPlusData.push(warningPlus);
                allAccumOffsetData.push(lastCxDatas[k].accumOffset);
            }
            // 指定图表的配置项和数据
            var symbolSize = 10;
            chartOption = {
                legend: {
                    data: ['本次累计位移', '报警值(+)', '报警值(-)', '控制值(+)', '控制值(-)'],
                    itemHeight:8,
                    itemWidth:18,
                    itemGap:8,
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
                tooltip: {
                    trigger: 'axis',
                    formatter: "深度：{b}m,累计位移{c}mm"
                },
                grid: {
                    left: '6%',
                    right: '6%',
                    containLabel: true
                },
                xAxis: {
                    name: '累计位移',
                    nameLocation: 'middle',
                    nameGap: '30',
                    position: 'top',
                    type: 'value',
                    splitLine: {
                        lineStyle: {
                            // 使用深浅的间隔色
                            color: ['#aaa']
                        }
                    },
                    axisLabel: {
                        formatter: '{value} mm'
                    }
                },
                yAxis: {
                    name: '埋深',
                    nameLocation: 'middle',
                    nameGap: '50',
                    type: 'category',
                    axisLine: {
                        onZero: false
                    },
                    axisLabel: {
                        formatter: '{value} m'
                    },
                    boundaryGap: false,
                    data: depthData
                },
                series: [{
                    name: '本次累计位移',
                    type: 'line',
                    smooth: false,
                    symbolSize: symbolSize,
                    symbol: 'diamond',
                    lineStyle: {
                        normal: {
                            width: 2
                        }
                    },
                    data: allAccumOffsetData
                }, {
                    name: '报警值(+)',
                    type: 'line',
                    smooth: false,
                    symbolSize: symbolSize,
                    symbol: 'Rect',
                    itemStyle: {
                        normal: {
                            color: '#FB0606'
                        }
                    },
                    lineStyle: {
                        normal: {
                            width: 2,
                            color: '#FB0606'
                        }
                    },
                    data: warningPlusData
                }, {
                    name: '报警值(-)',
                    type: 'line',
                    smooth: false,
                    symbolSize: symbolSize,
                    symbol: 'circle',
                    itemStyle: {
                        normal: {
                            color: '#FB0606'
                        }
                    },
                    lineStyle: {
                        normal: {
                            width: 2,
                            color: '#FB0606'
                        }
                    },
                    data: warningMIData
                }, {
                    name: '控制值(-)',
                    type: 'line',
                    smooth: false,
                    symbolSize: symbolSize,
                    symbol: 'roundRect',
                    itemStyle: {
                        normal: {
                            color: '#D62DE1'
                        }
                    },
                    lineStyle: {
                        normal: {
                            width: 2,
                            color: '#D62DE1'
                        }
                    },
                    data: controlMIData
                }, {
                    name: '控制值(+)',
                    type: 'line',
                    smooth: false,
                    symbolSize: symbolSize,
                    symbol: 'triangle',
                    itemStyle: {
                        normal: {
                            color: '#D62DE1'
                        }
                    },
                    lineStyle: {
                        normal: {
                            width: 2,
                            color: '#D62DE1'
                        }
                    },
                    data: controlPlusData
                }]

            };
            myLineChartAccum = echarts.init(document.getElementById('cxLine'), 'macarons');
            myLineChartAccum.setOption(chartOption);

            //点号选择后再选择次数
            $("#cxCoderCB").combobox({
                data: surveyPoints,
                valueField: 'surveyPointUuid',
                textField: 'code',
                panelHeight: 'auto',
                editable: false,
                onSelect: function(record) {
                    //若选择点号，则重新加载
                    initData();
                }
            });
            $("#cxNumberCB").combobox({
                data: cxNumbers,
                valueField: 'numberValue',
                textField: 'numberText',
                editable: false,
                onSelect: function(record) {
                    getCombobox();
                }
            });
            $('#cxNumberCB').combobox('setText', cxNumbers[0].numberText);
            $('#cxCoderCB').combobox('setValue', dataList[0][0].surveyPoint.surveyPointUuid);
            $('#cxCoderCB').combobox('setText', dataList[0][0].surveyPoint.code);
        });

    });
}